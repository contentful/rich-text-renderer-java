import com.contentful.java.cda.structured.CDAStructuredHyperLink;
import com.contentful.java.cda.structured.CDAStructuredText;
import com.contentful.structured.html.HtmlProcessor;

import org.junit.Test;

import java.util.ArrayList;

import static com.google.common.truth.Truth.assertThat;

public class LinksTest {
  @Test
  public void threeUnorderedElementsListTest() {
    final HtmlProcessor processor = new HtmlProcessor();

    final CDAStructuredHyperLink link = new CDAStructuredHyperLink("https://contentful.com");
    link.getContent().add(new CDAStructuredText(new ArrayList<>(), "Some link text<br/>"));

    final String result = processor.render(link);

    assertThat(result).isEqualTo("<a href=\"https://contentful.com\">\n  Some link text<br/>\n</a>\n");
  }

  @Test
  public void createUnsanitzedStrings() {
    final HtmlProcessor processor = new HtmlProcessor();

    final CDAStructuredHyperLink link = new CDAStructuredHyperLink("https://contentful.com");
    link.getContent().add(new CDAStructuredText(new ArrayList<>(), "Some link text</a>"));

    final String result = processor.render(link);

    assertThat(result).isEqualTo("<a href=\"https://contentful.com\">\n  Some link text</a>\n</a>\n");
  }
}
