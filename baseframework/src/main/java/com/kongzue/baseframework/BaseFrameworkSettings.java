package com.kongzue.baseframework;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.kongzue.baseframework.interfaces.OnBugReportListener;
import com.kongzue.baseframework.util.DebugLogG;
import com.kongzue.baseframework.util.JsonFormat;
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
    
    private static OnBugReportListener onBugReportListener;
    
    //是否开启debug模式，此开关影响打印Log日志等行为
    public static boolean DEBUGMODE = true;
    
    //是否开启beta计划，详情请参阅 https://github.com/kongzue/BaseFramework
    public static boolean BETA_PLAN = false;
    
    //语言设置
    public static Locale selectLocale;
    
    private static boolean running = true;
    
    //设置开启崩溃监听
    public static void turnOnReadErrorInfoPermissions(Context context, OnBugReportListener listener) {
        onBugReportListener = listener;
        String reporterFile = Preferences.getInstance().getString(context, "cache", "bugReporterFile");
        if (reporterFile != null && !reporterFile.isEmpty()) {
            onBugReportListener.onReporter(new File(reporterFile));
            Preferences.getInstance().commit(context, "cache", "bugReporterFile", "");
        }
        
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                while (running) {
                    try {
                        Looper.loop();
                    } catch (Throwable e) {
                        DebugLogG.catchException(e);
                        if (onBugReportListener != null) {
                            onBugReportListener.onReporter(new File(reporterFile));
                            if (onBugReportListener.onCrash(new Exception(e), new File(reporterFile))){
                                exitApp();
                            }
                        }
                    }
                }
            }
        });
        
        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread t, Throwable e) {
                new Thread() {
                    @Override
                    public void run() {
                        DebugLogG.catchException(e);
                        
                        if (onBugReportListener != null) {
                            Looper.prepare();
                            onBugReportListener.onReporter(new File(reporterFile));
                            if (onBugReportListener.onCrash(new Exception(e), new File(reporterFile))){
                                exitApp();
                            }
                            Looper.loop();
                        }
                        try {
                            Thread.sleep(3000);
                        } catch (InterruptedException ae) {
                            ae.printStackTrace();
                        }
                        super.run();
                    }
                }.start();
            }
        });
    }
    
    public static boolean setNavigationBarHeightZero = false;
    
    public static void log(Object s) {
        if (DEBUGMODE) {
            String logStr = String.valueOf(s);
            if (logStr.length() > 2048) {
                bigLog(logStr);
            } else {
                if (!JsonFormat.formatJson(logStr)) {
                    Log.v(">>>>>>", logStr);
                }
            }
        }
    }
    
    public static void bigLog(String msg) {
        Log.i(">>>bigLog", "BIGLOG.start=================================");
        if (isNull(msg)) {
            return;
        }
        int strLength = msg.length();
        int start = 0;
        int end = 2000;
        for (int i = 0; i < 100; i++) {
            //剩下的文本还是大于规定长度则继续重复截取并输出
            if (strLength > end) {
                Log.v(">>>", msg.substring(start, end));
                start = end;
                end = end + 2000;
            } else {
                Log.v(">>>", msg.substring(start, strLength));
                break;
            }
        }
        Log.i(">>>bigLog", "BIGLOG.end=================================");
    }
    
    private static boolean isNull(String s) {
        if (s == null || s.trim().isEmpty() || "null".equals(s) || "(null)".equals(s)) {
            return true;
        }
        return false;
    }
    
    public static void exitApp() {
        android.os.Process.killProcess(android.os.Process.myPid());
    }
}