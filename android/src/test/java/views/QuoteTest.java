package views;

import android.app.Activity;
import android.view.View;

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
}
