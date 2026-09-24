package com.contentful.rich.android.util;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.Browser;
import android.util.Log;

import com.contentful.rich.core.util.UrlSafety;

public final class LinkNavigator {
  private LinkNavigator() {
  }

  public static void open(Context context, String rawUrl) {
    final String safeUrl = UrlSafety.resolveSafeUrl(rawUrl);
    if (safeUrl == null) {
      return;
    }
    final Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(safeUrl));
    intent.putExtra(Browser.EXTRA_APPLICATION_ID, context.getPackageName());
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    try {
      context.startActivity(intent);
    } catch (ActivityNotFoundException exception) {
      Log.w("LinkNavigator", "No application can open this link.");
    }
  }
}
