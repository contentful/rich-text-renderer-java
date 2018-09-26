import com.contentful.java.cda.structured.CDAStructuredListItem;
import com.contentful.java.cda.structured.CDAStructuredMark;
import com.contentful.java.cda.structured.CDAStructuredOrderedList;
import com.contentful.java.cda.structured.CDAStructuredParagraph;
import com.contentful.java.cda.structured.CDAStructuredText;
import com.contentful.java.cda.structured.CDAStructuredUnorderedList;
import com.contentful.structured.html.HtmlProcessor;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.truth.Truth.assertThat;

public class ListsTest {
  @Test
  public void threeUnorderedElementsListTest() {
    final HtmlProcessor processor = new HtmlProcessor();

    final CDAStructuredUnorderedList list = new CDAStructuredUnorderedList();
    list.getContent().add(textListItem("one"));
    list.getContent().add(textListItem("two"));
    list.getContent().add(textListItem("three"));

    final String result = processor.render(list);

    assertThat(result).isEqualTo(
        "<ul>\n" +
            "  <li>\n" +
            "    <p>\n" +
            "      one\n" +
            "    </p>\n" +
            "  </li>\n" +
            "  <li>\n" +
            "    <p>\n" +
            "      two\n" +
            "    </p>\n" +
            "  </li>\n" +
            "  <li>\n" +
            "    <p>\n" +
            "      three\n" +
            "    </p>\n" +
            "  </li>\n" +
            "</ul>\n");
  }

  @Test
  public void threeUnorderedElementsListAndOneNestedOrdered() {
    final HtmlProcessor processor = new HtmlProcessor();

    final CDAStructuredOrderedList orderedList = new CDAStructuredOrderedList();
    orderedList.getContent().add(textListItem("a"));
    orderedList.getContent().add(textListItem("b"));
    orderedList.getContent().add(textListItem("c"));

    final CDAStructuredListItem orderedListListItem = new CDAStructuredListItem();
    orderedListListItem.getContent().add(orderedList);

    final CDAStructuredUnorderedList unorderedList = new CDAStructuredUnorderedList();
    unorderedList.getContent().add(textListItem("one"));
    unorderedList.getContent().add(textListItem("two"));
    unorderedList.getContent().add(textListItem("three"));
    unorderedList.getContent().add(orderedListListItem);
    unorderedList.getContent().add(beautifylTextItem("four"));

    final String result = processor.render(unorderedList);

    assertThat(result).isEqualTo("<ul>\n" +
        "  <li>\n" +
        "    <p>\n" +
        "      one\n" +
        "    </p>\n" +
        "  </li>\n" +
        "  <li>\n" +
        "    <p>\n" +
        "      two\n" +
        "    </p>\n" +
        "  </li>\n" +
        "  <li>\n" +
        "    <p>\n" +
        "      three\n" +
        "    </p>\n" +
        "  </li>\n" +
        "  <li>\n" +
        "    <ol>\n" +
        "      <li>\n" +
        "        <p>\n" +
        "          a\n" +
        "        </p>\n" +
        "      </li>\n" +
        "      <li>\n" +
        "        <p>\n" +
        "          b\n" +
        "        </p>\n" +
        "      </li>\n" +
        "      <li>\n" +
        "        <p>\n" +
        "          c\n" +
        "        </p>\n" +
        "      </li>\n" +
        "    </ol>\n" +
        "  </li>\n" +
        "  <li>\n" +
        "    <p>\n" +
        "      <\uD83D\uDC51><tt><i><b><u>four</u></b></i></tt></\uD83D\uDC51>\n" +
        "    </p>\n" +
        "  </li>\n" +
        "</ul>\n");
  }

  private CDAStructuredListItem textListItem(String text) {
    final CDAStructuredParagraph paragraph = new CDAStructuredParagraph();
    paragraph.getContent().add(new CDAStructuredText(new ArrayList<>(), text));
    final CDAStructuredListItem item = new CDAStructuredListItem();
    item.getContent().add(paragraph);
    return item;
  }

  private CDAStructuredListItem beautifylTextItem(String text) {
    final CDAStructuredParagraph paragraph = new CDAStructuredParagraph();
    final List<CDAStructuredMark> allTheMarks = new ArrayList<>();
    allTheMarks.add(new CDAStructuredMark.CDAStructuredMarkUnderline());
    allTheMarks.add(new CDAStructuredMark.CDAStructuredMarkBold());
    allTheMarks.add(new CDAStructuredMark.CDAStructuredMarkItalic());
    allTheMarks.add(new CDAStructuredMark.CDAStructuredMarkCode());
    allTheMarks.add(new CDAStructuredMark.CDAStructuredMarkCustom("\uD83D\uDC51"));
    paragraph.getContent().add(new CDAStructuredText(allTheMarks, text));
    final CDAStructuredListItem item = new CDAStructuredListItem();
    item.getContent().add(paragraph);
    return item;
  }
}
