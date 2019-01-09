package com.contentful.rich.core.simple;

import com.contentful.java.cda.rich.CDARichBlock;
import com.contentful.java.cda.rich.CDARichHorizontalRule;
import com.contentful.java.cda.rich.CDARichHyperLink;
import com.contentful.java.cda.rich.CDARichNode;
import com.contentful.java.cda.rich.CDARichText;

import java.util.ArrayList;
import java.util.List;

/**
 * This simplification removes all empty nodes, may it be leaf or internal nodes.
 * <p>
 * An empty text node will get removed from it's parent node, as well as a inner node, who just wraps a node.
 */
public class RemoveEmpties implements Simplification {

  /**
   * Return a node without step stone or empty nodes.
   *
   * @param node the graph root node to start simplification on.
   * @return the simplified graph.
   */
  @Override public CDARichNode simplify(CDARichNode node) {
    if (node instanceof CDARichBlock) {
      return simplifyBlocks((CDARichBlock) node);
    } else if (node instanceof CDARichText) {
      return simplifyTextNode((CDARichText) node);
    } else if (node instanceof CDARichHorizontalRule) {
      return node;
    } else if (node != null) {
      throw new IllegalStateException("Non mapped node of type " + node.getClass().getSimpleName() + " found.");
    } else {
      throw new IllegalStateException("Cannot simplify null nodes!");
    }
  }

  private CDARichNode simplifyTextNode(CDARichText text) {
    if (text.getText() == null || text.getText().length() == 0) {
      return null;
    } else {
      return text;
    }
  }

  private CDARichBlock simplifyBlocks(CDARichBlock block) {
    if (block == null) {
      return null;
    }

    if (block.getContent().size() == 0 && !(block instanceof CDARichHyperLink)) {
      return null;
    } else if (block.getContent().size() == 1 && block.getContent().get(0).getClass().equals(block.getClass())) {
      return simplifyBlocks(simplifyBlocks((CDARichBlock) block.getContent().get(0)));
    } else {
      final List<CDARichNode> simplifiedChildren = new ArrayList<>();
      for (final CDARichNode node : block.getContent()) {
        final CDARichNode simplified = simplify(node);
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
  }
}
