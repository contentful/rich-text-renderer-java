package com.contentful.rich.android;

import android.content.Context;

import com.contentful.rich.android.renderer.AndroidRenderer;
import com.contentful.rich.core.Processor;

import javax.annotation.Nonnull;

/**
 * Processor returning char sequenced rich text.
 */
public class CharSequenceProcessor extends Processor<RichTextContext, CharSequence> {
  private final Context androidContext;

  /**
   * Create a new empty processor.
   *
   * @param context the android Context used for further processing of the nodes.
   */
  public CharSequenceProcessor(Context context) {
    super(new RichTextContext(context));
    this.androidContext = context;

    new CharSequenceRendererProvider().provide(this);
  }

  @Nonnull
  public Processor addRenderer(@Nonnull AndroidRenderer renderer) {
    return super.addRenderer(renderer, renderer);
  }
}
