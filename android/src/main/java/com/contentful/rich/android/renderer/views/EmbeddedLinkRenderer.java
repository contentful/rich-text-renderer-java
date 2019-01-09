package com.contentful.rich.android.renderer.views;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.contentful.java.cda.CDAAsset;
import com.contentful.java.cda.CDAEntry;
import com.contentful.java.cda.rich.CDARichEmbeddedInline;
import com.contentful.java.cda.rich.CDARichNode;
import com.contentful.rich.android.AndroidContext;
import com.contentful.rich.android.AndroidProcessor;
import com.contentful.rich.android.R;
import com.contentful.rich.android.renderer.chars.EmbeddedLinkRenderer.BitmapProvider;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static com.contentful.rich.android.renderer.chars.EmbeddedLinkRenderer.defaultBitmapProvider;

public class EmbeddedLinkRenderer extends BlockRenderer {
  private final BitmapProvider provider;

  public EmbeddedLinkRenderer(@Nonnull AndroidProcessor<View> processor) {
    this(processor, defaultBitmapProvider);
  }

  public EmbeddedLinkRenderer(@Nonnull AndroidProcessor<View> processor, BitmapProvider provider) {
    super(processor);
    this.provider = provider;
  }

  @Override public boolean check(@Nullable AndroidContext context, @Nonnull CDARichNode node) {
    return node instanceof CDARichEmbeddedInline;
  }

  @Override protected View inflateRichLayout(@Nonnull AndroidContext context, @Nonnull CDARichNode node) {
    final View embedded = context.getInflater().inflate(R.layout.rich_embedded_layout, null, false);
    final ViewGroup content = embedded.findViewById(R.id.rich_content);

    final CDARichEmbeddedInline link = (CDARichEmbeddedInline) node;
    final Object data = link.getData();

    final View toBeEmbedded;
    if (data instanceof CDAEntry) {
      final CDAEntry entry = (CDAEntry) data;

      final TextView textView = new TextView(context.getAndroidContext());
      textView.setText(entry.getField("title"));

      toBeEmbedded = textView;
    } else if (data instanceof CDAAsset) {
      final CDAAsset asset = (CDAAsset) data;
      final ImageView image = new ImageView(context.getAndroidContext());
      image.setImageBitmap(provider.provide(context.getAndroidContext(), asset));

      toBeEmbedded = image;
    } else {
      final TextView textView = new TextView(context.getAndroidContext());
      textView.setText("⚠️");

      toBeEmbedded = textView;
    }

    content.addView(toBeEmbedded, 0);
    return embedded;
  }
}
