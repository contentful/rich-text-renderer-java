package com.contentful.rich.android.renderer.chars.span;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Parcel;
import android.text.Layout;
import android.text.ParcelableSpan;
import android.text.style.LeadingMarginSpan;

import androidx.annotation.ColorInt;
import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.annotation.Px;

/**
 * This is a custom span to decorate the children with a margin.
 */
public class QuoteSpan implements LeadingMarginSpan, ParcelableSpan {
  @ColorInt
  private final int mColor;
  @Px
  private final int mStripeWidth;
  @Px
  private final int mGapWidth;

  /**
   * Creates a {@link android.text.style.QuoteSpan} based on a color, a stripe width and the width of the gap
   * between the stripe and the text.
   *
   * @param color       the color of the quote stripe.
   * @param stripeWidth the width of the stripe.
   * @param gapWidth    the width of the gap between the stripe and the text.
   */
  public QuoteSpan(@ColorInt int color, @IntRange(from = 0) int stripeWidth,
                   @IntRange(from = 0) int gapWidth) {
    mColor = color;
    mStripeWidth = stripeWidth;
    mGapWidth = gapWidth;
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

  public int getSpanTypeIdInternal() {
    return getSpanTypeId();
  }

}
