import com.contentful.java.cda.structured.CDAStructuredNode;
import com.contentful.structured.core.Context;
import com.contentful.structured.core.Processor;

import org.junit.Before;
import org.junit.Test;

import static com.google.common.truth.Truth.assertThat;

public class RendererOrderTest {
  Processor<Context<String>, String> processor;

  @Before
  public void setup() {
    processor = new Processor<>(new Context<>());
    processor.addRenderer(
        (context, node) -> true,
        (context, node) -> "catch all"
    );
  }

  @Test
  public void overrideRenderer() {
    processor.addRendererUpFront(
        (context, node) -> true,
        (context, node) -> "overridden"
    );

    final String result = processor.render(new CDAStructuredNode());
    assertThat(result).isEqualTo("overridden");
  }

  @Test
  public void addButNotOverride() {
    processor.addRenderer(
        (context, node) -> true,
        (context, node) -> "not overridden"
    );

    final String result = processor.render(new CDAStructuredNode());
    assertThat(result).isEqualTo("catch all");
  }

}
