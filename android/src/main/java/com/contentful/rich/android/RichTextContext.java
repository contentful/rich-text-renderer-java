package com.contentful.rich.android;

import com.contentful.java.cda.rich.CDARichBlock;
import com.contentful.java.cda.rich.CDARichList;
import com.contentful.java.cda.rich.CDARichNode;
import com.contentful.rich.core.Context;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class RichTextContext extends Context<List<CDARichNode>> {

  private final List<CDARichNode> path = new ArrayList<>();
  private int siblingIndex = -1;

  @Override public void onBlockEntered(@Nonnull CDARichBlock block) {
    super.onBlockEntered(block);

    path.add(block);
  }

  @Override public void onBlockExited(@Nonnull CDARichBlock block) {
    super.onBlockExited(block);

    path.remove(path.size() - 1);
  }

  @Override public void onSiblingEncountered(@Nonnull CDARichNode node, int index) {
    super.onSiblingEncountered(node, index);

    siblingIndex = index;
  }

  @Nullable @Override public List<CDARichNode> getPath() {
    return path;
  }

  @Nullable
  public CDARichList getTopListOfPath() {
    for (int i = path.size() - 2; i >= 0; --i) {
      final CDARichNode node = path.get(i);
      if (node instanceof CDARichList) {
        return (CDARichList) node;
      }
    }
    return null;
  }

  public int getSiblingIndex() {
    return siblingIndex;
  }
}
