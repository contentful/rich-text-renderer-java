package com.contentful.rich.core;

import com.contentful.java.cda.rich.CDARichBlock;
import com.contentful.java.cda.rich.CDARichNode;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Class for handling context switches while parsing the rich text.
 *
 * @param <T> Parameter class to be used for the path.
 */
public class Context<T> {

  /**
   * This method gets called by the {@see Processor} when a new block node was found and entered.
   *
   * @param block the block node entered
   */
  public void onBlockEntered(@Nonnull CDARichBlock block) {
  }

  /**
   * This method gets called by the {@see Processor} when a block was exited.
   *
   * @param block the block node exited
   */
  public void onBlockExited(@Nonnull CDARichBlock block) {
  }

  /**
   * This method gets called by the {@see Processor} when a new sibling was entered.
   *
   * @param node the block node exited
   */
  public void onSiblingEncountered(@Nonnull CDARichNode node, int index) {
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
