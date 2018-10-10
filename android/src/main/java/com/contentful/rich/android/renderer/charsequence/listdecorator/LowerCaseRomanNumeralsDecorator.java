package com.contentful.rich.android.renderer.charsequence.listdecorator;

import javax.annotation.Nonnull;

public class LowerCaseRomanNumeralsDecorator extends UpperCaseRomanNumeralsDecorator {
  @Nonnull @Override public CharSequence getSymbol() {
    return "i";
  }

  @Nonnull @Override
  public CharSequence decorate(int index) {
    return super.decorate(index).toString().toLowerCase();
  }
}
