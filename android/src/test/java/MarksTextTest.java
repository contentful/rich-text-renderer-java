import android.app.Activity;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.style.BackgroundColorSpan;
import android.text.style.StyleSpan;
import android.text.style.TextAppearanceSpan;
import android.text.style.UnderlineSpan;

import com.contentful.java.cda.rich.CDARichMark;
import com.contentful.java.cda.rich.CDARichText;
import com.contentful.rich.android.CharSequenceProcessor;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.google.common.truth.Truth.assertThat;

@RunWith(RobolectricTestRunner.class)
public class MarksTextTest {
  private Activity activity;

  @Before
  public void setup() {
    activity = Robolectric.setupActivity(Activity.class);
  }

  @Test
  public void noMarksTest() {
    final CharSequenceProcessor processor = new CharSequenceProcessor(activity);

    final CharSequence result = processor.render(new CDARichText("text"));

    assertThat(result).isInstanceOf(Spannable.class);
    final Spannable spannable = (Spannable) result;
    assertThat(spannable).isNotNull();
    assertThat(spannable.toString()).isEqualTo("text");
    assertThat(spannable.getSpans(0, spannable.length(), Object.class).length).isEqualTo(0);
  }

  @Test
  public void boldTextTest() {
    final CharSequenceProcessor processor = new CharSequenceProcessor(activity);

    final List<CDARichMark> markers = new ArrayList<>();
    markers.add(new CDARichMark.CDARichMarkBold());

    final CharSequence result = processor.render(new CDARichText("BoldText", markers));

    assertThat(result).isInstanceOf(Spannable.class);

    final Spannable spannable = (Spannable) result;
    assertThat(spannable).isNotNull();
    assertThat(spannable.toString()).isEqualTo("BoldText");

    final Object[] spans = spannable.getSpans(0, spannable.length(), Object.class);
    assertThat(spans.length).isEqualTo(1);

    final Object span = spans[0];
    assertThat(span).isInstanceOf(StyleSpan.class);

    final StyleSpan styleSpan = (StyleSpan) span;
    assertThat(styleSpan.getStyle()).isEqualTo(Typeface.BOLD);
  }

  @Test
  public void underlineTextTest() {
    final CharSequenceProcessor processor = new CharSequenceProcessor(activity);

    final List<CDARichMark> markers = new ArrayList<>();
    markers.add(new CDARichMark.CDARichMarkUnderline());

    final CharSequence result = processor.render(new CDARichText("Underlined", markers));

    assertThat(result).isInstanceOf(Spannable.class);

    final Spannable spannable = (Spannable) result;
    assertThat(spannable).isNotNull();
    assertThat(spannable.toString()).isEqualTo("Underlined");

    final Object[] spans = spannable.getSpans(0, spannable.length(), Object.class);
    assertThat(spans.length).isEqualTo(1);

    final Object span = spans[0];
    assertThat(span).isInstanceOf(UnderlineSpan.class);
  }

  @Test
  public void testItalic() {
    final CharSequenceProcessor processor = new CharSequenceProcessor(activity);

    final List<CDARichMark> markers = new ArrayList<>();
    markers.add(new CDARichMark.CDARichMarkItalic());

    final CharSequence result = processor.render(new CDARichText("Italic", markers));

    assertThat(result).isInstanceOf(Spannable.class);

    final Spannable spannable = (Spannable) result;
    assertThat(spannable).isNotNull();
    assertThat(spannable.toString()).isEqualTo("Italic");

    final Object[] spans = spannable.getSpans(0, spannable.length(), Object.class);
    assertThat(spans.length).isEqualTo(1);

    final Object span = spans[0];
    assertThat(span).isInstanceOf(StyleSpan.class);

    final StyleSpan styleSpan = (StyleSpan) span;
    assertThat(styleSpan.getStyle()).isEqualTo(Typeface.ITALIC);
  }

  @Test
  public void codeTextTest() {
    final CharSequenceProcessor processor = new CharSequenceProcessor(activity);

    final List<CDARichMark> markers = new ArrayList<>();
    markers.add(new CDARichMark.CDARichMarkCode());

    final CharSequence result = processor.render(new CDARichText("final String code;", markers));

    assertThat(result).isInstanceOf(Spannable.class);

    final Spannable spannable = (Spannable) result;
    assertThat(spannable).isNotNull();
    assertThat(spannable.toString()).isEqualTo("final String code;");

    final Object[] spans = spannable.getSpans(0, spannable.length(), Object.class);
    assertThat(spans.length).isEqualTo(1);

    final Object span = spans[0];
    assertThat(span).isInstanceOf(TextAppearanceSpan.class);

    final TextAppearanceSpan styleSpan = (TextAppearanceSpan) span;
    assertThat(styleSpan.getFamily()).isEqualTo("monospace");
  }

  @Test
  public void customTextTest() {
    final CharSequenceProcessor processor = new CharSequenceProcessor(activity);

    final List<CDARichMark> markers = new ArrayList<>();
    markers.add(new CDARichMark.CDARichMarkCustom("🧀"));

    final CharSequence result = processor.render(new CDARichText("🐭", markers));

    assertThat(result).isInstanceOf(Spannable.class);

    final Spannable spannable = (Spannable) result;
    assertThat(spannable).isNotNull();
    assertThat(spannable.toString()).isEqualTo("🐭");

    final Object[] spans = spannable.getSpans(0, spannable.length(), Object.class);
    assertThat(spans.length).isEqualTo(1);

    final Object span = spans[0];
    assertThat(span).isInstanceOf(BackgroundColorSpan.class);
  }

  @Test
  public void allTextTest() {
    final CharSequenceProcessor processor = new CharSequenceProcessor(activity);

    final List<CDARichMark> markers = new ArrayList<>();
    markers.add(new CDARichMark.CDARichMarkCustom("custom"));
    markers.add(new CDARichMark.CDARichMarkItalic());
    markers.add(new CDARichMark.CDARichMarkBold());
    markers.add(new CDARichMark.CDARichMarkCode());
    markers.add(new CDARichMark.CDARichMarkUnderline());
    markers.add(new CDARichMark.CDARichMarkCustom("span"));

    final CharSequence result = processor.render(new CDARichText("All in all", markers));

    assertThat(result).isInstanceOf(Spannable.class);

    final Spannable spannable = (Spannable) result;
    assertThat(spannable).isNotNull();
    assertThat(spannable.toString()).isEqualTo("All in all");

    final Object[] spans = spannable.getSpans(0, spannable.length(), Object.class);
    assertThat(spans.length).isEqualTo(6);

    assertThat(Arrays.stream(spans).map(Object::getClass).collect(Collectors.toList())).containsExactlyElementsIn(
        new Object[]{BackgroundColorSpan.class, StyleSpan.class, StyleSpan.class, TextAppearanceSpan.class, UnderlineSpan.class, BackgroundColorSpan.class}
    );
  }
}
