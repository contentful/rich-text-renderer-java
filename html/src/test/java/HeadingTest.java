import com.contentful.java.cda.rich.CDARichHeading;
import com.contentful.rich.html.HtmlContext;
import com.contentful.rich.html.HtmlProcessor;

import org.junit.Test;

import static com.google.common.truth.Truth.assertThat;

public class HeadingTest {
  @Test
  public void canCreateAllHeadings() {
    final HtmlProcessor processor = new HtmlProcessor();
    final HtmlContext context = new HtmlContext();

    assertThat(processor.process(context, new CDARichHeading(1))).isEqualTo("<h1></h1>");
    assertThat(processor.process(context, new CDARichHeading(2))).isEqualTo("<h2></h2>");
    assertThat(processor.process(context, new CDARichHeading(3))).isEqualTo("<h3></h3>");
    assertThat(processor.process(context, new CDARichHeading(4))).isEqualTo("<h4></h4>");
    assertThat(processor.process(context, new CDARichHeading(5))).isEqualTo("<h5></h5>");
    assertThat(processor.process(context, new CDARichHeading(6))).isEqualTo("<h6></h6>");
  }
}
