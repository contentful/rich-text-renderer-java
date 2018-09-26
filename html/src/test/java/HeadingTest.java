import com.contentful.java.cda.structured.CDAStructuredHeading;
import com.contentful.structured.html.HtmlProcessor;

import org.junit.Test;

import static com.google.common.truth.Truth.assertThat;

public class HeadingTest {
  @Test
  public void canCreateAllHeadings() {
    final HtmlProcessor processor = new HtmlProcessor();

    assertThat(processor.render(new CDAStructuredHeading(0))).isEqualTo("<p>\n</p>\n");
    assertThat(processor.render(new CDAStructuredHeading(1))).isEqualTo("<h1>\n</h1>\n");
    assertThat(processor.render(new CDAStructuredHeading(2))).isEqualTo("<h2>\n</h2>\n");
    assertThat(processor.render(new CDAStructuredHeading(3))).isEqualTo("<h3>\n</h3>\n");
    assertThat(processor.render(new CDAStructuredHeading(4))).isEqualTo("<h4>\n</h4>\n");
    assertThat(processor.render(new CDAStructuredHeading(5))).isEqualTo("<h5>\n</h5>\n");
    assertThat(processor.render(new CDAStructuredHeading(6))).isEqualTo("<h6>\n</h6>\n");
    assertThat(processor.render(new CDAStructuredHeading(7))).isEqualTo("<p>\n</p>\n");
  }
}
