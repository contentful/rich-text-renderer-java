package com.contentful.rich.android.renderer.views;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.provider.Browser;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import com.contentful.java.cda.rich.CDARichBlock;
import com.contentful.java.cda.rich.CDARichHyperLink;
import com.contentful.java.cda.rich.CDARichNode;
import com.contentful.java.cda.rich.CDARichText;
import com.contentful.rich.android.AndroidContext;
import com.contentful.rich.android.AndroidProcessor;
import com.contentful.rich.android.R;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Map;

public class HyperLinkRenderer extends BlockRenderer {
  public HyperLinkRenderer(@Nonnull AndroidProcessor<View> processor) {
    super(processor);
  }

  @Override public boolean canRender(@Nullable AndroidContext context, @Nonnull CDARichNode node) {
    if (!(node instanceof CDARichHyperLink)) {
        return false;
    }
    Object data = ((CDARichHyperLink)node).getData();
    return data instanceof String || (data instanceof Map && ((Map<?, ?>) data).containsKey("uri"));
  }

  @Override protected View inflateRichLayout(@Nonnull AndroidContext context, @Nonnull CDARichNode node) {
    final View inflate = context.getInflater().inflate(R.layout.rich_text_layout, null, false);
    inflate.setOnClickListener(v -> HyperLinkRenderer.this.onClick(context, node));
    return inflate;
  }

  @Nullable
  @Override
  public View render(@Nonnull AndroidContext context, @Nonnull CDARichNode node) {
    final CDARichHyperLink hyperlink = (CDARichHyperLink) node;
    if(hyperlink.getContent().get(0) != null) {
      final CDARichText richText = (CDARichText) hyperlink.getContent().get(0);
      final View result = context.getInflater().inflate(R.layout.rich_text_layout, null);
      final TextView content = result.findViewById(R.id.rich_content);
      final SpannableStringBuilder textContent = new SpannableStringBuilder(richText.getText());
      ClickableSpan clickableSpan = new ClickableSpan() {
        @Override
        public void onClick(View textView) {
          HyperLinkRenderer.this.onClick(context, node);
        }

        @Override
        public void updateDrawState(@NonNull TextPaint ds) {
          ds.linkColor = Color.parseColor("#0645AD");
          super.updateDrawState(ds);
        }
      };

      content.setMovementMethod(LinkMovementMethod.getInstance());
      textContent.setSpan(clickableSpan, 0, textContent.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
      content.setText(textContent);
      return result;
    } else {
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
              lastTextView.setText(
                      new SpannableStringBuilder(lastTextView.getText()).append(childTextView.getText())
              );
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
              content.addView(childView);
            }
          }
        }
      }

      return result;
    }

  }

  public void onClick(AndroidContext context, CDARichNode node) {
    final Context androidContext = context.getAndroidContext();
    final Object data = ((CDARichHyperLink) node).getData();
    final String uri;

    if (data instanceof String) {
        uri = (String) data;
    } else if (data instanceof Map) {
        uri = (String) ((Map<?, ?>) data).get("uri");
    } else {
        return; // Don't handle click if data is neither String nor Map
    }

    final Uri parsedUri = Uri.parse(uri);
    final Intent intent = new Intent(Intent.ACTION_VIEW, parsedUri);
    intent.putExtra(Browser.EXTRA_APPLICATION_ID, androidContext.getPackageName());
    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    try {
        androidContext.startActivity(intent);
    } catch (ActivityNotFoundException e) {
        Log.w("URLSpan", "Activity was not found for intent, " + intent.toString());
    }
  }
}
