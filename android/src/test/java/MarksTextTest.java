import android.app.Activity;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.style.BackgroundColorSpan;
import android.text.style.StyleSpan;
import android.text.style.TextAppearanceSpan;
import android.text.style.UnderlineSpan;

import com.contentful.java.cda.structured.CDAStructuredMark;
import com.contentful.java.cda.structured.CDAStructuredText;
import com.contentful.structured.android.CharSequenceProcessor;

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

    final List<CDAStructuredMark> markers = new ArrayList<>();

    final CharSequence result = processor.render(new CDAStructuredText(markers, "text"));

    assertThat(result).isInstanceOf(Spannable.class);
    final Spannable spannable = (Spannable) result;
    assertThat(spannable).isNotNull();
    assertThat(spannable.toString()).isEqualTo("text");
    assertThat(spannable.getSpans(0, spannable.length(), Object.class).length).isEqualTo(0);
  }

  @Test
  public void boldTextTest() {
    final CharSequenceProcessor processor = new CharSequenceProcessor(activity);

    final List<CDAStructuredMark> markers = new ArrayList<>();
    markers.add(new CDAStructuredMark.CDAStructuredMarkBold());

    final CharSequence result = processor.render(new CDAStructuredText(markers, "BoldText"));

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

    final List<CDAStructuredMark> markers = new ArrayList<>();
    markers.add(new CDAStructuredMark.CDAStructuredMarkUnderline());

    final CharSequence result = processor.render(new CDAStructuredText(markers, "Underlined"));

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
  public void testItalicHtml() {
    final CharSequenceProcessor processor = new CharSequenceProcessor(activity);

    final List<CDAStructuredMark> markers = new ArrayList<>();
    markers.add(new CDAStructuredMark.CDAStructuredMarkItalic());

    final CharSequence result = processor.render(new CDAStructuredText(markers, "Italic"));

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

    final List<CDAStructuredMark> markers = new ArrayList<>();
    markers.add(new CDAStructuredMark.CDAStructuredMarkCode());

    final CharSequence result = processor.render(new CDAStructuredText(markers, "final String code;"));

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

    final List<CDAStructuredMark> markers = new ArrayList<>();
    markers.add(new CDAStructuredMark.CDAStructuredMarkCustom("🧀"));

    final CharSequence result = processor.render(new CDAStructuredText(markers, "🐭"));

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

    final List<CDAStructuredMark> markers = new ArrayList<>();
    markers.add(new CDAStructuredMark.CDAStructuredMarkCustom("custom"));
    markers.add(new CDAStructuredMark.CDAStructuredMarkItalic());
    markers.add(new CDAStructuredMark.CDAStructuredMarkBold());
    markers.add(new CDAStructuredMark.CDAStructuredMarkCode());
    markers.add(new CDAStructuredMark.CDAStructuredMarkUnderline());
    markers.add(new CDAStructuredMark.CDAStructuredMarkCustom("span"));

    final CharSequence result = processor.render(new CDAStructuredText(markers, "All in all"));

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
