package com.contentful.rich.android.renderer.charsequence;

import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;

import com.contentful.java.cda.rich.CDARichHeading;
import com.contentful.java.cda.rich.CDARichNode;
import com.contentful.rich.android.RichTextContext;
import com.contentful.rich.core.Processor;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class HeadingRenderer extends BlockRenderer {

  static private final List<Integer> SIZE_MAP = new ArrayList<>();

  static {
    SIZE_MAP.add(24); // Level 1
    SIZE_MAP.add(22); // Level 2
    SIZE_MAP.add(20); // Level 3
    SIZE_MAP.add(18); // Level 4
    SIZE_MAP.add(16); // Level 5
    SIZE_MAP.add(14); // Level 6
  }

  public HeadingRenderer(@Nonnull Processor<RichTextContext, CharSequence> processor) {
    super(processor);
  }

  @Override public boolean check(@Nullable RichTextContext context, @Nonnull CDARichNode node) {
    if (node instanceof CDARichHeading) {
      final CDARichHeading heading = (CDARichHeading) node;
      if (heading.getLevel() > 0 && ((CDARichHeading) node).getLevel() < 7) {
        return true;
      }
    }

    return false;
  }

  @Nullable @Override
  public SpannableStringBuilder wrap(@Nonnull CDARichNode node, @Nonnull SpannableStringBuilder builder) {
    final CDARichHeading heading = (CDARichHeading) node;

    final AbsoluteSizeSpan sizeSpan = new AbsoluteSizeSpan(SIZE_MAP.get(heading.getLevel() - 1), true);
    builder.setSpan(sizeSpan, 0, builder.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

    return builder;
  }
}
