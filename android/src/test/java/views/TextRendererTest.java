package views;

import android.app.Activity;
import android.view.View;

import com.contentful.java.cda.rich.CDARichNode;
import com.contentful.java.cda.rich.CDARichText;
import com.contentful.rich.android.AndroidContext;
import com.contentful.rich.android.AndroidProcessor;
import com.contentful.rich.android.R;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import static com.google.common.truth.Truth.assertThat;

/**
 * Regression tests for a NullPointerException risk in {@link
 * com.contentful.rich.android.renderer.views.TextRenderer}: {@code AndroidContext#getPath()} is
 * annotated {@code @Nullable}, so any {@link AndroidContext} implementation (or override) that
 * legitimately returns a null path must not crash rendering.
 */
@RunWith(RobolectricTestRunner.class)
public class TextRendererTest {
  private Activity activity;

  @Before
  public void setup() {
    activity = Robolectric.setupActivity(Activity.class);
  }

  @Test
  public void rendersPlainTextWithoutNpeWhenPathIsNull() {
    final AndroidProcessor<View> processor = AndroidProcessor.creatingNativeViews();
    final AndroidContext context = new AndroidContext(activity) {
      @Nullable @Override public List<CDARichNode> getPath() {
        return null;
      }
    };

    final CDARichText text = new CDARichText("Hello world", new ArrayList<>());

    final View result = processor.process(context, text);

    assertThat(result).isNotNull();
    final View content = result.findViewById(R.id.rich_content);
    assertThat(content).isNotNull();

    final ArrayList<View> views = new ArrayList<>();
    content.findViewsWithText(views, "Hello world", View.FIND_VIEWS_WITH_TEXT);
    assertThat(views).hasSize(1);
  }
}
