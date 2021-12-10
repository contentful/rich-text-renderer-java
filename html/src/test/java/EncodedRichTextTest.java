import com.contentful.java.cda.rich.CDARichText;
import com.contentful.rich.html.HtmlContext;
import com.contentful.rich.html.HtmlProcessor;

import org.junit.Test;

import java.util.ArrayList;

import static com.google.common.truth.Truth.assertThat;

public class EncodedRichTextTest {

  @Test
  public void renderRichTextTest() {
    final HtmlProcessor processor = new HtmlProcessor();
    final HtmlContext context = new HtmlContext();


    final CDARichText text = new CDARichText("Some rich text with html tags<br/>", new ArrayList<>());
    final String result = processor.process(context, text);

    assertThat(result).isEqualTo("Some rich text with html tags&lt;br/&gt;\n");
  }
}
