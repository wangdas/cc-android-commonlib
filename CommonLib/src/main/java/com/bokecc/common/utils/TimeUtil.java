package com.bokecc.common.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 作者 ${CC视频}.<br/>
 */

public class TimeUtil {
    private TimeUtil() {
        throw new UnsupportedOperationException();
    }

    public static String format(long mSeconds) {
        if (mSeconds < 60)
            return mSeconds + "秒";
        if (mSeconds >= 60 && mSeconds < 60 * 60) {
            long m = mSeconds / 60;
            int s = (int) (mSeconds % 60);
            return s != 0 ? m + "分" + s + "秒" : m + "分钟";
        }
        return null;
    }

    public static String formatNamed(long mSeconds) {
        if (mSeconds < 60)
            return "00:" + (mSeconds < 10 ? "0" + mSeconds : "" + mSeconds);
        if (mSeconds >= 60 && mSeconds < 60 * 60) {
            long m = mSeconds / 60;
            int s = (int) (mSeconds % 60);
            return s != 0 ? m < 10 ? "0" + m + ":" + (s < 10 ? "0" + s : "" + s) : m + ":" + (s < 10 ? "0" + s : "" + s) :
                    m < 10 ? "0" + m + ":00" : m + ":00";
        }
        return null;
    }

    public static String formatNamed99(long mSeconds) {
        if (mSeconds < 60)
            return "00:" + (mSeconds < 10 ? "0" + mSeconds : "" + mSeconds);
        if (mSeconds >= 60) {
            long m = mSeconds / 60;
            int s = (int) (mSeconds % 60);
            return s != 0 ? m < 10 ? "0" + m + ":" + (s < 10 ? "0" + s : "" + s) : m + ":" + (s < 10 ? "0" + s : "" + s) : m + ":00";
        }
        return null;
    }
    /**
     * 获取时间段
     * @param time 时间
     * @return
     */
    public static String getTime(long time){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
        Date date = new Date(time);
        return simpleDateFormat.format(date);
    }
}
