package com.contentful.structured.android;

import android.content.Context;

import com.contentful.structured.android.renderer.AndroidRenderer;
import com.contentful.structured.core.Processor;

import javax.annotation.Nonnull;

/**
 * Processor returning char sequenced structured text.
 */
public class CharSequenceProcessor extends Processor<StructuredTextContext, CharSequence> {
  private final Context androidContext;

  /**
   * Create a new empty processor.
   *
   * @param context the android Context used for further processing of the nodes.
   */
  public CharSequenceProcessor(Context context) {
    super(new StructuredTextContext());
    this.androidContext = context;

    new CharSequenceRendererProvider().provide(this);
  }

  @Nonnull
  public Processor addRenderer(@Nonnull AndroidRenderer renderer) {
    return super.addRenderer(renderer, renderer);
  }
}
