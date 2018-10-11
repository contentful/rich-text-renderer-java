import android.app.Activity;
import android.text.Spannable;

import com.contentful.java.cda.rich.CDARichQuote;
import com.contentful.java.cda.rich.CDARichText;
import com.contentful.rich.android.CharSequenceProcessor;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

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
    final CharSequenceProcessor processor = new CharSequenceProcessor(activity);
    final CDARichQuote quote = new CDARichQuote();

    quote.getContent().add(new CDARichText("Edel sei der Mensch,\n" +
        "Hilfreich und gut! — Johann Wolfgang von Goethe"));

    final CharSequence result = processor.render(quote);

    assertThat(result.toString()).isEqualTo("Edel sei der Mensch,\n" +
        "Hilfreich und gut! — Johann Wolfgang von Goethe");
    assertThat(((Spannable) result).getSpans(0, result.length(), Object.class)).hasLength(1);
  }
}
