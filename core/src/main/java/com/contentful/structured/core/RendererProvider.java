package com.contentful.structured.core;

import javax.annotation.Nonnull;

/**
 * This interface will be used by submodules to populate a processor with renderer specific for the
 * modules use case.
 *
 * @param <C> custom context class
 * @param <R> custom result class.
 */
public interface RendererProvider<C extends Context, R> {
  /**
   * Calling this method with a valid processor will add all available renderer provided by this
   * interface.
   *
   * @param processor the processor to be filled up with renderer.
   */
  void provide(@Nonnull Processor<C, R> processor);
}
