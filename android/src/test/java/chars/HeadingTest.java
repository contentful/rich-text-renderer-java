package chars;

import android.app.Activity;
import android.text.Spannable;
import android.text.style.AbsoluteSizeSpan;

import com.contentful.java.cda.rich.CDARichHeading;
import com.contentful.java.cda.rich.CDARichText;
import com.contentful.rich.android.AndroidContext;
import com.contentful.rich.android.AndroidProcessor;

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
    final AndroidProcessor<CharSequence> processor = AndroidProcessor.creatingCharSequences();
    final AndroidContext context = new AndroidContext(activity);

    final CDARichHeading heading = new CDARichHeading(1);
    heading.getContent().add(new CDARichText("heading 1", new ArrayList<>()));

    final CharSequence result = processor.process(context, heading);

    assertThat(result.toString()).isEqualTo("heading 1");

    assertThat(result).isInstanceOf(Spannable.class);
    final Spannable spannable = (Spannable) result;

    final Object[] spans = spannable.getSpans(0, result.length(), Object.class);
    assertThat(spans.length).isEqualTo(1);
    assertThat(spans[0]).isInstanceOf(AbsoluteSizeSpan.class);
    assertThat(((AbsoluteSizeSpan) spans[0]).getSize()).isEqualTo(72);
  }

  @Test
  public void notSupportedHeadingWillRenderDefaultText() {
    final AndroidProcessor<CharSequence> processor = AndroidProcessor.creatingCharSequences();
    final AndroidContext context = new AndroidContext(activity);

    final CDARichHeading heading = new CDARichHeading(-1);
    heading.getContent().add(new CDARichText("illegal", new ArrayList<>()));

    final CharSequence result = processor.process(context, heading);

    assertThat(((Spannable) result).getSpans(0, result.length(), Object.class)).hasLength(0);
  }
}
