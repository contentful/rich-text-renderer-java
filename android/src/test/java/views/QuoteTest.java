package views;

import android.app.Activity;
import android.content.Intent;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.ActivityNotFoundException;
import android.text.Spannable;
import android.text.style.ClickableSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.contentful.java.cda.rich.CDARichHyperLink;
import com.contentful.java.cda.rich.CDARichBlock;
import com.contentful.java.cda.rich.CDARichParagraph;
import com.contentful.java.cda.rich.CDARichTable;
import com.contentful.java.cda.rich.CDARichTableCell;
import com.contentful.java.cda.rich.CDARichTableRow;
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
import org.robolectric.RuntimeEnvironment;

import java.util.ArrayList;

import static com.google.common.truth.Truth.assertThat;

@RunWith(RobolectricTestRunner.class)
public class QuoteTest {
  private Activity activity;

  @Test public void quoteAndTableLinksSupportApplicationContextsAndMissingHandlers() {
    final Context application = RuntimeEnvironment.getApplication();
    final Context missingHandler = new ContextWrapper(application) {
      @Override public void startActivity(Intent intent) {
        throw new ActivityNotFoundException("No browser installed");
      }
    };
    for (Context context : new Context[]{application, missingHandler}) {
      final CDARichHyperLink link = new CDARichHyperLink("https://contentful.com");
      link.getContent().add(new CDARichText("Open link", new ArrayList<>()));
      final CDARichQuote quote = new CDARichQuote();
      quote.getContent().add(link);
      final CDARichParagraph paragraph = new CDARichParagraph();
      paragraph.getContent().add(link);
      final CDARichTableCell cell = new CDARichTableCell();
      cell.getContent().add(paragraph);
      final CDARichTableRow row = new CDARichTableRow();
      row.getContent().add(cell);
      final CDARichTable table = new CDARichTable();
      table.getContent().add(row);
      for (CDARichBlock block : new CDARichBlock[]{quote, table}) {
        final View result = AndroidProcessor.creatingNativeViews()
            .process(new AndroidContext(context), block);
        final ArrayList<View> matches = new ArrayList<>();
        result.findViewsWithText(matches, "Open link", View.FIND_VIEWS_WITH_TEXT);
        final TextView textView = (TextView) matches.get(0);
        final Spannable text = (Spannable) textView.getText();
        text.getSpans(0, text.length(), ClickableSpan.class)[0].onClick(textView);
        if (context == application) {
          final Intent intent = Shadows.shadowOf(RuntimeEnvironment.getApplication()).getNextStartedActivity();
          assertThat(intent.getFlags() & Intent.FLAG_ACTIVITY_NEW_TASK).isNotEqualTo(0);
        }
      }
    }
  }

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
