package com.contentful.rich.android;

import com.contentful.java.cda.rich.CDARichBlock;
import com.contentful.java.cda.rich.CDARichList;
import com.contentful.java.cda.rich.CDARichNode;
import com.contentful.rich.core.Context;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * This context stores all encountered path parents and the android context.
 */
public class AndroidContext extends Context<List<CDARichNode>> {

  private final android.content.Context androidContext;
  private final List<CDARichNode> path = new ArrayList<>();

  /**
   * Initialize this rich text rendering context by the help of an android context
   *
   * @param androidContext this android context will be used for android specific tasks renderers are executing.
   */
  public AndroidContext(android.content.Context androidContext) {
    this.androidContext = androidContext;
  }

  /**
   * @return the Android context provided.
   */
  public android.content.Context getAndroidContext() {
    return this.androidContext;
  }

  /**
   * This method is called once a block of rich text is encountered.
   *
   * @param block encountered block.
   */
  @Override public void onBlockEntered(@Nonnull CDARichBlock block) {
    super.onBlockEntered(block);

    path.add(block);
  }

  /**
   * Once an encountered block is exited, this method gets called.
   *
   * @param block exited block.
   */
  @Override public void onBlockExited(@Nonnull CDARichBlock block) {
    super.onBlockExited(block);

    path.remove(path.size() - 1);
  }

  /**
   * @return the path of nodes up to the root node.
   */
  @Nullable @Override public List<CDARichNode> getPath() {
    return path;
  }

  /**
   * @return the first rich list of the path.
   */
  @Nullable public CDARichList getTopListOfPath() {
    for (int i = path.size() - 2; i >= 0; --i) {
      final CDARichNode node = path.get(i);
      if (node instanceof CDARichList) {
        return (CDARichList) node;
      }
    }
    return null;
  }
}
