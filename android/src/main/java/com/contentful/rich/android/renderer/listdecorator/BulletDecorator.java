package com.contentful.rich.android.renderer.listdecorator;

import android.text.SpannableString;
import android.text.Spanned;

import javax.annotation.Nonnull;

public class BulletDecorator extends Decorator{
  @Nonnull @Override public CharSequence getSymbol() {
    return "*";
  }

  public @Nonnull CharSequence decorate(int index) {
    final SpannableString spannable = new SpannableString("• ");
    spannable.setSpan(this, 0, spannable.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
    return spannable;
  }
}
