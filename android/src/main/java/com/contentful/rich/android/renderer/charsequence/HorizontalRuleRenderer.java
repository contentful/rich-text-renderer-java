package com.contentful.rich.android.renderer.charsequence;

import android.text.Layout;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.AlignmentSpan;
import android.text.style.StrikethroughSpan;

import com.contentful.java.cda.rich.CDARichHorizontalRule;
import com.contentful.java.cda.rich.CDARichNode;
import com.contentful.rich.android.RichTextContext;
import com.contentful.rich.android.renderer.AndroidRenderer;
import com.contentful.rich.core.Processor;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static android.text.Spanned.SPAN_EXCLUSIVE_EXCLUSIVE;

public class HorizontalRuleRenderer extends AndroidRenderer<RichTextContext, CharSequence> {
  public HorizontalRuleRenderer(@Nonnull Processor<RichTextContext, CharSequence> processor) {
    super(processor);
  }

  @Override public boolean check(@Nullable RichTextContext context, @Nonnull CDARichNode node) {
    return node instanceof CDARichHorizontalRule;
  }

  @Nullable @Override
  public CharSequence render(@Nonnull RichTextContext context, @Nonnull CDARichNode node) {
    final Spannable result = new SpannableStringBuilder("                       ");
    result.setSpan(new StrikethroughSpan(), 0, result.length(), SPAN_EXCLUSIVE_EXCLUSIVE);
    result.setSpan(new AlignmentSpan.Standard(Layout.Alignment.ALIGN_OPPOSITE), 0, result.length(), SPAN_EXCLUSIVE_EXCLUSIVE);
    return result;
  }
}
