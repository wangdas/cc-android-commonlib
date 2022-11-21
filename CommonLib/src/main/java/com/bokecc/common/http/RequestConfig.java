package com.bokecc.common.http;

/**
 * Created by wangyue on 17/1/23.
 */

public class RequestConfig {

    /**操作成功的状态码*/
    public static int request_code_success = 100;

    /**操作成功的状态码*/
    public static int request_time_out = 110;

    /**解析状态码标签*/
    protected String keyCode = "code";
    /**解析状态提示标签*/
    protected String keyMessage = "msg";
    /**解析数据标签*/
    protected String keyData = "data";

}
