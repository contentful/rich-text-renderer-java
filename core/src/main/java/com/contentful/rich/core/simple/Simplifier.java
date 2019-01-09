package com.contentful.rich.core.simple;

import com.contentful.java.cda.rich.CDARichNode;

/**
 * This class will be used to simplify a given graph of rich text nodes, so only non empty nodes survive.
 */
public class Simplifier {

  private final Simplification[] simplifications;

  /**
   * Create a simplifier removing empties.
   */
  public Simplifier() {
    this(new RemoveEmpties());
  }

  /**
   * Construct a custom simplifier applying custom simplifications.
   *
   * @param simplifications to be applied on the graph.
   */
  public Simplifier(Simplification... simplifications) {
    if (simplifications == null || simplifications.length == 0) {
      this.simplifications = new Simplification[]{new RemoveEmpties()};
    } else {
      this.simplifications = simplifications;
    }
  }

  /**
   * This method does the heavy lifting of reducing the graph.
   *
   * @param node the node node to be started to be checked for empty nodes.
   * @return a cleaned graph.
   */
  public CDARichNode simplify(CDARichNode node) {
    for (final Simplification simplification : simplifications) {
      if (node != null) {
        node = simplification.simplify(node);
      }
    }
    return node;
  }
}
