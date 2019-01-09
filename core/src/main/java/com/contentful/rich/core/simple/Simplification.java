package com.contentful.rich.core.simple;

import com.contentful.java.cda.rich.CDARichNode;

/**
 * This interface specifies a simplification: An option to be done on the graph of rich text nodes.
 */
public interface Simplification {
  /**
   * Simplify the given graph pointed to by node.
   * @param node the graph root node to start simplification on.
   * @return a simplified node graph.
   */
  CDARichNode simplify(CDARichNode node);
}
