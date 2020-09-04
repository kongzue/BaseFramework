package com.kongzue.baseframework.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

/**
 * 本类采用单例设计模式，请使用getInstance()获取本类对象后进行使用
 * 本类在最新版本已废弃，建议直接使用 BaseApp.Settings 作为数据存储实现
 *
 * Created by Kongzue on 2017/3/28.
 * Version 3.0
 * Update 2020.1.15
 */

@Deprecated
public class Preferences {
    
    private static Preferences preferences;
    private static ChangeSharedPreferencesPathCallBack changeSharedPreferencesPathCallBack;
    
    private Preferences() {
    }
    
    public static Preferences getInstance() {
        if (preferences == null) {
            synchronized (Preferences.class) {
                if (preferences == null) {
                    preferences = new Preferences();
                }
            }
        }
        return preferences;
    }
    
    /**
     * 读取属性为String类型
     *
     * @param context         上下文索引
     * @param path            路径
     * @param preferencesName 属性名
     * @return String
     */
    public String getString(Context context, String path, String preferencesName) {
        return getString(context, path, preferencesName, "");
    }
    
    /**
     * 读取属性为Boolean类型
     *
     * @param context         上下文索引
     * @param path            路径
     * @param preferencesName 属性名
     * @return Boolean
     */
    public boolean getBoolean(Context context, String path, String preferencesName) {
        return getBoolean(context, path, preferencesName, false);
    }
    
    /**
     * 读取属性为Int类型
     *
     * @param context         上下文索引
     * @param path            路径
     * @param preferencesName 属性名
     * @return Int
     */
    public int getInt(Context context, String path, String preferencesName) {
        return getInt(context, path, preferencesName, 0);
    }
    
    /**
     * 读取属性为Int类型
     *
     * @param context         上下文索引
     * @param path            路径
     * @param preferencesName 属性名
     * @param defaultValue    默认值
     * @return Int
     */
    public int getInt(Context context, String path, String preferencesName, int defaultValue) {
        int value = initSharedPreferences(context, path).getInt(preferencesName, defaultValue);
        return value;
    }
    
    /**
     * 读取属性为String类型
     *
     * @param context         上下文索引
     * @param path            路径
     * @param preferencesName 属性名
     * @param defaultValue    默认值
     * @return String
     */
    public String getString(Context context, String path, String preferencesName, String defaultValue) {
        String value = initSharedPreferences(context, path).getString(preferencesName, defaultValue);
        return value;
    }
    
    /**
     * 读取属性为Boolean类型
     *
     * @param context         上下文索引
     * @param path            路径
     * @param preferencesName 属性名
     * @param defaultValue    默认值
     * @return Boolean
     */
    public boolean getBoolean(Context context, String path, String preferencesName, boolean defaultValue) {
        boolean value = initSharedPreferences(context, path).getBoolean(preferencesName, defaultValue);
        return value;
    }
    
    public float getFloat(Context context, String path, String preferencesName) {
        return getFloat(context, path, preferencesName, 0);
    }
    
    public float getFloat(Context context, String path, String preferencesName, float defaultValue) {
        float value = initSharedPreferences(context, path).getFloat(preferencesName, defaultValue);
        return value;
    }
    
    public long getLong(Context context, String path, String preferencesName) {
        return getLong(context, path, preferencesName, 0);
    }
    
    public long getLong(Context context, String path, String preferencesName, long defaultValue) {
        long value = initSharedPreferences(context, path).getLong(preferencesName, defaultValue);
        return value;
    }
    
    public double getDouble(Context context, String path, String preferencesName) {
        return getDouble(context, path, preferencesName, 0);
    }
    
    public double getDouble(Context context, String path, String preferencesName, double defaultValue) {
        String value = initSharedPreferences(context, path).getString(preferencesName, String.valueOf(defaultValue));
        if (value != null) {
            try {
                return Double.parseDouble(value);
            } catch (Exception e) {
                return defaultValue;
            }
        } else {
            return defaultValue;
        }
    }
    
    /**
     * 写入String属性方法，占用资源较少，但不会立即生效
     *
     * @param context         上下文索引
     * @param path            路径
     * @param preferencesName 属性名
     * @param value           值
     * @return none
     */
    public void set(Context context, String path, String preferencesName, String value) {
        SharedPreferences.Editor editor = initSharedPreferences(context, path).edit();
        editor.putString(preferencesName, value);
        editor.apply();
    }
    
    /**
     * 写入Boolean属性方法，占用资源较少，但不会立即生效
     *
     * @param context         上下文索引
     * @param path            路径
     * @param preferencesName 属性名
     * @param value           值
     * @return none
     */
    public void set(Context context, String path, String preferencesName, boolean value) {
        SharedPreferences.Editor editor = initSharedPreferences(context, path).edit();
        editor.putBoolean(preferencesName, value);
        editor.apply();
    }
    
    /**
     * 写入Int属性方法，占用资源较少，但不会立即生效
     *
     * @param context         上下文索引
     * @param path            路径
     * @param preferencesName 属性名
     * @param value           值
     * @return none
     */
    public void set(Context context, String path, String preferencesName, int value) {
        SharedPreferences.Editor editor = initSharedPreferences(context, path).edit();
        editor.putInt(preferencesName, value);
        editor.apply();
    }
    
    /**
     * 写入float属性方法，占用资源较少，但不会立即生效
     *
     * @param context         上下文索引
     * @param path            路径
     * @param preferencesName 属性名
     * @param value           值
     * @return none
     */
    public void set(Context context, String path, String preferencesName, float value) {
        SharedPreferences.Editor editor = initSharedPreferences(context, path).edit();
        editor.putFloat(preferencesName, value);
        editor.apply();
    }
    
    /**
     * 写入double属性方法，占用资源较少，但不会立即生效
     *
     * @param context         上下文索引
     * @param path            路径
     * @param preferencesName 属性名
     * @param value           值
     * @return none
     */
    public void set(Context context, String path, String preferencesName, double value) {
        SharedPreferences.Editor editor = initSharedPreferences(context, path).edit();
        editor.putString(preferencesName, String.valueOf(value));
        editor.apply();
    }
    
    /**
     * 写入long属性方法，占用资源较少，但不会立即生效
     *
     * @param context         上下文索引
     * @param path            路径
     * @param preferencesName 属性名
     * @param value           值
     * @return none
     */
    public void set(Context context, String path, String preferencesName, long value) {
        SharedPreferences.Editor editor = initSharedPreferences(context, path).edit();
        editor.putLong(preferencesName, value);
        editor.apply();
    }
    
    /**
     * 写入String属性方法，立即生效
     *
     * @param context         上下文索引
     * @param path            路径
     * @param preferencesName 属性名
     * @param value           值
     * @return none
     */
    public void commit(Context context, String path, String preferencesName, String value) {
        SharedPreferences.Editor editor = initSharedPreferences(context, path).edit();
        editor.putString(preferencesName, value);
        editor.commit();
    }
    
    /**
     * 写入Boolean属性方法，立即生效
     *
     * @param context         上下文索引
     * @param path            路径
     * @param preferencesName 属性名
     * @param value           值
     * @return none
     */
    public void commit(Context context, String path, String preferencesName, boolean value) {
        SharedPreferences.Editor editor = initSharedPreferences(context, path).edit();
        editor.putBoolean(preferencesName, value);
        editor.commit();
    }
    
    /**
     * 写入Int属性方法，立即生效
     *
     * @param context         上下文索引
     * @param path            路径
     * @param preferencesName 属性名
     * @param value           值
     * @return none
     */
    public void commit(Context context, String path, String preferencesName, int value) {
        SharedPreferences.Editor editor = initSharedPreferences(context, path).edit();
        editor.putInt(preferencesName, value);
        editor.commit();
    }
    
    public void commit(Context context, String path, String preferencesName, float value) {
        SharedPreferences.Editor editor = initSharedPreferences(context, path).edit();
        editor.putFloat(preferencesName, value);
        editor.commit();
    }
    
    public void commit(Context context, String path, String preferencesName, double value) {
        SharedPreferences.Editor editor = initSharedPreferences(context, path).edit();
        editor.putString(preferencesName, String.valueOf(value));
        editor.commit();
    }
    
    public void commit(Context context, String path, String preferencesName, long value) {
        SharedPreferences.Editor editor = initSharedPreferences(context, path).edit();
        editor.putLong(preferencesName, value);
        editor.commit();
    }
    
    /**
     * 清除（清空）所有属性的方法
     *
     * @param context 上下文索引
     * @param path    路径
     * @return none
     */
    public void cleanAll(Context context, String path) {
        SharedPreferences sp = initSharedPreferences(context, path);
        if (sp != null) {
            sp.edit().clear().apply();
        }
    }
    
    private SharedPreferences initSharedPreferences(Context context, String path) {
        Log.w(">>>", "initSharedPreferences: " + context.getClass().getName() + " path=" + path);
        SharedPreferences sp;
        if (changeSharedPreferencesPathCallBack != null) {
            sp = changeSharedPreferencesPathCallBack.onPathChange(path);
        } else {
            sp = context.getApplicationContext().getSharedPreferences(path, Context.MODE_PRIVATE);
        }
        return sp;
    }
    
    /**
     * ChangeSharedPreferencesPathCallBack 是用于切换 SharedPreferences 路径的回调函数，
     * 请返回已切换至指定 Path 后的 SharedPreferences 对象。
     * 一般可使用以下代码：
     * return Preferences.getInstance().getSharedPreferences().getSharedPreferences(path, Context.MODE_PRIVATE);
     * <p>
     * 完成切换操作，实际可能需要根据 SharedPreferences 实例的实际情况决定。
     */
    public interface ChangeSharedPreferencesPathCallBack {
        SharedPreferences onPathChange(String path);
    }
    
    public static ChangeSharedPreferencesPathCallBack getChangeSharedPreferencesPathCallBack() {
        return changeSharedPreferencesPathCallBack;
    }
    
    public static void setChangeSharedPreferencesPathCallBack(ChangeSharedPreferencesPathCallBack l) {
        changeSharedPreferencesPathCallBack = l;
    }
}