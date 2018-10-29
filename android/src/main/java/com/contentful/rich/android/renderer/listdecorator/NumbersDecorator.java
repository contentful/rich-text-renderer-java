package com.contentful.rich.android.renderer.listdecorator;

import android.text.SpannableString;
import android.text.Spanned;

import javax.annotation.Nonnull;

public class NumbersDecorator extends Decorator {
  @Nonnull @Override public CharSequence getSymbol() {
    return "1";
  }

  public @Nonnull CharSequence decorate(int position) {
    final SpannableString spannable = new SpannableString(position + ". ");
    spannable.setSpan(this, 0, spannable.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
    return spannable;
  }
}
