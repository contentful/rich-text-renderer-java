package com.contentful.rich.android.renderer.views;

import android.view.View;

import com.contentful.rich.android.AndroidProcessor;
import com.contentful.rich.android.renderer.chars.listdecorator.BulletDecorator;
import com.contentful.rich.android.renderer.chars.listdecorator.LowerCaseCharacterDecorator;
import com.contentful.rich.android.renderer.chars.listdecorator.LowerCaseRomanNumeralsDecorator;
import com.contentful.rich.android.renderer.chars.listdecorator.NumbersDecorator;
import com.contentful.rich.android.renderer.chars.listdecorator.UpperCaseCharacterDecorator;
import com.contentful.rich.android.renderer.chars.listdecorator.UpperCaseRomanNumeralsDecorator;

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
    processor.addRenderer(new ListRenderer(processor, new BulletDecorator()));
    processor.addRenderer(new ListRenderer(processor,
        new NumbersDecorator(),
        new UpperCaseCharacterDecorator(),
        new LowerCaseRomanNumeralsDecorator(),
        new LowerCaseCharacterDecorator(),
        new LowerCaseCharacterDecorator(),
        new UpperCaseRomanNumeralsDecorator()
    ));
    processor.addRenderer(new QuoteRenderer(processor));
    processor.addRenderer(new BlockRenderer(processor));
  }
}
