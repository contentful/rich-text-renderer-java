package com.contentful.rich.html.renderer;

import com.contentful.java.cda.rich.CDARichBlock;
import com.contentful.java.cda.rich.CDARichHyperLink;
import com.contentful.java.cda.rich.CDARichNode;
import com.contentful.rich.core.Processor;
import com.contentful.rich.core.Renderer;
import com.contentful.rich.html.HtmlContext;

import java.util.stream.Collectors;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static java.lang.String.format;
import static java.util.Locale.getDefault;

/**
 * This renderer will generate an html tag.
 */
public class TagRenderer implements Renderer<HtmlContext, String> {
  private final Processor<HtmlContext, String> processor;
  String tag;

  /**
   * Constructs a new tag renderer
   *
   * @param processor the processor containing all renderer
   * @param tag       the tag to be enclosing the content.
   */
  public TagRenderer(Processor<HtmlContext, String> processor, String tag) {
    this.processor = processor;
    this.tag = tag;
  }

  /**
   * Takes the given node and returns its html representation.
   *
   * @param context the generic context this node should be rendered in.
   * @param node    the node to be rendered.
   * @return a string containing the elements, if a non {@see CDARichParagraph} is
   * encountered only the opening and closing tags will be returned.
   */
  @Nullable @Override
  public String render(@Nonnull HtmlContext context, @Nonnull CDARichNode node) {
    final StringBuilder result = new StringBuilder(startTag(node));

    if (node instanceof CDARichBlock) {
      for (final CDARichNode item : ((CDARichBlock) node).getContent()) {
        final String itemResult = processor.process(context, item);
        if (itemResult != null) {
          result.append(itemResult);
        } else { // none found
          appendErrorNode(context, result, item);
        }
      }
    }

    return result.append(endTag(node)).toString();
  }

  private void appendErrorNode(@Nonnull HtmlContext context, StringBuilder result, CDARichNode item) {
    result
        .append("<!-- ").append("no processor accepts '")
        .append(createNodeName(item))
        .append("', found at path '")
        .append(context.getPath().stream()
            .map((x) -> createNodeName(x) + "[" + getIndexInParent(context, x) + "]")
            .collect(Collectors.joining(" > ")))
        .append("'. Please add a corresponding renderer using ")
        .append("'HtmlRenderer.addRenderer(...)'. -->");
  }

  private String createNodeName(CDARichNode node) {
    if (node instanceof CDARichHyperLink && ((CDARichHyperLink) node).getData() != null) {
      final CDARichHyperLink link = (CDARichHyperLink) node;
      return format(
          getDefault(),
          "%s<%s>",
          link.getClass().getSimpleName(),
          link.getData().getClass().getSimpleName());
    } else {
      return node.getClass().getSimpleName();
    }
  }

  private int getIndexInParent(@Nonnull HtmlContext context, CDARichNode x) {
    final int currentIndex = context.getPath().indexOf(x);
    if (currentIndex > 0) {
      return ((CDARichBlock) context.getPath().get(currentIndex - 1)).getContent().indexOf(x);
    } else {
      return 0;
    }
  }

  /**
   * Override this method if you want something special to be returned once the tag is openened.
   *
   * @param node the node used.
   * @return the tag setup.
   * @see TagWithArgumentsRenderer#startTag(CDARichNode)
   */
  @Nonnull
  protected String startTag(@Nonnull CDARichNode node) {
    return "<" + tag + ">";
  }

  /**
   * Return a string closing this tag.
   *
   * @param node the node to be rendered
   * @return a string closing this tag.
   */
  @Nonnull
  protected String endTag(@Nonnull CDARichNode node) {
    return "</" + tag + ">";
  }
}
