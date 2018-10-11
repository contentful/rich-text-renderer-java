package com.contentful.rich.android.renderer.charsequence;

import android.text.SpannableStringBuilder;
import android.text.style.LeadingMarginSpan;

import com.contentful.java.cda.rich.CDARichBlock;
import com.contentful.java.cda.rich.CDARichList;
import com.contentful.java.cda.rich.CDARichNode;
import com.contentful.rich.android.RichTextContext;
import com.contentful.rich.android.renderer.AndroidRenderer;
import com.contentful.rich.core.Processor;

import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import androidx.annotation.NonNull;

import static android.text.Spanned.SPAN_EXCLUSIVE_EXCLUSIVE;

public class BlockRenderer extends AndroidRenderer<RichTextContext, CharSequence> {
  public BlockRenderer(@Nonnull Processor<RichTextContext, CharSequence> processor) {
    super(processor);
  }

  @Override public boolean check(@Nullable RichTextContext context, @Nonnull CDARichNode node) {
    if (context != null) {
      if (node instanceof CDARichBlock) {
        return true;
      }
    }

    return false;
  }

  @Nullable @Override public CharSequence render(@Nonnull RichTextContext context, @Nonnull CDARichNode node) {
    final CDARichBlock block = (CDARichBlock) node;

    final SpannableStringBuilder result = new SpannableStringBuilder();

    for (final CDARichNode childNode : block.getContent()) {
      final CharSequence childResult = processor.render(childNode);
      if (childResult != null) {
        result.append(childResult);
      }
    }
    childWithNewline(result);

    return wrap(node, indent(context, node, decorate(context, node, result)));
  }

  @Nonnull
  protected void childWithNewline(@Nonnull SpannableStringBuilder builder) {
    while (builder.toString().endsWith("\n")) {
      builder.replace(builder.length() - 1, builder.length(), "");
    }
  }

  @Nonnull
  protected SpannableStringBuilder decorate(@Nonnull RichTextContext context, @Nonnull CDARichNode node, @Nonnull SpannableStringBuilder builder) {
    return builder;
  }

  @NonNull
  public SpannableStringBuilder wrap(@Nonnull CDARichNode node, @Nonnull SpannableStringBuilder builder) {
    return builder;
  }

  @Nonnull SpannableStringBuilder indent(
      @Nonnull RichTextContext context,
      @Nonnull CDARichNode node,
      @Nonnull SpannableStringBuilder builder) {
    int lists = 0;
    final List<CDARichNode> path = context.getPath();
    if (path != null) {
      for (final CDARichNode pathElement : path) {
        if (pathElement instanceof CDARichList) {
          lists++;
        }
      }
    }

    if (lists > 1) {
      final CDARichBlock block = (CDARichBlock) node;
      if (block.getContent().size() > 0) {
        if (block.getContent().get(0) instanceof CDARichBlock) {
          builder.append("\n");
        }
      }
      builder.setSpan(new LeadingMarginSpan.Standard(lists * 10 /*px*/), 0, builder.length(), SPAN_EXCLUSIVE_EXCLUSIVE);
    }

    return builder;
  }
}
