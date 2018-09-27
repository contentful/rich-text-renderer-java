import com.contentful.java.cda.CDAEntry;
import com.contentful.java.cda.CDAResource;
import com.contentful.java.cda.structured.CDAStructuredDocument;
import com.contentful.java.cda.structured.CDAStructuredEmbeddedLink;
import com.contentful.java.cda.structured.CDAStructuredHeading;
import com.contentful.java.cda.structured.CDAStructuredHorizontalRule;
import com.contentful.java.cda.structured.CDAStructuredHyperLink;
import com.contentful.java.cda.structured.CDAStructuredListItem;
import com.contentful.java.cda.structured.CDAStructuredMark;
import com.contentful.java.cda.structured.CDAStructuredNode;
import com.contentful.java.cda.structured.CDAStructuredOrderedList;
import com.contentful.java.cda.structured.CDAStructuredParagraph;
import com.contentful.java.cda.structured.CDAStructuredQuote;
import com.contentful.java.cda.structured.CDAStructuredText;
import com.contentful.java.cda.structured.CDAStructuredUnorderedList;
import com.contentful.structured.html.HtmlProcessor;

import org.junit.Test;

import java.lang.reflect.Field;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Maps.newHashMap;
import static com.google.common.truth.Truth.assertThat;

public class AllTheThingsTest {
  @Test
  public void allTheNodes() {
    final HtmlProcessor processor = new HtmlProcessor();

    final String result = processor.render(createAllNode());

    assertThat(result).isEqualTo("<main>\n" +
        "  <h1>\n" +
        "    heading - 1\n" +
        "  </h1>\n" +
        "  <h2>\n" +
        "    heading - 2\n" +
        "  </h2>\n" +
        "  <h3>\n" +
        "    heading - 3\n" +
        "  </h3>\n" +
        "  <h4>\n" +
        "    heading - 4\n" +
        "  </h4>\n" +
        "  <h5>\n" +
        "    heading - 5\n" +
        "  </h5>\n" +
        "  <h6>\n" +
        "    heading - 6\n" +
        "  </h6>\n" +
        "  <hr/>\n" +
        "  <div entry=\"CDAEntry{id='fake_id'}\">\n" +
        "  </div>\n" +
        "  <a href=\"https://contentful.com/\">\n" +
        "    Hyper hyper\n" +
        "  </a>\n" +
        "  <top><tt><i><b><u>ALL THE TEXT MARKS!</u></b></i></tt></top>\n" +
        "  <ol>\n" +
        "    <li>\n" +
        "      some list item content\n" +
        "    </li>\n" +
        "    <li>\n" +
        "      some list item content\n" +
        "    </li>\n" +
        "    <li>\n" +
        "      some list item content\n" +
        "    </li>\n" +
        "  </ol>\n" +
        "  <ul>\n" +
        "    <li>\n" +
        "      some list item content\n" +
        "    </li>\n" +
        "    <li>\n" +
        "      some list item content\n" +
        "    </li>\n" +
        "    <li>\n" +
        "      some list item content\n" +
        "    </li>\n" +
        "    <li>\n" +
        "      some list item content\n" +
        "    </li>\n" +
        "  </ul>\n" +
        "  <p>\n" +
        "    Paragraph\n" +
        "  </p>\n" +
        "  <blockquote>\n" +
        "    Famous quote\n" +
        "  </blockquote>\n" +
        "  <!-- no render accepts <tt>CDAStructuredNode</tt> with a path of " +
        "CDAStructuredDocument. Please add a corresponding renderer using " +
        "<tt>HtmlRenderer.addRenderer(…)</tt>. -->\n" +
        "</main>\n");
  }

  private CDAStructuredNode createAllNode() {
    final CDAStructuredDocument result = new CDAStructuredDocument();

    for (int i = 1; i < 7; ++i) {
      final CDAStructuredHeading heading = new CDAStructuredHeading(i);
      heading.getContent().add(new CDAStructuredText(newArrayList(), "heading - " + i));
      result.getContent().add(heading);
    }

    final CDAStructuredHorizontalRule horizontalRule = new CDAStructuredHorizontalRule();
    result.getContent().add(horizontalRule);

    final CDAStructuredEmbeddedLink embeddedLink = new CDAStructuredEmbeddedLink(newCDAEntry());
    result.getContent().add(embeddedLink);

    final CDAStructuredHyperLink hyperLink = new CDAStructuredHyperLink("https://contentful.com/");
    hyperLink.getContent().add(new CDAStructuredText(newArrayList(), "Hyper hyper"));
    result.getContent().add(hyperLink);

    final CDAStructuredText allTheMarks = new CDAStructuredText(
        newArrayList(
            new CDAStructuredMark.CDAStructuredMarkUnderline(),
            new CDAStructuredMark.CDAStructuredMarkBold(),
            new CDAStructuredMark.CDAStructuredMarkItalic(),
            new CDAStructuredMark.CDAStructuredMarkCode(),
            new CDAStructuredMark.CDAStructuredMarkCustom("top")
        ), "ALL THE TEXT MARKS!"
    );
    result.getContent().add(allTheMarks);

    final CDAStructuredOrderedList orderedList = new CDAStructuredOrderedList();
    final CDAStructuredListItem listItem = new CDAStructuredListItem();
    listItem.getContent().add(new CDAStructuredText(newArrayList(), "some list item content"));
    orderedList.getContent().add(listItem);
    orderedList.getContent().add(listItem);
    orderedList.getContent().add(listItem);
    result.getContent().add(orderedList);

    final CDAStructuredUnorderedList unorderedList = new CDAStructuredUnorderedList();
    unorderedList.getContent().add(listItem);
    unorderedList.getContent().add(listItem);
    unorderedList.getContent().add(listItem);
    unorderedList.getContent().add(listItem);
    result.getContent().add(unorderedList);

    final CDAStructuredParagraph paragraph = new CDAStructuredParagraph();
    paragraph.getContent().add(new CDAStructuredText(newArrayList(), "Paragraph"));
    result.getContent().add(paragraph);

    final CDAStructuredQuote quote = new CDAStructuredQuote();
    quote.getContent().add(new CDAStructuredText(newArrayList(), "Famous quote"));
    result.getContent().add(quote);

    final CDAStructuredNode node = new CDAStructuredNode();
    result.getContent().add(node);

    return result;
  }

  private CDAResource newCDAEntry() {
    final CDAEntry entry = new CDAEntry();
    Field attrs = null;
    for (final Field field : CDAResource.class.getDeclaredFields()) {
      if ("attrs".equals(field.getName())) {
        attrs = field;
      }
    }
    if (attrs != null) {
      attrs.setAccessible(true);
      try {
        attrs.set(entry, newHashMap());
      } catch (IllegalAccessException e) {
      }
      attrs.setAccessible(false);
    }

    entry.attrs().put("id", "fake_id");
    entry.attrs().put("contentType", "fake_type");
    return entry;
  }
}
