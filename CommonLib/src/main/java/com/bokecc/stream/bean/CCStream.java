package com.bokecc.stream.bean;

import android.view.SurfaceView;
import android.view.View;

import java.util.HashMap;

/**
 * 流对象
 */
public class CCStream {

    /**当前流对应的用户id*/
    private String mUserid;
    /**当前流是否有视频/音频*/
    private boolean hasVideo, hasAudio;

    /**当前远程流是否是本地流回调*/
    private boolean remoteIsLocal = false;

    /***/
    private boolean isBlackStream = false;

    /**是否为外接摄像头*/
    private boolean hasImprove = false;
    /**是否为共享桌面*/
    private boolean isScreenStream = false;
    /**是否为插入视频*/
    private boolean isInterCutVideo = false;
    /**是否为插入音频*/
    private boolean isInterCutAudio = false;

    /**设置用户名称*/
    private String mUserName;
    /***/
    private HashMap<String, String> attributes = null;
    /**处理双师课堂讲师的流，8就是双师课堂的讲师流。*/
    private int template = 0;
    /**视频对象*/
    private SurfaceView surfaceView;
    /**视频对象*/
    private View view;

    /**声网用户id*/
    private int mUid = 0;
    /**流id*/
    private String mStreamId = null;

    /**角色*/
    private int role;

    public boolean isInterCutAudio() {
        return isInterCutAudio;
    }

    public void setInterCutAudio(boolean interCutAudio) {
        isInterCutAudio = interCutAudio;
    }

    public boolean isScreenStream() {
        return isScreenStream;
    }

    public void setScreenStream(boolean screenStream) {
        isScreenStream = screenStream;
    }

    public boolean isInterCutVideo() {
        return isInterCutVideo;
    }

    public void setInterCutVideo(boolean interCutVideo) {
        isInterCutVideo = interCutVideo;
    }
    public SurfaceView getSurfaceView() {
        return surfaceView;
    }

    public void setSurfaceView(SurfaceView surfaceView) {
        this.surfaceView = surfaceView;
    }

    public boolean isBlackStream() {
        return isBlackStream;
    }

    public void setBlackStream(boolean blackStream) {
        isBlackStream = blackStream;
    }

    public void setUserid(String userid) {
        mUserid = userid;
    }

    public String getUserid() {
        return mUserid;
    }

    public void setHasAudio(boolean hasAudio) {
        this.hasAudio = hasAudio;
    }

    public boolean hasAudio() {
        return hasAudio;
    }

    public void setHasVideo(boolean hasVideo) {
        this.hasVideo = hasVideo;
    }

    public boolean hasVideo() {
        return hasVideo;
    }

    public boolean getHasImprove() {
        return hasImprove;
    }

    public void setHasImprove(boolean hasImprove) {
        this.hasImprove = hasImprove;
    }

    public void setTemplate(int template) {
        this.template = template;
    }

    public int getTemplate() {
        return template;
    }

    public boolean isRemoteIsLocal() {
        return remoteIsLocal;
    }

    public void setRemoteIsLocal(boolean remoteIsLocal) {
        this.remoteIsLocal = remoteIsLocal;
    }


    public void setStreamId(String mStreamId) {
        this.mStreamId = mStreamId;
    }

    public String getStreamId() {
        //兼容即构
        if (mStreamId != null) {
            return mStreamId;
        }
        //兼容声网SDK
        return mUserid;
    }

    public String getUserName() {
        return mUserName;
    }

    public void setUserName(String userName) {
        mUserName = userName;
    }

    public void setAttributes(HashMap<String, String> attributes) {
        this.attributes = attributes;
    }

    public HashMap<String, String> getAttributes() {
        return this.attributes;
    }

    public int getUserInfo() {
        return mUid;
    }

    public void setUserInfo(int uid) {
        this.mUid = uid;
    }

    /**
     * 返回id
     * @return
     */
    public String getSubRenderId() {
        //兼容即构
        if (mStreamId != null) {
            return mStreamId;
        }
        //返回声网的
        return String.valueOf(mUid);
    }

    public View getView() {
        return view;
    }

    public void setView(View view) {
        this.view = view;
    }

    @Deprecated
    public void detach(){}

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }
}
