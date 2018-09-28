package com.contentful.rich.html;

import com.contentful.rich.core.Processor;

/**
 * Main processor for processing all HTML renderer content. Contains default renderer.
 */
public class HtmlProcessor extends Processor<HtmlContext, String> {
  /**
   * Construct the processor.
   */
  public HtmlProcessor() {
    super(new HtmlContext());

    new HtmlRendererProvider().provide(this);
  }
}
