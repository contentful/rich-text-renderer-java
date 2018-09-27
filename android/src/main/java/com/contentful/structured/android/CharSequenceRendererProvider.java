package com.contentful.structured.android;

import com.contentful.structured.android.renderer.charsequence.HeadingRenderer;
import com.contentful.structured.android.renderer.charsequence.TextRenderer;

import javax.annotation.Nonnull;

/**
 * This provider will be used to fill the processor with renderer used in the text rendering for android.
 */
public class CharSequenceRendererProvider {
  public void provide(@Nonnull CharSequenceProcessor processor) {
    processor.addRenderer(new TextRenderer());
    processor.addRenderer(new HeadingRenderer(processor));
  }
}
