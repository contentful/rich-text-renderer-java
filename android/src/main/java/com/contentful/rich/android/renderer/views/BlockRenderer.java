package com.contentful.rich.android.renderer.views;

import android.text.SpannableStringBuilder;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.LinearLayout;

import com.contentful.java.cda.rich.CDARichBlock;
import com.contentful.java.cda.rich.CDARichNode;
import com.contentful.rich.android.AndroidContext;
import com.contentful.rich.android.AndroidProcessor;
import com.contentful.rich.android.AndroidRenderer;
import com.contentful.rich.android.R;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class BlockRenderer extends AndroidRenderer<AndroidContext, View> {
  public BlockRenderer(@Nonnull AndroidProcessor<View> processor) {
    super(processor);
  }

  @Override public boolean canRender(@Nullable AndroidContext context, @Nonnull CDARichNode node) {
    return node instanceof CDARichBlock;
  }

  @Nullable @Override public View render(@Nonnull AndroidContext context, @Nonnull CDARichNode node) {
    final CDARichBlock block = (CDARichBlock) node;
    final View result = inflateRichLayout(context, node);
    final ViewGroup content = result.findViewById(R.id.rich_content);

    TextView lastTextView = null;
    for (final CDARichNode childNode : block.getContent()) {
      final View childView = processor.process(context, childNode);

      if (childView != null) {
        if (childView instanceof TextView) {
          final TextView childTextView = (TextView) childView;
          if (lastTextView != null) {
            CharSequence lastText = lastTextView.getText();
            CharSequence childText = childTextView.getText();
            if (lastText == null) {
              lastText = "";
            }
            if (childText == null) {
              childText = "";
            }
            lastTextView.setText(new SpannableStringBuilder(lastText).append(childText));
            if(childTextView.getMovementMethod() != null) {
              lastTextView.setMovementMethod(childTextView.getMovementMethod());
            }
          } else {
            lastTextView = childTextView;
            content.addView(childView);
          }
        } else {
          if (context.getPath() != null && context.getPath().size() > 1) {
            final View indented = context.getInflater().inflate(R.layout.rich_indention_layout, null, false);
            ((ViewGroup) indented.findViewById(R.id.rich_content)).addView(childView);
            content.addView(indented);
          } else {
            // Add margin to the child view
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            );
            params.topMargin = 16; // Add 16dp margin at the top
            childView.setLayoutParams(params);
            content.addView(childView);
          }
        }
      }
    }

    return result;
  }

  protected View inflateRichLayout(@Nonnull AndroidContext context, @Nonnull CDARichNode node) {
    return context.getInflater().inflate(R.layout.rich_block_layout, null, false);
  }
}
