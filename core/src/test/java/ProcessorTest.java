import com.contentful.java.cda.rich.CDARichBlock;
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
    final Processor<Context<String>, CharSequence> processor = new Processor<>();
    new DescendingTextRendererProvider().provide(processor);

    final CharSequence result = processor.process(new Context<>(), new CDARichText("Foo", new ArrayList<>()));

    assertThat(result).isEqualTo("Foo");
  }

  @Test
  public void contextFactoryGetsNotInvolvedInTexts() {
    final Processor<Context<String>, CharSequence> processor = new Processor<>();
    final Context<String> context = new Context<String>() {
      @Override public void onBlockEntered(@Nonnull CDARichBlock paragraph) {
        fail("No block to be entered");
      }

      @Override public void onBlockExited(@Nonnull CDARichBlock block) {
        fail("No block to be exited");
      }

      @Override @Nonnull public String getPath() {
        return "Context!";
      }
    };

    new DescendingTextRendererProvider().provide(processor);

    final CharSequence result = processor.process(context, new CDARichText("Foo", new ArrayList<>()));
    assertThat(result).isEqualTo("Foo");
  }

  @Test
  public void nestingTest() {
    final Processor<Context<String>, CharSequence> processor = new Processor<>();
    new DescendingTextRendererProvider().provide(processor);

    final CDARichParagraph paragraph = new CDARichParagraph();
    paragraph.getContent().add(new CDARichText("Foo", new ArrayList<>()));

    final CharSequence result = processor.process(new Context<>(), paragraph);
    assertThat(result).isEqualTo("--Foo");
  }

  @Test
  public void contextFactoryMethodsAreCalledTest() {
    final List<String> events = new ArrayList<>();

    final Processor<Context<String>, CharSequence> processor = new Processor<>();
    final Context<String> context = new Context<String>() {
      @Override public void onBlockEntered(@Nonnull CDARichBlock block) {
        events.add("entered");
      }

      @Override public void onBlockExited(@Nonnull CDARichBlock block) {
        events.add("exited");
      }

      @Nullable @Override public String getPath() {
        return null;
      }
    };

    new DescendingTextRendererProvider().provide(processor);

    final CDARichParagraph paragraph = new CDARichParagraph();
    paragraph.getContent().add(new CDARichText("constant text", new ArrayList<>()));
    paragraph.getContent().add(new CDARichText("constant text", new ArrayList<>()));
    paragraph.getContent().add(new CDARichParagraph());

    processor.process(context, paragraph);

    assertThat(events).containsAllIn(new String[]{"entered", "entered", "exited", "exited"});
  }

}
