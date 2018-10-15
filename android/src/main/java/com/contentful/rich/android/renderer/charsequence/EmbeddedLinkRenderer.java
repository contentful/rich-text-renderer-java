package com.contentful.rich.android.renderer.charsequence;

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
import com.contentful.java.cda.rich.CDARichEmbeddedLink;
import com.contentful.java.cda.rich.CDARichHyperLink;
import com.contentful.java.cda.rich.CDARichNode;
import com.contentful.rich.android.RichTextContext;
import com.contentful.rich.core.Processor;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import androidx.annotation.NonNull;
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

public class EmbeddedLinkRenderer extends BlockRenderer {

  interface BitmapProvider {
    Bitmap provide(Context context, CDAAsset asset);
  }

  final BitmapProvider provider;

  public EmbeddedLinkRenderer(@Nonnull Processor<RichTextContext, CharSequence> processor) {
    this(processor, new BitmapProvider() {
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
    });
  }

  public EmbeddedLinkRenderer(@Nonnull Processor<RichTextContext, CharSequence> processor, @Nonnull BitmapProvider provider) {
    super(processor);
    this.provider = provider;
  }

  @Override public boolean check(@Nullable RichTextContext context, @Nonnull CDARichNode node) {
    return node instanceof CDARichEmbeddedLink;
  }

  @NonNull
  @Override protected SpannableStringBuilder decorate(
      @Nonnull RichTextContext context,
      @Nonnull CDARichNode node,
      @Nonnull SpannableStringBuilder builder) {

    final CDARichHyperLink link = (CDARichHyperLink) node;
    final Object data = link.getData();
    if (data instanceof CDAEntry) {
      final CDAEntry entry = (CDAEntry) data;
      final String title = entry.getField("title");
      builder.insert(0, title);
      builder.setSpan(new ForegroundColorSpan(Color.argb(1.0f, 1.0f, 0.5f, 1.0f)), 0, title.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
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
