package com.contentful.rich.android.renderer.views;

import android.view.View;

import com.contentful.java.cda.rich.CDARichNode;
import com.contentful.java.cda.rich.CDARichQuote;
import com.contentful.rich.android.AndroidContext;
import com.contentful.rich.android.AndroidProcessor;
import com.contentful.rich.android.R;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class QuoteRenderer extends BlockRenderer {
  public QuoteRenderer(@Nonnull AndroidProcessor<View> processor) {
    super(processor);
  }

  @Override public boolean check(@Nullable AndroidContext context, @Nonnull CDARichNode node) {
    return node instanceof CDARichQuote;
  }

  @Override protected View inflateRichLayout(@Nonnull AndroidContext context, @Nonnull CDARichNode node) {
    return context.getInflater().inflate(R.layout.rich_quote_layout, null, false);
  }
}
