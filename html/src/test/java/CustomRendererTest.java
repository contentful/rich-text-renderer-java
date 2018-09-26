import com.contentful.java.cda.structured.CDAStructuredNode;
import com.contentful.java.cda.structured.CDAStructuredParagraph;
import com.contentful.structured.html.HtmlProcessor;
import com.contentful.structured.html.renderer.TagRenderer;

import org.junit.Test;

import static com.google.common.truth.Truth.assertThat;

public class CustomRendererTest {
  @Test
  public void overrideExistingRenderer() {
    final HtmlProcessor processor = new HtmlProcessor();
    processor.addRendererUpFront(
        (context, node) -> node instanceof CDAStructuredParagraph,
        new TagRenderer(processor, "pete")
    );

    final CDAStructuredParagraph paragraph = new CDAStructuredParagraph();

    final String result = processor.render(paragraph);

    assertThat(result).isEqualTo("<pete>\n</pete>\n");
  }

  @Test
  public void addFallbackRenderer() {
    final HtmlProcessor processor = new HtmlProcessor();
    processor.addRenderer(
        (context, node) -> true,
        new TagRenderer(processor, "\uD83E\uDD37")
    );

    final CDAStructuredNode node = new CDAStructuredNode();

    final String result = processor.render(node);

    assertThat(result).isEqualTo("<\uD83E\uDD37>\n</\uD83E\uDD37>\n");
  }
}
