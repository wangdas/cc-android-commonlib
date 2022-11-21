package com.bokecc.glide;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.bokecc.glide.manager.RequestManagerRetriever;
import com.bokecc.glide.module.AppGlideModule;

import java.util.Set;

/**
 * Allows {@link AppGlideModule}s to exclude {@link com.bumptech.glide.annotation.GlideModule}s to
 * ease the migration from {@link com.bumptech.glide.annotation.GlideModule}s to Glide's annotation
 * processing system and optionally provides a
 * {@link RequestManagerRetriever.RequestManagerFactory} impl.
 */
abstract class GeneratedAppGlideModule extends AppGlideModule {
  /**
   * This method can be removed when manifest parsing is no longer supported.
   */
  @NonNull
  abstract Set<Class<?>> getExcludedModuleClasses();

  @Nullable
  RequestManagerRetriever.RequestManagerFactory getRequestManagerFactory() {
    return null;
  }
}