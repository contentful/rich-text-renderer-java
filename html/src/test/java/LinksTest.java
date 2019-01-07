import com.contentful.java.cda.rich.CDARichBlock;
import com.contentful.java.cda.rich.CDARichEmbeddedBlock;
import com.contentful.java.cda.rich.CDARichHyperLink;
import com.contentful.java.cda.rich.CDARichText;
import com.contentful.rich.html.HtmlContext;
import com.contentful.rich.html.HtmlProcessor;

import org.junit.Test;

import java.util.ArrayList;

import static com.google.common.truth.Truth.assertThat;
import static lib.ContentfulCreator.mockCDAEntry;

public class LinksTest {

  @Test
  public void renderLinkTest() {
    final HtmlProcessor processor = new HtmlProcessor();
    final HtmlContext context = new HtmlContext();


    final CDARichHyperLink link = new CDARichHyperLink("https://contentful.com");
    link.getContent().add(new CDARichText("Some link text<br/>", new ArrayList<>()));

    final String result = processor.process(context, link);

    assertThat(result).isEqualTo("" +
        "<a href=\"https://contentful.com\">\n" +
        "  Some link text<br/>\n" +
        "</a>\n");
  }

  @Test
  public void createUnsanitzedStrings() {
    final HtmlProcessor processor = new HtmlProcessor();
    final HtmlContext context = new HtmlContext();

    final CDARichHyperLink link = new CDARichHyperLink("https://contentful.com");
    link.getContent().add(new CDARichText("Some link text</a>", new ArrayList<>()));

    final String result = processor.process(context, link);

    assertThat(result).isEqualTo("" +
        "<a href=\"https://contentful.com\">\n" +
        "  Some link text</a>\n" +
        "</a>\n");
  }
}
