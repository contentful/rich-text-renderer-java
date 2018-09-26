import com.contentful.java.cda.structured.CDAStructuredNode;
import com.contentful.java.cda.structured.CDAStructuredParagraph;
import com.contentful.java.cda.structured.CDAStructuredText;
import com.contentful.structured.core.Context;
import com.contentful.structured.core.Processor;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import lib.DescendingTextRendererProvider;

import static com.google.common.truth.Truth.assertThat;
import static junit.framework.TestCase.fail;

public class ProcessorTest {

  @Test
  public void renderTextTest() {
    final Processor<Context<String>, String> processor = new Processor<>(new Context<>());
    new DescendingTextRendererProvider().provide(processor);

    final String result = processor.render(new CDAStructuredText(new ArrayList<>(), "Foo"));

    assertThat(result).isEqualTo("Foo");
  }

  @Test
  public void contextFactoryGetsNotInvolvedInTexts() {
    final Processor<Context<String>, String> processor = new Processor<>(new Context<String>() {
      @Override public void onParagraphEntered(@Nonnull CDAStructuredParagraph paragraph) {
        fail("No block to be entered");
      }

      @Override public void onParagraphExited(@Nonnull CDAStructuredParagraph paragraph) {
        fail("No block to be exited");
      }

      @Override public void onSiblingEncountered(@Nonnull CDAStructuredNode node, int index) {
        // ignore siblings
      }

      @Override @Nonnull public String getPath() {
        return "Context!";
      }
    });
    new DescendingTextRendererProvider().provide(processor);

    final String result = processor.render(new CDAStructuredText(new ArrayList<>(), "Foo"));
    assertThat(result).isEqualTo("Foo");
  }

  @Test
  public void nestingTest() {
    final Processor<Context<String>, String> processor = new Processor<>(new Context<>());
    new DescendingTextRendererProvider().provide(processor);

    final CDAStructuredParagraph paragraph = new CDAStructuredParagraph();
    paragraph.getContent().add(new CDAStructuredText(new ArrayList<>(), "Foo"));

    final String result = processor.render(paragraph);
    assertThat(result).isEqualTo("--Foo");
  }

  @Test
  public void contextFactoryMethodsAreCalledTest() {
    final List<String> events = new ArrayList<>();

    final Processor<Context<String>, String> processor = new Processor<>(new Context<String>() {
      @Override public void onParagraphEntered(@Nonnull CDAStructuredParagraph paragraph) {
        events.add("entered");
      }

      @Override public void onParagraphExited(@Nonnull CDAStructuredParagraph paragraph) {
        events.add("exited");
      }

      @Override public void onSiblingEncountered(@Nonnull CDAStructuredNode node, int index) {
        events.add("sibling " + index);
      }

      @Nullable @Override public String getPath() {
        return null;
      }
    });
    new DescendingTextRendererProvider().provide(processor);

    final CDAStructuredParagraph paragraph = new CDAStructuredParagraph();
    paragraph.getContent().add(new CDAStructuredText(new ArrayList<>(), "constant text"));
    paragraph.getContent().add(new CDAStructuredText(new ArrayList<>(), "constant text"));
    paragraph.getContent().add(new CDAStructuredParagraph());

    processor.render(paragraph);

    assertThat(events).containsAllIn(new String[]{"entered", "sibling 0", "sibling 1", "sibling 2", "entered", "sibling 0", "exited", "exited"});
  }

}
