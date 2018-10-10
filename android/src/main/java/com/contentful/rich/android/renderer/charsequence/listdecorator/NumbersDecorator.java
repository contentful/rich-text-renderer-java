package com.contentful.rich.android.renderer.charsequence.listdecorator;

import android.text.SpannableString;
import android.text.Spanned;

import javax.annotation.Nonnull;

public class NumbersDecorator extends Decorator {
  @Nonnull @Override public CharSequence getSymbol() {
    return "1";
  }

  public @Nonnull CharSequence decorate(int index) {
    final SpannableString spannable = new SpannableString((index + 1) + ". ");
    spannable.setSpan(this, 0, spannable.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
    return spannable;
  }
}
