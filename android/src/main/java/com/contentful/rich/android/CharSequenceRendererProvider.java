package com.contentful.rich.android;

import com.contentful.rich.android.renderer.charsequence.BlockRenderer;
import com.contentful.rich.android.renderer.charsequence.HeadingRenderer;
import com.contentful.rich.android.renderer.charsequence.ListRenderer;
import com.contentful.rich.android.renderer.charsequence.QuoteRenderer;
import com.contentful.rich.android.renderer.charsequence.TextRenderer;
import com.contentful.rich.android.renderer.charsequence.listdecorator.BulletDecorator;
import com.contentful.rich.android.renderer.charsequence.listdecorator.LowerCaseCharacterDecorator;
import com.contentful.rich.android.renderer.charsequence.listdecorator.LowerCaseRomanNumeralsDecorator;
import com.contentful.rich.android.renderer.charsequence.listdecorator.NumbersDecorator;
import com.contentful.rich.android.renderer.charsequence.listdecorator.UpperCaseCharacterDecorator;
import com.contentful.rich.android.renderer.charsequence.listdecorator.UpperCaseRomanNumeralsDecorator;

import javax.annotation.Nonnull;

/**
 * This provider will be used to fill the processor with renderer used in the text rendering for android.
 */
public class CharSequenceRendererProvider {
  public void provide(@Nonnull CharSequenceProcessor processor) {
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
    processor.addRenderer(new QuoteRenderer(processor));
    processor.addRenderer(new BlockRenderer(processor));
  }
}
