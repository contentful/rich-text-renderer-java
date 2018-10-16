package com.contentful.rich.android.renderer.chars.listdecorator;

import android.text.SpannableString;
import android.text.Spanned;

import java.util.TreeMap;

import javax.annotation.Nonnull;

public class UpperCaseRomanNumeralsDecorator extends Decorator {
  private final TreeMap<Integer, String> map = new TreeMap<>();

  public UpperCaseRomanNumeralsDecorator() {
    map.put(1000, "M");
    map.put(900, "CM");
    map.put(500, "D");
    map.put(400, "CD");
    map.put(100, "C");
    map.put(90, "XC");
    map.put(50, "L");
    map.put(40, "XL");
    map.put(10, "X");
    map.put(9, "IX");
    map.put(5, "V");
    map.put(4, "IV");
    map.put(3, "III");
    map.put(2, "II");
    map.put(1, "I");
//    map.put(1000, "Ⅿ");
//    map.put(900, "ⅭⅯ");
//    map.put(500, "Ⅾ");
//    map.put(400, "ⅭⅮ");
//    map.put(100, "Ⅽ");
//    map.put(90, "ⅩⅭ");
//    map.put(50, "Ⅼ");
//    map.put(40, "ⅩⅬ");
//    map.put(10, "Ⅹ");
//    map.put(9, "Ⅸ");
//    map.put(5, "Ⅴ");
//    map.put(4, "Ⅳ");
//    map.put(3, "Ⅲ");
//    map.put(2, "Ⅱ");
//    map.put(1, "Ⅰ");
  }

  @Nonnull @Override public CharSequence getSymbol() {
    return "I";
  }

  @Nonnull @Override
  public CharSequence decorate(int index) {
    final SpannableString spannable = new SpannableString(getRomanDecoration(index) + ". ");
    spannable.setSpan(this, 0, spannable.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
    return spannable;
  }

  public CharSequence getRomanDecoration(int index) {
    try {
      int l = map.floorKey(index);
      if (index == l) {
        return map.get(index);
      }
      return map.get(l) + getRomanDecoration(index - l);
    } catch (NullPointerException e) {
      return " ";
    }
  }
}
