package chars;

import android.app.Activity;
import android.text.Spannable;
import android.text.style.URLSpan;

import com.contentful.java.cda.rich.CDARichHyperLink;
import com.contentful.java.cda.rich.CDARichList;
import com.contentful.java.cda.rich.CDARichListItem;
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
public class LinkSafetyTest {
  private Activity activity;

  @Before
  public void setup() {
    activity = Robolectric.setupActivity(Activity.class);
  }

  private CharSequence render(String uri) {
    final CDARichHyperLink link = new CDARichHyperLink(uri);
    link.getContent().add(new CDARichText("Some link text", new ArrayList<>()));
    return AndroidProcessor.creatingCharSequences().process(new AndroidContext(activity), link);
  }

  private static URLSpan[] urlSpans(CharSequence result) {
    return ((Spannable) result).getSpans(0, result.length(), URLSpan.class);
  }

  @Test
  public void javascriptLinkIsNotClickable() {
    final CharSequence result = render("javascript:alert(1)");
    assertThat(result.toString()).isEqualTo("Some link text");
    assertThat(urlSpans(result)).isEmpty();
  }

  @Test
  public void intentAndTabObfuscatedLinksAreNotClickable() {
    assertThat(urlSpans(render("intent://scan/#Intent;scheme=zxing;end"))).isEmpty();
    assertThat(urlSpans(render("java\tscript:alert(1)"))).isEmpty();
  }

  @Test
  public void safeLinkIsClickable() {
    final URLSpan[] spans = urlSpans(render("https://contentful.com"));
    assertThat(spans).hasLength(1);
    assertThat(spans[0].getURL()).isEqualTo("https://contentful.com");
  }

  @Test
  public void bareHostGetsHttpScheme() {
    final URLSpan[] spans = urlSpans(render("contentful.com"));
    assertThat(spans).hasLength(1);
    assertThat(spans[0].getURL()).isEqualTo("http://contentful.com");
  }

  @Test
  public void listWithUnregisteredDecorationDoesNotCrash() {
    final CDARichListItem item = new CDARichListItem();
    item.getContent().add(new CDARichText("item", new ArrayList<>()));
    final CDARichList list = new CDARichList("?"); // no decorator registered for "?"
    list.getContent().add(item);

    final CharSequence result =
        AndroidProcessor.creatingCharSequences().process(new AndroidContext(activity), list);

    assertThat(result.toString()).contains("item");
  }
}
