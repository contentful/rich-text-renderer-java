package com.contentful.rich.android.renderer.charsequence;

import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.BackgroundColorSpan;
import android.text.style.StyleSpan;
import android.text.style.TextAppearanceSpan;
import android.text.style.UnderlineSpan;

import com.contentful.java.cda.rich.CDARichMark;
import com.contentful.java.cda.rich.CDARichMark.CDARichMarkBold;
import com.contentful.java.cda.rich.CDARichMark.CDARichMarkItalic;
import com.contentful.java.cda.rich.CDARichNode;
import com.contentful.java.cda.rich.CDARichText;
import com.contentful.rich.android.RichTextContext;
import com.contentful.rich.android.renderer.AndroidRenderer;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static android.text.Spanned.SPAN_EXCLUSIVE_EXCLUSIVE;
import static com.contentful.java.cda.rich.CDARichMark.CDARichMarkCode;
import static com.contentful.java.cda.rich.CDARichMark.CDARichMarkCustom;
import static com.contentful.java.cda.rich.CDARichMark.CDARichMarkUnderline;

public class TextRenderer implements AndroidRenderer<RichTextContext, CharSequence> {
  @Override public boolean check(@Nullable RichTextContext context, @Nonnull CDARichNode node) {
    return node instanceof CDARichText;
  }

  @Nullable @Override
  public CharSequence render(@Nonnull RichTextContext context, @Nonnull CDARichNode node) {
    final CDARichText richText = (CDARichText) node;

    final Spannable result = new SpannableStringBuilder(richText.getText());

    for (final CDARichMark mark : richText.getMarks()) {
      if (mark instanceof CDARichMarkUnderline) {
        result.setSpan(new UnderlineSpan(), 0, result.length(), SPAN_EXCLUSIVE_EXCLUSIVE);
      } else if (mark instanceof CDARichMarkBold) {
        result.setSpan(new StyleSpan(Typeface.BOLD), 0, result.length(), SPAN_EXCLUSIVE_EXCLUSIVE);
      } else if (mark instanceof CDARichMarkItalic) {
        result.setSpan(new StyleSpan(Typeface.ITALIC), 0, result.length(), SPAN_EXCLUSIVE_EXCLUSIVE);
      } else if (mark instanceof CDARichMarkCode) {
        result.setSpan(new TextAppearanceSpan("monospace", 0, 0, null, null), 0, result.length(), SPAN_EXCLUSIVE_EXCLUSIVE);
      } else if (mark instanceof CDARichMarkCustom) {
        result.setSpan(new BackgroundColorSpan(0x80ffff00), 0, result.length(), SPAN_EXCLUSIVE_EXCLUSIVE);
      }
    }

    return result;
  }
}
