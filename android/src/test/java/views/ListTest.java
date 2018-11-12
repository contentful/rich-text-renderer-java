package views;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;

import com.contentful.java.cda.rich.CDARichDocument;
import com.contentful.java.cda.rich.CDARichListItem;
import com.contentful.java.cda.rich.CDARichOrderedList;
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

import static com.google.common.truth.Truth.assertThat;

@RunWith(RobolectricTestRunner.class)
public class ListTest {
  private Activity activity;

  @Before
  public void setup() {
    activity = Robolectric.setupActivity(Activity.class);
  }

  @Test
  public void nestedListsUinO() {
    final AndroidProcessor<View> processor = AndroidProcessor.creatingNativeViews();
    final AndroidContext context = new AndroidContext(activity);

    final CDARichListItem item1 = new CDARichListItem();
    item1.getContent().add(new CDARichText("item1", new ArrayList<>()));

    final CDARichListItem item2 = new CDARichListItem();
    item2.getContent().add(new CDARichText("item2", new ArrayList<>()));

    final CDARichListItem item3 = new CDARichListItem();
    item3.getContent().add(new CDARichText("item3", new ArrayList<>()));

    final CDARichUnorderedList unorderedList = new CDARichUnorderedList();
    unorderedList.getContent().add(item1);
    unorderedList.getContent().add(item2);
    unorderedList.getContent().add(item3);

    final CDARichListItem itemWithList = new CDARichListItem();
    itemWithList.getContent().add(new CDARichText("ordered:", new ArrayList<>()));
    itemWithList.getContent().add(unorderedList);

    final CDARichOrderedList list = new CDARichOrderedList();
    list.getContent().add(item1);
    list.getContent().add(item2);
    list.getContent().add(itemWithList);
    list.getContent().add(item3);

    final CDARichDocument document = new CDARichDocument();
    document.getContent().add(new CDARichText("unordered:", new ArrayList<>()));
    document.getContent().add(list);

    final View result = processor.process(context, document);

    assertThat(result).isNotNull();
    final View content = result.findViewById(R.id.rich_content);
    assertThat(content).isNotNull();

    ArrayList<View> views = new ArrayList<>();
    content.findViewsWithText(views, "ordered", View.FIND_VIEWS_WITH_TEXT);
    assertThat(views).hasSize(2);
    assertThat(views.get(0)).isInstanceOf(TextView.class);

    content.findViewsWithText(views, "text1", View.FIND_VIEWS_WITH_TEXT);
    assertThat(views).hasSize(2);
    assertThat(views.get(0)).isInstanceOf(TextView.class);

    content.findViewsWithText(views, "text2", View.FIND_VIEWS_WITH_TEXT);
    assertThat(views).hasSize(2);
    assertThat(views.get(0)).isInstanceOf(TextView.class);

    content.findViewsWithText(views, "text3", View.FIND_VIEWS_WITH_TEXT);
    assertThat(views).hasSize(2);
    assertThat(views.get(0)).isInstanceOf(TextView.class);
  }

  @Test
  public void nestedListsOinU() {
    final AndroidProcessor<View> processor = AndroidProcessor.creatingNativeViews();
    final AndroidContext context = new AndroidContext(activity);

    final CDARichListItem item1 = new CDARichListItem();
    item1.getContent().add(new CDARichText("item1", new ArrayList<>()));

    final CDARichListItem item2 = new CDARichListItem();
    item2.getContent().add(new CDARichText("item2", new ArrayList<>()));

    final CDARichListItem item3 = new CDARichListItem();
    item3.getContent().add(new CDARichText("item3", new ArrayList<>()));

    final CDARichOrderedList orderedList = new CDARichOrderedList();
    orderedList.getContent().add(item1);
    orderedList.getContent().add(item2);
    orderedList.getContent().add(item3);

    final CDARichListItem ordered = new CDARichListItem();
    ordered.getContent().add(new CDARichText("ordered:", new ArrayList<>()));
    ordered.getContent().add(orderedList);

    final CDARichUnorderedList unordered = new CDARichUnorderedList();
    unordered.getContent().add(item1);
    unordered.getContent().add(item2);
    unordered.getContent().add(ordered);
    unordered.getContent().add(item3);

    final CDARichDocument document = new CDARichDocument();
    document.getContent().add(new CDARichText("unordered:", new ArrayList<>()));
    document.getContent().add(unordered);

    final View result = processor.process(context, document);

    assertThat(result).isNotNull();
    final View content = result.findViewById(R.id.rich_content);
    assertThat(content).isNotNull();

    ArrayList<View> views = new ArrayList<>();
    content.findViewsWithText(views, "ordered", View.FIND_VIEWS_WITH_TEXT);
    assertThat(views).hasSize(2);
    assertThat(views.get(0)).isInstanceOf(TextView.class);

    content.findViewsWithText(views, "text1", View.FIND_VIEWS_WITH_TEXT);
    assertThat(views).hasSize(2);
    assertThat(views.get(0)).isInstanceOf(TextView.class);

    content.findViewsWithText(views, "text2", View.FIND_VIEWS_WITH_TEXT);
    assertThat(views).hasSize(2);
    assertThat(views.get(0)).isInstanceOf(TextView.class);

    content.findViewsWithText(views, "text3", View.FIND_VIEWS_WITH_TEXT);
    assertThat(views).hasSize(2);
    assertThat(views.get(0)).isInstanceOf(TextView.class);

  }
}
