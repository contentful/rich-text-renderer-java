package com.contentful.rich.android;

import android.view.View;

import com.contentful.rich.android.renderer.chars.CharSequenceRendererProvider;
import com.contentful.rich.android.renderer.views.NativeViewsRendererProvider;
import com.contentful.rich.core.Processor;

import javax.annotation.Nonnull;

/**
 * Processor processing rich text to create Android output.
 * <p>
 * Use one of the factory methods to create an Android processor creating your desired output.
 *
 * @see AndroidProcessor#creatingCharSequences()
 */
public class AndroidProcessor<T> extends Processor<AndroidContext, T> {

  /**
   * Hide constructor to force creation through factory methods.
   */
  private AndroidProcessor() {
  }


  /**
   * Create an Android Processor capable of generating char sequences from rich text input nodes.
   *
   * @return AndroidProcessor to create char sequences
   * @see android.text.Spannable
   */
  public static AndroidProcessor<CharSequence> creatingCharSequences() {
    final AndroidProcessor<CharSequence> processor = new AndroidProcessor<>();
    new CharSequenceRendererProvider().provide(processor);

    return processor;
  }

  public static AndroidProcessor<View> creatingNativeViews() {
    final AndroidProcessor<View> processor = new AndroidProcessor<View>();
    new NativeViewsRendererProvider().provide(processor);

    return processor;
  }

  /**
   * Add a renderer to the processor using only one class.
   *
   * @param renderer the combined Android renderer
   * @return this instance for chaining
   * @see com.contentful.rich.core.Checker
   * @see com.contentful.rich.core.Renderer
   */
  @Nonnull
  public Processor addRenderer(@Nonnull AndroidRenderer renderer) {
    return super.addRenderer(renderer, renderer);
  }
}
