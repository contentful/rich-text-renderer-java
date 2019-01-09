package com.contentful.rich.android.renderer.chars;

import android.text.SpannableStringBuilder;

import com.contentful.java.cda.rich.CDARichBlock;
import com.contentful.java.cda.rich.CDARichList;
import com.contentful.java.cda.rich.CDARichListItem;
import com.contentful.java.cda.rich.CDARichNode;
import com.contentful.rich.android.AndroidContext;
import com.contentful.rich.android.AndroidProcessor;
import com.contentful.rich.android.renderer.listdecorator.Decorator;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import androidx.annotation.NonNull;

/**
 * Traverse a list of nodes, rendering everyone with a given decoration.
 */
public class ListRenderer extends BlockRenderer {

  private final Map<String, Decorator> decoratorBySymbolMap = new HashMap<>();
  private final List<Decorator> decoratorList = new LinkedList<>();

  /**
   * Create a renderer, responsabile for rendering given decorations.
   *
   * @param processor  the processor for child rendering.
   * @param decorators an array of decorators, decorating each child.
   */
  public ListRenderer(@Nonnull AndroidProcessor<CharSequence> processor, @Nonnull Decorator... decorators) {
    super(processor);

    decoratorList.addAll(Arrays.asList(decorators));

    for (final Decorator decorator : decorators) {
      this.decoratorBySymbolMap.put(decorator.getSymbol().toString(), decorator);
    }
  }

  /**
   * Is this a list? And is there one decorator matching?
   *
   * @param context context this check should be performed in
   * @param node    node to be checked
   * @return true if it is a list and atelast one decorator can be used.
   */
  @Override public boolean check(@Nullable AndroidContext context, @Nonnull CDARichNode node) {
    if (context != null && node instanceof CDARichListItem) {
      final CDARichList list = context.getTopListOfPath();
      if (list != null) {
        return decoratorBySymbolMap.containsKey(list.getDecoration().toString());
      }
    }

    return false;
  }

  /**
   * Render all children, decorating them as described.
   *
   * @param context the generic context this node should be rendered in.
   * @param node    the node to be rendered.
   * @return a spanable of a list of rendered and decorated children.
   */
  @Nullable @Override public CharSequence render(@Nonnull AndroidContext context, @Nonnull CDARichNode node) {
    final CDARichBlock block = (CDARichBlock) node;

    final SpannableStringBuilder result = new SpannableStringBuilder();

    for (final CDARichNode childNode : block.getContent()) {
      final CharSequence childResult = processor.process(context, childNode);
      result.append(childResult);
      childWithNewline(result);
    }

    final SpannableStringBuilder wrap = wrap(node, indent(context, node, decorate(context, node, result)));
    childWithNewline(wrap);
    return wrap;
  }

  /**
   * Decorate the given elements.
   *
   * @param context          the context this decoration should be applied to.
   * @param node             the actual node, rendered into the builder.
   * @param renderedChildren the builder containing the rendered children.
   * @return the decorated and rendered children.
   */
  @NonNull @Override protected SpannableStringBuilder decorate(
      @Nonnull AndroidContext context,
      @Nonnull CDARichNode node,
      @Nonnull SpannableStringBuilder renderedChildren) {
    final List<CDARichNode> path = context.getPath();
    if (path == null || path.size() <= 1) {
      return renderedChildren;
    }
    if (renderedChildren.getSpans(0, 1, Decorator.class).length > 0) {
      return renderedChildren;
    }

    final CDARichList list = context.getTopListOfPath();

    final int listIndex = path.indexOf(list);
    final int listItemIndexOnPath = listIndex + 1;
    final int childIndex = list.getContent().indexOf(path.get(listItemIndexOnPath));

    final int nestedListCount = (int) (getListOfTypeCount(context, list)) % Integer.MAX_VALUE;

    final Decorator initialDecorator = decoratorBySymbolMap.get(list.getDecoration().toString());
    final int initialDecoratorIndex = decoratorList.indexOf(initialDecorator);
    final int currentPosition = (initialDecoratorIndex + nestedListCount) % decoratorList.size();
    final Decorator currentDecorator = decoratorList.get(currentPosition);

    return renderedChildren.insert(0, currentDecorator.decorate(childIndex + 1)).append("\n");
  }

  /**
   * Ensure the child contains a newline. If not, add it.
   *
   * @param builder the current rendered spannable builder.
   */
  @Override protected void childWithNewline(@Nonnull SpannableStringBuilder builder) {
    if (builder.toString().endsWith("\n\n")) {
      builder.replace(builder.length() - 2, builder.length(), "\n");
    } else if (!builder.toString().endsWith("\n")) {
      builder.append("\n");
    }
  }

  /**
   * Count lists on the path.
   *
   * @param context where is the path stored in? The context!
   * @param list    the list to be listed.
   * @return the number of lists of the supported type.
   */
  private long getListOfTypeCount(@Nonnull AndroidContext context, CDARichList list) {
    return (context.getPath().stream().filter(new Predicate<CDARichNode>() {
      @Override public boolean test(CDARichNode x) {
        return x instanceof CDARichList && ((CDARichList) x).getDecoration().equals(list.getDecoration());
      }
    }).count() - 1);
  }
}
