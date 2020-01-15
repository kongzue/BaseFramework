package com.kongzue.baseframework.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * 本类采用单例设计模式，请使用getInstance()获取本类对象后进行使用
 * Created by Kongzue on 2017/3/28.
 * Version 3.0
 * Update 2020.1.15
 */

public class Preferences {
    
    private static Preferences preferences;
    private SharedPreferences sp;
    private ChangeSharedPreferencesPathCallBack changeSharedPreferencesPathCallBack;
    
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
     * 读取属性为String类型
     *
     * @param context         上下文索引
     * @param path            路径
     * @param preferencesName 属性名
     * @param defaultValue    默认值
     * @return String
     */
    public String getString(Context context, String path, String preferencesName, String defaultValue) {
        initSharedPreferences(context, path);
        String value = sp.getString(preferencesName, defaultValue);
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
        initSharedPreferences(context, path);
        boolean value = sp.getBoolean(preferencesName, defaultValue);
        return value;
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
        initSharedPreferences(context, path);
        int value = sp.getInt(preferencesName, defaultValue);
        return value;
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
        initSharedPreferences(context, path);
        SharedPreferences.Editor editor = sp.edit();
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
        initSharedPreferences(context, path);
        SharedPreferences.Editor editor = sp.edit();
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
        initSharedPreferences(context, path);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt(preferencesName, value);
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
        initSharedPreferences(context, path);
        SharedPreferences.Editor editor = sp.edit();
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
        initSharedPreferences(context, path);
        SharedPreferences.Editor editor = sp.edit();
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
        initSharedPreferences(context, path);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt(preferencesName, value);
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
        initSharedPreferences(context, path);
        if (sp != null) {
            sp.edit().clear().apply();
        }
    }
    
    private void initSharedPreferences(Context context, String path) {
        if (sp == null) {
            sp = context.getApplicationContext().getSharedPreferences(path, Context.MODE_PRIVATE);
        } else {
            if (changeSharedPreferencesPathCallBack!=null) {
                sp = changeSharedPreferencesPathCallBack.onPathChange(path);
            }
        }
    }
    
    /**
     * 获取 SharedPreferences 实例
     * @return SharedPreferences实例对象
     */
    public SharedPreferences getSharedPreferences() {
        return sp;
    }
    
    /**
     * ChangeSharedPreferencesPathCallBack 是用于切换 SharedPreferences 路径的回调函数，
     * 请返回已切换至指定 Path 后的 SharedPreferences 对象。
     * 一般可使用以下代码：
     * return Preferences.getInstance().getSharedPreferences().getSharedPreferences(path, Context.MODE_PRIVATE);
     *
     * 完成切换操作，实际可能需要根据 SharedPreferences 实例的实际情况决定。
     *
     */
    public interface ChangeSharedPreferencesPathCallBack {
        SharedPreferences onPathChange(String path);
    }
    
    /**
     * 此方法已废弃，原因是原方法需要单独指定 ChangeSharedPreferencesPathCallBack。
     * ChangeSharedPreferencesPathCallBack 是用于切换 SharedPreferences 路径的回调函数，
     * 如果要自定义 SharedPreferences 实例，一定要指定 ChangeSharedPreferencesPathCallBack 切换方法，
     * 否则可能导致无法切换至指定 Path
     *
     * @param sp    自定义 SharedPreferences 实例
     * @return      继续执行其他方法
     */
    @Deprecated
    public Preferences initSharedPreferences(SharedPreferences sp) {
        this.sp = sp;
        return this;
    }
    
    /**
     * 初始化自定义 SharedPreferences 实例，要执行此方法请务必于开始任何读写操作前进行。
     * ChangeSharedPreferencesPathCallBack 是用于切换 SharedPreferences 路径的回调函数，
     * 如果要自定义 SharedPreferences 实例，一定要指定 ChangeSharedPreferencesPathCallBack 切换方法，
     * 否则可能导致无法切换至指定 Path
     *
     * @param sp                                        自定义 SharedPreferences 实例
     * @param changeSharedPreferencesPathCallBack       ChangeSharedPreferencesPathCallBack 回调
     * @return                                          继续执行其他方法
     */
    public Preferences initSharedPreferences(SharedPreferences sp, ChangeSharedPreferencesPathCallBack changeSharedPreferencesPathCallBack) {
        this.sp = sp;
        this.changeSharedPreferencesPathCallBack = changeSharedPreferencesPathCallBack;
        return this;
    }
    
    public ChangeSharedPreferencesPathCallBack getChangeSharedPreferencesPathCallBack() {
        return changeSharedPreferencesPathCallBack;
    }
    
    public Preferences setChangeSharedPreferencesPathCallBack(ChangeSharedPreferencesPathCallBack changeSharedPreferencesPathCallBack) {
        this.changeSharedPreferencesPathCallBack = changeSharedPreferencesPathCallBack;
        return this;
    }
}