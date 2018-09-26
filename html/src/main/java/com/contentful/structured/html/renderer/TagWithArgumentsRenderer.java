package com.contentful.structured.html.renderer;

import com.contentful.java.cda.structured.CDAStructuredNode;
import com.contentful.structured.core.Processor;
import com.contentful.structured.html.HtmlContext;

import java.util.Map;

import javax.annotation.Nonnull;

/**
 * Adds arguments to a tag when rendering a Structured Text.
 */
public class TagWithArgumentsRenderer extends TagRenderer {
  private final ArgumentsProvider provider;

  /**
   * Create a tag renderer.
   *
   * @param processor the processor containing all renderers.
   * @param tag       the tag to be rendered.
   * @param provider  the provider of arguments.
   */
  public TagWithArgumentsRenderer(Processor<HtmlContext, String> processor, String tag, ArgumentsProvider provider) {
    super(processor, tag);
    this.provider = provider;
  }

  /**
   * Overridden to enable arguments parsing.
   *
   * @param node the node to be used by the provider to get the arguments.
   * @return a html tag including arguments.
   */
  @Nonnull @Override
  protected String startTag(@Nonnull CDAStructuredNode node) {
    return "<" + tag + " " + stringifyArgumentMap(provider.provide(node)) + ">\n";
  }

  String stringifyArgumentMap(Map<String, String> arguments) {
    final StringBuilder builder = new StringBuilder();
    for (final String key : arguments.keySet()) {
      builder.append(key).append("=").append('"').append(arguments.get(key)).append('"');
    }
    return builder.toString();
  }

  /**
   * Create arguments from a node.
   */
  public interface ArgumentsProvider {
    Map<String, String> provide(CDAStructuredNode node);
  }

}
