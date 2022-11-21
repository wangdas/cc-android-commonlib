package com.bokecc.glide.load.engine;

import com.bokecc.glide.load.Key;
import com.bokecc.glide.load.Options;
import com.bokecc.glide.load.Transformation;

import java.util.Map;

class EngineKeyFactory {

  @SuppressWarnings("rawtypes")
  EngineKey buildKey(Object model, Key signature, int width, int height,
      Map<Class<?>, Transformation<?>> transformations, Class<?> resourceClass,
      Class<?> transcodeClass, Options options) {
    return new EngineKey(model, signature, width, height, transformations, resourceClass,
        transcodeClass, options);
  }
}
