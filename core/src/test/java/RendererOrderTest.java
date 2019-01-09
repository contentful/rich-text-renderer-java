import com.contentful.java.cda.rich.CDARichNode;
import com.contentful.rich.core.Context;
import com.contentful.rich.core.Processor;

import org.junit.Before;
import org.junit.Test;

import static com.google.common.truth.Truth.assertThat;

public class RendererOrderTest {
  Processor<Context<String>, String> processor;

  @Before
  public void setup() {
    processor = new Processor<>();
    processor.addRenderer(
        (context, node) -> true,
        (context, node) -> "catch all"
    );
  }

  @Test
  public void overrideRenderer() {
    processor.overrideRenderer(
        (context, node) -> true,
        (context, node) -> "overridden"
    );

    final String result = processor.process(new Context<>(), new CDARichNode());
    assertThat(result).isEqualTo("overridden");
  }

  @Test
  public void addButNotOverride() {
    processor.addRenderer(
        (context, node) -> true,
        (context, node) -> "not overridden"
    );

    final String result = processor.process(new Context<>(), new CDARichNode());
    assertThat(result).isEqualTo("catch all");
  }

}
