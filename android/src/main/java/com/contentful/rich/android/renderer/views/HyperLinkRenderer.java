package com.contentful.rich.android.renderer.views;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.Browser;
import android.util.Log;
import android.view.View;

import com.contentful.java.cda.rich.CDARichHyperLink;
import com.contentful.java.cda.rich.CDARichNode;
import com.contentful.rich.android.AndroidContext;
import com.contentful.rich.android.AndroidProcessor;
import com.contentful.rich.android.R;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class HyperLinkRenderer extends BlockRenderer {
  public HyperLinkRenderer(@Nonnull AndroidProcessor<View> processor) {
    super(processor);
  }

  @Override public boolean check(@Nullable AndroidContext context, @Nonnull CDARichNode node) {
    return node instanceof CDARichHyperLink && ((CDARichHyperLink) node).getData() instanceof String;
  }

  @Override protected View inflateRichLayout(@Nonnull AndroidContext context, @Nonnull CDARichNode node) {
    final View inflate = context.getInflater().inflate(R.layout.rich_hyperlink_layout, null, false);
    inflate.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        HyperLinkRenderer.this.onClick(context, node);
      }
    });
    return inflate;
  }

  public void onClick(AndroidContext context, CDARichNode node) {
    final Context androidContext = context.getAndroidContext();
    final Uri uri = Uri.parse((String) ((CDARichHyperLink) node).getData());

    final Intent intent = new Intent(Intent.ACTION_VIEW, uri);
    intent.putExtra(Browser.EXTRA_APPLICATION_ID, androidContext.getPackageName());
    try {
      androidContext.startActivity(intent);
    } catch (ActivityNotFoundException e) {
      Log.w("URLSpan", "Activity was not found for intent, " + intent.toString());
    }
  }
}
