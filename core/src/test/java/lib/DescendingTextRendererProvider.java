package lib;

import com.contentful.java.cda.rich.CDARichParagraph;
import com.contentful.java.cda.rich.CDARichText;
import com.contentful.rich.core.Context;
import com.contentful.rich.core.Processor;

import java.util.stream.Collectors;

import javax.annotation.Nonnull;

public class DescendingTextRendererProvider {
  public void provide(@Nonnull Processor<Context<String>, CharSequence> processor) {
    processor.addRenderer(
        (context, node) -> node instanceof CDARichText,
        (context, node) -> ((CDARichText) node).getText()
    );

    processor.addRenderer(
        (context, node) -> node instanceof CDARichParagraph,
        (context, node) -> {
          final CDARichParagraph block = (CDARichParagraph) node;
          return "--" + block.getContent().stream().map(innerBlock -> processor.process(context, innerBlock)).collect(Collectors.joining());
        }
    );
  }
}
