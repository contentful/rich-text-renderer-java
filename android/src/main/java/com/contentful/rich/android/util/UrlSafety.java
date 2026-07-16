package com.contentful.rich.android.util;

import java.util.Locale;
import java.util.regex.Pattern;

import javax.annotation.Nullable;

/**
 * Centralized allowlist validation for URLs originating from rich text content, before they are
 * handed to the Android platform (e.g. via an {@code Intent#ACTION_VIEW}).
 * <p>
 * Rich text content can come from an external CMS entry and must be treated as untrusted input.
 * Without validation, a malicious or compromised entry could supply a {@code javascript:},
 * {@code file:}, or {@code intent:} URI, which could lead to script execution or launching of
 * arbitrary intents on the device.
 */
public final class UrlSafety {

  // Matches a leading URI scheme, e.g. "javascript:", "file:", "http:", "mailto:".
  // Per RFC 3986: scheme = ALPHA *( ALPHA / DIGIT / "+" / "-" / "." )
  private static final Pattern SCHEME_PREFIX = Pattern.compile("^[a-zA-Z][a-zA-Z0-9+.-]*:");

  private UrlSafety() {
  }

  /**
   * @param url the url to be checked, as supplied by rich text content.
   * @return true if the url uses an allowlisted scheme ({@code http}, {@code https} or {@code
   *     mailto}), false otherwise (including null or blank input).
   */
  public static boolean isSafeUrl(@Nullable String url) {
    if (url == null) {
      return false;
    }

    final String trimmed = url.trim().toLowerCase(Locale.US);
    return trimmed.startsWith("http://")
        || trimmed.startsWith("https://")
        || trimmed.startsWith("mailto:");
  }

  /**
   * Some renderers accept bare hostnames (e.g. {@code "contentful.com"}) without an explicit
   * scheme and, historically, prepend {@code "http://"} to them before navigating. Applying that
   * prefix blindly is unsafe: a value such as {@code "javascript:alert(1)"} has no {@code
   * "http"} prefix either, so naively prepending would turn it into {@code
   * "http://javascript:alert(1)"} which passes a naive scheme check while still carrying the
   * dangerous {@code javascript:} payload.
   * <p>
   * This method only treats a value as a "bare host" (safe to prepend {@code http://} to) when it
   * has no URI scheme at all. If a scheme is present, it must already be one of the allowlisted
   * ones ({@code http}, {@code https}, {@code mailto}).
   *
   * @param rawUrl the raw, untrusted url/host string as supplied by rich text content.
   * @return the resolved absolute url if safe to navigate to, or {@code null} if the input is
   *     null, blank, or resolves to a disallowed scheme.
   */
  @Nullable
  public static String resolveSafeUrl(@Nullable String rawUrl) {
    if (rawUrl == null) {
      return null;
    }

    final String trimmed = rawUrl.trim();
    if (trimmed.isEmpty()) {
      return null;
    }

    if (!SCHEME_PREFIX.matcher(trimmed).find()) {
      // No scheme at all: treat as a bare host and safely default to http://.
      final String withScheme = "http://" + trimmed;
      return isSafeUrl(withScheme) ? withScheme : null;
    }

    return isSafeUrl(trimmed) ? trimmed : null;
  }
}
