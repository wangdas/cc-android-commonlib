package com.bokecc.common.stream;

/**
 * 流媒体播放器回调
 */
public interface CCStreamPlayerCallback {

    /**开始播放*/
    void onPlayStart();

    /**暂停播放*/
    void onPlayPause();

    /**恢复播放*/
    void onPlayResume();

    /**停止播放*/
    void onPlayStop();

    /**
     * 完成快进到指定时刻
     * @param code 大于等于0表示成功，其它表示失败
     * @param millisecond 实际快进的进度
     */
    void onSeekComplete(int code, long millisecond);

    /**
     * 播放器进度回调
     * @param timestamp
     */
    void onProcessInterval(long timestamp);

    /**播放失败*/
    void onPlayError(int errorCode);

    /**音频开始播放*/
    void onAudioBegin();
    /**视频开始播放*/
    void onVideoBegin();

    /**开始缓冲*/
    void onBufferBegin();
    /**缓冲结束*/
    void onBufferEnd();
    /**预加载完成*/
    void onLoadComplete();
    /**播放结束*/
    void onPlayEnd();

    /**视频宽高*/
    void onSize(int width,int height);

}
