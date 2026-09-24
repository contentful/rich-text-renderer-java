import com.contentful.java.cda.rich.CDARichHyperLink;
import com.contentful.java.cda.rich.CDARichText;
import com.contentful.rich.html.HtmlContext;
import com.contentful.rich.html.HtmlProcessor;

import org.junit.Test;

import java.util.ArrayList;

import static com.google.common.truth.Truth.assertThat;

public class LinkSafetyTest {

  private static String render(String uri) {
    final CDARichHyperLink link = new CDARichHyperLink(uri);
    link.getContent().add(new CDARichText("text", new ArrayList<>()));
    return new HtmlProcessor().process(new HtmlContext(), link);
  }

  @Test
  public void javascriptLinkHasNoHref() {
    assertThat(render("javascript:alert(document.cookie)")).isEqualTo("<a>text</a>");
  }

  @Test
  public void tabObfuscatedJavascriptLinkHasNoHref() {
    assertThat(render("java\tscript:alert(1)")).isEqualTo("<a>text</a>");
  }

  @Test
  public void quoteCannotBreakOutOfTheAttribute() {
    assertThat(render("https://x.com/\" onmouseover=\"alert(1)"))
        .isEqualTo("<a href=\"https://x.com/&quot; onmouseover=&quot;alert(1)\">text</a>");
  }

  @Test
  public void relativeLinksAreKept() {
    assertThat(render("/about")).isEqualTo("<a href=\"/about\">text</a>");
  }

  @Test
  public void ampersandsAreEscaped() {
    assertThat(render("https://x.com/?a=1&b=2")).isEqualTo("<a href=\"https://x.com/?a=1&amp;b=2\">text</a>");
  }
}
