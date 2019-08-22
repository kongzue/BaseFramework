package com.kongzue.baseframework.interfaces;

import com.kongzue.baseframework.BaseActivity;

/**
 * Author: @Kongzue
 * Github: https://github.com/kongzue/
 * Homepage: http://kongzue.com/
 * Mail: myzcxhh@live.cn
 * CreateTime: 2018/9/1 15:44
 */
public abstract class GlobalLifeCircleListener {
    
    public abstract void onCreate(BaseActivity me, String className);
    
    public abstract void onResume(BaseActivity me, String className);
    
    public abstract void onPause(BaseActivity me, String className);
    
    public abstract void onDestroy(BaseActivity me, String className);
    
    public void WindowFocus(BaseActivity me, String className, boolean hasFocus) {
    
    }
}