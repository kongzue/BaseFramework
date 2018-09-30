package com.kongzue.baseframeworkdemo;

import android.app.Application;
import android.util.Log;

import com.kongzue.baseframework.BaseFrameworkSettings;
import com.kongzue.baseframework.interfaces.OnBugReportListener;

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
        
        BaseFrameworkSettings.turnOnReadErrorInfoPermissions(this, new OnBugReportListener() {
            @Override
            public void onReporter(File file) {
                Log.i(">>>", "onReporter: "+file.getAbsolutePath());
            }
        });
    }
}
