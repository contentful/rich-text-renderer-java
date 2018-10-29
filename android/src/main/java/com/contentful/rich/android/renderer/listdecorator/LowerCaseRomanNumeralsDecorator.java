package com.contentful.rich.android.renderer.listdecorator;

import javax.annotation.Nonnull;

public class LowerCaseRomanNumeralsDecorator extends UpperCaseRomanNumeralsDecorator {
  @Nonnull @Override public CharSequence getSymbol() {
    return "i";
  }

  @Nonnull @Override
  public CharSequence decorate(int position) {
    return super.decorate(position).toString().toLowerCase();
  }
}
