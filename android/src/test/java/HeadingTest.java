import android.app.Activity;
import android.text.Spannable;
import android.text.style.AbsoluteSizeSpan;

import com.contentful.java.cda.structured.CDAStructuredHeading;
import com.contentful.java.cda.structured.CDAStructuredText;
import com.contentful.structured.android.CharSequenceProcessor;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

import static com.google.common.collect.Lists.newArrayList;
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
    final CharSequenceProcessor processor = new CharSequenceProcessor(activity);

    final CDAStructuredHeading heading = new CDAStructuredHeading(1);
    heading.getContent().add(new CDAStructuredText(newArrayList(), "heading 1"));

    final CharSequence result = processor.render(heading);

    assertThat(result.toString()).isEqualTo("heading 1");

    assertThat(result).isInstanceOf(Spannable.class);
    final Spannable spannable = (Spannable) result;

    final Object[] spans = spannable.getSpans(0, result.length(), Object.class);
    assertThat(spans.length).isEqualTo(1);
    assertThat(spans[0]).isInstanceOf(AbsoluteSizeSpan.class);
    assertThat(((AbsoluteSizeSpan) spans[0]).getSize()).isEqualTo(24);
  }
}
