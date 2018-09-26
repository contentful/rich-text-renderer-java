package com.contentful.structured.core;

import com.contentful.java.cda.structured.CDAStructuredNode;
import com.contentful.java.cda.structured.CDAStructuredParagraph;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Class for handling context switches while parsing the structured text.
 *
 * @param <T> Parameter class to be used for the path.
 */
public class Context<T> {

  /**
   * This method gets called by the {@see Processor} when a new paragraph node was found and entered.
   *
   * @param paragraph the paragraph node entered
   */
  public void onParagraphEntered(@Nonnull CDAStructuredParagraph paragraph) {
  }

  /**
   * This method gets called by the {@see Processor} when a paragraph was exited.
   *
   * @param paragraph the paragraph node exited
   */
  public void onParagraphExited(@Nonnull CDAStructuredParagraph paragraph) {
  }

  /**
   * This method gets called by the {@see Processor} when a new sibling was entered.
   *
   * @param node the block node exited
   */
  public void onSiblingEncountered(@Nonnull CDAStructuredNode node, int index) {
  }

  /**
   * See implementors and or test cases for usage of this method.
   *
   * @return the accumulated path of this context.
   */
  @Nullable public T getPath() {
    return null;
  }
}
