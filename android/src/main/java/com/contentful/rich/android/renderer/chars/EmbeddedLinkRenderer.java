package com.contentful.rich.android.renderer.chars;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;

import com.contentful.java.cda.CDAAsset;
import com.contentful.java.cda.CDAEntry;
import com.contentful.java.cda.rich.CDARichEmbeddedInline;
import com.contentful.java.cda.rich.CDARichHyperLink;
import com.contentful.java.cda.rich.CDARichNode;
import com.contentful.rich.android.AndroidContext;
import com.contentful.rich.android.AndroidProcessor;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

import static com.contentful.java.cda.image.ImageOption.Format.jpg;
import static com.contentful.java.cda.image.ImageOption.formatOf;
import static com.contentful.java.cda.image.ImageOption.heightOf;
import static com.contentful.java.cda.image.ImageOption.https;
import static com.contentful.java.cda.image.ImageOption.widthOf;

/**
 * Render a given rich embeded link into a text, colored for highlighting.
 */
public class EmbeddedLinkRenderer extends BlockRenderer {

  /**
   * An interface to control how to generate the bitmap to be used.
   */
  public interface BitmapProvider {
    /**
     * This method is called to retrieve an image from the given asset.
     *
     * @param context the Android context used for rendering.
     * @param asset   the asset containing the url to be used.
     * @return a Bitmap containing the image found, or null if none was found.
     */
    @Nullable Bitmap provide(Context context, CDAAsset asset);
  }

  /**
   * Create a simple image downloader to be used with care.
   * <p>
   * This provider will be used by default, please overwrite it if possible clientside.
   */
  public static final BitmapProvider defaultBitmapProvider = new BitmapProvider() {
    @Override public Bitmap provide(Context context, CDAAsset asset) {
      final String url = asset.urlForImageWith(https(), widthOf(80), heightOf(80), formatOf(jpg));
      final CountDownLatch latch = new CountDownLatch(1);
      final Map<String, Bitmap> bitmaps = new HashMap<>();
      new OkHttpClient.Builder().build().newCall(new Request.Builder().get().url(url).build()).enqueue(
          new Callback() {
            @Override public void onFailure(Call call, IOException e) {
              latch.countDown();
            }

            @Override public void onResponse(Call call, Response response) throws IOException {
              final ResponseBody body = response.body();
              if (body != null) {
                final byte[] bytes = body.bytes();
                final Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                bitmaps.put(url, bitmap);
              }
              latch.countDown();
            }
          }
      );

      try {
        latch.await();

        final Bitmap bitmap = bitmaps.get(url);
        if (bitmap != null) {
          return bitmap;
        }
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      return BitmapFactory.decodeResource(context.getResources(), android.R.drawable.ic_dialog_alert);
    }
  };

  private final BitmapProvider provider;

  /**
   * Create a new EmbeddedLinkRenderer, using the default {@link BitmapProvider}.
   *
   * @param processor the processor to be used for child rendering.
   */
  public EmbeddedLinkRenderer(@Nonnull AndroidProcessor<CharSequence> processor) {
    this(processor, defaultBitmapProvider);
  }

  /**
   * Constructor for defining custom BitmapProvider.
   *
   * @param processor the processor to be used for child rendering.
   * @param provider  the custom provider providing bitmaps to the spannable.
   */
  public EmbeddedLinkRenderer(@Nonnull AndroidProcessor<CharSequence> processor, @Nonnull BitmapProvider provider) {
    super(processor);
    this.provider = provider;
  }

  /**
   * Is the given node an embedded link?
   *
   * @param context context this check should be performed in.
   * @param node    node to be checked.
   * @return true if node is a link.
   */
  @Override public boolean canRender(@Nullable AndroidContext context, @Nonnull CDARichNode node) {
    return node instanceof CDARichEmbeddedInline && ((CDARichHyperLink) node).getData() != null;
  }

  /**
   * Add entries 'title'-field to the current text and highlight it accordingly. If not a CDAEntry got linked, the
   * image of the asset gets downloaded and displayed.
   *
   * @param context the context this decoration should be applied to.
   * @param node    the actual node, rendered into the builder.
   * @param builder a builder for generating the desired output.
   * @return a representation of the currently rendered string, decorated by the title-field of the entry.
   */
  @Nonnull @Override protected SpannableStringBuilder decorate(
      @Nonnull AndroidContext context,
      @Nonnull CDARichNode node,
      @Nonnull SpannableStringBuilder builder) {

    final CDARichHyperLink link = (CDARichHyperLink) node;
    final Object data = link.getData();
    if (data instanceof CDAEntry) {
      final CDAEntry entry = (CDAEntry) data;
      final String title = entry.getField("title");

      if (title != null) {
        builder.insert(0, title);
        builder.setSpan(new ForegroundColorSpan(Color.argb(255, 255, 255, 255)), 0, title.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
      }
    } else if (data instanceof CDAAsset) {
      final CDAAsset asset = (CDAAsset) data;

      final String imageReplacement = "\uD83D\uDDBC️";
      builder.insert(0, imageReplacement);

      final Bitmap bitmap = provider.provide(context.getAndroidContext(), asset);
      final ImageSpan span = new ImageSpan(context.getAndroidContext(), bitmap, ImageSpan.ALIGN_BASELINE);

      builder.setSpan(span, 0, imageReplacement.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
    }

    return builder;
  }
}
