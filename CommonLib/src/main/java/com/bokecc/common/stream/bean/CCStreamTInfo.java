package com.bokecc.common.stream.bean;

/**
 * trtc对象
 */
public class CCStreamTInfo {

    /**appId*/
    private int sdkAppId;
    /**密钥*/
    private String secretkey;

    public int getSdkAppId() {
        return sdkAppId;
    }

    public void setSdkAppId(int sdkAppId) {
        this.sdkAppId = sdkAppId;
    }

    public String getSecretkey() {
        return secretkey;
    }

    public void setSecretkey(String secretkey) {
        this.secretkey = secretkey;
    }
}
