package com.contentful.rich.android.renderer.charsequence;

import android.text.SpannableStringBuilder;

import com.contentful.java.cda.rich.CDARichBlock;
import com.contentful.java.cda.rich.CDARichList;
import com.contentful.java.cda.rich.CDARichNode;
import com.contentful.rich.android.RichTextContext;
import com.contentful.rich.android.renderer.charsequence.listdecorator.Decorator;
import com.contentful.rich.core.Processor;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import androidx.annotation.NonNull;

public class ListRenderer extends BlockRenderer {

  private final Map<String, Decorator> decoratorBySymbolMap = new HashMap<>();
  private final List<Decorator> decoratorList = new LinkedList<>();

  public ListRenderer(@Nonnull Processor<RichTextContext, CharSequence> processor, @Nonnull Decorator... decorators) {
    super(processor);

    decoratorList.addAll(Arrays.asList(decorators));

    for (final Decorator decorator : decorators) {
      this.decoratorBySymbolMap.put(decorator.getSymbol().toString(), decorator);
    }
  }

  @Override public boolean check(@Nullable RichTextContext context, @Nonnull CDARichNode node) {
    if (context != null) {
      final CDARichList list = context.getTopListOfPath();
      if (list != null) {
        return decoratorBySymbolMap.containsKey(list.getDecoration().toString());
      }
    }

    return false;
  }

  @Nullable @Override public CharSequence render(@Nonnull RichTextContext context, @Nonnull CDARichNode node) {
    final CDARichBlock block = (CDARichBlock) node;

    final SpannableStringBuilder result = new SpannableStringBuilder();

    for (final CDARichNode childNode : block.getContent()) {
      final CharSequence childResult = processor.render(childNode);
      result.append(childResult);
      childWithNewline(result);
    }

    final SpannableStringBuilder wrap = wrap(node, indent(context, node, decorate(context, node, result)));
    childWithNewline(wrap);
    return wrap;
  }

  @NonNull
  @Override protected SpannableStringBuilder decorate(
      @Nonnull RichTextContext context,
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

    return renderedChildren.insert(0, currentDecorator.decorate(childIndex)).append("\n");
  }

  private long getListOfTypeCount(@Nonnull RichTextContext context, CDARichList list) {
    return (context.getPath().stream().filter(new Predicate<CDARichNode>() {
      @Override public boolean test(CDARichNode x) {
        return x instanceof CDARichList && ((CDARichList) x).getDecoration().equals(list.getDecoration());
      }
    }).count() - 1);
  }

}
