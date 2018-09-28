package com.contentful.rich.html;

import com.contentful.java.cda.rich.CDARichDocument;
import com.contentful.java.cda.rich.CDARichEmbeddedLink;
import com.contentful.java.cda.rich.CDARichHeading;
import com.contentful.java.cda.rich.CDARichHorizontalRule;
import com.contentful.java.cda.rich.CDARichHyperLink;
import com.contentful.java.cda.rich.CDARichListItem;
import com.contentful.java.cda.rich.CDARichNode;
import com.contentful.java.cda.rich.CDARichOrderedList;
import com.contentful.java.cda.rich.CDARichParagraph;
import com.contentful.java.cda.rich.CDARichQuote;
import com.contentful.java.cda.rich.CDARichText;
import com.contentful.java.cda.rich.CDARichUnorderedList;
import com.contentful.rich.core.Checker;
import com.contentful.rich.core.Processor;
import com.contentful.rich.core.Renderer;
import com.contentful.rich.core.RendererProvider;
import com.contentful.rich.html.renderer.DynamicTagRenderer;
import com.contentful.rich.html.renderer.TagRenderer;
import com.contentful.rich.html.renderer.TagWithArgumentsRenderer;
import com.contentful.rich.html.renderer.TextRenderer;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nonnull;

/**
 * This provider of renderer will provide all available default renderer for creating html output
 * from rich text.
 *
 * @see Processor#addRenderer(Checker, Renderer)
 * @see Processor#render(CDARichNode)
 */
public class HtmlRendererProvider implements RendererProvider<HtmlContext, String> {

  /**
   * Call this method with a processor to add all default html renderer.
   *
   * @param processor the processor to be filled up with renderer.
   */
  @Override public void provide(@Nonnull Processor<HtmlContext, String> processor) {
    processor.addRenderer(
        (context, node) -> node instanceof CDARichText,
        new TextRenderer()
    );
    processor.addRenderer((context, node) -> node instanceof CDARichHorizontalRule,
        (context, node) -> "<hr/>");
    processor.addRenderer(
        (context, node) -> node instanceof CDARichDocument,
        new TagRenderer(processor, "div")
    );
    processor.addRenderer(
        (context, node) -> node instanceof CDARichEmbeddedLink && ((CDARichEmbeddedLink) node).getData() != null,
        new TagWithArgumentsRenderer(
            processor,
            "div",
            (node) -> mapifyArguments("entry", ((CDARichEmbeddedLink) node).getData().toString()))
    );
    processor.addRenderer(
        (context, node) -> node instanceof CDARichHyperLink && ((CDARichHyperLink) node).getData() != null,
        new TagWithArgumentsRenderer(
            processor,
            "a",
            (node) -> mapifyArguments("href", (String) ((CDARichHyperLink) node).getData()))
    );
    processor.addRenderer(
        (context, node) -> node instanceof CDARichHyperLink && ((CDARichHyperLink) node).getData() == null,
        new TagRenderer(processor, "a")
    );
    processor.addRenderer(
        (context, node) -> node instanceof CDARichQuote,
        new TagRenderer(processor, "blockquote")
    );
    processor.addRenderer(
        (context, node) ->
            node instanceof CDARichHeading
                && ((CDARichHeading) node).getLevel() >= 1
                && ((CDARichHeading) node).getLevel() <= 6,
        new DynamicTagRenderer(processor, (node -> "h" + ((CDARichHeading) node).getLevel()))
    );
    processor.addRenderer(
        (context, node) -> node instanceof CDARichOrderedList,
        new TagRenderer(processor, "ol")
    );
    processor.addRenderer(
        (context, node) -> node instanceof CDARichListItem,
        new TagRenderer(processor, "li")
    );
    processor.addRenderer(
        (context, node) -> node instanceof CDARichUnorderedList,
        new TagRenderer(processor, "ul")
    );
    // needs to be last but one
    processor.addRenderer(
        (context, node) -> node instanceof CDARichParagraph,
        new TagRenderer(processor, "p")
    );
  }

  @Nonnull
  private Map<String, String> mapifyArguments(@Nonnull String... args) {
    final HashMap<String, String> result = new HashMap<>(args.length / 2);
    for (int i = 0; i < args.length; i += 2) {
      final String key = args[i];
      final String value = args[i + 1];
      result.put(key, value);
    }
    return result;
  }
}
