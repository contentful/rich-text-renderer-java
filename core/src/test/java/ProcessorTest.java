import com.contentful.java.cda.rich.CDARichBlock;
import com.contentful.java.cda.rich.CDARichNode;
import com.contentful.java.cda.rich.CDARichParagraph;
import com.contentful.java.cda.rich.CDARichText;
import com.contentful.rich.core.Context;
import com.contentful.rich.core.Processor;

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
    final Processor<Context<String>, CharSequence> processor = new Processor<>(new Context<>());
    new DescendingTextRendererProvider().provide(processor);

    final CharSequence result = processor.render(new CDARichText("Foo"));

    assertThat(result).isEqualTo("Foo");
  }

  @Test
  public void contextFactoryGetsNotInvolvedInTexts() {
    final Processor<Context<String>, CharSequence> processor = new Processor<>(new Context<String>() {
      @Override public void onBlockEntered(@Nonnull CDARichBlock paragraph) {
        fail("No block to be entered");
      }

      @Override public void onBlockExited(@Nonnull CDARichBlock block) {
        fail("No block to be exited");
      }

      @Override public void onSiblingEncountered(@Nonnull CDARichNode node, int index) {
        // ignore siblings
      }

      @Override @Nonnull public String getPath() {
        return "Context!";
      }
    });
    new DescendingTextRendererProvider().provide(processor);

    final CharSequence result = processor.render(new CDARichText("Foo"));
    assertThat(result).isEqualTo("Foo");
  }

  @Test
  public void nestingTest() {
    final Processor<Context<String>, CharSequence> processor = new Processor<>(new Context<>());
    new DescendingTextRendererProvider().provide(processor);

    final CDARichParagraph paragraph = new CDARichParagraph();
    paragraph.getContent().add(new CDARichText("Foo"));

    final CharSequence result = processor.render(paragraph);
    assertThat(result).isEqualTo("--Foo");
  }

  @Test
  public void contextFactoryMethodsAreCalledTest() {
    final List<String> events = new ArrayList<>();

    final Processor<Context<String>, CharSequence> processor = new Processor<>(new Context<String>() {
      @Override public void onBlockEntered(@Nonnull CDARichBlock block) {
        events.add("entered");
      }

      @Override public void onBlockExited(@Nonnull CDARichBlock block) {
        events.add("exited");
      }

      @Override public void onSiblingEncountered(@Nonnull CDARichNode node, int index) {
        events.add("sibling " + index);
      }

      @Nullable @Override public String getPath() {
        return null;
      }
    });
    new DescendingTextRendererProvider().provide(processor);

    final CDARichParagraph paragraph = new CDARichParagraph();
    paragraph.getContent().add(new CDARichText("constant text"));
    paragraph.getContent().add(new CDARichText("constant text"));
    paragraph.getContent().add(new CDARichParagraph());

    processor.render(paragraph);

    assertThat(events).containsAllIn(new String[]{"entered", "sibling 0", "sibling 1", "sibling 2", "entered", "sibling 0", "exited", "exited"});
  }

}
