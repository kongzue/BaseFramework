package com.kongzue.baseframework.util;

import android.util.Log;

import com.kongzue.baseframework.BaseApp;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.kongzue.baseframework.BaseApp.isNull;
import static com.kongzue.baseframework.BaseFrameworkSettings.BETA_PLAN;
import static com.kongzue.baseframework.BaseFrameworkSettings.DEBUGMODE;

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
    
    public static final String LINE_SEPARATOR = System.getProperty("line.separator");
    
    private static File logFile;
    private static FileWriter logWriter;
    
    public static void LogI(Object obj) {
        try {
            if (DEBUGMODE) {
                if (obj instanceof Map) {
                    Log.v(">>>>>>", "Map: " + obj.getClass().getName() + "@" + Integer.toHexString(obj.hashCode()) + " {\n");
                    Set keySet = ((Map) obj).keySet();
                    for (Object key : keySet) {
                        String keyStr = String.valueOf(key);
                        String valueStr = String.valueOf(((Map) obj).get(key));
                        Log.v(">>>>>>", "    \"" + keyStr + "\": \"" + valueStr + "\",");
                    }
                    Log.v(">>>>>>", "}");
                } else if (obj instanceof List) {
                    List list = (List) obj;
                    Log.v(">>>>>>", "List: " + obj.getClass().getName() + "@" + Integer.toHexString(obj.hashCode()) + " [\n");
                    for (Object value : list) {
                        Log.v(">>>>>>", "    " + value + ",");
                    }
                    Log.v(">>>>>>", "]");
                } else {
                    String msg = String.valueOf(obj);
                    if (isNull(msg)) {
                        return;
                    }
                    if (obj.toString().length() > 2048) {
                        bigLog(msg, false);
                    } else {
                        LogG(msg);
                        if (!JsonFormat.formatJson(msg)) {
                            Log.v(">>>>>>", msg);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static void LogE(Object obj) {
        try {
            if (DEBUGMODE) {
                if (obj instanceof Map) {
                    Log.e(">>>>>>", "Map: " + obj.getClass().getName() + "@" + Integer.toHexString(obj.hashCode()) + " {\n");
                    Set keySet = ((Map) obj).keySet();
                    for (Object key : keySet) {
                        String keyStr = String.valueOf(key);
                        String valueStr = String.valueOf(((Map) obj).get(key));
                        Log.e(">>>>>>", "    \"" + keyStr + "\": \"" + valueStr + "\",");
                    }
                    Log.e(">>>>>>", "}");
                } else if (obj instanceof List) {
                    List list = (List) obj;
                    Log.e(">>>>>>", "List: " + obj.getClass().getName() + "@" + Integer.toHexString(obj.hashCode()) + " [\n");
                    for (Object value : list) {
                        Log.e(">>>>>>", "    " + value + ",");
                    }
                    Log.e(">>>>>>", "]");
                } else {
                    String msg = String.valueOf(obj);
                    if (isNull(msg)) {
                        return;
                    }
                    if (obj.toString().length() > 2048) {
                        bigLog(msg, false);
                    } else {
                        LogG(msg);
                        if (!JsonFormat.formatJson(msg)) {
                            Log.e(">>>>>>", msg);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static void bigLog(String msg, boolean error) {
        Log.i(">>>bigLog", "BIGLOG.start=================================");
        if (isNull(msg)) {
            return;
        }
        LogG(msg);
        int strLength = msg.length();
        int start = 0;
        int end = 2000;
        for (int i = 0; i < 100; i++) {
            //剩下的文本还是大于规定长度则继续重复截取并输出
            if (strLength > end) {
                if (error) {
                    Log.e(">>>", msg.substring(start, end));
                } else {
                    Log.v(">>>", msg.substring(start, end));
                }
                start = end;
                end = end + 2000;
            } else {
                if (error) {
                    Log.e(">>>", msg.substring(start, strLength));
                } else {
                    Log.v(">>>", msg.substring(start, strLength));
                }
                break;
            }
        }
        Log.i(">>>bigLog", "BIGLOG.end=================================");
    }
    
    public static void LogG(String s) {
        if (!BETA_PLAN) {
            return;
        }
        try {
            if (logFile == null) {
                createWriter();
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
    
    public static void catchException(Throwable e) {
        if (logFile == null) {
            createWriter();
        }
        if (BaseApp.getPrivateInstance() != null) {
            if (logFile == null) {
                createWriter();
            }
            Preferences.getInstance().commit(BaseApp.getPrivateInstance(), "cache", "bugReporterFile", logFile.getAbsolutePath());
        }
        showInfo(e);
    }
    
    private static void showInfo(Throwable e) {
        if (!DEBUGMODE) {
            return;
        }
        String errorInfo = DebugLogG.getExceptionInfo(e);
        String[] lines = errorInfo.split(LINE_SEPARATOR);
        for (String line : lines) {
            Log.e(">>>>>>", line);
        }
    }
    
    public static void createWriter() {
        try {
            logFile = new File(BaseApp.getPrivateInstance().getCacheDir(), System.currentTimeMillis() + ".bfl");
            logWriter = new FileWriter(logFile, true);
            
            logWriter.write("BaseApp.Start===============" +
                    "\npackageName>>>" + BaseApp.getPrivateInstance().getPackageName() +
                    "\nappVer>>>" + BaseApp.getPrivateInstance().getPackageManager().getPackageInfo(BaseApp.getPrivateInstance().getPackageName(), 0).versionName + "(" + BaseApp.getPrivateInstance().getPackageManager().getPackageInfo(BaseApp.getPrivateInstance().getPackageName(), 0).versionCode + ")" +
                    "\nmanufacturer>>>" + android.os.Build.BRAND.toLowerCase() +
                    "\nmodel>>>" + android.os.Build.MODEL.toLowerCase() +
                    "\nos-ver>>>" + android.os.Build.VERSION.RELEASE.toLowerCase() +
                    "\nandroidId>>>" + android.provider.Settings.System.getString(BaseApp.getPrivateInstance().getContentResolver(), android.provider.Settings.System.ANDROID_ID) +
                    "\n\nLog.Start===============\n"
            );
            logWriter.close();
        } catch (Exception e) {
        
        }
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
