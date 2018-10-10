package com.contentful.rich.android.renderer.charsequence;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Parcel;
import android.text.Layout;
import android.text.ParcelableSpan;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.LeadingMarginSpan;

import com.contentful.java.cda.rich.CDARichNode;
import com.contentful.java.cda.rich.CDARichQuote;
import com.contentful.rich.android.RichTextContext;
import com.contentful.rich.core.Processor;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import androidx.annotation.ColorInt;
import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.annotation.Px;

public class QuoteRenderer extends BlockRenderer {

  public static class CustomQuoteSpan implements LeadingMarginSpan, ParcelableSpan {
    /**
     * Default stripe width in pixels.
     */
    public static final int STANDARD_STRIPE_WIDTH_PX = 2;

    /**
     * Default gap width in pixels.
     */
    public static final int STANDARD_GAP_WIDTH_PX = 2;

    /**
     * Default color for the quote stripe.
     */
    @ColorInt
    public static final int STANDARD_COLOR = 0xff0000ff;

    @ColorInt
    private final int mColor;
    @Px
    private final int mStripeWidth;
    @Px
    private final int mGapWidth;

    /**
     * Creates a {@link android.text.style.QuoteSpan} with the default values.
     */
    public CustomQuoteSpan() {
      this(STANDARD_COLOR, STANDARD_STRIPE_WIDTH_PX, STANDARD_GAP_WIDTH_PX);
    }

    /**
     * Creates a {@link android.text.style.QuoteSpan} based on a color.
     *
     * @param color the color of the quote stripe.
     */
    public CustomQuoteSpan(@ColorInt int color) {
      this(color, STANDARD_STRIPE_WIDTH_PX, STANDARD_GAP_WIDTH_PX);
    }

    /**
     * Creates a {@link android.text.style.QuoteSpan} based on a color, a stripe width and the width of the gap
     * between the stripe and the text.
     *
     * @param color       the color of the quote stripe.
     * @param stripeWidth the width of the stripe.
     * @param gapWidth    the width of the gap between the stripe and the text.
     */
    public CustomQuoteSpan(@ColorInt int color, @IntRange(from = 0) int stripeWidth,
                           @IntRange(from = 0) int gapWidth) {
      mColor = color;
      mStripeWidth = stripeWidth;
      mGapWidth = gapWidth;
    }

    /**
     * Create a {@link android.text.style.QuoteSpan} from a parcel.
     */
    public CustomQuoteSpan(@NonNull Parcel src) {
      mColor = src.readInt();
      mStripeWidth = src.readInt();
      mGapWidth = src.readInt();
    }

    @Override
    public int describeContents() {
      return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
      writeToParcelInternal(dest, flags);
    }

    public void writeToParcelInternal(Parcel dest, int flags) {
      dest.writeInt(mColor);
      dest.writeInt(mStripeWidth);
      dest.writeInt(mGapWidth);
    }

    /**
     * Get the color of the quote stripe.
     *
     * @return the color of the quote stripe.
     */
    @ColorInt
    public int getColor() {
      return mColor;
    }

    /**
     * Get the width of the quote stripe.
     *
     * @return the width of the quote stripe.
     */
    public int getStripeWidth() {
      return mStripeWidth;
    }

    /**
     * Get the width of the gap between the stripe and the text.
     *
     * @return the width of the gap between the stripe and the text.
     */
    public int getGapWidth() {
      return mGapWidth;
    }

    @Override
    public int getLeadingMargin(boolean first) {
      return mStripeWidth + mGapWidth;
    }

    @Override
    public void drawLeadingMargin(@NonNull Canvas c, @NonNull Paint p, int x, int dir,
                                  int top, int baseline, int bottom,
                                  @NonNull CharSequence text, int start, int end,
                                  boolean first, @NonNull Layout layout) {
      Paint.Style style = p.getStyle();
      int color = p.getColor();

      p.setStyle(Paint.Style.FILL);
      p.setColor(mColor);

      c.drawRect(x, top, x + dir * mStripeWidth, bottom, p);

      p.setStyle(style);
      p.setColor(color);
    }

    @Override public int getSpanTypeId() {
      return 1000;
    }
  }

  public QuoteRenderer(@Nonnull Processor<RichTextContext, CharSequence> processor) {
    super(processor);
  }

  @Override public boolean check(@Nullable RichTextContext context, @Nonnull CDARichNode node) {
    return node instanceof CDARichQuote;
  }

  @NonNull
  @Override protected SpannableStringBuilder decorate(
      @Nonnull RichTextContext context,
      @Nonnull CDARichNode node,
      @Nonnull SpannableStringBuilder renderedChildren) {

    final CustomQuoteSpan span = new CustomQuoteSpan(0x80808080, 30, 20);
    renderedChildren.setSpan(span, 0, renderedChildren.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
    return renderedChildren;
  }

}
