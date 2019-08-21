package com.kongzue.baseframeworkdemo;

import android.app.Application;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.kongzue.baseframework.BaseActivity;
import com.kongzue.baseframework.BaseFrameworkSettings;
import com.kongzue.baseframework.interfaces.OnBugReportListener;
import com.kongzue.baseframework.util.AppManager;

import java.io.File;

/**
 * Author: @Kongzue
 * Github: https://github.com/kongzue/
 * Homepage: http://kongzue.com/
 * Mail: myzcxhh@live.cn
 * CreateTime: 2018/9/30 04:12
 */
public class App extends Application {
    
    @Override
    public void onCreate() {
        super.onCreate();
        
        BaseFrameworkSettings.DEBUGMODE = true;
        BaseFrameworkSettings.BETA_PLAN = true;
    }
}
