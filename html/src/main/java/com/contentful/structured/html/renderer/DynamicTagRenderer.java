package com.contentful.structured.html.renderer;

import com.contentful.java.cda.structured.CDAStructuredNode;
import com.contentful.structured.core.Processor;
import com.contentful.structured.html.HtmlContext;

import javax.annotation.Nonnull;

/**
 * This renderer updates it's tag based on the incomming node.
 */
public class DynamicTagRenderer extends TagRenderer {
  private final TagNameProvider provider;

  /**
   * Constructs a new tag renderer
   *
   * @param processor the processor containing all renderer
   * @param provider  a provider for dynamic tag names
   */
  public DynamicTagRenderer(Processor<HtmlContext, String> processor, TagNameProvider provider) {
    super(processor, null);
    this.provider = provider;
  }

  @Nonnull @Override protected String startTag(@Nonnull CDAStructuredNode node) {
    return "<" + provider.getTag(node) + ">\n";
  }

  @Nonnull @Override protected String endTag(@Nonnull CDAStructuredNode node) {
    return "</" + provider.getTag(node) + ">\n";
  }

  public interface TagNameProvider {
    @Nonnull
    String getTag(@Nonnull CDAStructuredNode node);
  }
}
