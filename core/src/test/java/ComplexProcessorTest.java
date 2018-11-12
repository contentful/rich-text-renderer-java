import com.contentful.java.cda.rich.CDARichBlock;
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
    final Processor<SiblingCountingContext, String> processor = new Processor<>();
    final SiblingCountingContext siblingCountingContext = new SiblingCountingContext();
    processor.addRenderer(
        (context, node) -> node instanceof CDARichBlock,
        (context, node) -> ((CDARichBlock) node).getContent().stream().map(block -> processor.process(context, block)).collect(joining("\n"))
    );
    processor.addRenderer(
        (context, node) -> node instanceof CDARichText,
        (context, node) -> {
          final List<String> path = context.getPath();
          final CharSequence text = ((CDARichText) node).getText();
          String symbol = path.get(path.size() - 1);
          if ("1".equals(symbol)) {
            symbol = "1. ";
          } else if ("*".equals(symbol)) {
            symbol = "* ";
          }
          final String indentation = path.stream().map((x) -> "  ").collect(joining());

          return indentation + symbol + text;
        }
    );

    final CDARichUnorderedList unorderedList = new CDARichUnorderedList();
    unorderedList.getContent().add(new CDARichText("unordered_a", new ArrayList<>()));
    unorderedList.getContent().add(new CDARichText("unordered_b", new ArrayList<>()));
    unorderedList.getContent().add(new CDARichText("unordered_c", new ArrayList<>()));

    final CDARichOrderedList innerList = new CDARichOrderedList();
    innerList.getContent().add(new CDARichText("inner_first", new ArrayList<>()));
    innerList.getContent().add(new CDARichText("inner_second", new ArrayList<>()));
    innerList.getContent().add(unorderedList);
    innerList.getContent().add(new CDARichText("inner_third", new ArrayList<>()));

    final CDARichOrderedList outerList = new CDARichOrderedList();
    outerList.getContent().add(new CDARichText("outer_top", new ArrayList<>()));
    outerList.getContent().add(innerList);
    outerList.getContent().add(new CDARichText("outer_bottom", new ArrayList<>()));

    final CDARichParagraph paragraph = new CDARichParagraph();
    paragraph.getContent().add(new CDARichText("first", new ArrayList<>()));
    paragraph.getContent().add(new CDARichText("second", new ArrayList<>()));
    paragraph.getContent().add(outerList);
    paragraph.getContent().add(new CDARichText("last", new ArrayList<>()));

    final String result = processor.process(siblingCountingContext, paragraph);

    assertThat(result).isEqualTo(
        "  first\n" +
            "  second\n" +
            "    1. outer_top\n" +
            "      1. inner_first\n" +
            "      1. inner_second\n" +
            "        * unordered_a\n" +
            "        * unordered_b\n" +
            "        * unordered_c\n" +
            "      1. inner_third\n" +
            "    1. outer_bottom\n" +
            "  last");
  }

  private class SiblingCountingContext extends Context<List<String>> {
    private List<String> path = new ArrayList<>();

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

    @Nonnull @Override public List<String> getPath() {
      super.getPath();
      return path;
    }
  }
}
