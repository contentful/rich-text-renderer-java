import com.contentful.java.cda.rich.CDARichDocument;
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
import com.contentful.rich.html.HtmlContext;
import com.contentful.rich.html.HtmlProcessor;

import org.junit.Test;

import java.util.ArrayList;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.truth.Truth.assertThat;

public class AllTheThingsTest {
  @Test
  public void allTheNodes() {
    final HtmlProcessor processor = new HtmlProcessor();

    final String result = processor.process(new HtmlContext(), createAllNode());

    assertThat(result).isEqualTo("<div>"+
        "<h1>"+
        "heading - 1"+
        "</h1>"+
        "<h2>"+
        "heading - 2"+
        "</h2>"+
        "<h3>"+
        "heading - 3"+
        "</h3>"+
        "<h4>"+
        "heading - 4"+
        "</h4>"+
        "<h5>"+
        "heading - 5"+
        "</h5>"+
        "<h6>"+
        "heading - 6"+
        "</h6>"+
        "<hr/>"+
        "<a href=\"https://contentful.com/\">"+
        "Hyper hyper"+
        "</a>"+
        "<top><code><i><b><u>ALL THE TEXT MARKS!</u></b></i></code></top>"+
        "<ol>"+
        "<li>"+
        "some list item content"+
        "</li>"+
        "<li>"+
        "some list item content"+
        "</li>"+
        "<li>"+
        "some list item content"+
        "</li>"+
        "</ol>"+
        "<ul>"+
        "<li>"+
        "some list item content"+
        "</li>"+
        "<li>"+
        "some list item content"+
        "</li>"+
        "<li>"+
        "some list item content"+
        "</li>"+
        "<li>"+
        "some list item content"+
        "</li>"+
        "</ul>"+
        "<p>"+
        "Paragraph"+
        "</p>"+
        "<blockquote>"+
        "Famous quote"+
        "</blockquote>"+
        "  <!-- no processor accepts 'CDARichNode', found at path 'CDARichDocument[0]'. Please add a corresponding renderer using 'HtmlRenderer.addRenderer(...)'. --></div>");
  }

  private CDARichNode createAllNode() {
    final CDARichDocument result = new CDARichDocument();

    for (int i = 1; i < 7; ++i) {
      final CDARichHeading heading = new CDARichHeading(i);
      heading.getContent().add(new CDARichText("heading - "+ i, new ArrayList<>()));
      result.getContent().add(heading);
    }

    final CDARichHorizontalRule horizontalRule = new CDARichHorizontalRule();
    result.getContent().add(horizontalRule);

    final CDARichHyperLink hyperLink = new CDARichHyperLink("https://contentful.com/");
    hyperLink.getContent().add(new CDARichText("Hyper hyper", new ArrayList<>()));
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
    listItem.getContent().add(new CDARichText("some list item content", new ArrayList<>()));
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
    paragraph.getContent().add(new CDARichText("Paragraph", new ArrayList<>()));
    result.getContent().add(paragraph);

    final CDARichQuote quote = new CDARichQuote();
    quote.getContent().add(new CDARichText("Famous quote", new ArrayList<>()));
    result.getContent().add(quote);

    final CDARichNode node = new CDARichNode();
    result.getContent().add(node);

    return result;
  }
}
