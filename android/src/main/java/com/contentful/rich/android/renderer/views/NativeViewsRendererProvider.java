package com.contentful.rich.android.renderer.views;

import android.view.View;

import com.contentful.rich.android.AndroidProcessor;

import javax.annotation.Nonnull;

/**
 * Provider for all native renderers.
 */
public class NativeViewsRendererProvider {
  /**
   * Fill up the given processor with the default renderers supporting native views.
   *
   * @param processor the processor to be augmented.
   */
  public void provide(@Nonnull AndroidProcessor<View> processor) {
    processor.addRenderer(new TextRenderer(processor));
    processor.addRenderer(new HorizontalRuleRenderer(processor));
  }
}
