package com.contentful.rich.android.renderer.chars;

import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import com.contentful.java.cda.rich.CDARichHeading;
import com.contentful.java.cda.rich.CDARichNode;
import com.contentful.rich.android.AndroidContext;
import com.contentful.rich.android.AndroidProcessor;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

/**
 * Render all available headings into a spannable changing it's vertical size.
 */
public class HeadingRenderer extends BlockRenderer {

  static private final List<Integer> SIZE_MAP = new ArrayList<>();

  static {
    SIZE_MAP.add(72); // Level 1
    SIZE_MAP.add(60); // Level 2
    SIZE_MAP.add(52); // Level 3
    SIZE_MAP.add(44); // Level 4
    SIZE_MAP.add(36); // Level 5
    SIZE_MAP.add(28); // Level 6
  }

  public HeadingRenderer(@Nonnull AndroidProcessor<CharSequence> processor) {
    super(processor);
  }

  /**
   * Is the given node a CDARichHeading?
   *
   * @param context context this check should be performed in
   * @param node    node to be checked
   * @return true if node is a rich CDARichHeading.
   */
  @Override public boolean canRender(@Nullable AndroidContext context, @Nonnull CDARichNode node) {
    if (node instanceof CDARichHeading) {
      final CDARichHeading heading = (CDARichHeading) node;
      if (heading.getLevel() > 0 && ((CDARichHeading) node).getLevel() < 7) {
        return true;
      }
    }

    return false;
  }

  /**
   * Add a size changing span to the rendered children.
   *
   * @param node    the node to be rendered.
   * @param builder the result of the rendering.
   * @return the builder, enhanced by a size altering span.
   */
  @Nonnull @Override
  public SpannableStringBuilder wrap(@Nonnull CDARichNode node, @Nonnull SpannableStringBuilder builder) {
    final CDARichHeading heading = (CDARichHeading) node;

    final AbsoluteSizeSpan sizeSpan = new AbsoluteSizeSpan(SIZE_MAP.get(heading.getLevel() - 1), true);
    builder.setSpan(sizeSpan, 0, builder.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

    return builder;
  }
}
