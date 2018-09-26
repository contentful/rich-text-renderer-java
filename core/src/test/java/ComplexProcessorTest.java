import com.contentful.java.cda.structured.CDAStructuredNode;
import com.contentful.java.cda.structured.CDAStructuredOrderedList;
import com.contentful.java.cda.structured.CDAStructuredParagraph;
import com.contentful.java.cda.structured.CDAStructuredText;
import com.contentful.java.cda.structured.CDAStructuredUnorderedList;
import com.contentful.structured.core.Context;
import com.contentful.structured.core.Processor;

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
        (context, node) -> node instanceof CDAStructuredParagraph,
        (context, node) -> ((CDAStructuredParagraph) node).getContent().stream().map(processor::render).collect(joining("\n"))
    );
    processor.addRenderer(
        (context, node) -> node instanceof CDAStructuredText,
        (context, node) -> {
          final List<String> path = context.getPath();
          final String text = ((CDAStructuredText) node).getText();
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

    final CDAStructuredUnorderedList unorderedList = new CDAStructuredUnorderedList();
    unorderedList.getContent().add(new CDAStructuredText(new ArrayList<>(), "unordered_a"));
    unorderedList.getContent().add(new CDAStructuredText(new ArrayList<>(), "unordered_b"));
    unorderedList.getContent().add(new CDAStructuredText(new ArrayList<>(), "unordered_c"));

    final CDAStructuredOrderedList innerList = new CDAStructuredOrderedList();
    innerList.getContent().add(new CDAStructuredText(new ArrayList<>(), "inner_first"));
    innerList.getContent().add(new CDAStructuredText(new ArrayList<>(), "inner_second"));
    innerList.getContent().add(unorderedList);
    innerList.getContent().add(new CDAStructuredText(new ArrayList<>(), "inner_third"));

    final CDAStructuredOrderedList outerList = new CDAStructuredOrderedList();
    outerList.getContent().add(new CDAStructuredText(new ArrayList<>(), "outer_top"));
    outerList.getContent().add(innerList);
    outerList.getContent().add(new CDAStructuredText(new ArrayList<>(), "outer_bottom"));

    final CDAStructuredParagraph paragraph = new CDAStructuredParagraph();
    paragraph.getContent().add(new CDAStructuredText(new ArrayList<>(), "first"));
    paragraph.getContent().add(new CDAStructuredText(new ArrayList<>(), "second"));
    paragraph.getContent().add(outerList);
    paragraph.getContent().add(new CDAStructuredText(new ArrayList<>(), "last"));

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

    @Override public void onParagraphEntered(@Nonnull CDAStructuredParagraph paragraph) {
      if (paragraph instanceof CDAStructuredOrderedList) {
        path.add("1");
      } else if (paragraph instanceof CDAStructuredUnorderedList) {
        path.add("*");
      } else {
        path.add("");
      }
    }

    @Override public void onParagraphExited(@Nonnull CDAStructuredParagraph paragraph) {
      path.remove(path.size() - 1);
    }

    @Override public void onSiblingEncountered(@Nonnull CDAStructuredNode node, int index) {
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
