package com.contentful.structured.html;

import com.contentful.java.cda.structured.CDAStructuredNode;
import com.contentful.java.cda.structured.CDAStructuredParagraph;
import com.contentful.structured.core.Context;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

/**
 * The HTML Context: saving all html nodes up to the root.
 */
public class HtmlContext extends Context<List<CDAStructuredNode>> {
  final List<CDAStructuredNode> path = new ArrayList<>();
  final String indentation;

  /**
   * Create a default context with indentation of two spaces.
   */
  public HtmlContext() {
    this("  ");
  }

  /**
   * Create a context using the given string as indentation for siblings.
   *
   * @param indentation a string used for indentation. Use spaces only.
   */
  public HtmlContext(String indentation) {
    this.indentation = indentation;
  }

  /**
   * Returns the indentation used for one level.
   */
  public String getIndentation() {
    return indentation;
  }

  /**
   * Called by the infrastructure once a paragraph is entered.
   *
   * @param paragraph the paragraph node entered
   */
  @Override public void onParagraphEntered(@Nonnull CDAStructuredParagraph paragraph) {
    path.add(paragraph);
  }

  /**
   * This method gets called by the infrastructure once a parent node was exited.
   *
   * @param paragraph the paragraph node exited
   */
  @Override public void onParagraphExited(@Nonnull CDAStructuredParagraph paragraph) {
    path.remove(path.size() - 1);
  }

  /**
   * Once a sibling is found, this method will get called.
   *
   * @param node  the block node exited
   * @param index the index of the child currently visited.
   */
  @Override
  public void onSiblingEncountered(@Nonnull CDAStructuredNode node, int index) {
  }

  /**
   * @return the path up to the root element.
   */
  @Override @Nonnull public List<CDAStructuredNode> getPath() {
    return path;
  }
}
