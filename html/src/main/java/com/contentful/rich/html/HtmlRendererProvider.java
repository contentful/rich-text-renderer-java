package com.contentful.rich.html;

import com.contentful.java.cda.rich.CDARichDocument;
import com.contentful.java.cda.rich.CDARichHeading;
import com.contentful.java.cda.rich.CDARichHorizontalRule;
import com.contentful.java.cda.rich.CDARichHyperLink;
import com.contentful.java.cda.rich.CDARichListItem;
import com.contentful.java.cda.rich.CDARichNode;
import com.contentful.java.cda.rich.CDARichOrderedList;
import com.contentful.java.cda.rich.CDARichParagraph;
import com.contentful.java.cda.rich.CDARichQuote;
import com.contentful.java.cda.rich.CDARichTable;
import com.contentful.java.cda.rich.CDARichTableCell;
import com.contentful.java.cda.rich.CDARichTableHeaderCell;
import com.contentful.java.cda.rich.CDARichTableRow;
import com.contentful.java.cda.rich.CDARichText;
import com.contentful.java.cda.rich.CDARichUnorderedList;
import com.contentful.rich.core.Processor;
import com.contentful.rich.core.RenderabilityChecker;
import com.contentful.rich.core.Renderer;
import com.contentful.rich.html.renderer.DynamicTagRenderer;
import com.contentful.rich.html.renderer.TagRenderer;
import com.contentful.rich.html.renderer.TagWithArgumentsRenderer;
import com.contentful.rich.html.renderer.TextRenderer;

import javax.annotation.Nonnull;
import java.util.Map;

import static com.contentful.rich.html.renderer.TagWithArgumentsRenderer.mapifyArguments;

/**
 * This provider of renderer will provide all available default renderer for creating html output
 * from rich text.
 *
 * @see Processor#addRenderer(RenderabilityChecker, Renderer)
 * @see Processor#process(com.contentful.rich.core.Context, CDARichNode)
 */
class HtmlRendererProvider {

  /**
   * Call this method with a processor to add all default html renderer.
   *
   * @param processor the processor to be filled up with renderer.
   */
  void provide(@Nonnull Processor<HtmlContext, String> processor) {
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
        (context, node) -> node instanceof CDARichHyperLink && ((CDARichHyperLink) node).getData() instanceof String,
        new TagWithArgumentsRenderer(
            processor,
            "a",
            (node) -> mapifyArguments("href", (String) ((CDARichHyperLink) node).getData()))
    );
    processor.addRenderer(
            (context, node) -> node instanceof CDARichHyperLink && ((CDARichHyperLink) node).getData() instanceof Map,
            new TagWithArgumentsRenderer(
                    processor,
                    "a",
                    (node) -> mapifyArguments("href", (String) ((Map<?, ?>) ((CDARichHyperLink) node).getData()).get("uri")))
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
    processor.addRenderer(
            (context, node) -> node instanceof CDARichTable,
            new TagRenderer(processor, "table")
    );
    processor.addRenderer(
            (context, node) -> node instanceof CDARichTableHeaderCell,
            new TagRenderer(processor, "th")
    );
    processor.addRenderer(
            (context, node) -> node instanceof CDARichTableRow,
            new TagRenderer(processor, "tr")
    );
    processor.addRenderer(
            (context, node) -> node instanceof CDARichTableCell,
            new TagRenderer(processor, "td")
    );
    // needs to be last but one
    processor.addRenderer(
        (context, node) -> node instanceof CDARichParagraph,
        new TagRenderer(processor, "p")
    );
  }
}
