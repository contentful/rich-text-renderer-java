package com.contentful.rich.core.util;

import java.util.Locale;
import java.util.regex.Pattern;

import javax.annotation.Nullable;

/**
 * Allowlist validation for URLs that come from rich text content, before any renderer turns them
 * into something clickable (an Android {@code Intent#ACTION_VIEW}, a span, or an HTML
 * {@code href}).
 * <p>
 * Rich text comes from CMS entries and must be treated as untrusted input. Without validation, a
 * malicious or compromised entry could supply a {@code javascript:}, {@code file:}, {@code data:}
 * or {@code intent:} URI, leading to script execution or arbitrary intents.
 * <p>
 * Allowed schemes: {@code http}, {@code https}, {@code mailto}, {@code tel} and {@code sms}.
 */
public final class UrlSafety {

  // Matches a leading URI scheme, e.g. "javascript:", "file:", "http:", "mailto:".
  // Per RFC 3986: scheme = ALPHA *( ALPHA / DIGIT / "+" / "-" / "." )
  private static final Pattern SCHEME_PREFIX = Pattern.compile("^[a-zA-Z][a-zA-Z0-9+.-]*:");

  private static final String[] ALLOWED_PREFIXES = {
      "http://", "https://", "mailto:", "tel:", "sms:"
  };

  // Browsers remove ASCII tab and newline anywhere in a URL ("java\tscript:" runs as
  // "javascript:"), so remove them before checking the scheme.
  private static final Pattern TAB_OR_NEWLINE = Pattern.compile("[\\t\\n\\r]");

  private UrlSafety() {
  }

  /** Removes tabs and newlines, and leading/trailing spaces and control characters. */
  static String normalize(String url) {
    final String withoutTabs = TAB_OR_NEWLINE.matcher(url).replaceAll("");
    int start = 0;
    int end = withoutTabs.length();
    while (start < end && withoutTabs.charAt(start) <= ' ') {
      start++;
    }
    while (end > start && withoutTabs.charAt(end - 1) <= ' ') {
      end--;
    }
    return withoutTabs.substring(start, end);
  }

  private static boolean hasScheme(String normalizedUrl) {
    return SCHEME_PREFIX.matcher(normalizedUrl).find();
  }

  /**
   * @param url the url to be checked, as supplied by rich text content.
   * @return true if the url uses an allowlisted scheme, false otherwise (including null input).
   */
  public static boolean isSafeUrl(@Nullable String url) {
    if (url == null) {
      return false;
    }

    final String normalized = normalize(url).toLowerCase(Locale.US);
    for (String prefix : ALLOWED_PREFIXES) {
      if (normalized.startsWith(prefix)) {
        return true;
      }
    }
    return false;
  }

  /**
   * Resolves a raw rich text link to a url that is safe to open.
   * <p>
   * Values without any scheme (a bare host such as {@code "contentful.com"}) get {@code http://}.
   * Values with a scheme must use an allowlisted one; prepending {@code http://} to them would
   * otherwise turn {@code "javascript:alert(1)"} into a value that passes a naive check.
   *
   * @param rawUrl the raw, untrusted url or host string.
   * @return the trimmed, absolute url if safe, or {@code null} if the input is null, blank, or uses
   *     a disallowed scheme.
   */
  @Nullable
  public static String resolveSafeUrl(@Nullable String rawUrl) {
    if (rawUrl == null) {
      return null;
    }

    final String normalized = normalize(rawUrl);
    if (normalized.isEmpty()) {
      return null;
    }

    if (!hasScheme(normalized)) {
      final String withScheme = "http://" + normalized;
      return isSafeUrl(withScheme) ? withScheme : null;
    }

    return isSafeUrl(normalized) ? normalized : null;
  }

  /**
   * Checks a link target for an HTML {@code href}. Unlike {@link #resolveSafeUrl(String)}, values
   * without a scheme are allowed unchanged, because in HTML they are relative links
   * ({@code /about}, {@code #top}, {@code page.html}) and can't run script.
   *
   * @param rawUrl the raw, untrusted url.
   * @return the normalized url if safe to use as an {@code href}, or {@code null} if it is null,
   *     blank, or uses a disallowed scheme such as {@code javascript:}. The caller must still
   *     HTML-escape the returned value.
   */
  @Nullable
  public static String safeHref(@Nullable String rawUrl) {
    if (rawUrl == null) {
      return null;
    }
    final String normalized = normalize(rawUrl);
    if (normalized.isEmpty()) {
      return null;
    }
    if (!hasScheme(normalized)) {
      return normalized;
    }
    return isSafeUrl(normalized) ? normalized : null;
  }
}
