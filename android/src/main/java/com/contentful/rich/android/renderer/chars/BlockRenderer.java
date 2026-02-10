package com.contentful.rich.android.renderer.chars;

import android.text.SpannableStringBuilder;
import android.text.style.LeadingMarginSpan;

import com.contentful.java.cda.rich.CDARichBlock;
import com.contentful.java.cda.rich.CDARichList;
import com.contentful.java.cda.rich.CDARichNode;
import com.contentful.java.cda.rich.CDARichParagraph;
import com.contentful.rich.android.AndroidContext;
import com.contentful.rich.android.AndroidProcessor;
import com.contentful.rich.android.AndroidRenderer;

import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import androidx.annotation.NonNull;

import static android.text.Spanned.SPAN_EXCLUSIVE_EXCLUSIVE;

/**
 * Renders the given block and all of it's child nodes.
 */
public class BlockRenderer extends AndroidRenderer<AndroidContext, CharSequence> {
  /**
   * Create a new BlockRenderer.
   *
   * @param processor to be used for child processing.
   */
  public BlockRenderer(@Nonnull AndroidProcessor<CharSequence> processor) {
    super(processor);
  }

  /**
   * Can the given node be rendered with this renderer and the given context?
   *
   * @param context context this check should be performed in
   * @param node    node to be checked
   * @return true if the node can be rendered.
   */
  @Override public boolean canRender(@Nullable AndroidContext context, @Nonnull CDARichNode node) {
    if (context != null) {
      if (node instanceof CDARichBlock) {
        return true;
      }
    }

    return false;
  }

  /**
   * Perform rendering of the given block into the a character sequence.
   *
   * @param context the generic context this node should be rendered in.
   * @param node    the node to be rendered.
   * @return all child nodes and this block rendered into one character sequence.
   */
  @Nullable @Override public CharSequence render(@Nonnull AndroidContext context, @Nonnull CDARichNode node) {
    final CDARichBlock block = (CDARichBlock) node;

    final SpannableStringBuilder result = new SpannableStringBuilder();

    if (block.getContent() != null) {
      CDARichNode previousChild = null;
      for (final CDARichNode childNode : block.getContent()) {
        if (childNode != null) {
          final CharSequence childResult = processor.process(context, childNode);
          if (childResult != null) {
            if (previousChild instanceof CDARichParagraph && childNode instanceof CDARichParagraph) {
              result.append("\n");
            }
            result.append(childResult);
            previousChild = childNode;
          }
        }
      }
    }
    childWithNewline(result);

    return wrap(node, indent(context, node, decorate(context, node, result)));
  }

  /**
   * Ensures child ends on exactly one newline.
   * This method preserves all spans while removing trailing newlines.
   *
   * @param builder the current rendered spannable builder.
   */
  @Nonnull
  protected void childWithNewline(@Nonnull SpannableStringBuilder builder) {
    if (builder != null && builder.length() > 0) {
      int length = builder.length();
      while (length > 0 && builder.charAt(length - 1) == '\n') {
        length--;
      }
      if (length < builder.length()) {
        builder.delete(length, builder.length());
      }
    }
  }

  /**
   * Prefix given builder by a custom decoration.
   *
   * @param context the context this decoration should be applied to.
   * @param node    the actual node, rendered into the builder.
   * @param builder a builder for generating the desired output.
   * @return builder for convenience.
   */
  @Nonnull
  protected SpannableStringBuilder decorate(@Nonnull AndroidContext context, @Nonnull CDARichNode node, @Nonnull SpannableStringBuilder builder) {
    return builder;
  }

  /**
   * Surround the given builder, if needed
   *
   * @param node    the node to be rendered.
   * @param builder the result of the rendering.
   * @return the rendered result plus a optional wrap.
   */
  @NonNull
  public SpannableStringBuilder wrap(@Nonnull CDARichNode node, @Nonnull SpannableStringBuilder builder) {
    return builder;
  }

  /**
   * Add inline spannables to the first character to ensure indentation as needed.
   *
   * @param context of the node to be indented.
   * @param node    the node to be indented.
   * @param builder the result of the node's children to be indented.
   * @return an indented spannable.
   */
  @Nonnull SpannableStringBuilder indent(
      @Nonnull AndroidContext context,
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
      if (!block.getContent().isEmpty()) {
        if (block.getContent().get(0) instanceof CDARichBlock) {
          builder.append("\n");
        }
      }
      builder.setSpan(new LeadingMarginSpan.Standard(lists * 10 /*px*/), 0, builder.length(), SPAN_EXCLUSIVE_EXCLUSIVE);
    }

    return builder;
  }
}
