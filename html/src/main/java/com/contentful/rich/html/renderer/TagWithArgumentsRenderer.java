package com.contentful.rich.html.renderer;

import com.contentful.java.cda.rich.CDARichNode;
import com.contentful.rich.core.Processor;
import com.contentful.rich.html.HtmlContext;

import org.apache.commons.text.StringEscapeUtils;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;

/**
 * Adds arguments to a tag when rendering a Rich Text.
 */
public class TagWithArgumentsRenderer extends TagRenderer {
  private final ArgumentsProvider provider;

  @Nonnull
  public static Map<String, String> mapifyArguments(@Nonnull String... args) {
    final HashMap<String, String> result = new HashMap<>(args.length / 2);
    for (int i = 0; i < args.length; i += 2) {
      final String key = args[i];
      final String value = args[i + 1];
      result.put(key, value);
    }
    return result;
  }

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
  protected String startTag(@Nonnull CDARichNode node) {
    final Map<String, String> arguments = provider.provide(node);
    if (arguments == null || arguments.isEmpty()) {
      return "<" + tag + ">";
    }
    return "<" + tag + " " + stringifyArgumentMap(arguments) + ">";
  }

  String stringifyArgumentMap(Map<String, String> arguments) {
    final StringBuilder builder = new StringBuilder();
    for (final Map.Entry<String, String> argument : arguments.entrySet()) {
      if (argument.getValue() == null) {
        continue;
      }
      if (builder.length() > 0) {
        builder.append(' ');
      }
      // Values come from CMS content: escape them so a '"' can't end the attribute early and
      // inject new attributes (for example onmouseover) into the tag.
      builder.append(argument.getKey())
          .append("=\"")
          .append(StringEscapeUtils.escapeHtml4(argument.getValue()))
          .append('"');
    }
    return builder.toString();
  }

  /**
   * Create arguments from a node.
   */
  public interface ArgumentsProvider {
    Map<String, String> provide(CDARichNode node);
  }

}
