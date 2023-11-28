package com.contentful.rich.android.renderer.views;

import android.text.SpannableStringBuilder;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.contentful.java.cda.rich.CDARichBlock;
import com.contentful.java.cda.rich.CDARichList;
import com.contentful.java.cda.rich.CDARichListItem;
import com.contentful.java.cda.rich.CDARichNode;
import com.contentful.rich.android.AndroidContext;
import com.contentful.rich.android.AndroidProcessor;
import com.contentful.rich.android.R;
import com.contentful.rich.android.renderer.listdecorator.Decorator;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListRenderer extends BlockRenderer {

  private final Map<CharSequence, Decorator> decoratorBySymbolMap = new HashMap<>();
  private final List<Decorator> decorators = new ArrayList<>();

  public ListRenderer(@Nonnull AndroidProcessor<View> processor, @Nonnull Decorator... decorators) {
    super(processor);

    this.decorators.addAll(Arrays.asList(decorators));

    for (final Decorator decorator : decorators) {
      this.decoratorBySymbolMap.put(decorator.getSymbol().toString(), decorator);
    }
  }

  @Override public boolean canRender(@Nullable AndroidContext context, @Nonnull CDARichNode node) {
    if (context != null && node instanceof CDARichListItem) {
      final CDARichList list = context.getTopListOfPath();
      if (list != null) {
        return decoratorBySymbolMap.containsKey(list.getDecoration().toString());
      }
    }

    return false;
  }

  @Nullable @Override public View render(@Nonnull AndroidContext context, @Nonnull CDARichNode node) {
    final CDARichBlock block = (CDARichBlock) node;
    final ViewGroup result = (ViewGroup) context.getInflater().inflate(R.layout.rich_list_layout, null, false);
    provideDecoration(context, result, node);

    final ViewGroup content = result.findViewById(R.id.rich_content);

    TextView lastTextView = null;
    for (final CDARichNode childNode : block.getContent()) {
      final View childView = processor.process(context, childNode);

      if (childView != null) {
        if (childView instanceof TextView) {
          final TextView childTextView = (TextView) childView;
          if (lastTextView != null) {
            lastTextView.setText(
                new SpannableStringBuilder(lastTextView.getText()).append(childTextView.getText())
            );
          } else {
            lastTextView = childTextView;
            content.addView(childView);
          }
        } else {
          content.addView(childView);
        }
      }
    }

    return result;
  }

  protected void provideDecoration(@Nonnull AndroidContext context, @Nonnull ViewGroup group, @Nonnull CDARichNode node) {
    final TextView decoration = group.findViewById(R.id.rich_list_decoration);

    final List<CDARichNode> path = context.getPath();
    CDARichList list = context.getTopListOfPath();
    final Decorator currentDecorator;
    final int childIndex;
    if (list == null) {
      list = (CDARichList) node;
      childIndex = 0;
      currentDecorator = decoratorBySymbolMap.get(list.getDecoration());
    } else {
      final int listIndex = path.indexOf(list);
      final int listItemIndexOnPath = listIndex + 1;
      childIndex = list.getContent().indexOf(path.get(listItemIndexOnPath));

      final int nestedListCount = (int) (getListOfTypeCount(context, list)) % Integer.MAX_VALUE;

      final Decorator initialDecorator = decoratorBySymbolMap.get(list.getDecoration().toString());
      final int initialDecoratorIndex = decorators.indexOf(initialDecorator);
      int currentPosition = ((initialDecoratorIndex + nestedListCount) % decorators.size()) - 1;
      if(currentPosition < 0) {
        currentPosition = 0;
      }

      currentDecorator = decorators.get(currentPosition);
    }


    decoration.setText(currentDecorator.decorate(childIndex + 1));
  }

  /**
   * Count lists on the path.
   *
   * @param context where is the path stored in? The context!
   * @param list    the list to be listed.
   * @return the number of lists of the supported type.
   */
  private long getListOfTypeCount(@Nonnull AndroidContext context, CDARichList list) {
    if (context.getPath() == null) {
      return 0;
    }
    int count = 0;
    for (CDARichNode node: context.getPath()) {
      if (node instanceof CDARichList && ((CDARichList) node).getDecoration().equals(list.getDecoration())) {
        count++;
      }
    }
    return count;
  }

}
