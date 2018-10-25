package com.contentful.rich.android.renderer.listdecorator;

import android.text.SpannableString;
import android.text.Spanned;

import javax.annotation.Nonnull;

public class UpperCaseCharacterDecorator extends Decorator {
  @Nonnull @Override public CharSequence getSymbol() {
    return "A";
  }

  public @Nonnull CharSequence decorate(int index) {
    final SpannableString spannable = new SpannableString(getColumnDecoration(index) + ". ");
    spannable.setSpan(this, 0, spannable.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
    return spannable;
  }

  private CharSequence getColumnDecoration(int index) {
    if (index < 26) {
      return Character.toString((char) ('A' + index));
    } else {
      return getColumnDecoration((index / 26) - 1).toString() + getColumnDecoration(index % 26);
    }
  }
}
