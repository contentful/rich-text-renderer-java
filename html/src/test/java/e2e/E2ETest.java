package e2e;

import com.contentful.java.cda.CDAAsset;
import com.contentful.java.cda.CDAClient;
import com.contentful.java.cda.CDAEntry;
import com.contentful.java.cda.rich.CDARichDocument;
import com.contentful.java.cda.rich.CDARichEmbeddedBlock;
import com.contentful.java.cda.rich.CDARichEmbeddedInline;
import com.contentful.java.cda.rich.CDARichHyperLink;
import com.contentful.java.cda.rich.CDARichNode;
import com.contentful.java.cda.rich.CDARichParagraph;
import com.contentful.rich.core.simple.RemoveEmpties;
import com.contentful.rich.core.simple.RemoveToDeepNesting;
import com.contentful.rich.core.simple.Simplifier;
import com.contentful.rich.html.HtmlContext;
import com.contentful.rich.html.HtmlProcessor;

import org.junit.Before;
import org.junit.Test;

import java.util.Map;

import static com.contentful.java.cda.QueryOperation.Matches;
import static com.contentful.java.cda.image.ImageOption.Format.png;
import static com.contentful.java.cda.image.ImageOption.formatOf;
import static com.contentful.java.cda.image.ImageOption.heightOf;
import static com.contentful.java.cda.image.ImageOption.https;
import static com.contentful.java.cda.image.ImageOption.widthOf;
import static com.google.common.truth.Truth.assertThat;
import static e2e.E2EResources.PRERENDERED_RESULTS;
import static e2e.E2EResources.SIMPLIFIED_RESULTS;
import static e2e.E2EResources.SIMPLIFIED_WITH_DEPTH_LIMITING_RESULT;
import static java.lang.String.format;
import static java.util.Locale.getDefault;

public class E2ETest {

  private CDAClient client;
    private String SPACE_ID = "i1ppeoxgdpvt";
    private String TOKEN = "QuYNYLv6rVnCKOT4_-d3552xD4YIPFcKTWRb2Y227Ic";
    private String ENVIRONMENT = "master";
  @Before
  public void setup() {
    client = CDAClient.builder()
        .setSpace(SPACE_ID)
        .setToken(TOKEN)
        .setEnvironment(ENVIRONMENT)
        .build();
  }

  @Test
  public void missingEmbeddedAndTypedHyperlinksAreRenderedAsComments() {
    final CDARichDocument document = fetchAndMergeContentfulContent();
    final HtmlContext context = new HtmlContext();
    final HtmlProcessor processor = new HtmlProcessor();

    final String result = processor.process(context, document);

    assertThat(result).contains("<!-- no processor accepts 'CDARichHyperLink<CDAAsset>'");
    assertThat(result).contains("<!-- no processor accepts 'CDARichHyperLink<CDAEntry>'");
    assertThat(result).contains("<!-- no processor accepts 'CDARichEmbeddedBlock<CDAAsset>'");
    assertThat(result).contains("<!-- no processor accepts 'CDARichEmbeddedBlock<CDAEntry>'");
    assertThat(result).contains("<!-- no processor accepts 'CDARichEmbeddedInline<CDAEntry>'");
  }

  @Test
  public void producesCannedHtml() {
    final CDARichDocument document = fetchAndMergeContentfulContent();
    final HtmlContext context = new HtmlContext();
    final HtmlProcessor processor = new HtmlProcessor();
    addCustomRenderer(processor);

    final String result = processor.process(context, document);

    assertThat(result).doesNotContain("<!-- no processor accepts");
    assertThat(result).isEqualTo(PRERENDERED_RESULTS);
  }

  @Test
  public void producesSimplifiedHtml() {
    CDARichNode document = fetchAndMergeContentfulContent();
    document = new Simplifier().simplify(document);

    final HtmlContext context = new HtmlContext();
    final HtmlProcessor processor = new HtmlProcessor();
    addCustomRenderer(processor);

    final String result = processor.process(context, document);

    assertThat(result).doesNotContain("<!-- no processor accepts");
    assertThat(result).isEqualTo(SIMPLIFIED_RESULTS);
  }

  @Test
  public void simplifiedHtmlCanBeLimitedByNestingLevel() {
    CDARichNode document = fetchAndMergeContentfulContent();
    document = new Simplifier(
        new RemoveToDeepNesting(10),
        new RemoveEmpties()
    ).simplify(document);

    final HtmlContext context = new HtmlContext();
    final HtmlProcessor processor = new HtmlProcessor();
    addCustomRenderer(processor);

    final String result = processor.process(context, document);

    assertThat(result).doesNotContain("<!-- no processor accepts");
    assertThat(result).isEqualTo(SIMPLIFIED_WITH_DEPTH_LIMITING_RESULT);
  }

  private CDARichDocument fetchAndMergeContentfulContent() {
    return client
        .fetch(CDAEntry.class)
        .withContentType("rich")
        .where("fields.name", Matches, "simple")
        .orderBy("fields.name")
        .all()
        .entries()
        .values()
        .stream()
        .map(entry -> (CDARichDocument) entry.getField("rich"))
        .reduce((a, b) -> {
              final CDARichParagraph p = new CDARichParagraph();
              p.getContent().addAll(b.getContent());
              a.getContent().add(p);
              return a;
            }
        ).orElse(null);
  }

  private void addCustomRenderer(HtmlProcessor processor) {
    processor.overrideRenderer(
        (context, node) -> node instanceof CDARichHyperLink && ((CDARichHyperLink) node).getData() instanceof CDAEntry,
        (context, node) -> {
          final CDAEntry entry = (CDAEntry) ((CDARichHyperLink) node).getData();
          return format(
              getDefault(),
              "<a href=\"https://app.contentful.com/spaces/%s/entries/%s\">%s</a>",
              spaceIdFromEntry(entry),
              entry.id(),
              entry.getField("name")
          );
        }
    );
    processor.overrideRenderer(
        (context, node) -> node instanceof CDARichHyperLink && ((CDARichHyperLink) node).getData() instanceof CDAAsset,
        (context, node) -> {
          final CDAAsset asset = (CDAAsset) ((CDARichHyperLink) node).getData();
          final String url = asset.urlForImageWith(https(), formatOf(png));
          return format(
              getDefault(),
              "<a href=\"%s\">A hyperlink to an asset named '%s'</a>",
              url,
              asset.<String>getField("title")
          );
        }
    );
    processor.overrideRenderer(
        (context, node) -> node instanceof CDARichEmbeddedInline && ((CDARichEmbeddedInline) node).getData() instanceof CDAEntry,
        (context, node) -> {
          final CDAEntry entry = (CDAEntry) ((CDARichEmbeddedInline) node).getData();
          return format(
              getDefault(),
              "<span style=\"background-color:gray\">%s</span>",
              entry.<String>getField("name")
          );
        }
    );
    processor.overrideRenderer(
        (context, node) -> node instanceof CDARichEmbeddedInline && ((CDARichEmbeddedInline) node).getData() instanceof CDAAsset,
        (context, node) -> {
          final CDAAsset asset = (CDAAsset) ((CDARichEmbeddedInline) node).getData();
          return format(
              getDefault(),
              "<span style=\"background-color:lightgreen\">%s</span >",
              asset.<String>getField("title")
          );
        }
    );
    processor.overrideRenderer(
        (context, node) -> node instanceof CDARichEmbeddedBlock && ((CDARichEmbeddedBlock) node).getData() instanceof CDAEntry,
        (context, node) -> {
          final CDAEntry entry = (CDAEntry) ((CDARichEmbeddedBlock) node).getData();
          return format(
              getDefault(),
              "<div style=\"display: inline-block;background-color:orange;border:3px inset;\">Entry "
                  + "<b>%s</b><sup>%s</sup><br/><hr/>%s</div>",
              entry.<String>getField("name"),
              entry.id(),
              entry.<String>getField("name")
          );
        }
    );
    processor.overrideRenderer(
        (context, node) -> node instanceof CDARichEmbeddedBlock && ((CDARichEmbeddedBlock) node).getData() instanceof CDAAsset,
        (context, node) -> {
          final CDAAsset asset = (CDAAsset) ((CDARichEmbeddedBlock) node).getData();
          final String url = asset.urlForImageWith(https(), widthOf(100), heightOf(100), formatOf(png));
          return format(
              getDefault(),
              "<div style=\"display: inline-block;background-color:purple;border:3px outset;\">Asset "
                  + "<b>%s</b><sup>%s</sup><br/><hr/><img src=\"%s\" title=\"%s\"/></div>",
              asset.<String>getField("title"),
              asset.id(),
              url,
              asset.toString()
          );
        }
    );
  }

  @SuppressWarnings("unchecked")
  private Object spaceIdFromEntry(CDAEntry entry) {
    return ((Map<String, String>) ((Map<String, Object>) entry.getAttribute("space")).get("sys")).get("id");
  }
}