package chars;

import com.contentful.rich.android.renderer.chars.listdecorator.LowerCaseCharacterDecorator;
import com.contentful.rich.android.renderer.chars.listdecorator.LowerCaseRomanNumeralsDecorator;
import com.contentful.rich.android.renderer.chars.listdecorator.UpperCaseCharacterDecorator;
import com.contentful.rich.android.renderer.chars.listdecorator.UpperCaseRomanNumeralsDecorator;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import static com.google.common.truth.Truth.assertThat;

@RunWith(RobolectricTestRunner.class)
public class ListDecoratorTest {
  @Test
  public void testUppercaseRomanNumbers() {
    final UpperCaseRomanNumeralsDecorator roman = new UpperCaseRomanNumeralsDecorator();
    assertThat(roman.decorate(0).toString()).isEqualTo(" . ");
    assertThat(roman.decorate(1).toString()).isEqualTo("I. ");
    assertThat(roman.decorate(2).toString()).isEqualTo("II. ");
    assertThat(roman.decorate(3).toString()).isEqualTo("III. ");
    assertThat(roman.decorate(4).toString()).isEqualTo("IV. ");
    assertThat(roman.decorate(5).toString()).isEqualTo("V. ");
    assertThat(roman.decorate(6).toString()).isEqualTo("VI. ");
    assertThat(roman.decorate(7).toString()).isEqualTo("VII. ");
    assertThat(roman.decorate(8).toString()).isEqualTo("VIII. ");
    assertThat(roman.decorate(9).toString()).isEqualTo("IX. ");
    assertThat(roman.decorate(10).toString()).isEqualTo("X. ");
    assertThat(roman.decorate(11).toString()).isEqualTo("XI. ");
    assertThat(roman.decorate(12).toString()).isEqualTo("XII. ");
    assertThat(roman.decorate(13).toString()).isEqualTo("XIII. ");
    assertThat(roman.decorate(14).toString()).isEqualTo("XIV. ");
    assertThat(roman.decorate(15).toString()).isEqualTo("XV. ");
    assertThat(roman.decorate(16).toString()).isEqualTo("XVI. ");
    assertThat(roman.decorate(17).toString()).isEqualTo("XVII. ");
    assertThat(roman.decorate(18).toString()).isEqualTo("XVIII. ");
    assertThat(roman.decorate(19).toString()).isEqualTo("XIX. ");
    assertThat(roman.decorate(20).toString()).isEqualTo("XX. ");
    assertThat(roman.decorate(1985).toString()).isEqualTo("MCMLXXXV. ");
    assertThat(roman.decorate(2018).toString()).isEqualTo("MMXVIII. ");
  }

  @Test
  public void testLowerCaseRomanNumbers() {
    final LowerCaseRomanNumeralsDecorator roman = new LowerCaseRomanNumeralsDecorator();
    assertThat(roman.decorate(1).toString()).isEqualTo("i. ");
    assertThat(roman.decorate(2018).toString()).isEqualTo("mmxviii. ");
  }

  @Test
  public void testCaptialAlphaNumbers() {
    final UpperCaseCharacterDecorator alpha = new UpperCaseCharacterDecorator();
    assertThat(alpha.decorate(0).toString()).isEqualTo("A. ");
    assertThat(alpha.decorate(1).toString()).isEqualTo("B. ");
    assertThat(alpha.decorate(2).toString()).isEqualTo("C. ");
    assertThat(alpha.decorate(3).toString()).isEqualTo("D. ");
    assertThat(alpha.decorate(23).toString()).isEqualTo("X. ");
    assertThat(alpha.decorate(24).toString()).isEqualTo("Y. ");
    assertThat(alpha.decorate(25).toString()).isEqualTo("Z. ");
    assertThat(alpha.decorate(26).toString()).isEqualTo("AA. ");
    assertThat(alpha.decorate(27).toString()).isEqualTo("AB. ");
    assertThat(alpha.decorate(28).toString()).isEqualTo("AC. ");
    assertThat(alpha.decorate(49).toString()).isEqualTo("AX. ");
    assertThat(alpha.decorate(50).toString()).isEqualTo("AY. ");
    assertThat(alpha.decorate(51).toString()).isEqualTo("AZ. ");
    assertThat(alpha.decorate(52).toString()).isEqualTo("BA. ");
    assertThat(alpha.decorate(53).toString()).isEqualTo("BB. ");
    assertThat(alpha.decorate(400).toString()).isEqualTo("OK. ");
    assertThat(alpha.decorate(700).toString()).isEqualTo("ZY. ");
    assertThat(alpha.decorate(701).toString()).isEqualTo("ZZ. ");
    assertThat(alpha.decorate(702).toString()).isEqualTo("AAA. ");
    assertThat(alpha.decorate(703).toString()).isEqualTo("AAB. ");
    assertThat(alpha.decorate(5970680).toString()).isEqualTo("MARIO. ");
  }

  @Test
  public void testLowercaseAlphaNumbers() {
    final UpperCaseCharacterDecorator alpha = new LowerCaseCharacterDecorator();
    assertThat(alpha.decorate(0).toString()).isEqualTo("a. ");
    assertThat(alpha.decorate(1).toString()).isEqualTo("b. ");
    assertThat(alpha.decorate(2).toString()).isEqualTo("c. ");
    assertThat(alpha.decorate(3).toString()).isEqualTo("d. ");
    assertThat(alpha.decorate(23).toString()).isEqualTo("x. ");
    assertThat(alpha.decorate(24).toString()).isEqualTo("y. ");
    assertThat(alpha.decorate(25).toString()).isEqualTo("z. ");
    assertThat(alpha.decorate(26).toString()).isEqualTo("aa. ");
    assertThat(alpha.decorate(27).toString()).isEqualTo("ab. ");
    assertThat(alpha.decorate(28).toString()).isEqualTo("ac. ");
    assertThat(alpha.decorate(49).toString()).isEqualTo("ax. ");
    assertThat(alpha.decorate(50).toString()).isEqualTo("ay. ");
    assertThat(alpha.decorate(51).toString()).isEqualTo("az. ");
    assertThat(alpha.decorate(52).toString()).isEqualTo("ba. ");
    assertThat(alpha.decorate(53).toString()).isEqualTo("bb. ");
    assertThat(alpha.decorate(400).toString()).isEqualTo("ok. ");
    assertThat(alpha.decorate(700).toString()).isEqualTo("zy. ");
    assertThat(alpha.decorate(701).toString()).isEqualTo("zz. ");
    assertThat(alpha.decorate(702).toString()).isEqualTo("aaa. ");
    assertThat(alpha.decorate(703).toString()).isEqualTo("aab. ");
    assertThat(alpha.decorate(5970680).toString()).isEqualTo("mario. ");
  }
}
