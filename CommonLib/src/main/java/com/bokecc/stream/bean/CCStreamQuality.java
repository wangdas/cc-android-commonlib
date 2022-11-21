package com.bokecc.stream.bean;

/**
 * 流状态信息
 */
public class CCStreamQuality {

    /** 本机到服务器的往返时延(ms) */
    private int rtt;
    /**上行丢包(0~255)，数值越大丢包越高，丢包率 = pktLostRate/255 */
    private int pktLostRate;
    /**下行丢包率*/
    private int downLostRate;
    /** 上行综合网络质量(0~3)，分别对应优、良、中、差 */
    private int txQuality;
    /** 下行综合网络质量(0~3)，分别对应优、良、中、差*/
    private int rxQuality;

    /**音频码率*/
    private double akbps;
    /**视频码率*/
    private double vkbps;

    /**视频帧率*/
    private double fps;
    /**音频帧率*/
    private double afps;


    public int getRtt() {
        return rtt;
    }

    public void setRtt(int rtt) {
        this.rtt = rtt;
    }

    public int getPktLostRate() {
        return pktLostRate;
    }

    public void setPktLostRate(int pktLostRate) {
        this.pktLostRate = pktLostRate;
    }

    public int getTxQuality() {
        return txQuality;
    }

    public void setTxQuality(int txQuality) {
        this.txQuality = txQuality;
    }

    public double getAkbps() {
        return akbps;
    }

    public void setAkbps(double akbps) {
        this.akbps = akbps;
    }

    public double getVkbps() {
        return vkbps;
    }

    public void setVkbps(double vkbps) {
        this.vkbps = vkbps;
    }

    public int getRxQuality() {
        return rxQuality;
    }

    public void setRxQuality(int rxQuality) {
        this.rxQuality = rxQuality;
    }

    public int getDownLostRate() {
        return downLostRate;
    }

    public void setDownLostRate(int downLostRate) {
        this.downLostRate = downLostRate;
    }

    public double getFps() {
        return fps;
    }

    public void setFps(double fps) {
        this.fps = fps;
    }

    public double getAfps() {
        return afps;
    }

    public void setAfps(double afps) {
        this.afps = afps;
    }
}
