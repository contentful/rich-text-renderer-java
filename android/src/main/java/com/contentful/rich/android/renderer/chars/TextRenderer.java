package com.contentful.rich.android.renderer.chars;

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
import com.contentful.rich.android.AndroidContext;
import com.contentful.rich.android.AndroidProcessor;
import com.contentful.rich.android.AndroidRenderer;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static android.text.Spanned.SPAN_EXCLUSIVE_EXCLUSIVE;
import static com.contentful.java.cda.rich.CDARichMark.CDARichMarkCode;
import static com.contentful.java.cda.rich.CDARichMark.CDARichMarkCustom;
import static com.contentful.java.cda.rich.CDARichMark.CDARichMarkUnderline;

/**
 * This renderer will render a rich text node into a spannable, respecting it's marks.
 */
public class TextRenderer extends AndroidRenderer<AndroidContext, CharSequence> {
  /**
   * Constructor taking a processor for child processing.
   * <p>
   * Since CDARichText do not have children, the parameter will be ignored.
   *
   * @param processor used for subrendering of children.
   */
  public TextRenderer(@Nonnull AndroidProcessor<CharSequence> processor) {
    super(processor);
  }

  /**
   * Is the incoming node a rich text?
   *
   * @param context context this check should be performed in
   * @param node    node to be checked
   * @return true if the node is a rich text node.
   */
  @Override public boolean check(@Nullable AndroidContext context, @Nonnull CDARichNode node) {
    return node instanceof CDARichText;
  }

  /**
   * Converts the incoming rich text into a string and adds spans according to its markers.
   *
   * @param context the generic context this node should be rendered in.
   * @param node    the node to be rendered.
   * @return a spannable containing the text content of the rich text and decorations based on its markers.
   */
  @Nullable @Override public CharSequence render(@Nonnull AndroidContext context, @Nonnull CDARichNode node) {
    final CDARichText richText = (CDARichText) node;

    final Spannable result = new SpannableStringBuilder(richText.getText());

    for (final CDARichMark mark : richText.getMarks()) {
      if (mark instanceof CDARichMarkUnderline) {
        result.setSpan(new UnderlineSpan(), 0, result.length(), SPAN_EXCLUSIVE_EXCLUSIVE);
      }

      if (mark instanceof CDARichMarkBold) {
        result.setSpan(new StyleSpan(Typeface.BOLD), 0, result.length(), SPAN_EXCLUSIVE_EXCLUSIVE);
      }
      if (mark instanceof CDARichMarkItalic) {
        result.setSpan(new StyleSpan(Typeface.ITALIC), 0, result.length(), SPAN_EXCLUSIVE_EXCLUSIVE);
      }
      if (mark instanceof CDARichMarkCode) {
        result.setSpan(new TextAppearanceSpan("monospace", 0, 0, null, null), 0, result.length(), SPAN_EXCLUSIVE_EXCLUSIVE);
      }
      if (mark instanceof CDARichMarkCustom) {
        result.setSpan(new BackgroundColorSpan(0x80ffff00), 0, result.length(), SPAN_EXCLUSIVE_EXCLUSIVE);
      }
    }

    return result;
  }
}
