package com.bokecc.common.stream.bean;

/**
 * 流媒体对象信息
 * @author wy
 */
public class CCStreamInfo {

    /**角色*/
    private int role;
    /**房间id*/
    private String roomId;
    /**用户id*/
    private String userId;
    /**平台*/
    private int platform;

    /**声网对象*/
    private CCStreamAInfo agoraInfo;
    /**即构对象*/
    private CCStreamZInfo zegoInfo;
    /**腾讯对象*/
    private CCStreamTInfo trtcInfo;

    /**
     * 是否是高音质
     */
    private boolean audio_quality_music = false;

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public CCStreamAInfo getAgoraInfo() {
        return agoraInfo;
    }

    public void setAgoraInfo(CCStreamAInfo agoraInfo) {
        this.agoraInfo = agoraInfo;
    }

    public int getPlatform() {
        return platform;
    }

    public void setPlatform(int platform) {
        this.platform = platform;
    }

    public CCStreamZInfo getZegoInfo() {
        return zegoInfo;
    }

    public void setZegoInfo(CCStreamZInfo zegoInfo) {
        this.zegoInfo = zegoInfo;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public CCStreamTInfo getTrtcInfo() {
        return trtcInfo;
    }

    public void setTrtcInfo(CCStreamTInfo trtcInfo) {
        this.trtcInfo = trtcInfo;
    }

    public boolean isAudio_quality_music() {
        return audio_quality_music;
    }

    public void setAudio_quality_music(boolean audio_quality_music) {
        this.audio_quality_music = audio_quality_music;
    }
}
