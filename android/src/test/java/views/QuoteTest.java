package views;

import android.app.Activity;
import android.content.Intent;
import android.text.Spannable;
import android.text.style.ClickableSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.contentful.java.cda.rich.CDARichHyperLink;
import com.contentful.java.cda.rich.CDARichQuote;
import com.contentful.java.cda.rich.CDARichText;
import com.contentful.rich.android.AndroidContext;
import com.contentful.rich.android.AndroidProcessor;
import com.contentful.rich.android.R;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.Shadows;

import java.util.ArrayList;

import static com.google.common.truth.Truth.assertThat;

@RunWith(RobolectricTestRunner.class)
public class QuoteTest {
  private Activity activity;

  @Before
  public void setup() {
    activity = Robolectric.setupActivity(Activity.class);
  }

  @Test
  public void quoteTest() {
    final AndroidProcessor<View> processor = AndroidProcessor.creatingNativeViews();
    final AndroidContext context = new AndroidContext(activity);
    final CDARichQuote quote = new CDARichQuote();

    quote.getContent().add(new CDARichText("Edel sei der Mensch,\n" +
        "Hilfreich und gut! — Johann Wolfgang von Goethe", new ArrayList<>()));

    final View result = processor.process(context, quote);

    assertThat(result).isNotNull();
    final View content = result.findViewById(R.id.rich_content);
    assertThat(content).isNotNull();

    final ArrayList<View> views = new ArrayList<>();
    content.findViewsWithText(views, "Edel sei der Mensch", View.FIND_VIEWS_WITH_TEXT);

    assertThat(views).isNotNull();
    assertThat(views).hasSize(1);
  }

  @Test
  public void quoteHyperlinkClickBlocksJavascriptUriScheme() {
    final AndroidProcessor<View> processor = AndroidProcessor.creatingNativeViews();
    final AndroidContext context = new AndroidContext(activity);
    final CDARichQuote quote = new CDARichQuote();

    final CDARichHyperLink hyperLink = new CDARichHyperLink("javascript:alert(1)");
    hyperLink.getContent().add(new CDARichText("Click me", new ArrayList<>()));
    quote.getContent().add(hyperLink);

    final View result = processor.process(context, quote);
    assertThat(result).isNotNull();
    final ViewGroup content = result.findViewById(R.id.rich_content);
    assertThat(content).isNotNull();

    TextView linkTextView = null;
    for (int i = 0; i < content.getChildCount(); i++) {
      final View child = content.getChildAt(i);
      if (child instanceof TextView) {
        linkTextView = (TextView) child;
      }
    }
    assertThat(linkTextView).isNotNull();

    final CharSequence text = linkTextView.getText();
    assertThat(text).isInstanceOf(Spannable.class);
    final ClickableSpan[] spans = ((Spannable) text).getSpans(0, text.length(), ClickableSpan.class);
    assertThat(spans).hasLength(1);

    spans[0].onClick(linkTextView);

    final Intent startedIntent = Shadows.shadowOf(activity).getNextStartedActivity();
    assertThat(startedIntent).isNull();
  }

  @Test
  public void quoteHyperlinkClickAllowsHttpUriScheme() {
    final AndroidProcessor<View> processor = AndroidProcessor.creatingNativeViews();
    final AndroidContext context = new AndroidContext(activity);
    final CDARichQuote quote = new CDARichQuote();

    final CDARichHyperLink hyperLink = new CDARichHyperLink("contentful.com");
    hyperLink.getContent().add(new CDARichText("Click me", new ArrayList<>()));
    quote.getContent().add(hyperLink);

    final View result = processor.process(context, quote);
    assertThat(result).isNotNull();
    final ViewGroup content = result.findViewById(R.id.rich_content);
    assertThat(content).isNotNull();

    TextView linkTextView = null;
    for (int i = 0; i < content.getChildCount(); i++) {
      final View child = content.getChildAt(i);
      if (child instanceof TextView) {
        linkTextView = (TextView) child;
      }
    }
    assertThat(linkTextView).isNotNull();

    final CharSequence text = linkTextView.getText();
    final ClickableSpan[] spans = ((Spannable) text).getSpans(0, text.length(), ClickableSpan.class);
    assertThat(spans).hasLength(1);

    spans[0].onClick(linkTextView);

    final Intent startedIntent = Shadows.shadowOf(activity).getNextStartedActivity();
    assertThat(startedIntent).isNotNull();
    assertThat(startedIntent.getData().toString()).isEqualTo("http://contentful.com");
  }
}
