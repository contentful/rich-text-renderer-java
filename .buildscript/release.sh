#!/bin/bash
# Release rich-text-renderer-java through JitPack. The GitHub Actions workflow
# .github/workflows/release.yml runs the same steps as a manual release. See RELEASING.md.
#
# Usage: .buildscript/release.sh <step>
#
#   validate        build.gradle has a release version X.Y.Z that is not tagged and is greater than the
#                   last tag; the commit is on origin/master; the working tree is clean
#   tag             tag RELEASE_COMMIT (default: HEAD) as X.Y.Z (no prefix) and push only that tag
#   github-release  create the GitHub release with GitHub's generated notes
#   jitpack         ask JitPack to build the tag and wait until core, html and android are available
#   all             validate, tag, github-release, jitpack
#
# Environment:
#   DRY_RUN=1         print the tag push, the release command and the JitPack request instead of running
#                     them. Also allows validating a commit that is not on master.
#   EXPECTED_VERSION  fail unless build.gradle has this version (the workflow's confirmation input)
#   RELEASE_COMMIT    commit to tag (CI passes $GITHUB_SHA); defaults to HEAD
#   GH_TOKEN          GitHub token for gh (CI); otherwise your gh login is used
#   GIT_REMOTE        remote to fetch from and push to (default: origin)

set -euo pipefail

cd "$(dirname "$0")/.."

REPO_SLUG="contentful/rich-text-renderer-java"
JITPACK_GROUP="com.github.contentful"
JITPACK_REPO="rich-text-renderer-java"
MODULES="core html android"
GIT_REMOTE="${GIT_REMOTE:-origin}"
DRY_RUN="${DRY_RUN:-0}"

VERSION="$(sed -n -E "s/^[[:space:]]*version[[:space:]]*=[[:space:]]*['\"]([^'\"]*)['\"].*/\1/p" build.gradle | head -1)"
TAG="$VERSION"

log()  { printf '\n==> %s\n' "$*"; }
fail() { printf 'ERROR: %s\n' "$*" >&2; exit 1; }

# Runs a command that changes remote state, or prints it when DRY_RUN=1.
publish() {
  if [[ "$DRY_RUN" == "1" ]]; then
    printf '[dry-run] %s\n' "$*"
  else
    "$@"
  fi
}

remote_tag_sha() {
  local refs
  refs="$(git ls-remote "$GIT_REMOTE" "refs/tags/$TAG^{}" "refs/tags/$TAG")" \
    || fail "Could not list the tags on $GIT_REMOTE"
  awk 'NR==1{print $1}' <<<"$refs"
}

step_validate() {
  log "Validating release $VERSION"

  [[ "$VERSION" =~ ^[0-9]+\.[0-9]+\.[0-9]+$ ]] \
    || fail "build.gradle has version '$VERSION'. A release needs X.Y.Z. Run .buildscript/set-version.sh X.Y.Z in a PR"
  if [[ -n "${EXPECTED_VERSION:-}" && "$EXPECTED_VERSION" != "$VERSION" ]]; then
    fail "You asked to release $EXPECTED_VERSION but build.gradle has $VERSION"
  fi

  [[ -z "$(git status --porcelain --untracked-files=no)" ]] \
    || fail "Working tree has uncommitted changes to tracked files"

  git fetch --quiet --tags --force "$GIT_REMOTE"
  [[ -z "$(remote_tag_sha)" ]] || fail "Tag $TAG already exists on $GIT_REMOTE"

  local latest
  latest="$(git tag -l '[0-9]*.[0-9]*.[0-9]*' | grep -E '^[0-9]+\.[0-9]+\.[0-9]+$' | sort -V | tail -1 || true)"
  if [[ -n "$latest" ]]; then
    [[ "$(printf '%s\n%s\n' "$latest" "$VERSION" | sort -V | tail -1)" == "$VERSION" && "$latest" != "$VERSION" ]] \
      || fail "$VERSION is not greater than the latest tag $latest"
  fi

  if git ls-remote --exit-code --heads "$GIT_REMOTE" "$VERSION" >/dev/null 2>&1; then
    fail "A branch named '$VERSION' exists on $GIT_REMOTE. JitPack could build the branch instead of the tag; rename or delete it"
  fi

  local commit
  commit="$(git rev-parse "${RELEASE_COMMIT:-HEAD}^{commit}")"
  git fetch --quiet "$GIT_REMOTE" master
  if ! git merge-base --is-ancestor "$commit" "$GIT_REMOTE/master"; then
    [[ "$DRY_RUN" == "1" ]] || fail "Commit $commit is not on $GIT_REMOTE/master. Releases are made from master only"
    echo "WARNING: $commit is not on $GIT_REMOTE/master (allowed for a dry run only)"
  fi

  echo "OK: $VERSION (previous: ${latest:-none}) from $commit"
}

step_tag() {
  local commit existing
  commit="$(git rev-parse "${RELEASE_COMMIT:-HEAD}^{commit}")"
  log "Tagging $commit as $TAG"

  existing="$(remote_tag_sha)"
  if [[ -n "$existing" ]]; then
    [[ "$existing" == "$commit" ]] || fail "Tag $TAG already exists on a different commit ($existing)"
    echo "Tag $TAG already points at $commit, nothing to do"
    return
  fi

  if git rev-parse -q --verify "refs/tags/$TAG" >/dev/null; then
    [[ "$(git rev-parse "refs/tags/$TAG^{commit}")" == "$commit" ]] \
      || fail "A local tag $TAG exists on a different commit. Delete it with: git tag -d $TAG"
  elif [[ "$DRY_RUN" == "1" ]]; then
    printf '[dry-run] git tag %s %s\n' "$TAG" "$commit"
  else
    git tag "$TAG" "$commit"
  fi

  # Push only this tag, never --tags.
  publish git push "$GIT_REMOTE" "refs/tags/$TAG"
}

step_github_release() {
  log "Publishing GitHub release $TAG"
  command -v gh >/dev/null 2>&1 || fail "'gh' is required. Install it with: brew install gh"

  if gh release view "$TAG" --repo "$REPO_SLUG" >/dev/null 2>&1; then
    echo "Release $TAG already exists, nothing to do"
    return
  fi

  publish gh release create "$TAG" \
    --repo "$REPO_SLUG" \
    --title "$VERSION" \
    --generate-notes \
    --latest \
    --verify-tag
}

jitpack_status() {
  curl -s -m 30 "https://jitpack.io/api/builds/$JITPACK_GROUP/$JITPACK_REPO/$VERSION" \
    | sed -n -E 's/^[[:space:]]*"status"[[:space:]]*:[[:space:]]*"([^"]*)".*/\1/p'
}

step_jitpack() {
  log "Building $VERSION on JitPack"
  local base="https://jitpack.io/com/github/contentful/$JITPACK_REPO"
  local log_url="$base/$VERSION/build.log"

  # Requesting an artifact starts the build if JitPack hasn't built the tag yet.
  if [[ "$DRY_RUN" == "1" ]]; then
    printf '[dry-run] curl %s/core/%s/core-%s.pom, then wait for the build\n' "$base" "$VERSION" "$VERSION"
    return
  fi
  curl -s -m 60 -o /dev/null "$base/core/$VERSION/core-$VERSION.pom" || true

  local status deadline=$((SECONDS + 1800))
  while :; do
    status="$(jitpack_status)"
    case "$status" in
      ok)    break ;;
      error) fail "JitPack failed to build $VERSION. Build log: $log_url" ;;
    esac
    (( SECONDS < deadline )) || fail "JitPack did not finish building $VERSION within 30 minutes (status '${status:-unknown}'). Build log: $log_url"
    echo "JitPack status: ${status:-queued}, waiting"
    sleep 20
  done

  local module
  for module in $MODULES; do
    [[ "$(curl -s -m 60 -o /dev/null -w '%{http_code}' "$base/$module/$VERSION/$module-$VERSION.pom")" == "200" ]] \
      || fail "JitPack built $VERSION but $module is missing. Build log: $log_url"
  done
  echo "OK: com.github.contentful.$JITPACK_REPO:{${MODULES// /,}}:$VERSION are available on JitPack"
}

case "${1:-}" in
  validate)       step_validate ;;
  tag)            step_tag ;;
  github-release) step_github_release ;;
  jitpack)        step_jitpack ;;
  all)
    step_validate
    step_tag
    step_github_release
    step_jitpack
    if [[ "$DRY_RUN" == "1" ]]; then
      log "Dry run of $VERSION finished. Nothing was pushed"
    else
      log "$REPO_SLUG $VERSION released"
    fi
    ;;
  *)
    sed -n '2,21p' "$0" | sed 's/^# \{0,1\}//'
    exit 1
    ;;
esac
