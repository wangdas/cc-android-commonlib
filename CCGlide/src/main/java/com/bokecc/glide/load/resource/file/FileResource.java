package com.bokecc.glide.load.resource.file;

import com.bokecc.glide.load.engine.Resource;
import com.bokecc.glide.load.resource.SimpleResource;

import java.io.File;

/**
 * A simple {@link Resource} that wraps a {@link File}.
 */
// Public API.
@SuppressWarnings("WeakerAccess")
public class FileResource extends SimpleResource<File> {
  public FileResource(File file) {
    super(file);
  }
}
