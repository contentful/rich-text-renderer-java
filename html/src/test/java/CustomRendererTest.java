import com.contentful.java.cda.rich.CDARichNode;
import com.contentful.java.cda.rich.CDARichParagraph;
import com.contentful.rich.html.HtmlContext;
import com.contentful.rich.html.HtmlProcessor;
import com.contentful.rich.html.renderer.TagRenderer;

import org.junit.Test;

import static com.google.common.truth.Truth.assertThat;

public class CustomRendererTest {
  @Test
  public void overrideExistingRenderer() {
    final HtmlProcessor processor = new HtmlProcessor();
    processor.addRendererUpFront(
        (context, node) -> node instanceof CDARichParagraph,
        new TagRenderer(processor, "pete")
    );

    final CDARichParagraph paragraph = new CDARichParagraph();

    final String result = processor.process(new HtmlContext(), paragraph);

    assertThat(result).isEqualTo("<pete>\n</pete>\n");
  }

  @Test
  public void addFallbackRenderer() {
    final HtmlProcessor processor = new HtmlProcessor();
    processor.addRenderer(
        (context, node) -> true,
        new TagRenderer(processor, "\uD83E\uDD37")
    );

    final CDARichNode node = new CDARichNode();

    final String result = processor.process(new HtmlContext(), node);

    assertThat(result).isEqualTo("<\uD83E\uDD37>\n</\uD83E\uDD37>\n");
  }
}
