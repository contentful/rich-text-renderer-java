import com.contentful.java.cda.rich.CDARichDocument;
import com.contentful.java.cda.rich.CDARichNode;
import com.contentful.java.cda.rich.CDARichParagraph;
import com.contentful.java.cda.rich.CDARichText;
import com.contentful.rich.core.simple.RemoveEmpties;
import com.contentful.rich.core.simple.RemoveToDeepNesting;
import com.contentful.rich.core.simple.Simplifier;

import org.junit.Test;

import static com.google.common.truth.Truth.assertThat;
import static java.util.Collections.emptyList;

public class SimplifierTest {
  @Test
  public void removeDirectEmptyNodes() {
    final CDARichDocument document = new CDARichDocument();
    document.getContent().add(new CDARichParagraph());
    document.getContent().add(new CDARichText("only one there", emptyList()));

    final CDARichNode simplified = new Simplifier().simplify(document);

    assertThat(simplified).isInstanceOf(CDARichDocument.class);

    final CDARichDocument simplifiedDocument = (CDARichDocument) simplified;
    assertThat((simplifiedDocument.getContent())).hasSize(1);
    assertThat(simplifiedDocument.getContent().get(0)).isInstanceOf(CDARichText.class);
    assertThat(((CDARichText) simplifiedDocument.getContent().get(0)).getText()).isEqualTo("only one there");
  }

  @Test
  public void removeEmptyParentsNodes() {
    final CDARichDocument document = new CDARichDocument();

    final CDARichParagraph emptyParent = new CDARichParagraph();
    emptyParent.getContent().add(new CDARichParagraph());

    document.getContent().add(emptyParent);
    document.getContent().add(new CDARichText("only one there", emptyList()));

    final CDARichDocument simplified = (CDARichDocument) new Simplifier().simplify(document);

    assertThat(simplified).isInstanceOf(CDARichDocument.class);

    assertThat((simplified.getContent())).hasSize(1);
    assertThat(simplified.getContent().get(0)).isInstanceOf(CDARichText.class);
    assertThat(((CDARichText) simplified.getContent().get(0)).getText()).isEqualTo("only one there");
  }

  @Test
  public void oneElementBlockInABlockRemovedInnerBlock() {
    final CDARichDocument document = new CDARichDocument();

    final CDARichParagraph first = new CDARichParagraph();

    final CDARichParagraph second = new CDARichParagraph();
    second.getContent().add(new CDARichText("only one there", emptyList()));

    first.getContent().add(second);

    document.getContent().add(first);

    final CDARichDocument simplified = (CDARichDocument) new Simplifier().simplify(document);

    assertThat((simplified.getContent())).hasSize(1);
    assertThat(simplified.getContent().get(0)).isInstanceOf(CDARichParagraph.class);
    final CDARichParagraph paragraph = (CDARichParagraph) simplified.getContent().get(0);
    assertThat(paragraph.getContent().get(0)).isInstanceOf(CDARichText.class);
    assertThat(((CDARichText) paragraph.getContent().get(0)).getText()).isEqualTo("only one there");
  }

  @Test
  public void nullifySingleEmptyTextNode() {
    final CDARichNode simplified = new Simplifier().simplify(new CDARichText(null, emptyList()));

    assertThat(simplified).isNull();
  }

  @Test
  public void simplifyByGraphDepth() {
    final CDARichDocument document = new CDARichDocument();

    final CDARichParagraph first = new CDARichParagraph();
    final CDARichParagraph second = new CDARichParagraph();
    final CDARichParagraph third = new CDARichParagraph();
    third.getContent().add(new CDARichText("this should be removed", emptyList()));

    second.getContent().add(third);
    first.getContent().add(second);

    document.getContent().add(first);
    document.getContent().add(new CDARichText("only one there", emptyList()));

    final CDARichDocument simplified = (CDARichDocument) new Simplifier(
        new RemoveToDeepNesting(),
        new RemoveEmpties()
    ).simplify(document);

    assertThat((simplified.getContent())).hasSize(1);
    assertThat(simplified.getContent().get(0)).isInstanceOf(CDARichText.class);
    assertThat(((CDARichText) simplified.getContent().get(0)).getText()).isEqualTo("only one there");
  }
}
