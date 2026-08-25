package views;

import android.app.Activity;
import android.graphics.Typeface;
import android.text.Spanned;
import android.text.style.BackgroundColorSpan;
import android.text.style.StyleSpan;
import android.text.style.TextAppearanceSpan;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.TextView;

import com.contentful.java.cda.rich.CDARichMark;
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
    final AndroidProcessor<View> processor = AndroidProcessor.creatingNativeViews();
    final AndroidContext context = new AndroidContext(activity);

    final View result = processor.process(context, new CDARichText("text", new ArrayList<>()));

    assertThat(result).isNotNull();
    final View content = result.findViewById(R.id.rich_content);
    assertThat(content).isNotNull();

    final ArrayList<View> views = new ArrayList<>();
    content.findViewsWithText(views, "text", View.FIND_VIEWS_WITH_TEXT);
    assertThat(views).hasSize(1);
    assertThat(views.get(0)).isInstanceOf(TextView.class);

    final CharSequence sequence = ((TextView) views.get(0)).getText();
    assertThat(sequence).isNotNull();
    assertThat(sequence).isInstanceOf(Spanned.class);
    assertThat(sequence.toString()).isEqualTo("text");

    final Spanned spannedString = (Spanned) sequence;
    assertThat(contentSpanCount(spannedString)).isEqualTo(0);
  }

  @Test
  public void boldTextTest() {
    final AndroidProcessor<View> processor = AndroidProcessor.creatingNativeViews();
    final AndroidContext context = new AndroidContext(activity);

    final List<CDARichMark> markers = new ArrayList<>();
    markers.add(new CDARichMark.CDARichMarkBold());

    final View result = processor.process(context, new CDARichText("BoldText", markers));

    assertThat(result).isNotNull();
    final View content = result.findViewById(R.id.rich_content);
    assertThat(content).isNotNull();

    final ArrayList<View> views = new ArrayList<>();
    content.findViewsWithText(views, "BoldText", View.FIND_VIEWS_WITH_TEXT);
    assertThat(views).hasSize(1);
    assertThat(views.get(0)).isInstanceOf(TextView.class);

    final CharSequence sequence = ((TextView) views.get(0)).getText();
    assertThat(sequence).isNotNull();
    assertThat(sequence).isInstanceOf(Spanned.class);
    assertThat(sequence.toString()).isEqualTo("BoldText");

    final Spanned spannedString = (Spanned) sequence;
    final Object[] spans = contentSpans(spannedString);
    assertThat(spans.length).isEqualTo(1);

    final Object span = spans[0];
    assertThat(span).isInstanceOf(StyleSpan.class);

    final StyleSpan styleSpan = (StyleSpan) span;
    assertThat(styleSpan.getStyle()).isEqualTo(Typeface.BOLD);
  }

  @Test
  public void underlineTextTest() {
    final AndroidProcessor<View> processor = AndroidProcessor.creatingNativeViews();
    final AndroidContext context = new AndroidContext(activity);

    final List<CDARichMark> markers = new ArrayList<>();
    markers.add(new CDARichMark.CDARichMarkUnderline());

    final View result = processor.process(context, new CDARichText("Underlined", markers));

    assertThat(result).isNotNull();
    final View content = result.findViewById(R.id.rich_content);
    assertThat(content).isNotNull();

    final ArrayList<View> views = new ArrayList<>();
    content.findViewsWithText(views, "Underlined", View.FIND_VIEWS_WITH_TEXT);
    assertThat(views).hasSize(1);
    assertThat(views.get(0)).isInstanceOf(TextView.class);

    final CharSequence sequence = ((TextView) views.get(0)).getText();
    assertThat(sequence).isNotNull();
    assertThat(sequence).isInstanceOf(Spanned.class);
    assertThat(sequence.toString()).isEqualTo("Underlined");

    final Spanned spannedString = (Spanned) sequence;
    assertThat(contentSpanCount(spannedString)).isEqualTo(1);

    final Object[] spans = contentSpans(spannedString);
    assertThat(spans.length).isEqualTo(1);

    final Object span = spans[0];
    assertThat(span).isInstanceOf(UnderlineSpan.class);
  }

  @Test
  public void testItalic() {
    final AndroidProcessor<View> processor = AndroidProcessor.creatingNativeViews();
    final AndroidContext context = new AndroidContext(activity);

    final List<CDARichMark> markers = new ArrayList<>();
    markers.add(new CDARichMark.CDARichMarkItalic());

    final View result = processor.process(context, new CDARichText("Italic", markers));

    assertThat(result).isNotNull();
    final View content = result.findViewById(R.id.rich_content);
    assertThat(content).isNotNull();

    final ArrayList<View> views = new ArrayList<>();
    content.findViewsWithText(views, "Italic", View.FIND_VIEWS_WITH_TEXT);
    assertThat(views).hasSize(1);
    assertThat(views.get(0)).isInstanceOf(TextView.class);

    final CharSequence sequence = ((TextView) views.get(0)).getText();
    assertThat(sequence).isNotNull();
    assertThat(sequence).isInstanceOf(Spanned.class);
    assertThat(sequence.toString()).isEqualTo("Italic");

    final Spanned spannable = (Spanned) sequence;
    assertThat(contentSpanCount(spannable)).isEqualTo(1);

    final Object[] spans = contentSpans(spannable);
    assertThat(spans.length).isEqualTo(1);

    final Object span = spans[0];
    assertThat(span).isInstanceOf(StyleSpan.class);

    final StyleSpan styleSpan = (StyleSpan) span;
    assertThat(styleSpan.getStyle()).isEqualTo(Typeface.ITALIC);
  }

  @Test
  public void codeTextTest() {
    final AndroidProcessor<View> processor = AndroidProcessor.creatingNativeViews();
    final AndroidContext context = new AndroidContext(activity);

    final List<CDARichMark> markers = new ArrayList<>();
    markers.add(new CDARichMark.CDARichMarkCode());

    final View result = processor.process(context, new CDARichText("final String code;", markers));

    assertThat(result).isNotNull();
    final View content = result.findViewById(R.id.rich_content);
    assertThat(content).isNotNull();

    final ArrayList<View> views = new ArrayList<>();
    content.findViewsWithText(views, "final String code;", View.FIND_VIEWS_WITH_TEXT);
    assertThat(views).hasSize(1);
    assertThat(views.get(0)).isInstanceOf(TextView.class);

    final CharSequence sequence = ((TextView) views.get(0)).getText();
    assertThat(sequence).isNotNull();
    assertThat(sequence).isInstanceOf(Spanned.class);
    assertThat(sequence.toString()).isEqualTo("final String code;");

    final Spanned spannable = (Spanned) sequence;
    assertThat(contentSpanCount(spannable)).isEqualTo(1);

    final Object[] spans = contentSpans(spannable);
    assertThat(spans.length).isEqualTo(1);

    final Object span = spans[0];
    assertThat(span).isInstanceOf(TextAppearanceSpan.class);

    final TextAppearanceSpan styleSpan = (TextAppearanceSpan) span;
    assertThat(styleSpan.getFamily()).isEqualTo("monospace");
  }

  @Test
  public void customTextTest() {
    final AndroidProcessor<View> processor = AndroidProcessor.creatingNativeViews();
    final AndroidContext context = new AndroidContext(activity);

    final List<CDARichMark> markers = new ArrayList<>();
    markers.add(new CDARichMark.CDARichMarkCustom("🧀"));

    final View result = processor.process(context, new CDARichText("🐭", markers));

    assertThat(result).isNotNull();
    final View content = result.findViewById(R.id.rich_content);
    assertThat(content).isNotNull();

    final ArrayList<View> views = new ArrayList<>();
    content.findViewsWithText(views, "🐭", View.FIND_VIEWS_WITH_TEXT);
    assertThat(views).hasSize(1);
    assertThat(views.get(0)).isInstanceOf(TextView.class);

    final CharSequence sequence = ((TextView) views.get(0)).getText();
    assertThat(sequence).isNotNull();
    assertThat(sequence).isInstanceOf(Spanned.class);
    assertThat(sequence.toString()).isEqualTo("🐭");

    final Spanned spannable = (Spanned) sequence;
    assertThat(contentSpanCount(spannable)).isEqualTo(1);

    final Object[] spans = contentSpans(spannable);
    assertThat(spans.length).isEqualTo(1);

    final Object span = spans[0];
    assertThat(span).isInstanceOf(BackgroundColorSpan.class);
  }

  @Test
  public void allTextTest() {
    final AndroidProcessor<View> processor = AndroidProcessor.creatingNativeViews();
    final AndroidContext context = new AndroidContext(activity);

    final List<CDARichMark> markers = new ArrayList<>();
    markers.add(new CDARichMark.CDARichMarkCustom("custom"));
    markers.add(new CDARichMark.CDARichMarkItalic());
    markers.add(new CDARichMark.CDARichMarkBold());
    markers.add(new CDARichMark.CDARichMarkCode());
    markers.add(new CDARichMark.CDARichMarkUnderline());
    markers.add(new CDARichMark.CDARichMarkCustom("span"));

    final View result = processor.process(context, new CDARichText("All in all", markers));

    assertThat(result).isNotNull();
    final View content = result.findViewById(R.id.rich_content);
    assertThat(content).isNotNull();

    final ArrayList<View> views = new ArrayList<>();
    content.findViewsWithText(views, "All in all", View.FIND_VIEWS_WITH_TEXT);
    assertThat(views).hasSize(1);
    assertThat(views.get(0)).isInstanceOf(TextView.class);

    final CharSequence sequence = ((TextView) views.get(0)).getText();
    assertThat(sequence).isNotNull();
    assertThat(sequence).isInstanceOf(Spanned.class);
    assertThat(sequence.toString()).isEqualTo("All in all");

    final Spanned spannable = (Spanned) sequence;
    assertThat(contentSpanCount(spannable)).isEqualTo(6);

    final Object[] spans = contentSpans(spannable);
    assertThat(spans.length).isEqualTo(6);

    assertThat(Arrays.stream(spans).map(Object::getClass).collect(Collectors.toList())).containsExactlyElementsIn(
        new Object[]{BackgroundColorSpan.class, StyleSpan.class, StyleSpan.class, TextAppearanceSpan.class, UnderlineSpan.class, BackgroundColorSpan.class}
    );
  }

  /**
   * Count spans excluding TextView's internal bookkeeping (for example ChangeWatcher),
   * which TextView adds itself once a MovementMethod is set.
   */
  private static Object[] contentSpans(android.text.Spanned spanned) {
    final java.util.List<Object> spans = new java.util.ArrayList<>();
    for (final Object span : spanned.getSpans(0, spanned.length(), Object.class)) {
      final String name = span.getClass().getName();
      if (!name.startsWith("android.widget.TextView$")
          && !name.startsWith("android.text.method.")) {
        spans.add(span);
      }
    }
    return spans.toArray();
  }

  private static int contentSpanCount(android.text.Spanned spanned) {
    return contentSpans(spanned).length;
  }
}
