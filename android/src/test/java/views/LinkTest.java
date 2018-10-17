package views;

import android.app.Activity;
import android.text.Spannable;
import android.text.style.ForegroundColorSpan;
import android.text.style.URLSpan;
import android.view.View;
import android.widget.TextView;

import com.contentful.java.cda.rich.CDARichEmbeddedLink;
import com.contentful.java.cda.rich.CDARichHyperLink;
import com.contentful.java.cda.rich.CDARichText;
import com.contentful.rich.android.AndroidContext;
import com.contentful.rich.android.AndroidProcessor;
import com.contentful.rich.android.R;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

import java.util.ArrayList;

import lib.ContentfulCreator;

import static com.google.common.truth.Truth.assertThat;

@RunWith(RobolectricTestRunner.class)
public class LinkTest {
  private Activity activity;

  @Before
  public void setup() {
    activity = Robolectric.setupActivity(Activity.class);
  }

  @Test
  public void hyperlinkTest() {
    final AndroidProcessor<View> processor = AndroidProcessor.creatingNativeViews();
    final AndroidContext context = new AndroidContext(activity);

    final CDARichHyperLink link = new CDARichHyperLink("https://contentful.com");
    link.getContent().add(new CDARichText("Some link text"));

    final View result = processor.process(context, link);

    assertThat(result).isNotNull();
    final View content = result.findViewById(R.id.rich_text_content);
    assertThat(content).isNotNull();

    ArrayList<View> views = new ArrayList<>();
    content.findViewsWithText(views, "Some link text", View.FIND_VIEWS_WITH_TEXT);
    assertThat(views).hasSize(1);
    assertThat(views.get(0)).isInstanceOf(TextView.class);
  }

  @Test
  public void createUnsanitzedStrings() {
    final AndroidProcessor<View> processor = AndroidProcessor.creatingNativeViews();
    final AndroidContext context = new AndroidContext(activity);

    final CDARichHyperLink link = new CDARichHyperLink("https://contentful.com");
    link.getContent().add(new CDARichText("Some link text</a>"));

    final View result = processor.process(context, link);

    assertThat(result).isNotNull();
    final View content = result.findViewById(R.id.rich_text_content);
    assertThat(content).isNotNull();

    ArrayList<View> views = new ArrayList<>();
    content.findViewsWithText(views, "Some link text", View.FIND_VIEWS_WITH_TEXT);
    assertThat(views).hasSize(1);
    assertThat(views.get(0)).isInstanceOf(TextView.class);
  }

  @Test
  public void createEmbeddedLink() {
    final AndroidProcessor<View> processor = AndroidProcessor.creatingNativeViews();
    final AndroidContext context = new AndroidContext(activity);

    final CDARichHyperLink link = new CDARichEmbeddedLink(ContentfulCreator.mockCDAEntry());
    link.getContent().add(new CDARichText("My embedded entry"));

    final View result = processor.process(context, link);

    assertThat(result).isNotNull();
    final View content = result.findViewById(R.id.rich_text_content);
    assertThat(content).isNotNull();

    ArrayList<View> views = new ArrayList<>();
    content.findViewsWithText(views, "Some link text", View.FIND_VIEWS_WITH_TEXT);
    assertThat(views).hasSize(1);
    assertThat(views.get(0)).isInstanceOf(TextView.class);
  }
}
