package com.bokecc.common.stream;

import com.bokecc.stream.bean.CCStream;
import com.bokecc.stream.bean.CCStreamQuality;
import com.bokecc.stream.bean.CCStreamSoundLevelInfo;

import java.util.HashMap;
import java.util.List;

/**
 * 直播监听
 */
public interface CCStreamCallback {

    /**初始化成功*/
    void onInitSuccess();
    /**初始化失败*/
    void onInitFailure(int errCode);

    /**加入房间成功*/
    void onJoinChannelSuccess();
    /**加入房间失败*/
    void onJoinFailure(int errCode);

    /**推流成功*/
    void onPublishSuccess(String streamId);
    /**推流失败*/
    void onPublishFailure(String streamId, int code, String errorMsg);

    /**订阅流成功*/
    void onRemoteStreamSuccess(String streamId);
    /**订阅流失败*/
    void onRemoteStreamFailure(String streamId, int errCode, String errorMsg);

    /**有用户加入*/
    void onUserJoined(CCStream stream);
    /**用户掉线*/
    void onUserOffline(CCStream stream, boolean isLocalStream);

    /**直播间状态回调*/
    void onLiveEvent(int event, HashMap<String, String> hashMap);

    /**重连中*/
    void onTryToConnection();
    /**重连成功*/
    void onReconnect();
    /**连接完全断开*/
    void onDisconnect();

    /**
     * 推流网络状态回调
     * @param publishQuality
     */
    void onPublishQuality(CCStreamQuality publishQuality);

    /**
     * 拉网络状态回调
     * @param uid
     * @param publishQuality
     */
    void onPlayQuality(String uid, CCStreamQuality publishQuality);

    /**
     * 首帧返回
     * @param uid     用户id
     * @param width   首帧宽
     * @param height  首帧高
     * @param elapsed 首帧延时
     */
    void onFirstRemoteVideoDecoded(int uid, int width, int height, int elapsed);

    /**
     * 自研webrtc-发送IceCandidate
     * @param userId
     * @param candidate
     */
    void onWebrtcSendIceCandidate(String userId, String sdpMid, int sdpMLineIndex, String candidate);

    /**
     * 自研webrtc-发送SDP
     * @param userId
     * @param type
     * @param data
     */
    void onWebrtcSendSdp(String userId, String type, String data);

    /**
     * 自研webrtc-本地摄像头打开
     * @param cameraWidth
     * @param cameraHeight
     */
    void onCameraOpen(int cameraWidth, int cameraHeight);

    /**
     * 拉流音浪回调
     * @param streamSoundLevelInfos
     */
    void onSoundLevelUpdate(List<CCStreamSoundLevelInfo> streamSoundLevelInfos);

    /**
     * 推流音浪回调
     * @param streamSoundLevelInfo
     */
    void onCaptureSoundLevelUpdate(CCStreamSoundLevelInfo streamSoundLevelInfo);
}
