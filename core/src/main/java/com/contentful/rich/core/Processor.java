package com.contentful.rich.core;

import com.contentful.java.cda.rich.CDARichBlock;
import com.contentful.java.cda.rich.CDARichNode;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * This base class for all processing needed to be done on a rich text type. It will walk
 * through all the nodes of a rich text element and call associated node renderer and
 * checkers on it.
 * <p>
 * Use {@see #addRenderer} for adding more low level renders on it or use one of the extension
 * modules.
 *
 * @param <C> the context type used throughout this rendering process.
 * @param <R> the result type of the rendering process.
 * @see Renderer
 * @see Checker
 */
public class Processor<C extends Context, R> {

  @Nonnull
  private final List<CheckingRenderer<C, R>> nodeRenderer = new ArrayList<>();

  /**
   * Create a new empty processor.
   */
  public Processor() {
  }

  /**
   * Add a renderer to the end of the list of available renders.
   * <p>
   * If a renderer before hand matches the node, the new renderer might not be executed. If you want
   * to make sure your renderer is considered first, either {@see #reset} the list of renderer, or
   * {@see #overrideRenderer} to make your renderer the first to be checked, overriding other
   * renderer added.
   *
   * @param renderer may not be null.
   * @return this renderer for chaining.
   */
  @Nonnull
  public Processor addRenderer(@Nonnull Checker<C> checker, @Nonnull Renderer<C, R> renderer) {
    nodeRenderer.add(new CheckingRenderer<>(checker, renderer));
    return this;
  }

  /**
   * Adds the renderer to the front of the list of renderer, overriding similar renderer if needed.
   *
   * @param renderer may not be null.
   * @return this renderer for chaining.
   */
  @Nonnull
  public Processor overrideRenderer(@Nonnull Checker<C> checker, @Nonnull Renderer<C, R> renderer) {
    nodeRenderer.add(0, new CheckingRenderer<>(checker, renderer));
    return this;
  }

  /**
   * Start the node rendering process.
   * <p>
   * This process will process the node and all of it's child nodes by using the added renderer.
   *
   * @param node a not null node to be rendered.
   * @return the result in the form given by the renderer.
   */
  @Nullable public R process(@Nonnull C context, @Nonnull CDARichNode node) {
    if (node instanceof CDARichBlock) {
      context.onBlockEntered((CDARichBlock) node);
    }

    R result = null;
    for (final CheckingRenderer<C, R> pair : nodeRenderer) {
      final Checker<C> checker = pair.checker;

      if (checker.check(context, node)) {
        final Renderer<C, R> renderer = pair.renderer;
        result = renderer.render(context, node);
        if (result != null) {
          break;
        }
      }
    }

    if (node instanceof CDARichBlock) {
      context.onBlockExited((CDARichBlock) node);
    }

    return result;
  }

  /**
   * Internal class for combining checkers and renderer.
   *
   * @param <C> Custom context class
   * @param <R> Result class of the rendering
   */
  private static class CheckingRenderer<C, R> {
    final Checker<C> checker;
    final Renderer<C, R> renderer;

    /**
     * Construct a new CheckerRenderer
     *
     * @param checker  the checker to be used
     * @param renderer the renderer to be checked
     */
    CheckingRenderer(@Nonnull Checker<C> checker, @Nonnull Renderer<C, R> renderer) {
      this.checker = checker;
      this.renderer = renderer;
    }
  }
}
