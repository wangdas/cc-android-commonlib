package com.bokecc.common.stream.bean;

/**
 * 声网对象
 * @author wy 
 */
public class CCStreamAInfo {

    /**声网appid*/
    private String agoraAppId;
    /**声网的uid*/
    private int agoraUid;
    /***/
    private String agoraToken;
    /**是否推声网CDN流0不推，1推*/
    private int pubCdnSwitch;
    /**agora的RTMP地址*/
    private String agoRtmpCdn;
    /**默认是false有画面，true无画面*/
    private boolean isUpdateRtmpLayout;

    public String getAgoraAppId() {
        return agoraAppId;
    }

    public void setAgoraAppId(String agoraAppId) {
        this.agoraAppId = agoraAppId;
    }

    public int getAgoraUid() {
        return agoraUid;
    }

    public void setAgoraUid(int agoraUid) {
        this.agoraUid = agoraUid;
    }

    public String getAgoraToken() {
        return agoraToken;
    }

    public void setAgoraToken(String agoraToken) {
        this.agoraToken = agoraToken;
    }

    public int getPubCdnSwitch() {
        return pubCdnSwitch;
    }

    public void setPubCdnSwitch(int pubCdnSwitch) {
        this.pubCdnSwitch = pubCdnSwitch;
    }

    public String getAgoRtmpCdn() {
        return agoRtmpCdn;
    }

    public void setAgoRtmpCdn(String agoRtmpCdn) {
        this.agoRtmpCdn = agoRtmpCdn;
    }

    public boolean isUpdateRtmpLayout() {
        return isUpdateRtmpLayout;
    }

    public void setUpdateRtmpLayout(boolean updateRtmpLayout) {
        isUpdateRtmpLayout = updateRtmpLayout;
    }
}
