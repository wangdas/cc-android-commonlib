package com.bokecc.common.http;

import java.util.Map;

/**
 * 下载文件请求
 * Created by wangyue on 16/6/14.
 */
public class DownloadRequest extends BaseRequest {

    public DownloadRequest() {
        super(null);
    }

    @Override
    protected Map<String, String> getRequestHeaders() {
        return null;
    }

    @Override
    protected Object parserTask(String response) throws Exception {
        return null;
    }

    @Override
    protected void finishTask(Object object) throws Exception {

    }

}
