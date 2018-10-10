package com.contentful.rich.android.renderer;

import com.contentful.java.cda.rich.CDARichNode;
import com.contentful.rich.core.Checker;
import com.contentful.rich.core.Context;
import com.contentful.rich.core.Processor;
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
  protected final Processor<C, R> processor;

  public AndroidRenderer(@Nonnull Processor<C, R> processor) {
    this.processor = processor;
  }

  @Override abstract public boolean check(@Nullable C context, @Nonnull CDARichNode node);

  @Nullable @Override abstract public R render(@Nonnull C context, @Nonnull CDARichNode node);
}
