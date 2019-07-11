package com.kongzue.baseframework.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Kongzue on 2017/3/28.
 * Version 2.0
 * Update 2018.5.27
 */

public class Preferences {

    //本类采用单例设计模式，请使用getInstance()获取本类对象后进行使用
    private static Preferences preferences;

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

    //读取属性为String类型
    //参数：context上下文索引，path路径，preferencesName属性名
    
    public String getString(Context context, String path, String preferencesName){
        return getString(context,path,preferencesName,"");
    }
    
    public boolean getBoolean(Context context, String path, String preferencesName){
        return getBoolean(context,path,preferencesName,false);
    }
    
    public int getInt(Context context, String path, String preferencesName){
        return getInt(context,path,preferencesName,0);
    }
    
    public String getString(Context context, String path, String preferencesName,String defaultValue){
        SharedPreferences preferences = context.getSharedPreferences(path, Context.MODE_PRIVATE);
        String value = preferences.getString(preferencesName, defaultValue);
        return value;
    }

    public boolean getBoolean(Context context, String path, String preferencesName,boolean defaultValue){
        SharedPreferences preferences = context.getSharedPreferences(path, Context.MODE_PRIVATE);
        boolean value = preferences.getBoolean(preferencesName, defaultValue);
        return value;
    }

    public int getInt(Context context, String path, String preferencesName,int defaultValue){
        SharedPreferences preferences = context.getSharedPreferences(path, Context.MODE_PRIVATE);
        int value = preferences.getInt(preferencesName, defaultValue);
        return value;
    }

    //写入属性方法
    //参数：context上下文索引，path路径，preferencesName属性名，value根据属性数据类型定义
    public void set(Context context, String path, String preferencesName,String value){
        SharedPreferences preferences = context.getSharedPreferences(path, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(preferencesName, value);
        editor.apply();
    }

    public void set(Context context, String path, String preferencesName,boolean value){
        SharedPreferences preferences = context.getSharedPreferences(path, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(preferencesName, value);
        editor.apply();
    }

    public void set(Context context, String path, String preferencesName,int value){
        SharedPreferences preferences = context.getSharedPreferences(path, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(preferencesName, value);
        editor.apply();
    }
    
    public void commit(Context context, String path, String preferencesName,String value){
        SharedPreferences preferences = context.getSharedPreferences(path, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(preferencesName, value);
        editor.commit();
    }
    
    public void commit(Context context, String path, String preferencesName,boolean value){
        SharedPreferences preferences = context.getSharedPreferences(path, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(preferencesName, value);
        editor.commit();
    }
    
    public void commit(Context context, String path, String preferencesName,int value){
        SharedPreferences preferences = context.getSharedPreferences(path, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(preferencesName, value);
        editor.commit();
    }

    //清除（清空）所有属性的方法
    public void cleanAll(Context context, String path){
        SharedPreferences sp=context.getSharedPreferences(path,Context.MODE_PRIVATE);
        if(sp!=null) {
            sp.edit().clear().apply();
        }
    }

}