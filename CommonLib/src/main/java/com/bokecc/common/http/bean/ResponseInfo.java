package com.bokecc.common.http.bean;

import java.util.List;
import java.util.Map;

/**
 * Created by wy on 2018/7/2.
 */
public class ResponseInfo {

    private String mBody;

    private Map<String, List<String>> mHeaders;

    public ResponseInfo(String body,Map<String, List<String>> headers) {
        this.mBody = body;
        this.mHeaders = headers;
    }

    public String getBody() {
        return mBody;
    }

    public Map<String, List<String>> getHeaders() {
        return mHeaders;
    }

}
