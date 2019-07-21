import com.contentful.java.cda.rich.CDARichMark;
import com.contentful.java.cda.rich.CDARichText;
import com.contentful.rich.html.HtmlContext;
import com.contentful.rich.html.HtmlProcessor;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.truth.Truth.assertThat;

public class MarksTextTest {

  @Test
  public void noMarksTest() {
    final HtmlProcessor processor = new HtmlProcessor();
    final HtmlContext context = new HtmlContext();

    final String result = processor.process(context, new CDARichText("text", new ArrayList<>()));

    assertThat(result).isEqualTo("text");
  }

  @Test
  public void boldTextTest() {
    final HtmlProcessor processor = new HtmlProcessor();
    final HtmlContext context = new HtmlContext();

    final List<CDARichMark> markers = new ArrayList<>();
    markers.add(new CDARichMark.CDARichMarkBold());

    final String result = processor.process(context, new CDARichText("BoldText", markers));

    assertThat(result).isEqualTo("<b>BoldText</b>");
  }

  @Test
  public void underlineTextTest() {
    final HtmlProcessor processor = new HtmlProcessor();
    final HtmlContext context = new HtmlContext();

    final List<CDARichMark> markers = new ArrayList<>();
    markers.add(new CDARichMark.CDARichMarkUnderline());

    final String result = processor.process(context, new CDARichText("Underlined", markers));

    assertThat(result).isEqualTo("<u>Underlined</u>");
  }

  @Test
  public void testItalicHtml() {
    final HtmlProcessor processor = new HtmlProcessor();
    final HtmlContext context = new HtmlContext();

    final List<CDARichMark> markers = new ArrayList<>();
    markers.add(new CDARichMark.CDARichMarkItalic());

    final String result = processor.process(context, new CDARichText("Italic", markers));

    assertThat(result).isEqualTo("<i>Italic</i>");
  }

  @Test
  public void codeTextTest() {
    final HtmlProcessor processor = new HtmlProcessor();
    final HtmlContext context = new HtmlContext();

    final List<CDARichMark> markers = new ArrayList<>();
    markers.add(new CDARichMark.CDARichMarkCode());

    final String result = processor.process(context, new CDARichText("final String code;", markers));

    assertThat(result).isEqualTo("<code>final String code;</code>");
  }

  @Test
  public void customTextTest() {
    final HtmlProcessor processor = new HtmlProcessor();
    final HtmlContext context = new HtmlContext();

    final List<CDARichMark> markers = new ArrayList<>();
    markers.add(new CDARichMark.CDARichMarkCustom("🧀"));

    final String result = processor.process(context, new CDARichText("🐭", markers));

    assertThat(result).isEqualTo("<🧀>🐭</🧀>");
  }

  @Test
  public void allTextTest() {
    final HtmlProcessor processor = new HtmlProcessor();
    final HtmlContext context = new HtmlContext();

    final List<CDARichMark> markers = new ArrayList<>();
    markers.add(new CDARichMark.CDARichMarkCustom("custom"));
    markers.add(new CDARichMark.CDARichMarkItalic());
    markers.add(new CDARichMark.CDARichMarkBold());
    markers.add(new CDARichMark.CDARichMarkCode());
    markers.add(new CDARichMark.CDARichMarkUnderline());
    markers.add(new CDARichMark.CDARichMarkCustom("span"));

    final String result = processor.process(context, new CDARichText("All in all", markers));

    assertThat(result).isEqualTo(
      "<span><u><code><b><i><custom>All in all</custom></i></b></code></u></span>"
    );
  }
}
