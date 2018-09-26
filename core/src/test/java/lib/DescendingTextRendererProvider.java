package lib;

import com.contentful.java.cda.structured.CDAStructuredParagraph;
import com.contentful.java.cda.structured.CDAStructuredText;
import com.contentful.structured.core.Context;
import com.contentful.structured.core.Processor;
import com.contentful.structured.core.RendererProvider;

import java.util.stream.Collectors;

public class DescendingTextRendererProvider implements RendererProvider<Context<String>, String> {
  @Override public void provide(Processor<Context<String>, String> processor) {
    processor.addRenderer(
        (context, node) -> node instanceof CDAStructuredText,
        (context, node) -> ((CDAStructuredText) node).getText()
    );

    processor.addRenderer(
        (context, node) -> node instanceof CDAStructuredParagraph,
        (context, node) -> {
          final CDAStructuredParagraph block = (CDAStructuredParagraph) node;
          return "--" + block.getContent().stream().map(processor::render).collect(Collectors.joining());
        }
    );
  }
}
