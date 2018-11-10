package com.kongzue.baseframework.util;

import android.app.Application;
import android.content.Context;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * LoGG 此类为日志活动监控类，负责记录活动日志，包含设备信息、Activity基本生命周期、使用log(...)语句输出的、使用toast(...)语句建立提示的、以及崩溃信息
 * <p>
 * Author: @Kongzue
 * Github: https://github.com/kongzue/
 * Homepage: http://kongzue.com/
 * Mail: myzcxhh@live.cn
 * CreateTime: 2018/9/30 03:38
 */
public class DebugLogG {
    
    private static File logFile;
    private static FileWriter logWriter;
    
    public static void LogG(Context context, String s) {
        try {
            if (logFile == null) {
                createWriter(context);
            }
            logWriter = new FileWriter(logFile, true);
            logWriter.write(s + "\n");
        } catch (Exception e) {
        
        } finally {
            try {
                logWriter.close();
            } catch (Exception e) {
            }
        }
    }
    
    public static void createWriter(Context context) {
        try {
            logFile = new File(context.getExternalCacheDir(), System.currentTimeMillis() + ".bfl");
            logWriter = new FileWriter(logFile, true);
            
            logWriter.write("App.Start===============" +
                                    "\npackageName>>>" + context.getPackageName() +
                                    "\nappVer>>>" + context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName + "(" + context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode + ")" +
                                    "\nmanufacturer>>>" + android.os.Build.BRAND.toLowerCase() +
                                    "\nmodel>>>" + android.os.Build.MODEL.toLowerCase() +
                                    "\nos-ver>>>" + android.os.Build.VERSION.RELEASE.toLowerCase() +
                                    "\nandroidId>>>" + android.provider.Settings.System.getString(context.getContentResolver(), android.provider.Settings.System.ANDROID_ID) +
                                    "\n\nLog.Start===============\n"
            );
            logWriter.close();
        } catch (Exception e) {
        
        }
    }
    
    public static void catchException(Context context, Thread t, Throwable e) {
        if (logFile == null) {
            createWriter(context);
        }
        Preferences.getInstance().set(context, "cache", "bugReporterFile", logFile.getAbsolutePath());
        LogG(context, "\nError>>>\n" + getExceptionInfo(e));
    }
    
    public static String getExceptionInfo(Throwable e) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw, true);
        e.printStackTrace(pw);
        pw.flush();
        sw.flush();
        return sw.toString();
    }
}
