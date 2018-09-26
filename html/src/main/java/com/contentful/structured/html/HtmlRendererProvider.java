package com.contentful.structured.html;

import com.contentful.java.cda.structured.CDAStructuredDocument;
import com.contentful.java.cda.structured.CDAStructuredEmbeddedLink;
import com.contentful.java.cda.structured.CDAStructuredHeading;
import com.contentful.java.cda.structured.CDAStructuredHorizontalRule;
import com.contentful.java.cda.structured.CDAStructuredHyperLink;
import com.contentful.java.cda.structured.CDAStructuredListItem;
import com.contentful.java.cda.structured.CDAStructuredNode;
import com.contentful.java.cda.structured.CDAStructuredOrderedList;
import com.contentful.java.cda.structured.CDAStructuredParagraph;
import com.contentful.java.cda.structured.CDAStructuredQuote;
import com.contentful.java.cda.structured.CDAStructuredText;
import com.contentful.java.cda.structured.CDAStructuredUnorderedList;
import com.contentful.structured.core.Checker;
import com.contentful.structured.core.Processor;
import com.contentful.structured.core.Renderer;
import com.contentful.structured.core.RendererProvider;
import com.contentful.structured.html.renderer.DynamicTagRenderer;
import com.contentful.structured.html.renderer.TagRenderer;
import com.contentful.structured.html.renderer.TagWithArgumentsRenderer;
import com.contentful.structured.html.renderer.TextRenderer;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nonnull;

/**
 * This provider of renderer will provide all available default renderer for creating html output
 * from structured text.
 *
 * @see Processor#addRenderer(Checker, Renderer)
 * @see Processor#render(CDAStructuredNode)
 */
public class HtmlRendererProvider implements RendererProvider<HtmlContext, String> {

  /**
   * Call this method with a processor to add all default html renderer.
   *
   * @param processor the processor to be filled up with renderer.
   */
  @Override public void provide(@Nonnull Processor<HtmlContext, String> processor) {
    processor.addRenderer(
        (context, node) -> node instanceof CDAStructuredText,
        new TextRenderer()
    );
    processor.addRenderer((context, node) -> node instanceof CDAStructuredHorizontalRule,
        (context, node) -> "<hr/>");
    processor.addRenderer(
        (context, node) -> node instanceof CDAStructuredDocument,
        new TagRenderer(processor, "main")
    );
    processor.addRenderer(
        (context, node) -> node instanceof CDAStructuredEmbeddedLink && ((CDAStructuredEmbeddedLink) node).getData() != null,
        new TagWithArgumentsRenderer(
            processor,
            "div",
            (node) -> mapifyArguments("entry", ((CDAStructuredEmbeddedLink) node).getData().toString()))
    );
    processor.addRenderer(
        (context, node) -> node instanceof CDAStructuredHyperLink && ((CDAStructuredHyperLink) node).getData() != null,
        new TagWithArgumentsRenderer(
            processor,
            "a",
            (node) -> mapifyArguments("href", (String) ((CDAStructuredHyperLink) node).getData()))
    );
    processor.addRenderer(
        (context, node) -> node instanceof CDAStructuredHyperLink && ((CDAStructuredHyperLink) node).getData() == null,
        new TagRenderer(
            processor,
            "a")
    );
    processor.addRenderer(
        (context, node) -> node instanceof CDAStructuredQuote,
        new TagRenderer(processor, "blockquote")
    );
    processor.addRenderer(
        (context, node) ->
            node instanceof CDAStructuredHeading
                && ((CDAStructuredHeading) node).getLevel() >= 1
                && ((CDAStructuredHeading) node).getLevel() <= 6,
        new DynamicTagRenderer(processor, (node -> "h" + ((CDAStructuredHeading) node).getLevel()))
    );
    processor.addRenderer(
        (context, node) -> node instanceof CDAStructuredOrderedList,
        new TagRenderer(processor, "ol")
    );
    processor.addRenderer(
        (context, node) -> node instanceof CDAStructuredListItem,
        new TagRenderer(processor, "li")
    );
    processor.addRenderer(
        (context, node) -> node instanceof CDAStructuredUnorderedList,
        new TagRenderer(processor, "ul")
    );
    // needs to be last but one
    processor.addRenderer(
        (context, node) -> node instanceof CDAStructuredParagraph,
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
