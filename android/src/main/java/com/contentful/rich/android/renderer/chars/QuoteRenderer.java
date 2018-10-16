package com.contentful.rich.android.renderer.chars;

import android.text.SpannableStringBuilder;
import android.text.Spanned;

import com.contentful.java.cda.rich.CDARichNode;
import com.contentful.java.cda.rich.CDARichQuote;
import com.contentful.rich.android.AndroidContext;
import com.contentful.rich.android.AndroidProcessor;
import com.contentful.rich.android.renderer.chars.span.QuoteSpan;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import androidx.annotation.NonNull;

/**
 * Add a fancy bar next to the children, to indicate this text is a quote.
 */
public class QuoteRenderer extends BlockRenderer {

  /**
   * Create a new quote renderer, saving its processor for child node processing.
   *
   * @param processor
   */
  public QuoteRenderer(@Nonnull AndroidProcessor<CharSequence> processor) {
    super(processor);
  }

  /**
   * Is the given node a quote?
   *
   * @param context context this check should be performed in
   * @param node    node to be checked
   * @return true or false. Depending on whether the node given is a CDARichQuote.
   */
  @Override public boolean check(@Nullable AndroidContext context, @Nonnull CDARichNode node) {
    return node instanceof CDARichQuote;
  }

  /**
   * Decorate the given children by adding a start-aligned bar.
   *
   * @param context          the context this decoration should be applied to.
   * @param node             the actual node, rendered into the builder.
   * @param renderedChildren the rendered child nodes as a builder.
   * @return the builder, enhanced by vertical bar.
   */
  @NonNull @Override protected SpannableStringBuilder decorate(
      @Nonnull AndroidContext context,
      @Nonnull CDARichNode node,
      @Nonnull SpannableStringBuilder renderedChildren) {

    final QuoteSpan span = new QuoteSpan(0x80808080, 30, 20);
    renderedChildren.setSpan(span, 0, renderedChildren.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
    return renderedChildren;
  }

}
