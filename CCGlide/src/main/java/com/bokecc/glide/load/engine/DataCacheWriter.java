package com.bokecc.glide.load.engine;

import android.support.annotation.NonNull;

import com.bokecc.glide.load.Encoder;
import com.bokecc.glide.load.Options;
import com.bokecc.glide.load.ResourceEncoder;
import com.bokecc.glide.load.engine.cache.DiskCache.Writer;

import java.io.File;

/**
 * Writes original source data or downsampled/transformed resource data to cache using the
 * provided {@link Encoder} or
 * {@link ResourceEncoder} and the given data or
 * {@link Resource}.
 *
 * @param <DataType> The type of data that will be encoded (InputStream, ByteBuffer,
 *                  Resource<Bitmap> etc).
 */
class DataCacheWriter<DataType> implements Writer {
  private final Encoder<DataType> encoder;
  private final DataType data;
  private final Options options;

  DataCacheWriter(Encoder<DataType> encoder, DataType data, Options options) {
    this.encoder = encoder;
    this.data = data;
    this.options = options;
  }

  @Override
  public boolean write(@NonNull File file) {
    return encoder.encode(data, file, options);
  }
}
