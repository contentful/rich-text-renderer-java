package com.contentful.rich.android.renderer.views;

import android.view.View;

import com.contentful.java.cda.rich.CDARichHorizontalRule;
import com.contentful.java.cda.rich.CDARichNode;
import com.contentful.rich.android.AndroidContext;
import com.contentful.rich.android.AndroidProcessor;
import com.contentful.rich.android.AndroidRenderer;
import com.contentful.rich.android.R;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * This renderer will render a rich text node into a TextView, respecting it's marks.
 */
public class HorizontalRuleRenderer extends AndroidRenderer<AndroidContext, View> {
  /**
   * Constructor taking a processor for child processing.
   * <p>
   * Since CDARichText do not have children, the parameter will be ignored.
   *
   * @param processor used for subrendering of children.
   */
  public HorizontalRuleRenderer(@Nonnull AndroidProcessor<View> processor) {
    super(processor);
  }

  /**
   * Is the incoming node a horizontal rule?
   *
   * @param context context this check should be performed in
   * @param node    node to be checked
   * @return true if the node is a rich rule node.
   */
  @Override public boolean canRender(@Nullable AndroidContext context, @Nonnull CDARichNode node) {
    return node instanceof CDARichHorizontalRule;
  }

  /**
   * Creates a horizontal line.
   *
   * @param context the generic context this node should be rendered in.
   * @param node    the node to be rendered.
   * @return a view representing a horizontal line.
   */
  @Nullable @Override public View render(@Nonnull AndroidContext context, @Nonnull CDARichNode node) {
    return context.getInflater().inflate(R.layout.rich_horizontal_rule_layout, null);
  }
}
