package com.contentful.rich.android.renderer.chars;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.util.concurrent.CountDownLatch;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;

import static com.google.common.truth.Truth.assertThat;

/**
 * Regression tests for a blocking main-thread / ANR risk in {@link EmbeddedLinkRenderer}'s
 * default {@code BitmapProvider}: waiting on the network download latch must be bounded by a
 * timeout, must cancel the in-flight call instead of leaking it, and must never propagate an
 * unchecked exception or hang indefinitely.
 * <p>
 * These tests exercise {@link EmbeddedLinkRenderer#awaitDownload} directly and perform no real
 * network I/O, so they are fast and deterministic.
 */
@RunWith(RobolectricTestRunner.class)
public class EmbeddedLinkRendererTest {

  private static Call newUnstartedCall() {
    return new OkHttpClient.Builder().build()
        .newCall(new Request.Builder().url("https://contentful.com/never-started").build());
  }

  @Test(timeout = 5_000)
  public void awaitDownloadTimesOutAndCancelsCallInsteadOfHanging() {
    final CountDownLatch neverCountedDown = new CountDownLatch(1);
    final Call call = newUnstartedCall();

    final long start = System.nanoTime();
    final boolean completed = EmbeddedLinkRenderer.awaitDownload(neverCountedDown, call, 1, "https://contentful.com/image.jpg");
    final long elapsedMillis = (System.nanoTime() - start) / 1_000_000;

    assertThat(completed).isFalse();
    assertThat(elapsedMillis).isLessThan(4_000L);
    assertThat(call.isCanceled()).isTrue();
  }

  @Test(timeout = 2_000)
  public void awaitDownloadReturnsTrueImmediatelyWhenAlreadyComplete() {
    final CountDownLatch alreadyDone = new CountDownLatch(0);
    final Call call = newUnstartedCall();

    final boolean completed = EmbeddedLinkRenderer.awaitDownload(alreadyDone, call, 5, "https://contentful.com/image.jpg");

    assertThat(completed).isTrue();
    assertThat(call.isCanceled()).isFalse();
  }

  @Test(timeout = 5_000)
  public void awaitDownloadHandlesInterruptionWithoutThrowing() throws InterruptedException {
    final CountDownLatch neverCountedDown = new CountDownLatch(1);
    final Call call = newUnstartedCall();

    final Thread[] worker = new Thread[1];
    final boolean[] result = new boolean[1];
    final Throwable[] thrown = new Throwable[1];

    worker[0] = new Thread(() -> {
      try {
        result[0] = EmbeddedLinkRenderer.awaitDownload(neverCountedDown, call, 30, "https://contentful.com/image.jpg");
      } catch (Throwable t) {
        thrown[0] = t;
      }
    });
    worker[0].start();
    Thread.sleep(100);
    worker[0].interrupt();
    worker[0].join(4_000);

    assertThat(thrown[0]).isNull();
    assertThat(result[0]).isFalse();
    assertThat(call.isCanceled()).isTrue();
    assertThat(worker[0].isAlive()).isFalse();
  }
}
