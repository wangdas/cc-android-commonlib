package com.bokecc.common.log.model;

/**
 * 日志对象
 */
public class CCLogInfo {

    private String media_cdnurl;

    private String callback;

    private String signature;

    private String policy;

    private String accessid;

    private String host;

    private String expire;

    private String dir;

    public String getMedia_cdnurl() {
        return media_cdnurl;
    }

    public void setMedia_cdnurl(String media_cdnurl) {
        this.media_cdnurl = media_cdnurl;
    }

    public String getCallback() {
        return callback;
    }

    public void setCallback(String callback) {
        this.callback = callback;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getPolicy() {
        return policy;
    }

    public void setPolicy(String policy) {
        this.policy = policy;
    }

    public String getAccessid() {
        return accessid;
    }

    public void setAccessid(String accessid) {
        this.accessid = accessid;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getExpire() {
        return expire;
    }

    public void setExpire(String expire) {
        this.expire = expire;
    }

    public String getDir() {
        return dir;
    }

    public void setDir(String dir) {
        this.dir = dir;
    }
}
