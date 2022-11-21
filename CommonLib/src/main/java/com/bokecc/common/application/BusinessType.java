package com.bokecc.common.application;

/**
 * BusinessType
 * @author Zhang
 */
@Deprecated
public enum BusinessType {
    /**
     * 直播
     */
    LIVE(2001),
    /**
     * 点播
     */
    VOD(1001),
    /**
     * 班课
     */
    CLASS(3001),
    /**
     * 默认
     */
    DEFAULT(0);

    BusinessType(int i) {

    }
}
