package com.contentful.rich.android.renderer.views;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.BackgroundColorSpan;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import com.contentful.java.cda.rich.CDARichHyperLink;
import com.contentful.java.cda.rich.CDARichMark;
import com.contentful.java.cda.rich.CDARichMark.CDARichMarkBold;
import com.contentful.java.cda.rich.CDARichMark.CDARichMarkCode;
import com.contentful.java.cda.rich.CDARichMark.CDARichMarkCustom;
import com.contentful.java.cda.rich.CDARichMark.CDARichMarkItalic;
import com.contentful.java.cda.rich.CDARichMark.CDARichMarkUnderline;
import com.contentful.java.cda.rich.CDARichNode;
import com.contentful.java.cda.rich.CDARichParagraph;
import com.contentful.java.cda.rich.CDARichQuote;
import com.contentful.java.cda.rich.CDARichText;
import com.contentful.rich.android.AndroidContext;
import com.contentful.rich.android.AndroidProcessor;
import com.contentful.rich.android.R;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class QuoteRenderer extends BlockRenderer {
  public QuoteRenderer(@Nonnull AndroidProcessor<View> processor) {
    super(processor);
  }

  @Override
  public boolean canRender(@Nullable AndroidContext context, @Nonnull CDARichNode node) {
    return node instanceof CDARichQuote;
  }

  @Override
  protected View inflateRichLayout(@Nonnull AndroidContext context, @Nonnull CDARichNode node) {
    return context.getInflater().inflate(R.layout.rich_quote_layout, null, false);
  }

  @Nullable
  @Override
  public View render(@Nonnull AndroidContext context, @Nonnull CDARichNode node) {
    final CDARichQuote quote = (CDARichQuote) node;
    final View result = inflateRichLayout(context, node);
    final ViewGroup content = result.findViewById(R.id.rich_content);
    
    // Set proper layout parameters
    content.setLayoutParams(new LinearLayout.LayoutParams(
        ViewGroup.LayoutParams.MATCH_PARENT,
        ViewGroup.LayoutParams.WRAP_CONTENT
    ));

    // Process quote content
    for (final CDARichNode childNode : quote.getContent()) {
      if (childNode instanceof CDARichParagraph) {
        final TextView paragraphView = new TextView(context.getAndroidContext());
        SpannableStringBuilder paragraphContent = new SpannableStringBuilder();
        
        // Enable clickable links
        paragraphView.setMovementMethod(LinkMovementMethod.getInstance());
        
        // Process paragraph content
        for (final CDARichNode paragraphChild : ((CDARichParagraph) childNode).getContent()) {
          if (paragraphChild instanceof CDARichText) {
            final CDARichText richText = (CDARichText) paragraphChild;
            SpannableString textContent = new SpannableString(richText.getText());
            
            // Apply marks
            for (final CDARichMark mark : richText.getMarks()) {
              if (mark instanceof CDARichMarkBold) {
                textContent.setSpan(new StyleSpan(Typeface.BOLD), 0, textContent.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
              }
              if (mark instanceof CDARichMarkItalic) {
                textContent.setSpan(new StyleSpan(Typeface.ITALIC), 0, textContent.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
              }
              if (mark instanceof CDARichMarkUnderline) {
                textContent.setSpan(new UnderlineSpan(), 0, textContent.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
              }
              if (mark instanceof CDARichMarkCode) {
                textContent.setSpan(new BackgroundColorSpan(0xFFF5F5F5), 0, textContent.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                textContent.setSpan(new StyleSpan(Typeface.MONOSPACE.getStyle()), 0, textContent.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
              }
              if (mark instanceof CDARichMarkCustom) {
                textContent.setSpan(new BackgroundColorSpan(0x80FFFF00), 0, textContent.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
              }
            }
            
            paragraphContent.append(textContent);
          } else if (paragraphChild instanceof CDARichHyperLink) {
            final CDARichHyperLink hyperlink = (CDARichHyperLink) paragraphChild;
            final String uri = (String) hyperlink.getData();
            
            // Process hyperlink content
            for (final CDARichNode hyperlinkContent : hyperlink.getContent()) {
              if (hyperlinkContent instanceof CDARichText) {
                final CDARichText richText = (CDARichText) hyperlinkContent;
                SpannableString linkText = new SpannableString(richText.getText());
                
                // Apply link styling
                ClickableSpan clickableSpan = new ClickableSpan() {
                  @Override
                  public void onClick(@NonNull View widget) {
                    String url = uri.startsWith("http") ? uri : "http://" + uri;
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    context.getAndroidContext().startActivity(intent);
                  }
                };
                linkText.setSpan(clickableSpan, 0, linkText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                linkText.setSpan(new ForegroundColorSpan(Color.parseColor("#0645AD")), 0, linkText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                linkText.setSpan(new UnderlineSpan(), 0, linkText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                
                // Apply any additional marks from the hyperlink text
                for (final CDARichMark mark : richText.getMarks()) {
                  if (mark instanceof CDARichMarkBold) {
                    linkText.setSpan(new StyleSpan(Typeface.BOLD), 0, linkText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                  }
                  if (mark instanceof CDARichMarkItalic) {
                    linkText.setSpan(new StyleSpan(Typeface.ITALIC), 0, linkText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                  }
                }
                
                paragraphContent.append(linkText);
              }
            }
          }
        }
        
        // Set paragraph properties
        paragraphView.setPadding(0, 4, 0, 4);
        paragraphView.setText(paragraphContent);
        paragraphView.setLayoutParams(new LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        ));
        
        content.addView(paragraphView);
      }
    }

    return result;
  }
}
