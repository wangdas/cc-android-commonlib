package com.bokecc.glide.load.resource.file;

import android.support.annotation.NonNull;

import com.bokecc.glide.load.Options;
import com.bokecc.glide.load.ResourceDecoder;
import com.bokecc.glide.load.engine.Resource;

import java.io.File;

/**
 * A simple {@link ResourceDecoder} that creates resource for a given {@link
 * File}.
 */
public class FileDecoder implements ResourceDecoder<File, File> {

  @Override
  public boolean handles(@NonNull File source, @NonNull Options options) {
    return true;
  }

  @Override
  public Resource<File> decode(@NonNull File source, int width, int height,
      @NonNull Options options) {
    return new FileResource(source);
  }
}
