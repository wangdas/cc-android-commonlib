package com.bokecc.common.stream.bean;

/**
 * 即构对象
 * @author wy
 */
public class CCStreamZInfo {

    /**即构appid*/
    private long zegoAppId;
    /**即构签名信息*/
    private byte[] zegoAppSign;
    /**即构token*/
    private String zegoToken;
    /**即构的流id*/
    private String zegoStreamId;

    public long getZegoAppId() {
        return zegoAppId;
    }

    public void setZegoAppId(long zegoAppId) {
        this.zegoAppId = zegoAppId;
    }

    public byte[] getZegoAppSign() {
        return zegoAppSign;
    }

    public void setZegoAppSign(byte[] zegoAppSign) {
        this.zegoAppSign = zegoAppSign;
    }

    public String getZegoToken() {
        return zegoToken;
    }

    public void setZegoToken(String zegoToken) {
        this.zegoToken = zegoToken;
    }

    public String getZegoStreamId() {
        return zegoStreamId;
    }

    public void setZegoStreamId(String zegoStreamId) {
        this.zegoStreamId = zegoStreamId;
    }
}
