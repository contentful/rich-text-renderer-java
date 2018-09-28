import com.contentful.java.cda.rich.CDARichHyperLink;
import com.contentful.java.cda.rich.CDARichText;
import com.contentful.rich.html.HtmlProcessor;

import org.junit.Test;

import static com.google.common.truth.Truth.assertThat;

public class LinksTest {
  @Test
  public void threeUnorderedElementsListTest() {
    final HtmlProcessor processor = new HtmlProcessor();

    final CDARichHyperLink link = new CDARichHyperLink("https://contentful.com");
    link.getContent().add(new CDARichText("Some link text<br/>"));

    final String result = processor.render(link);

    assertThat(result).isEqualTo("<a href=\"https://contentful.com\">\n  Some link text<br/>\n</a>\n");
  }

  @Test
  public void createUnsanitzedStrings() {
    final HtmlProcessor processor = new HtmlProcessor();

    final CDARichHyperLink link = new CDARichHyperLink("https://contentful.com");
    link.getContent().add(new CDARichText("Some link text</a>"));

    final String result = processor.render(link);

    assertThat(result).isEqualTo("<a href=\"https://contentful.com\">\n  Some link text</a>\n</a>\n");
  }
}
