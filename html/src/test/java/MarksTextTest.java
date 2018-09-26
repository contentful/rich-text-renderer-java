import com.contentful.java.cda.structured.CDAStructuredMark;
import com.contentful.java.cda.structured.CDAStructuredText;
import com.contentful.structured.html.HtmlProcessor;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.truth.Truth.assertThat;

public class MarksTextTest {

  @Test
  public void noMarksTest() {
    final HtmlProcessor processor = new HtmlProcessor();

    final List<CDAStructuredMark> markers = new ArrayList<>();

    final String result = processor.render(new CDAStructuredText(markers, "text"));

    assertThat(result).isEqualTo("text\n");
  }

  @Test
  public void boldTextTest() {
    final HtmlProcessor processor = new HtmlProcessor();

    final List<CDAStructuredMark> markers = new ArrayList<>();
    markers.add(new CDAStructuredMark.CDAStructuredMarkBold());

    final String result = processor.render(new CDAStructuredText(markers, "BoldText"));

    assertThat(result).isEqualTo("<b>BoldText</b>\n");
  }

  @Test
  public void underlineTextTest() {
    final HtmlProcessor processor = new HtmlProcessor();

    final List<CDAStructuredMark> markers = new ArrayList<>();
    markers.add(new CDAStructuredMark.CDAStructuredMarkUnderline());

    final String result = processor.render(new CDAStructuredText(markers, "Underlined"));

    assertThat(result).isEqualTo("<u>Underlined</u>\n");
  }

  @Test
  public void testItalicHtml() {
    final HtmlProcessor processor = new HtmlProcessor();

    final List<CDAStructuredMark> markers = new ArrayList<>();
    markers.add(new CDAStructuredMark.CDAStructuredMarkItalic());

    final String result = processor.render(new CDAStructuredText(markers, "Italic"));

    assertThat(result).isEqualTo("<i>Italic</i>\n");
  }

  @Test
  public void codeTextTest() {
    final HtmlProcessor processor = new HtmlProcessor();

    final List<CDAStructuredMark> markers = new ArrayList<>();
    markers.add(new CDAStructuredMark.CDAStructuredMarkCode());

    final String result = processor.render(new CDAStructuredText(markers, "final String code;"));

    assertThat(result).isEqualTo("<tt>final String code;</tt>\n");
  }

  @Test
  public void customTextTest() {
    final HtmlProcessor processor = new HtmlProcessor();

    final List<CDAStructuredMark> markers = new ArrayList<>();
    markers.add(new CDAStructuredMark.CDAStructuredMarkCustom("🧀"));

    final String result = processor.render(new CDAStructuredText(markers, "🐭"));

    assertThat(result).isEqualTo("<🧀>🐭</🧀>\n");
  }

  @Test
  public void allTextTest() {
    final HtmlProcessor processor = new HtmlProcessor();

    final List<CDAStructuredMark> markers = new ArrayList<>();
    markers.add(new CDAStructuredMark.CDAStructuredMarkCustom("custom"));
    markers.add(new CDAStructuredMark.CDAStructuredMarkItalic());
    markers.add(new CDAStructuredMark.CDAStructuredMarkBold());
    markers.add(new CDAStructuredMark.CDAStructuredMarkCode());
    markers.add(new CDAStructuredMark.CDAStructuredMarkUnderline());
    markers.add(new CDAStructuredMark.CDAStructuredMarkCustom("span"));

    final String result = processor.render(new CDAStructuredText(markers, "All in all"));

    assertThat(result).isEqualTo("<span><u><tt><b><i><custom>All in all</custom></i></b></tt></u></span>\n");
  }
}
