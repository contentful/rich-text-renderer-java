import android.app.Activity;
import android.text.Spannable;
import android.text.style.ForegroundColorSpan;
import android.text.style.URLSpan;

import com.contentful.java.cda.rich.CDARichEmbeddedLink;
import com.contentful.java.cda.rich.CDARichHyperLink;
import com.contentful.java.cda.rich.CDARichText;
import com.contentful.rich.android.CharSequenceProcessor;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

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
    final CharSequenceProcessor processor = new CharSequenceProcessor(activity);

    final CDARichHyperLink link = new CDARichHyperLink("https://contentful.com");
    link.getContent().add(new CDARichText("Some link text"));

    final CharSequence result = processor.render(link);

    assertThat(result).isInstanceOf(Spannable.class);
    assertThat(result.toString()).isEqualTo("Some link text");

    assertThat(((Spannable) result).getSpans(0, result.length(), Object.class)).hasLength(1);
    assertThat(((Spannable) result).getSpans(0, result.length(), Object.class)[0]).isInstanceOf(URLSpan.class);

    final URLSpan span = (URLSpan) ((Spannable) result).getSpans(0, result.length(), Object.class)[0];
    assertThat(span.getURL()).isEqualTo("https://contentful.com");
  }

  @Test
  public void createUnsanitzedStrings() {
    final CharSequenceProcessor processor = new CharSequenceProcessor(activity);

    final CDARichHyperLink link = new CDARichHyperLink("https://contentful.com");
    link.getContent().add(new CDARichText("Some link text</a>"));

    final CharSequence result = processor.render(link);

    assertThat(result).isInstanceOf(Spannable.class);
    assertThat(result.toString()).isEqualTo("Some link text</a>");

    final Object[] spans = ((Spannable) result).getSpans(0, result.length(), Object.class);
    assertThat(spans).hasLength(1);
    assertThat(spans[0]).isInstanceOf(URLSpan.class);

    final URLSpan span = (URLSpan) ((Spannable) result).getSpans(0, result.length(), Object.class)[0];
    assertThat(span.getURL()).isEqualTo("https://contentful.com");
  }

  @Test
  public void createEmbeddedLink() {
    final CharSequenceProcessor processor = new CharSequenceProcessor(activity);

    final CDARichHyperLink link = new CDARichEmbeddedLink(ContentfulCreator.mockCDAEntry());
    link.getContent().add(new CDARichText("My embedded entry"));

    final CharSequence result = processor.render(link);

    assertThat(result).isInstanceOf(Spannable.class);
    assertThat(result.toString()).isEqualTo("TitleMy embedded entry");

    final Object[] spans = ((Spannable) result).getSpans(0, result.length(), Object.class);
    assertThat(spans).hasLength(1);
    assertThat(spans[0]).isInstanceOf(ForegroundColorSpan.class);
  }
}
