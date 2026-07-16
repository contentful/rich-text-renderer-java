package views;

import android.app.Activity;
import android.content.Intent;
import android.text.Spannable;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.contentful.java.cda.rich.CDARichHyperLink;
import com.contentful.java.cda.rich.CDARichParagraph;
import com.contentful.java.cda.rich.CDARichTable;
import com.contentful.java.cda.rich.CDARichTableCell;
import com.contentful.java.cda.rich.CDARichTableHeaderCell;
import com.contentful.java.cda.rich.CDARichTableRow;
import com.contentful.java.cda.rich.CDARichText;
import com.contentful.rich.android.AndroidContext;
import com.contentful.rich.android.AndroidProcessor;
import com.contentful.rich.android.R;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.Shadows;

import java.util.ArrayList;

import static com.google.common.truth.Truth.assertThat;

/**
 * Regression tests for {@link com.contentful.rich.android.renderer.views.TableRenderer}:
 * <ul>
 *   <li>a malformed cell (not a {@code CDARichBlock}) must not throw a {@code
 *       ClassCastException} and crash rendering of the whole table;</li>
 *   <li>hyperlinks inside table cells must not navigate to unsafe URL schemes.</li>
 * </ul>
 */
@RunWith(RobolectricTestRunner.class)
public class TableTest {
  private Activity activity;

  @Before
  public void setup() {
    activity = Robolectric.setupActivity(Activity.class);
  }

  private static CDARichTableCell cellWithText(String text) {
    final CDARichTableCell cell = new CDARichTableCell();
    final CDARichParagraph paragraph = new CDARichParagraph();
    paragraph.getContent().add(new CDARichText(text, new ArrayList<>()));
    cell.getContent().add(paragraph);
    return cell;
  }

  @Test
  public void rendersSimpleTableWithHeaderAndDataRows() {
    final AndroidProcessor<View> processor = AndroidProcessor.creatingNativeViews();
    final AndroidContext context = new AndroidContext(activity);

    final CDARichTableHeaderCell headerCell = new CDARichTableHeaderCell();
    final CDARichParagraph headerParagraph = new CDARichParagraph();
    headerParagraph.getContent().add(new CDARichText("Header", new ArrayList<>()));
    headerCell.getContent().add(headerParagraph);

    final CDARichTableRow headerRow = new CDARichTableRow();
    headerRow.getContent().add(headerCell);

    final CDARichTableRow dataRow = new CDARichTableRow();
    dataRow.getContent().add(cellWithText("Data"));

    final CDARichTable table = new CDARichTable();
    table.getContent().add(headerRow);
    table.getContent().add(dataRow);

    final View result = processor.process(context, table);

    assertThat(result).isNotNull();
    final TableLayout tableLayout = result.findViewById(R.id.rich_table);
    assertThat(tableLayout).isNotNull();
    assertThat(tableLayout.getChildCount()).isEqualTo(2);

    final ArrayList<View> views = new ArrayList<>();
    result.findViewsWithText(views, "Header", View.FIND_VIEWS_WITH_TEXT);
    assertThat(views).hasSize(1);

    views.clear();
    result.findViewsWithText(views, "Data", View.FIND_VIEWS_WITH_TEXT);
    assertThat(views).hasSize(1);
  }

  @Test
  public void malformedCellDoesNotThrowClassCastException() {
    final AndroidProcessor<View> processor = AndroidProcessor.creatingNativeViews();
    final AndroidContext context = new AndroidContext(activity);

    // A well-formed cell next to a malformed one (a bare CDARichText, which is not a
    // CDARichBlock) simulating corrupted/unexpected API data.
    final CDARichTableRow row = new CDARichTableRow();
    row.getContent().add(cellWithText("Good cell"));
    row.getContent().add(new CDARichText("not a block", new ArrayList<>()));

    final CDARichTable table = new CDARichTable();
    table.getContent().add(row);

    final View result = processor.process(context, table);

    assertThat(result).isNotNull();
    final TableLayout tableLayout = result.findViewById(R.id.rich_table);
    assertThat(tableLayout).isNotNull();
    assertThat(tableLayout.getChildCount()).isEqualTo(1);

    final ArrayList<View> views = new ArrayList<>();
    result.findViewsWithText(views, "Good cell", View.FIND_VIEWS_WITH_TEXT);
    assertThat(views).hasSize(1);
  }

  @Test
  public void tableHyperlinkClickBlocksJavascriptUriScheme() {
    final AndroidProcessor<View> processor = AndroidProcessor.creatingNativeViews();
    final AndroidContext context = new AndroidContext(activity);

    final CDARichHyperLink hyperlink = new CDARichHyperLink("javascript:alert(1)");
    hyperlink.getContent().add(new CDARichText("Click me", new ArrayList<>()));

    final CDARichParagraph paragraph = new CDARichParagraph();
    paragraph.getContent().add(hyperlink);

    final CDARichTableCell cell = new CDARichTableCell();
    cell.getContent().add(paragraph);

    final CDARichTableRow row = new CDARichTableRow();
    row.getContent().add(cell);

    final CDARichTable table = new CDARichTable();
    table.getContent().add(row);

    final View result = processor.process(context, table);
    assertThat(result).isNotNull();

    final ArrayList<View> views = new ArrayList<>();
    result.findViewsWithText(views, "Click me", View.FIND_VIEWS_WITH_TEXT);
    assertThat(views).hasSize(1);
    assertThat(views.get(0)).isInstanceOf(TextView.class);

    final TextView cellView = (TextView) views.get(0);
    final CharSequence text = cellView.getText();
    assertThat(text).isInstanceOf(Spannable.class);
    final ClickableSpan[] spans = ((Spannable) text).getSpans(0, text.length(), ClickableSpan.class);
    assertThat(spans).hasLength(1);

    spans[0].onClick(cellView);

    final Intent startedIntent = Shadows.shadowOf(activity).getNextStartedActivity();
    assertThat(startedIntent).isNull();
  }
}
