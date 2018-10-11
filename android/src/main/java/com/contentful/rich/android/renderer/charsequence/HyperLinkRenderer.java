package com.contentful.rich.android.renderer.charsequence;

import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.URLSpan;
import android.view.View;

import com.contentful.java.cda.rich.CDARichHyperLink;
import com.contentful.java.cda.rich.CDARichNode;
import com.contentful.rich.android.RichTextContext;
import com.contentful.rich.core.Processor;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import androidx.annotation.NonNull;

public class HyperLinkRenderer extends BlockRenderer {

  public HyperLinkRenderer(@Nonnull Processor<RichTextContext, CharSequence> processor) {
    super(processor);
  }

  @Override public boolean check(@Nullable RichTextContext context, @Nonnull CDARichNode node) {
    return node instanceof CDARichHyperLink;
  }

  @NonNull
  @Override protected SpannableStringBuilder decorate(
      @Nonnull RichTextContext context,
      @Nonnull CDARichNode node,
      @Nonnull SpannableStringBuilder builder) {

    final CDARichHyperLink link = (CDARichHyperLink) node;
    final URLSpan span = new URLSpan((String) link.getData()){
      @Override public void onClick(View widget) {
        super.onClick(widget);
      }
    };
    builder.setSpan(span, 0, builder.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
    return builder;
  }
}
