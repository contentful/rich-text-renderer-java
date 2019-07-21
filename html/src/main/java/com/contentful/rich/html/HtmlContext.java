package com.contentful.rich.html;

import com.contentful.java.cda.rich.CDARichBlock;
import com.contentful.java.cda.rich.CDARichNode;
import com.contentful.rich.core.Context;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

/**
 * The HTML Context: saving all html nodes up to the root.
 */
public class HtmlContext extends Context<List<CDARichNode>> {
  private final List<CDARichNode> path = new ArrayList<>();

  /**
   * Called by the infrastructure once a paragraph is entered.
   *
   * @param paragraph the paragraph node entered
   */
  @Override public void onBlockEntered(@Nonnull CDARichBlock paragraph) {
    path.add(paragraph);
  }

  /**
   * This method gets called by the infrastructure once a parent node was exited.
   *
   * @param block the paragraph node exited
   */
  @Override public void onBlockExited(@Nonnull CDARichBlock block) {
    path.remove(path.size() - 1);
  }

  /**
   * @return the path up to the root element.
   */
  @Override @Nonnull public List<CDARichNode> getPath() {
    return path;
  }
}
