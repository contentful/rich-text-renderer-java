import com.contentful.java.cda.CDAEntry;
import com.contentful.java.cda.CDAResource;
import com.contentful.java.cda.rich.CDARichDocument;
import com.contentful.java.cda.rich.CDARichEmbeddedLink;
import com.contentful.java.cda.rich.CDARichHeading;
import com.contentful.java.cda.rich.CDARichHorizontalRule;
import com.contentful.java.cda.rich.CDARichHyperLink;
import com.contentful.java.cda.rich.CDARichListItem;
import com.contentful.java.cda.rich.CDARichMark;
import com.contentful.java.cda.rich.CDARichNode;
import com.contentful.java.cda.rich.CDARichOrderedList;
import com.contentful.java.cda.rich.CDARichParagraph;
import com.contentful.java.cda.rich.CDARichQuote;
import com.contentful.java.cda.rich.CDARichText;
import com.contentful.java.cda.rich.CDARichUnorderedList;
import com.contentful.rich.html.HtmlProcessor;

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
        "  <!-- no render accepts <tt>CDARichNode</tt> with a path of " +
        "CDARichDocument. Please add a corresponding renderer using " +
        "<tt>HtmlRenderer.addRenderer(…)</tt>. -->\n" +
        "</main>\n");
  }

  private CDARichNode createAllNode() {
    final CDARichDocument result = new CDARichDocument();

    for (int i = 1; i < 7; ++i) {
      final CDARichHeading heading = new CDARichHeading(i);
      heading.getContent().add(new CDARichText("heading - " + i));
      result.getContent().add(heading);
    }

    final CDARichHorizontalRule horizontalRule = new CDARichHorizontalRule();
    result.getContent().add(horizontalRule);

    final CDARichEmbeddedLink embeddedLink = new CDARichEmbeddedLink(newCDAEntry());
    result.getContent().add(embeddedLink);

    final CDARichHyperLink hyperLink = new CDARichHyperLink("https://contentful.com/");
    hyperLink.getContent().add(new CDARichText("Hyper hyper"));
    result.getContent().add(hyperLink);

    final CDARichText allTheMarks = new CDARichText(
        "ALL THE TEXT MARKS!",
        newArrayList(
            new CDARichMark.CDARichMarkUnderline(),
            new CDARichMark.CDARichMarkBold(),
            new CDARichMark.CDARichMarkItalic(),
            new CDARichMark.CDARichMarkCode(),
            new CDARichMark.CDARichMarkCustom("top")
        )
    );
    result.getContent().add(allTheMarks);

    final CDARichOrderedList orderedList = new CDARichOrderedList();
    final CDARichListItem listItem = new CDARichListItem();
    listItem.getContent().add(new CDARichText("some list item content"));
    orderedList.getContent().add(listItem);
    orderedList.getContent().add(listItem);
    orderedList.getContent().add(listItem);
    result.getContent().add(orderedList);

    final CDARichUnorderedList unorderedList = new CDARichUnorderedList();
    unorderedList.getContent().add(listItem);
    unorderedList.getContent().add(listItem);
    unorderedList.getContent().add(listItem);
    unorderedList.getContent().add(listItem);
    result.getContent().add(unorderedList);

    final CDARichParagraph paragraph = new CDARichParagraph();
    paragraph.getContent().add(new CDARichText("Paragraph"));
    result.getContent().add(paragraph);

    final CDARichQuote quote = new CDARichQuote();
    quote.getContent().add(new CDARichText("Famous quote"));
    result.getContent().add(quote);

    final CDARichNode node = new CDARichNode();
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
