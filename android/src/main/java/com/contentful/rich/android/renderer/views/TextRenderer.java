package com.contentful.rich.android.renderer.views;

import android.graphics.Typeface;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.text.style.BackgroundColorSpan;
import android.text.style.StyleSpan;
import android.text.style.SubscriptSpan;
import android.text.style.SuperscriptSpan;
import android.text.style.TextAppearanceSpan;
import android.text.style.UnderlineSpan;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;
import com.contentful.java.cda.rich.CDARichHeading;
import com.contentful.java.cda.rich.CDARichMark;
import com.contentful.java.cda.rich.CDARichMark.CDARichMarkBold;
import com.contentful.java.cda.rich.CDARichMark.CDARichMarkItalic;
import com.contentful.java.cda.rich.CDARichNode;
import com.contentful.java.cda.rich.CDARichText;
import com.contentful.rich.android.AndroidContext;
import com.contentful.rich.android.AndroidProcessor;
import com.contentful.rich.android.AndroidRenderer;
import com.contentful.rich.android.R;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static android.text.Spanned.SPAN_EXCLUSIVE_EXCLUSIVE;
import static com.contentful.java.cda.rich.CDARichMark.CDARichMarkCode;
import static com.contentful.java.cda.rich.CDARichMark.CDARichMarkCustom;
import static com.contentful.java.cda.rich.CDARichMark.CDARichMarkUnderline;

/**
 * This renderer will render a rich text node into a TextView, respecting it's marks.
 */
public class TextRenderer extends AndroidRenderer<AndroidContext, View> {
  /**
   * Constructor taking a processor for child processing.
   * <p>
   * Since CDARichText do not have children, the parameter will be ignored.
   *
   * @param processor used for subrendering of children.
   */
  public TextRenderer(@Nonnull AndroidProcessor<View> processor) {
    super(processor);
  }

  /**
   * Is the incoming node a rich text?
   *
   * @param context context this check should be performed in
   * @param node    node to be checked
   * @return true if the node is a rich text node.
   */
  @Override public boolean canRender(@Nullable AndroidContext context, @Nonnull CDARichNode node) {
    return node instanceof CDARichText;
  }

  /**
   * Converts the incoming rich text into a string and adds spans according to its markers.
   *
   * @param context the generic context this node should be rendered in.
   * @param node    the node to be rendered.
   * @return a view containing the text content of the rich text and decorations based on its markers.
   */
  @Nullable @Override public View render(@Nonnull AndroidContext context, @Nonnull CDARichNode node) {
    final CDARichText richText = (CDARichText) node;

    final View result = context.getInflater().inflate(R.layout.rich_text_layout, null);
    final TextView content = result.findViewById(R.id.rich_content);
    final SpannableStringBuilder textContent = new SpannableStringBuilder(richText.getText());

    for (final CDARichMark mark : richText.getMarks()) {
      if (mark instanceof CDARichMarkUnderline) {
        textContent.setSpan(new UnderlineSpan(), 0, textContent.length(), SPAN_EXCLUSIVE_EXCLUSIVE);
      }
      if (mark instanceof CDARichMarkBold) {
        textContent.setSpan(new StyleSpan(Typeface.BOLD), 0, textContent.length(), SPAN_EXCLUSIVE_EXCLUSIVE);
      }
      if (mark instanceof CDARichMark.CDARichMarkSuperscript) {
        textContent.setSpan(new SuperscriptSpan(), 0, textContent.length(), SPAN_EXCLUSIVE_EXCLUSIVE);
      }
      if (mark instanceof CDARichMark.CDARichMarkSubscript) {
        textContent.setSpan(new SubscriptSpan(), 0, textContent.length(), SPAN_EXCLUSIVE_EXCLUSIVE);
      }
      if (mark instanceof CDARichMarkItalic) {
        textContent.setSpan(new StyleSpan(Typeface.ITALIC), 0, textContent.length(), SPAN_EXCLUSIVE_EXCLUSIVE);
      }
      if (mark instanceof CDARichMarkCode) {
        textContent.setSpan(new TextAppearanceSpan("monospace", 0, 0, null, null), 0, textContent.length(), SPAN_EXCLUSIVE_EXCLUSIVE);
      }
      if (mark instanceof CDARichMarkCustom) {
        textContent.setSpan(new BackgroundColorSpan(0x80ffff00), 0, textContent.length(), SPAN_EXCLUSIVE_EXCLUSIVE);
      }
    }

    if (context.getPath() != null) {
      CDARichHeading heading = null;
      for (final CDARichNode pathNode : context.getPath()) {
        if (pathNode instanceof CDARichHeading) {
          heading = (CDARichHeading) pathNode;
        }
      }

      if (heading != null) {
        applyHeadingStyle(content, heading.getLevel());
      }
    }
    content.setMovementMethod(LinkMovementMethod.getInstance());
    content.setText(textContent);
    return result;
  }

  private float getHeadingTextSize(int level) {
    switch(level) {
      case 1:
        return 32f;
      case 2:
        return 28f;
      case 3:
        return 24f;
      case 4:
        return 20f;
      case 5:
        return 18f;
      case 6:
        return 16f;
    }
    return 16f;
  }

  private void applyHeadingStyle(TextView view, int level) {
    view.setTextSize(TypedValue.COMPLEX_UNIT_SP, getHeadingTextSize(level));
    // Use a heavier font weight for more prominent headings
    view.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
    // Add more vertical spacing for headings
    view.setPadding(
        view.getPaddingLeft(),
        (int) (16 * view.getResources().getDisplayMetrics().density),
        view.getPaddingRight(),
        (int) (8 * view.getResources().getDisplayMetrics().density)
    );
  }
}
