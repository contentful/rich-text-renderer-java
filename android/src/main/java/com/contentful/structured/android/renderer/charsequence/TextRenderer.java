package com.contentful.structured.android.renderer.charsequence;

import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.BackgroundColorSpan;
import android.text.style.StyleSpan;
import android.text.style.TextAppearanceSpan;
import android.text.style.UnderlineSpan;

import com.contentful.java.cda.structured.CDAStructuredMark;
import com.contentful.java.cda.structured.CDAStructuredMark.CDAStructuredMarkBold;
import com.contentful.java.cda.structured.CDAStructuredMark.CDAStructuredMarkItalic;
import com.contentful.java.cda.structured.CDAStructuredNode;
import com.contentful.java.cda.structured.CDAStructuredText;
import com.contentful.structured.android.StructuredTextContext;
import com.contentful.structured.android.renderer.AndroidRenderer;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static android.text.Spanned.SPAN_EXCLUSIVE_EXCLUSIVE;
import static com.contentful.java.cda.structured.CDAStructuredMark.CDAStructuredMarkCode;
import static com.contentful.java.cda.structured.CDAStructuredMark.CDAStructuredMarkCustom;
import static com.contentful.java.cda.structured.CDAStructuredMark.CDAStructuredMarkUnderline;

public class TextRenderer implements AndroidRenderer<StructuredTextContext, CharSequence> {
  @Override public boolean check(@Nullable StructuredTextContext context, @Nonnull CDAStructuredNode node) {
    return node instanceof CDAStructuredText;
  }

  @Nullable @Override
  public CharSequence render(@Nonnull StructuredTextContext context, @Nonnull CDAStructuredNode node) {
    final CDAStructuredText structuredText = (CDAStructuredText) node;

    final Spannable result = new SpannableStringBuilder(structuredText.getText());

    for (final CDAStructuredMark mark : structuredText.getMarks()) {
      if (mark instanceof CDAStructuredMarkUnderline) {
        result.setSpan(new UnderlineSpan(), 0, result.length(), SPAN_EXCLUSIVE_EXCLUSIVE);
      } else if (mark instanceof CDAStructuredMarkBold) {
        result.setSpan(new StyleSpan(Typeface.BOLD), 0, result.length(), SPAN_EXCLUSIVE_EXCLUSIVE);
      } else if (mark instanceof CDAStructuredMarkItalic) {
        result.setSpan(new StyleSpan(Typeface.ITALIC), 0, result.length(), SPAN_EXCLUSIVE_EXCLUSIVE);
      } else if (mark instanceof CDAStructuredMarkCode) {
        result.setSpan(new TextAppearanceSpan("monospace", 0, 0, null, null), 0, result.length(), SPAN_EXCLUSIVE_EXCLUSIVE);
      } else if (mark instanceof CDAStructuredMarkCustom) {
        result.setSpan(new BackgroundColorSpan(0x80ffff00), 0, result.length(), SPAN_EXCLUSIVE_EXCLUSIVE);
      }
    }

    return result;
  }
}
