package com.contentful.rich.core.simple;

import com.contentful.java.cda.rich.CDARichBlock;
import com.contentful.java.cda.rich.CDARichHyperLink;
import com.contentful.java.cda.rich.CDARichNode;

import java.util.ArrayList;
import java.util.List;

/**
 * This simplification will remove all nodes below a given nesting level. If more then the given amount of depth is
 * reached, no children will be present.
 * <p>
 * This simplification is best to be used first in the chain of simplifications, so that subsequent simplifications can
 * work on an already reduced graph.
 */
public class RemoveToDeepNesting implements Simplification {

  private static final int DEFAULT_LEVEL_DEPTH = 4;
  private final int level;

  /**
   * Create a simplification using {@link #DEFAULT_LEVEL_DEPTH} number of levels.
   */
  public RemoveToDeepNesting() {
    this(DEFAULT_LEVEL_DEPTH);
  }

  /**
   * Create a simplification using custom number of levels as a maximum.
   *
   * @param level
   */
  public RemoveToDeepNesting(int level) {
    this.level = level;
  }

  /**
   * Start the heavy lifting of reducing the graph.
   *
   * @param node the graph root node to start simplification on.
   * @return the reduced graph.
   */
  @Override public CDARichNode simplify(CDARichNode node) {
    return simplifyByLevel(node, level);
  }

  private CDARichNode simplifyByLevel(CDARichNode node, int level) {
    if (level <= 0) {
      return null;
    }

    if (node == null) {
      return null;
    }

    if (node instanceof CDARichBlock) {
      final CDARichBlock block = (CDARichBlock) node;
      final List<CDARichNode> simplifiedChildren = new ArrayList<>();
      for (final CDARichNode contentNode : block.getContent()) {
        final CDARichNode simplified = simplifyByLevel(contentNode, level - 1);
        if (simplified != null) {
          simplifiedChildren.add(simplified);
        }
      }

      if (simplifiedChildren.size() == 0 && !(block instanceof CDARichHyperLink)) {
        return null;
      } else {
        block.getContent().clear();
        block.getContent().addAll(simplifiedChildren);
        return block;
      }
    }

    return node;
  }
}
