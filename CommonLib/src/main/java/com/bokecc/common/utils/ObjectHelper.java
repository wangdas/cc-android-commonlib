package com.bokecc.common.utils;

import android.text.TextUtils;

/**
 *
 */
 public class ObjectHelper {

    private ObjectHelper() {}

    public static <T> T requireNonNull(T object, String message) {
        if (object instanceof String) {
            if (TextUtils.isEmpty((String) object)) {
                throw new NullPointerException(message);
            }
        }
        if (object == null) {
            throw new NullPointerException(message);
        }
        return object;
    }

}
