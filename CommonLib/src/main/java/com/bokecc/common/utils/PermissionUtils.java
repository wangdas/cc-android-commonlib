package com.bokecc.common.utils;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;

public class PermissionUtils {

    private static String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};

    /**
     * 检测是集合权限是否开启
     *
     * @param ctx
     * @return true-已开启权限，false-没有权限
     */
    public static boolean permitPermissions(Context ctx) {
        for (String permission : permissions) {
            if (permitPermission(ctx, permission)) {
                return true;
            }
        }
        return false;
    }


    public static boolean permitPermission(Context ctx, String permission) {
        if (ctx == null) return false;
        if (Build.VERSION.SDK_INT >= 23) {
            return ctx.checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED;
        } else {
            return true;
        }
    }

}