package com.contentful.rich.core;

import com.contentful.java.cda.rich.CDARichNode;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Implementing classes of this interface will control how the given node will be rendered in the
 * given Context.
 *
 * @param <C> the custom context class to passed
 * @param <R> the result type of the rendering
 * @see RenderabilityChecker
 */
public interface Renderer<C, R> {
  /**
   * Renders the given node in the given context.
   *
   * @param context the generic context this node should be rendered in.
   * @param node    the node to be rendered.
   * @return the rendered node, unless this renderer encountered an error.
   */
  @Nullable R render(
      @Nonnull C context,
      @Nonnull CDARichNode node
  );
}
