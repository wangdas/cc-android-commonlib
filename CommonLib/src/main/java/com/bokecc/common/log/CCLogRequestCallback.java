package com.bokecc.common.log;

/**
 * Created by wy on 2019/1/11.
 */

public interface CCLogRequestCallback<T> {

    void onSuccess(T response);

    void onFailure(int errorCode, String errorMsg);

}
