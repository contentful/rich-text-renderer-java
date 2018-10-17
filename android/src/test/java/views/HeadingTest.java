package views;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;

import com.contentful.java.cda.rich.CDARichHeading;
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

import static com.google.common.truth.Truth.assertThat;

@RunWith(RobolectricTestRunner.class)
public class HeadingTest {
  private Activity activity;

  @Before
  public void setup() {
    activity = Robolectric.setupActivity(Activity.class);
  }

  @Test
  public void firstHeadingParsesContentTest() {
    final AndroidProcessor<View> processor = AndroidProcessor.creatingNativeViews();
    final AndroidContext context = new AndroidContext(activity);

    final CDARichHeading heading = new CDARichHeading(1);
    heading.getContent().add(new CDARichText("heading 1"));

    final View result = processor.process(context, heading);

    assertThat(result).isNotNull();
    final View content = result.findViewById(R.id.rich_text_content);
    assertThat(content).isNotNull();

    ArrayList<View> views = new ArrayList<>();
    content.findViewsWithText(views, "heading 1", View.FIND_VIEWS_WITH_TEXT);
    assertThat(views).hasSize(1);
    assertThat(views.get(0)).isInstanceOf(TextView.class);
  }

  @Test
  public void notSupportedHeadingWillRenderDefaultText() {
    final AndroidProcessor<View> processor = AndroidProcessor.creatingNativeViews();
    final AndroidContext context = new AndroidContext(activity);

    final CDARichHeading heading = new CDARichHeading(-1);
    heading.getContent().add(new CDARichText("illegal"));

    final View result = processor.process(context, heading);

    assertThat(result).isNotNull();
    final View content = result.findViewById(R.id.rich_text_content);
    assertThat(content).isNotNull();

    ArrayList<View> views = new ArrayList<>();
    content.findViewsWithText(views, "illegal", View.FIND_VIEWS_WITH_TEXT);
    assertThat(views).hasSize(0);
  }
}
