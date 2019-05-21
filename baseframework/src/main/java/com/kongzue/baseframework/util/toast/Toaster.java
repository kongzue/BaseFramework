package com.kongzue.baseframework.util.toast;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import androidx.core.app.NotificationManagerCompat;
import android.widget.Toast;

import static com.kongzue.baseframework.BaseActivity.isMIUI;

public class Toaster {
    
    public static boolean isSupportToast = false;
    public static int DURATION = Toast.LENGTH_SHORT;
    public static BaseToast nowToast;
    
    public static BaseToast build(Context context, int DURATION) {
        Toaster.DURATION = DURATION;
        return build(context);
    }
    
    public static BaseToast build(Context context) {
        if (context == null) return null;
        if (NotificationManagerCompat.from(context).areNotificationsEnabled() || isAndroidO() || isMIUI()) {
            //若开启了通知权限
            return new SystemToast(context);
        } else {
            if (context instanceof Activity) {
                //使用兼容模式的Toast
                isSupportToast = true;
                return new CompatToast(context);
            } else {
                //暂无可行的适配方案，尝试系统toast
                return new SystemToast(context);
            }
        }
    }
    
    public static void cancel() {
        if (nowToast != null) {
            nowToast.cancel();
            nowToast = null;
        }
    }
    
    public static boolean isAndroidO() {
        return Build.VERSION.SDK_INT == Build.VERSION_CODES.O || Build.VERSION.SDK_INT == Build.VERSION_CODES.O_MR1;
    }
}
