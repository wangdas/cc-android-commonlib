package com.bokecc.common.http;

import com.bokecc.common.utils.Tools;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * header类
 *
 * @author wangyue
 */
public class RequestHeaders {

    /**
     * 获取header
     *
     * @return header
     */
    public static Map<String, String> getHeaders() {
        Map<String, String> map = new HashMap<>(16);
        try {
            //包名
            String packageName = Tools.getPackageName();
            //系统版本号
            String sv = "";
            try {
                sv = URLEncoder.encode(Tools.getSystemVersion(), "utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            //手机型号
            String pm = "";
            try {
                pm = URLEncoder.encode(Tools.getPhoneModel(), "utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            //屏幕尺寸
            String ss = Tools.getScreenWidth() + "*" + Tools.getScreenHeight();
            map.put("packageName", packageName);
            map.put("sv", sv);
            map.put("pm", pm);
            map.put("ss", ss);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }

}
