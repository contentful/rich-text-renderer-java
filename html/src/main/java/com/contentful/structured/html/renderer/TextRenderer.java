package com.contentful.structured.html.renderer;

import com.contentful.java.cda.structured.CDAStructuredMark;
import com.contentful.java.cda.structured.CDAStructuredNode;
import com.contentful.java.cda.structured.CDAStructuredText;
import com.contentful.structured.core.Renderer;
import com.contentful.structured.html.HtmlContext;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * This class will take a {@see CDAStructuredText} node and render a html representation of it.
 */
public class TextRenderer implements Renderer<HtmlContext, String> {
  /**
   * Transforms (renders) a {@see CDAStructuredNode} as a string.
   *
   * @param context the generic context this node should be rendered in.
   * @param node    the node to be rendered.
   * @return the text represented by the text node or null if no text node is given.
   */
  @Nullable @Override
  public String render(@Nonnull HtmlContext context, @Nonnull CDAStructuredNode node) {
    final String text = ((CDAStructuredText) node).getText();
    final StringBuilder result = new StringBuilder(text);
    for (final CDAStructuredMark mark : ((CDAStructuredText) node).getMarks()) {

      if (mark instanceof CDAStructuredMark.CDAStructuredMarkUnderline) {
        result.insert(0, "<u>").append("</u>");
      }
      if (mark instanceof CDAStructuredMark.CDAStructuredMarkBold) {
        result.insert(0, "<b>").append("</b>");
      }
      if (mark instanceof CDAStructuredMark.CDAStructuredMarkItalic) {
        result.insert(0, "<i>").append("</i>");
      }
      if (mark instanceof CDAStructuredMark.CDAStructuredMarkCode) {
        result.insert(0, "<tt>").append("</tt>");
      }
      if (mark instanceof CDAStructuredMark.CDAStructuredMarkCustom) {
        final String tag = ((CDAStructuredMark.CDAStructuredMarkCustom) mark).getType();
        result.insert(0, ">").insert(0, tag).insert(0, "<").append("</").append(tag).append(">");
      }
    }

    result.append("\n");
    return result.toString();
  }
}
