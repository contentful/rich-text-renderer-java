package com.contentful.structured.android.renderer.charsequence;

import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;

import com.contentful.java.cda.structured.CDAStructuredHeading;
import com.contentful.java.cda.structured.CDAStructuredNode;
import com.contentful.structured.android.StructuredTextContext;
import com.contentful.structured.android.renderer.AndroidRenderer;
import com.contentful.structured.core.Processor;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class HeadingRenderer implements AndroidRenderer<StructuredTextContext, CharSequence> {

  static private final List<Integer> SIZE_MAP = new ArrayList<>();

  static {
    SIZE_MAP.add(24); // Level 1
    SIZE_MAP.add(22); // Level 2
    SIZE_MAP.add(20); // Level 3
    SIZE_MAP.add(18); // Level 4
    SIZE_MAP.add(16); // Level 5
    SIZE_MAP.add(14); // Level 6
  }

  private final Processor<StructuredTextContext, CharSequence> processor;

  public HeadingRenderer(Processor<StructuredTextContext, CharSequence> processor) {
    this.processor = processor;
  }

  @Override public boolean check(@Nullable StructuredTextContext context, @Nonnull CDAStructuredNode node) {
    return node instanceof CDAStructuredHeading;
  }

  @Nullable @Override
  public CharSequence render(@Nonnull StructuredTextContext context, @Nonnull CDAStructuredNode node) {
    final CDAStructuredHeading heading = (CDAStructuredHeading) node;

    final SpannableStringBuilder result = new SpannableStringBuilder();

    for (final CDAStructuredNode childNode : heading.getContent()) {
      result.append(processor.render(childNode));
    }

    final AbsoluteSizeSpan sizeSpan = new AbsoluteSizeSpan(SIZE_MAP.get(heading.getLevel() - 1), true);
    result.setSpan(sizeSpan, 0, result.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

    return result;
  }
}
