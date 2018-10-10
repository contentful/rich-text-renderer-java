import com.contentful.java.cda.rich.CDARichBlock;
import com.contentful.java.cda.rich.CDARichNode;
import com.contentful.java.cda.rich.CDARichOrderedList;
import com.contentful.java.cda.rich.CDARichParagraph;
import com.contentful.java.cda.rich.CDARichText;
import com.contentful.java.cda.rich.CDARichUnorderedList;
import com.contentful.rich.core.Context;
import com.contentful.rich.core.Processor;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import static com.google.common.truth.Truth.assertThat;
import static java.util.stream.Collectors.joining;

public class ComplexProcessorTest {
  @Test
  public void complexListRenderingTest() {
    final Processor<SiblingCountingContext, String> processor = new Processor<>(new SiblingCountingContext());
    processor.addRenderer(
        (context, node) -> node instanceof CDARichBlock,
        (context, node) -> ((CDARichBlock) node).getContent().stream().map(processor::render).collect(joining("\n"))
    );
    processor.addRenderer(
        (context, node) -> node instanceof CDARichText,
        (context, node) -> {
          final List<String> path = context.getPath();
          final CharSequence text = ((CDARichText) node).getText();
          String symbol = path.get(path.size() - 1);
          if ("1".equals(symbol)) {
            symbol = context.getSiblingIndex() + ". ";
          } else if ("*".equals(symbol)) {
            symbol = "* ";
          }
          final String indentation = path.stream().map((x) -> "  ").collect(joining());

          return indentation + symbol + text;
        }
    );

    final CDARichUnorderedList unorderedList = new CDARichUnorderedList();
    unorderedList.getContent().add(new CDARichText("unordered_a"));
    unorderedList.getContent().add(new CDARichText("unordered_b"));
    unorderedList.getContent().add(new CDARichText("unordered_c"));

    final CDARichOrderedList innerList = new CDARichOrderedList();
    innerList.getContent().add(new CDARichText("inner_first"));
    innerList.getContent().add(new CDARichText("inner_second"));
    innerList.getContent().add(unorderedList);
    innerList.getContent().add(new CDARichText("inner_third"));

    final CDARichOrderedList outerList = new CDARichOrderedList();
    outerList.getContent().add(new CDARichText("outer_top"));
    outerList.getContent().add(innerList);
    outerList.getContent().add(new CDARichText("outer_bottom"));

    final CDARichParagraph paragraph = new CDARichParagraph();
    paragraph.getContent().add(new CDARichText("first"));
    paragraph.getContent().add(new CDARichText("second"));
    paragraph.getContent().add(outerList);
    paragraph.getContent().add(new CDARichText("last"));

    final String result = processor.render(paragraph);

    assertThat(result).isEqualTo(
        "  first\n" +
            "  second\n" +
            "    1. outer_top\n" +
            "      1. inner_first\n" +
            "      2. inner_second\n" +
            "        * unordered_a\n" +
            "        * unordered_b\n" +
            "        * unordered_c\n" +
            "      3. inner_third\n" +
            "    2. outer_bottom\n" +
            "  last");
  }

  private class SiblingCountingContext extends Context<List<String>> {
    private List<String> path = new ArrayList<>();
    private int siblingIndex = 0;

    @Override public void onBlockEntered(@Nonnull CDARichBlock block) {
      if (block instanceof CDARichOrderedList) {
        path.add("1");
      } else if (block instanceof CDARichUnorderedList) {
        path.add("*");
      } else {
        path.add("");
      }
    }

    @Override public void onBlockExited(@Nonnull CDARichBlock block) {
      path.remove(path.size() - 1);
    }

    @Override public void onSiblingEncountered(@Nonnull CDARichNode node, int index) {
      siblingIndex = index;
    }

    @Nonnull @Override public List<String> getPath() {
      super.getPath();
      return path;
    }

    int getSiblingIndex() {
      return siblingIndex;
    }
  }
}
