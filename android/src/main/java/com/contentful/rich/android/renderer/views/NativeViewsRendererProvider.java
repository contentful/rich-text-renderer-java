package com.contentful.rich.android.renderer.views;

import android.view.View;

import com.contentful.rich.android.AndroidProcessor;

import javax.annotation.Nonnull;

public class NativeViewsRendererProvider {
  public void provide(@Nonnull AndroidProcessor<View> processor) {
    processor.addRenderer(new TextRenderer(processor));
  }
}
