package views;

import android.app.Activity;
import android.view.View;

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
import com.contentful.rich.android.AndroidContext;
import com.contentful.rich.android.AndroidProcessor;
import com.contentful.rich.android.R;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

import java.util.ArrayList;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.truth.Truth.assertThat;
import static lib.ContentfulCreator.mockCDAEntry;

@RunWith(RobolectricTestRunner.class)
public class AllTheThingsTest {
  private Activity activity;

  @Before
  public void setup() {
    activity = Robolectric.setupActivity(Activity.class);
  }

  @Test
  public void allTheNodes() {
    final AndroidProcessor<View> processor = AndroidProcessor.creatingNativeViews();
    final AndroidContext context = new AndroidContext(activity);

    final View result = processor.process(context, createAllNode());

    assertThat(result).isNotNull();
    final View content = result.findViewById(R.id.rich_content);
    assertThat(content).isNotNull();

    ArrayList<View> views = new ArrayList<>();
    content.findViewsWithText(views, "heading - 1", View.FIND_VIEWS_WITH_TEXT);
    assertThat(views).hasSize(1);

    content.findViewsWithText(views, "heading - 6", View.FIND_VIEWS_WITH_TEXT);
    assertThat(views).hasSize(2);

    content.findViewsWithText(views, "some list item content", View.FIND_VIEWS_WITH_TEXT);
    assertThat(views).hasSize(9);
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

    final CDARichEmbeddedLink embeddedLink = new CDARichEmbeddedLink(mockCDAEntry());
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
}
