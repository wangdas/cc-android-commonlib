package com.bokecc.common.socket;

public interface CCSocketCallback<T> {

    void onResponse(T response);
}