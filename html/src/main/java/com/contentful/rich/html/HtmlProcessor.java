package com.contentful.rich.html;

import com.contentful.rich.core.Processor;

/**
 * Main processor for processing all HTML renderer content. Contains default renderer.
 */
public class HtmlProcessor extends Processor<HtmlContext, String> {
  /**
   * Construct the processor, including all html renderer.
   */
  public HtmlProcessor() {
    super();

    new HtmlRendererProvider().provide(this);
  }
}
