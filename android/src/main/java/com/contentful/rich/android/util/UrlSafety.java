package com.contentful.rich.android.util;

import javax.annotation.Nullable;

/**
 * Allowlist validation for rich text URLs on Android.
 *
 * @deprecated Kept for compatibility with 2.4.0. Use
 * {@link com.contentful.rich.core.util.UrlSafety}, which the HTML renderer shares.
 */
@Deprecated
public final class UrlSafety {

  private UrlSafety() {
  }

  /** @see com.contentful.rich.core.util.UrlSafety#isSafeUrl(String) */
  public static boolean isSafeUrl(@Nullable String url) {
    return com.contentful.rich.core.util.UrlSafety.isSafeUrl(url);
  }

  /** @see com.contentful.rich.core.util.UrlSafety#resolveSafeUrl(String) */
  @Nullable
  public static String resolveSafeUrl(@Nullable String rawUrl) {
    return com.contentful.rich.core.util.UrlSafety.resolveSafeUrl(rawUrl);
  }
}
