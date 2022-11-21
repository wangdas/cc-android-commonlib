package com.bokecc.common.stream;

import android.content.Context;
import android.graphics.Outline;
import android.os.Build;
import android.view.Gravity;
import android.view.SurfaceView;
import android.view.TextureView;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.widget.FrameLayout;

import com.bokecc.common.utils.Tools;
import com.bokecc.stream.bean.CCStream;

import java.util.List;

/**
 * 流基础管理者
 * altas/zego/agora/trtc等视频平台管理者都需要继承此类
 * @author wy
 */
public abstract class BaseLiveManager {

    public static final int ROLE_TEACHER = 0;
    public static final int ROLE_STUDENT = 1;

    /**
     * 监听
     */
    protected CCStreamCallback liveManagerListener;

    /**
     * 播放器回调
     */
    protected CCStreamPlayerCallback streamPlayerCallback;

    /**
     * 初始化
     */
    public abstract void init();

    /**
     * 加入频道
     * 注意：必须要在初始化sdk成功后才可以调用
     */
    public abstract void joinChannel();

    /**
     * 离开房间
     */
    public abstract void leaveChannel();

    /**
     * 开始预览
     * @param context
     * @param renderMode RENDER_MODE_FIT自适应/RENDER_MODE_HIDDEN全铺满
     * @return
     */
    public abstract SurfaceView startPreview(Context context,int renderMode);

    /**
     * 开始预览
     * @param context
     * @param renderMode RENDER_MODE_FIT自适应/RENDER_MODE_HIDDEN全铺满
     * @return
     */
    public abstract View startPreview2(Context context,int renderMode);

    /**
     * 停止预览
     */
    public abstract void stopPreview();

    /**
     * 开始推流
     */
    public abstract void startPublish();

    /**
     * 停止推流
     */
    public abstract void stopPublish();

    /**
     * 开始远端用户订阅
     * @param context
     * @param stream 流信息
     * @param renderMode  RENDER_MODE_FIT自适应/RENDER_MODE_HIDDEN全铺满
     * @return
     */
    public abstract SurfaceView setupRemoteVideo(Context context, CCStream stream, int renderMode);
    /**
     * 开始远端用户订阅
     * @param context
     * @param stream 流信息
     * @param renderMode  RENDER_MODE_FIT自适应/RENDER_MODE_HIDDEN全铺满
     * @param mirror  是否镜像
     * @return
     */
    public abstract SurfaceView setupRemoteVideo(Context context,CCStream stream, int renderMode,boolean mirror);

    /**
     * 开始远端用户订阅
     * @param context
     * @param stream 流信息
     * @param renderMode  RENDER_MODE_FIT自适应/RENDER_MODE_HIDDEN全铺满
     * @param mirror  是否镜像
     * @return
     */
    public abstract View setupRemoteVideo2(Context context,CCStream stream, int renderMode,boolean mirror);

    /**
     * 停止远端用户订阅
     * @param stream
     */
    public abstract void stopRemoteVideo(CCStream stream);

    /**
     * 创建SurfaceView
     * @param context
     * @return
     */
    protected abstract SurfaceView createRendererView(Context context);

    /**
     * 创建TextureView
     * @param context
     * @return
     */
    protected TextureView createTextureView(Context context){
        return new TextureView(context);
    }

    /**
     * 设置视频视图有圆弧
     * @param textureView
     * @param corner
     * @return
     */
    public abstract boolean setViewCorner(View textureView,final int corner);

    /**
     * 设置视频视图有圆弧
     * @param textureView
     * @param corner
     * @return
     */
    protected boolean setTextureViewCorner(TextureView textureView,final int corner){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && corner > 0) {
            textureView.setOutlineProvider(new ViewOutlineProvider() {
                @Override
                public void getOutline(View view, Outline outline) {
                    outline.setRoundRect(0,0,view.getWidth(), view.getHeight(),Tools.dipToPixel(corner));
                }
            });
            textureView.setClipToOutline(true);
            return true;
        }
        return false;
    }

    /**
     * 旋转视频
     * @param textureView
     * @param rotate
     * @param width
     * @param height
     * @return
     */
    public abstract boolean rotateView(View textureView, int rotate, int width, int height);

    /**
     * 旋转视频
     *
     * @param textureView
     * @param rotate
     * @param width
     * @param height
     */
    protected boolean rotateTextureView(TextureView textureView, int rotate, int width, int height) {
        if (width == 0 || height == 0) {
            return false;
        }
        FrameLayout.LayoutParams layoutParams;
        if (rotate == 90 || rotate == 270) {
            float sy;
            if (width > height) {
                sy = (float) width / height;
            } else {
                sy = (float) height / width;
            }

            int w = (int) (width * sy);
            int h = (int) (height * sy);
            layoutParams = new FrameLayout.LayoutParams(w, h);
            layoutParams.leftMargin = 0;
            layoutParams.topMargin = 0;
            layoutParams.gravity = Gravity.CENTER;
            textureView.setLayoutParams(layoutParams);
        }else if(rotate == 0){
            layoutParams = new FrameLayout.LayoutParams(width, height);
            layoutParams.leftMargin = 0;
            layoutParams.topMargin = 0;
            layoutParams.gravity = Gravity.CENTER;
            textureView.setLayoutParams(layoutParams);
        }
        textureView.setRotation(rotate);
        return true;
    }

    /**
     * 开/关本地视频采集
     * @param enabled true开启/false关闭
     */
    public abstract void enableLocalVideo(boolean enabled);

    /**
     * 开/关本地音频采集
     * @param enabled true语音开启/false关闭语音采集
     */
    public abstract void enableLocalAudio(boolean enabled);


    /**
     * 停止/恢复接收指定视频流
     * @param muted true恢复/false停止
     * @param stream 流信息
     */
    public abstract void muteRemoteVideoStream(CCStream stream,boolean muted);

    /**
     * 停止/恢复接收指定音频流
     * @param muted true恢复/false停止
     * @param stream 流信息
     */
    public abstract void muteRemoteAudioStream(CCStream stream,boolean muted);

    /**
     * 设置显示方向
     * 如果是横屏需要设置,设置为枚举类型1或3 横屏模式，0;2是倒着竖屏
     * @param orientation 0-3 四个方向
     */
    public abstract void setAppOrientation(int orientation);

    /**
     * 设置分辨率
     * @param resolution 分辨率
     */
    public abstract void setResolution(int resolution);

    /**
     * 设置分辨率
     * @param resolution  分辨率
     * @param isVideoRenderPortrait 是否横屏显示
     */
    public abstract void setResolution(int resolution,boolean isVideoRenderPortrait);

    /**
     * 设置本地视频镜像
     * @param mirror
     */
    public abstract void setLocalVideoMirrorMode(boolean mirror);

    /**
     * 设置远程视频镜像
     * @param stream
     * @param mirror
     */
    public abstract void setRemoteVideoMirrorMode(CCStream stream,boolean mirror);

    /**
     * 设置视频镜像模式，这几种模式只针对使用前置摄像头的时候，要么预览和拉流都是镜像的，要么预览和拉流都不镜像
     * 主播端和观众端都是镜像效果，设置对应枚举值为：1  预览启用镜像，推流启用镜像
     * 主播端镜像，观众端非镜像效果，设置对应枚举值为 0  预览启用镜像，推流不启用镜像
     * 主播和观众端都非镜像效果，设置对应枚举值为：2   预览不启用镜像，推流不启用镜像
     * 主播非镜像，观众镜像效果，设置对应枚举值为：3  预览不启用镜像，推流启用镜像
     *
     * @param mode
     */
    public abstract void setVideoMirrorMode(int mode);

    /**
     * 切换摄像头
     */
    public abstract boolean switchCamera();
    /**
     * 设置摄像头
     */
    public abstract boolean setCameraType(boolean isFrontCamera);


    /**
     * 释放所有资源
     */
    public abstract void destroy();

    /**
     * 获取当前的流id
     * @return
     */
    public abstract String getStreamId();

    /**开始播放*/
    public abstract boolean startPlay(View view, String path, boolean repeat, CCStreamPlayerCallback streamPlayerCallback);
    /**停止播放*/
    public abstract void stopPlay();
    /**暂停播放*/
    public abstract void pausePlay();
    /**重启播放*/
    public abstract void resumePlay();
    /**跳转*/
    public abstract void seekToPlayer(long l);
    /**设置音量*/
    public abstract void setVolume(int volume);
    /**关闭播放器*/
    public abstract void closePlayer();
    /**获取长度*/
    public abstract long mediaPlayerDuration();
    /**获取当前播放时间点*/
    public abstract long mediaPlayerCurrentPosition();

    /**自研webrtc-接收需要连麦的人员列表*/
    public abstract void receiveSpeakPeerList(List<String> peerList);
    /**自研webrtc-接收offer*/
    public abstract void onSpeakOffer(String type, String data);
    /**自研webrtc-接收answer*/
    public abstract void onSpeakAnswer(String type, String data);
    /**自研webrtc-交换候选地址IceCandidate*/
    public abstract void onSpeakIceCandidate(String sdpMid, int sdpMLineIndex, String candidate);


    /**用户角色 老师*/
    public static final int PRESENTER = 0;
    /**用户角色 学生*/
    public static final int TALKER = 1;
    /**用户角色 助教*/
    public static final int ASSISTANT = 4;
    /**用户角色 旁听*/
    public static final int AUDITOR = 2;
    /**用户角色 隐身者*/
    public static final int INSPECTOR = 3;

    /**用户角色 麦下观众(RTC-CDN)*/
    public static final int AULOW = 7;
    /**用户角色 低延迟观众(RTMP-CDN)*/
    public static final int AUMIDDLE = 8;
    /**用户角色 普通观众(RTMP)*/
    public static final int AUHIGH = 9;

    /**
     * 获取角色
     * @param role
     * @return
     */
    public static int getRoleInt(String role){
        switch (role) {
            case "presenter":
                return PRESENTER;
            case "talker":
                return TALKER;
            case "inspector":
                return INSPECTOR;
            case "aul":
                return AULOW;
            case "aum":
                return AUMIDDLE;
            case "auh":
                return AUHIGH;
            default:
                return -1;
        }
    }

}
