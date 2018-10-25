package com.contentful.rich.android.renderer.chars;

import com.contentful.rich.android.AndroidProcessor;
import com.contentful.rich.android.renderer.listdecorator.BulletDecorator;
import com.contentful.rich.android.renderer.listdecorator.LowerCaseCharacterDecorator;
import com.contentful.rich.android.renderer.listdecorator.LowerCaseRomanNumeralsDecorator;
import com.contentful.rich.android.renderer.listdecorator.NumbersDecorator;
import com.contentful.rich.android.renderer.listdecorator.UpperCaseCharacterDecorator;
import com.contentful.rich.android.renderer.listdecorator.UpperCaseRomanNumeralsDecorator;

import javax.annotation.Nonnull;

/**
 * This provider will be used to fill the processor with renderer used in the text rendering for android.
 */
public class CharSequenceRendererProvider {

  /**
   * Add all relevant renderers to the AndroidProcessor.
   *
   * @param processor to be modified.
   */
  public void provide(@Nonnull AndroidProcessor<CharSequence> processor) {
    processor.addRenderer(new HorizontalRuleRenderer(processor));
    processor.addRenderer(new TextRenderer(processor));
    processor.addRenderer(new HeadingRenderer(processor));
    processor.addRenderer(new ListRenderer(processor, new BulletDecorator()));
    processor.addRenderer(new ListRenderer(processor,
        new NumbersDecorator(),
        new UpperCaseCharacterDecorator(),
        new LowerCaseRomanNumeralsDecorator(),
        new LowerCaseCharacterDecorator(),
        new LowerCaseCharacterDecorator(),
        new UpperCaseRomanNumeralsDecorator()
    ));
    processor.addRenderer(new EmbeddedLinkRenderer(processor));
    processor.addRenderer(new HyperLinkRenderer(processor));
    processor.addRenderer(new QuoteRenderer(processor));
    processor.addRenderer(new BlockRenderer(processor));
  }
}
