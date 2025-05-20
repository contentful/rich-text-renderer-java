package com.contentful.rich.android.renderer.chars;

import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.MovementMethod;
import android.text.style.URLSpan;

import com.contentful.java.cda.rich.CDARichHyperLink;
import com.contentful.java.cda.rich.CDARichNode;
import com.contentful.rich.android.AndroidContext;
import com.contentful.rich.android.AndroidProcessor;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import androidx.annotation.NonNull;

/**
 * Render the children into a hyperlink.
 * <p>
 * Please remember to use {@link android.widget.TextView#setMovementMethod(MovementMethod)}
 * with {@link android.text.method.LinkMovementMethod#}
 */
public class HyperLinkRenderer extends BlockRenderer {

  /**
   * Create the processor preserving hyperlink renderer.
   *
   * @param processor the processor to be used for child rendering.
   */
  public HyperLinkRenderer(@Nonnull AndroidProcessor<CharSequence> processor) {
    super(processor);
  }

  /**
   * Checks if the giben node is a hyperlink.
   *
   * @param context context this check should be performed in
   * @param node    node to be checked
   * @return true if the given node is a hyperlink.
   */
  @Override public boolean canRender(@Nullable AndroidContext context, @Nonnull CDARichNode node) {
    if (!(node instanceof CDARichHyperLink)) {
        return false;
    }
    Object data = ((CDARichHyperLink)node).getData();
    return data instanceof String || (data instanceof Map && ((Map<?, ?>) data).containsKey("uri"));
  }

  /**
   * Decorate the rendered children to either be using a clickable span.
   *
   * @param context the context this decoration should be applied to.
   * @param node    the actual node, rendered into the builder.
   * @param builder a builder for generating the desired output.
   * @return the enhanced children, rendered as a hyperlink.
   */
  @NonNull @Override protected SpannableStringBuilder decorate(
      @Nonnull AndroidContext context,
      @Nonnull CDARichNode node,
      @Nonnull SpannableStringBuilder builder) {

    final CDARichHyperLink link = (CDARichHyperLink) node;
    final Object data = link.getData();
    final String uri;
    
    if (data instanceof String) {
        uri = (String) data;
    } else if (data instanceof Map) {
        uri = (String) ((Map<?, ?>) data).get("uri");
    } else {
        return builder; // Return unchanged if data is neither String nor Map
    }
    
    final URLSpan span = new URLSpan(uri);
    builder.setSpan(span, 0, builder.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
    return builder;
  }
}
