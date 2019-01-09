import com.contentful.java.cda.rich.CDARichListItem;
import com.contentful.java.cda.rich.CDARichMark;
import com.contentful.java.cda.rich.CDARichOrderedList;
import com.contentful.java.cda.rich.CDARichParagraph;
import com.contentful.java.cda.rich.CDARichText;
import com.contentful.java.cda.rich.CDARichUnorderedList;
import com.contentful.rich.html.HtmlContext;
import com.contentful.rich.html.HtmlProcessor;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.truth.Truth.assertThat;

public class ListsTest {
  @Test
  public void threeUnorderedElementsListTest() {
    final HtmlProcessor processor = new HtmlProcessor();
    final HtmlContext context = new HtmlContext();

    final CDARichUnorderedList list = new CDARichUnorderedList();
    list.getContent().add(textListItem("one"));
    list.getContent().add(textListItem("two"));
    list.getContent().add(textListItem("three"));

    final String result = processor.process(context, list);

    assertThat(result).isEqualTo(
        "<ul>\n" +
            "  <li>\n" +
            "    <div>\n" +
            "      one\n" +
            "    </div>\n" +
            "  </li>\n" +
            "  <li>\n" +
            "    <div>\n" +
            "      two\n" +
            "    </div>\n" +
            "  </li>\n" +
            "  <li>\n" +
            "    <div>\n" +
            "      three\n" +
            "    </div>\n" +
            "  </li>\n" +
            "</ul>\n");
  }

  @Test
  public void threeUnorderedElementsListAndOneNestedOrdered() {
    final HtmlProcessor processor = new HtmlProcessor();
    final HtmlContext context = new HtmlContext();

    final CDARichOrderedList orderedList = new CDARichOrderedList();
    orderedList.getContent().add(textListItem("a"));
    orderedList.getContent().add(textListItem("b"));
    orderedList.getContent().add(textListItem("c"));

    final CDARichListItem orderedListListItem = new CDARichListItem();
    orderedListListItem.getContent().add(orderedList);

    final CDARichUnorderedList unorderedList = new CDARichUnorderedList();
    unorderedList.getContent().add(textListItem("one"));
    unorderedList.getContent().add(textListItem("two"));
    unorderedList.getContent().add(textListItem("three"));
    unorderedList.getContent().add(orderedListListItem);
    unorderedList.getContent().add(beautifyTextItem("four"));

    final String result = processor.process(context, unorderedList);

    assertThat(result).isEqualTo("<ul>\n" +
        "  <li>\n" +
        "    <div>\n" +
        "      one\n" +
        "    </div>\n" +
        "  </li>\n" +
        "  <li>\n" +
        "    <div>\n" +
        "      two\n" +
        "    </div>\n" +
        "  </li>\n" +
        "  <li>\n" +
        "    <div>\n" +
        "      three\n" +
        "    </div>\n" +
        "  </li>\n" +
        "  <li>\n" +
        "    <ol>\n" +
        "      <li>\n" +
        "        <div>\n" +
        "          a\n" +
        "        </div>\n" +
        "      </li>\n" +
        "      <li>\n" +
        "        <div>\n" +
        "          b\n" +
        "        </div>\n" +
        "      </li>\n" +
        "      <li>\n" +
        "        <div>\n" +
        "          c\n" +
        "        </div>\n" +
        "      </li>\n" +
        "    </ol>\n" +
        "  </li>\n" +
        "  <li>\n" +
        "    <div>\n" +
        "      <\uD83D\uDC51><code><i><b><u>four</u></b></i></code></\uD83D\uDC51>\n" +
        "    </div>\n" +
        "  </li>\n" +
        "</ul>\n");
  }

  private CDARichListItem textListItem(String text) {
    final CDARichParagraph paragraph = new CDARichParagraph();
    paragraph.getContent().add(new CDARichText(text, new ArrayList<>()));
    final CDARichListItem item = new CDARichListItem();
    item.getContent().add(paragraph);
    return item;
  }

  private CDARichListItem beautifyTextItem(String text) {
    final CDARichParagraph paragraph = new CDARichParagraph();
    final List<CDARichMark> allTheMarks = new ArrayList<>();
    allTheMarks.add(new CDARichMark.CDARichMarkUnderline());
    allTheMarks.add(new CDARichMark.CDARichMarkBold());
    allTheMarks.add(new CDARichMark.CDARichMarkItalic());
    allTheMarks.add(new CDARichMark.CDARichMarkCode());
    allTheMarks.add(new CDARichMark.CDARichMarkCustom("\uD83D\uDC51"));
    paragraph.getContent().add(new CDARichText(text, allTheMarks));
    final CDARichListItem item = new CDARichListItem();
    item.getContent().add(paragraph);
    return item;
  }
}
