package com.contentful.rich.html.renderer;

import com.contentful.java.cda.rich.CDARichNode;
import com.contentful.rich.core.Processor;
import com.contentful.rich.html.HtmlContext;

import javax.annotation.Nonnull;

/**
 * This renderer updates it's tag based on the incoming node.
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

  @Nonnull @Override protected String startTag(@Nonnull CDARichNode node) {
    return "<" + provider.getTag(node) + ">\n";
  }

  @Nonnull @Override protected String endTag(@Nonnull CDARichNode node) {
    return "</" + provider.getTag(node) + ">\n";
  }

  public interface TagNameProvider {
    @Nonnull
    String getTag(@Nonnull CDARichNode node);
  }
}
