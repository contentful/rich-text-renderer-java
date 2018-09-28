package com.contentful.rich.core;

import com.contentful.java.cda.rich.CDARichNode;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Implementer of this interface will be able to tell if a given node can be rendered in the given
 * context.
 *
 * @param <C> Custom Context Class
 * @see Renderer
 */
public interface Checker<C> {

  /**
   * Return true if the associated renderer can render the node.
   *
   * @param context context this check should be performed in
   * @param node    node to be checked
   * @return false if the renderer cannot be used.
   */
  boolean check(@Nullable C context, @Nonnull CDARichNode node);
}
