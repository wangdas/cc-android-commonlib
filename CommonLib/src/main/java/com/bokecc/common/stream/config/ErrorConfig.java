package com.bokecc.common.stream.config;

/**
 * 错误状态码
 */
public class ErrorConfig {

    /**zego房间未初始化*/
    public static final int cc_zego_no_init_room_error = 1000;
    /**加入zego房间失败*/
    public static final int cc_join_zego_room_error = 1001;

    /**加入agora房间失败*/
    public static final int cc_agora_join_room_error = 2001;
    /**加入agora房间失败*/
    public static final int cc_agora_leave_room_error = 2002;

    /**加入atlas房间失败*/
    public static final int  cc_atlas_join_room_error = 3001;
    /**离开atlas房间失败*/
    public static final int  cc_atlas_leave_room_error = 3002;

    /**加入trtc房间失败*/
    public static final int cc_join_trtc_room_error = 4001;
}
