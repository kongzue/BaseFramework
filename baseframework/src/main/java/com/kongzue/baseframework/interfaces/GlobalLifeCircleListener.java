package com.kongzue.baseframework.interfaces;

import com.kongzue.baseframework.BaseActivity;

/**
 * Author: @Kongzue
 * Github: https://github.com/kongzue/
 * Homepage: http://kongzue.com/
 * Mail: myzcxhh@live.cn
 * CreateTime: 2018/9/1 15:44
 */
public interface GlobalLifeCircleListener {
    
    void onCreate(BaseActivity me, String className);
    
    void onResume(BaseActivity me, String className);
    
    void onPause(BaseActivity me, String className);
    
    void onDestroy(BaseActivity me, String className);
    
}
