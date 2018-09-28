package com.contentful.rich.html.renderer;

import com.contentful.java.cda.rich.CDARichNode;
import com.contentful.java.cda.rich.CDARichParagraph;
import com.contentful.rich.core.Processor;
import com.contentful.rich.core.Renderer;
import com.contentful.rich.html.HtmlContext;

import java.util.stream.Collectors;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

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

    if (node instanceof CDARichParagraph) {
      for (final CDARichNode item : ((CDARichParagraph) node).getContent()) {
        final String itemResult = processor.render(item);
        if (itemResult != null) {
          if (itemResult.contains("\n")) {
            for (final String line : itemResult.split("\n")) {
              result.append(context.getIndentation()).append(line).append("\n");
            }
          } else { // no new line found.
            result.append(context.getIndentation()).append(itemResult).append("\n");
          }
        } else { // null found
          result.append(context.getIndentation()).append("<!-- ").append("no render accepts <tt>")
              .append(item.getClass().getSimpleName())
              .append("</tt> with a path of ")
              .append(context.getPath().stream()
                  .map((x) -> x.getClass().getSimpleName())
                  .collect(Collectors.joining(" > ")))
              .append(". Please add a corresponding renderer using ")
              .append("<tt>HtmlRenderer.addRenderer(…)</tt>. -->\n");
        }
      }
    }

    return result.append(endTag(node)).toString();
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
    return "<" + tag + ">\n";
  }

  /**
   * Return a string closing this tag.
   *
   * @param node the node to be rendered
   * @return a string closing this tag.
   */
  @Nonnull
  protected String endTag(@Nonnull CDARichNode node) {
    return "</" + tag + ">\n";
  }
}
