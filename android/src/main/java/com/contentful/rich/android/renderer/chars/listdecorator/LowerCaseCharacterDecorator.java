package com.contentful.rich.android.renderer.chars.listdecorator;

import javax.annotation.Nonnull;

public class LowerCaseCharacterDecorator extends UpperCaseCharacterDecorator {
  @Nonnull @Override public CharSequence getSymbol() {
    return "a";
  }

  public @Nonnull CharSequence decorate(int index) {
    return super.decorate(index).toString().toLowerCase();
  }

}
