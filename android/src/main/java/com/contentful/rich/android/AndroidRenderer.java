package com.contentful.rich.android;

import com.contentful.java.cda.rich.CDARichNode;
import com.contentful.rich.core.Checker;
import com.contentful.rich.core.Context;
import com.contentful.rich.core.Renderer;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * This is a combination interface for android renderer
 *
 * @param <C> context to be applied to this renderer.
 * @param <R> result of rendering a node.
 */
public abstract class AndroidRenderer<C extends Context, R> implements Checker<C>, Renderer<C, R> {
  protected final AndroidProcessor<R> processor;

  /**
   * Create an Android Renderer, containing its processor for child node rendering.
   *
   * @param processor save the processor for child processing.
   */
  public AndroidRenderer(@Nonnull AndroidProcessor<R> processor) {
    this.processor = processor;
  }

  /**
   * Overwritten to check on whether this renderer can be used to render this node in the given context.
   *
   * @param context context this check should be performed in
   * @param node    node to be checked
   * @return true if the node can be rendered in the context.
   */
  @Override abstract public boolean check(@Nullable C context, @Nonnull CDARichNode node);

  /**
   * Performs the rendering resulting in type R.
   *
   * @param context the generic context this node should be rendered in.
   * @param node    the node to be rendered.
   * @return the rendered result.
   */
  @Nullable @Override abstract public R render(@Nonnull C context, @Nonnull CDARichNode node);
}
