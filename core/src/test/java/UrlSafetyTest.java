import com.contentful.rich.core.util.UrlSafety;

import org.junit.Test;

import static com.google.common.truth.Truth.assertThat;

public class UrlSafetyTest {

  @Test
  public void allowsWebMailPhoneAndSms() {
    assertThat(UrlSafety.isSafeUrl("https://contentful.com")).isTrue();
    assertThat(UrlSafety.isSafeUrl("HTTP://CONTENTFUL.COM")).isTrue();
    assertThat(UrlSafety.isSafeUrl("mailto:someone@example.com")).isTrue();
    assertThat(UrlSafety.isSafeUrl("tel:+441234567890")).isTrue();
    assertThat(UrlSafety.isSafeUrl("sms:+441234567890")).isTrue();
  }

  @Test
  public void blocksScriptAndLocalSchemes() {
    assertThat(UrlSafety.isSafeUrl("javascript:alert(1)")).isFalse();
    assertThat(UrlSafety.isSafeUrl("JaVaScRiPt:alert(1)")).isFalse();
    assertThat(UrlSafety.isSafeUrl("intent://scan/#Intent;scheme=zxing;end")).isFalse();
    assertThat(UrlSafety.isSafeUrl("file:///data/data/app/secret")).isFalse();
    assertThat(UrlSafety.isSafeUrl("data:text/html,<script>alert(1)</script>")).isFalse();
    assertThat(UrlSafety.isSafeUrl(null)).isFalse();
  }

  @Test
  public void tabsAndNewlinesCannotHideAScheme() {
    // Browsers remove these characters, turning the value into "javascript:".
    assertThat(UrlSafety.resolveSafeUrl("java\tscript:alert(1)")).isNull();
    assertThat(UrlSafety.resolveSafeUrl("java\nscript:alert(1)")).isNull();
    assertThat(UrlSafety.safeHref("  java\r\nscript:alert(1)")).isNull();
  }

  @Test
  public void resolveSafeUrlPrefixesBareHostsOnly() {
    assertThat(UrlSafety.resolveSafeUrl(" contentful.com ")).isEqualTo("http://contentful.com");
    assertThat(UrlSafety.resolveSafeUrl("https://contentful.com")).isEqualTo("https://contentful.com");
    assertThat(UrlSafety.resolveSafeUrl("javascript:alert(1)")).isNull();
    assertThat(UrlSafety.resolveSafeUrl("   ")).isNull();
  }

  @Test
  public void safeHrefKeepsRelativeLinks() {
    assertThat(UrlSafety.safeHref("/about")).isEqualTo("/about");
    assertThat(UrlSafety.safeHref("#top")).isEqualTo("#top");
    assertThat(UrlSafety.safeHref("page.html")).isEqualTo("page.html");
    assertThat(UrlSafety.safeHref("https://contentful.com")).isEqualTo("https://contentful.com");
    assertThat(UrlSafety.safeHref("javascript:alert(1)")).isNull();
  }
}
