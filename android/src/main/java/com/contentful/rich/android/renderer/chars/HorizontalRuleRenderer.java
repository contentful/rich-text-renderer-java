package com.contentful.rich.android.renderer.chars;

import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.StrikethroughSpan;

import com.contentful.java.cda.rich.CDARichHorizontalRule;
import com.contentful.java.cda.rich.CDARichNode;
import com.contentful.rich.android.AndroidContext;
import com.contentful.rich.android.AndroidProcessor;
import com.contentful.rich.android.AndroidRenderer;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static android.text.Spanned.SPAN_EXCLUSIVE_EXCLUSIVE;

/**
 * Renderer for a horizontal rule.
 */
public class HorizontalRuleRenderer extends AndroidRenderer<AndroidContext, CharSequence> {
  /**
   * Create the renderer, referencing its processor.
   * <p>
   * This element does not have any children.
   *
   * @param processor the unused processor for this renderer.
   */
  public HorizontalRuleRenderer(@Nonnull AndroidProcessor<CharSequence> processor) {
    super(processor);
  }

  /**
   * Check if the given node is a horizontal rule.
   *
   * @param context context this check should be performed in
   * @param node    node to be checked
   * @return true if the given node is a rule
   */
  @Override public boolean canRender(@Nullable AndroidContext context, @Nonnull CDARichNode node) {
    return node instanceof CDARichHorizontalRule;
  }

  /**
   * Render a horizontal line.
   *
   * @param context the generic context this node should be rendered in.
   * @param node    the node to be rendered.
   * @return a span containing only a line.
   */
  @Nullable @Override
  public CharSequence render(@Nonnull AndroidContext context, @Nonnull CDARichNode node) {
    final Spannable result = new SpannableStringBuilder("                       ");
    result.setSpan(new StrikethroughSpan(), 0, result.length(), SPAN_EXCLUSIVE_EXCLUSIVE);
    return result;
  }
}
