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
    public void onCreate(BaseActivity me, String className){}
    public void onResume(BaseActivity me, String className){}
    public void onPause(BaseActivity me, String className){}
    public void onDestroy(BaseActivity me, String className){}
    public void windowFocus(BaseActivity me, String className, boolean hasFocus) { }
    public void onStart(BaseActivity activity, String className){}
    public void onStop(BaseActivity activity, String className){}
}
