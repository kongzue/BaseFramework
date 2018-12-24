package com.kongzue.baseframework;

import android.content.Context;
import android.util.Log;

import com.kongzue.baseframework.interfaces.OnBugReportListener;
import com.kongzue.baseframework.util.DebugLogG;
import com.kongzue.baseframework.util.Preferences;

import java.io.File;
import java.util.Locale;

/**
 * Author: @Kongzue
 * Github: https://github.com/kongzue/
 * Homepage: http://kongzue.com/
 * Mail: myzcxhh@live.cn
 * CreateTime: 2018/9/30 03:22
 */
public class BaseFrameworkSettings {
    
    public static final String LINE_SEPARATOR = System.getProperty("line.separator");
    
    private static OnBugReportListener onBugReportListener;
    
    //是否开启debug模式，此开关影响打印Log日志等行为
    public static boolean DEBUGMODE = true;
    
    //是否开启beta计划，详情请参阅 https://github.com/kongzue/BaseFramework
    public static boolean BETA_PLAN = false;
    
    //语言设置
    public static Locale selectLocale;
    
    //设置开启崩溃监听
    public static void turnOnReadErrorInfoPermissions(Context context, OnBugReportListener listener) {
        onBugReportListener = listener;
        String reporterFile = Preferences.getInstance().getString(context, "cache", "bugReporterFile");
        if (reporterFile != null && !reporterFile.isEmpty()) {
            onBugReportListener.onReporter(new File(reporterFile));
            Preferences.getInstance().set(context, "cache", "bugReporterFile", "");
        }
        
        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread t, Throwable e) {
                DebugLogG.catchException(context, t, e);
                showInfo(e);
                android.os.Process.killProcess(android.os.Process.myPid());
            }
        });
    }
    
    private static void showInfo(Throwable e) {
        if (!DEBUGMODE) return;
        String errorInfo = DebugLogG.getExceptionInfo(e);
        String[] lines = errorInfo.split(LINE_SEPARATOR);
        for (String line : lines) {
            Log.e(">>>>>>", line);
        }
    }
    
    public static boolean setNavigationBarHeightZero = false;
}
