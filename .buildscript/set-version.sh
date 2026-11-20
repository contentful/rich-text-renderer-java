#!/bin/bash
# Sets the release version in build.gradle (core, html and android inherit it). Run it in the release PR.
#
# Usage: .buildscript/set-version.sh X.Y.Z

set -euo pipefail

cd "$(dirname "$0")/.."

VERSION="${1:-}"
[[ "$VERSION" =~ ^[0-9]+\.[0-9]+\.[0-9]+$ ]] || { echo "Usage: $0 X.Y.Z" >&2; exit 1; }

sed -i.bak -E "s/^([[:space:]]*version[[:space:]]*=[[:space:]]*)['\"][^'\"]*['\"]/\1'$VERSION'/" build.gradle
rm -f build.gradle.bak
grep -n -E "^[[:space:]]*version[[:space:]]*=" build.gradle
