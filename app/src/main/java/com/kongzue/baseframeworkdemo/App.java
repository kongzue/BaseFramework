package com.kongzue.baseframeworkdemo;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.util.Log;
import android.widget.Toast;

import com.kongzue.baseframework.BaseApp;
import com.kongzue.baseframework.BaseFrameworkSettings;
import com.kongzue.baseframework.interfaces.OnSDKInitializedCallBack;
import com.kongzue.baseframework.util.Preferences;
import com.kongzue.baseframeworkdemo.activity.ResponseActivity;

/**
 * Author: @Kongzue
 * Github: https://github.com/kongzue/
 * Homepage: http://kongzue.com/
 * Mail: myzcxhh@live.cn
 * CreateTime: 2018/9/30 04:12
 */
public class App extends BaseApp<App> {
    
    @Override
    public void init() {
        setOnSDKInitializedCallBack(new OnSDKInitializedCallBack() {
            @Override
            public void onInitialized() {
                log("onInitialized: ");
                Toast.makeText(me, "SDK已加载完毕", Toast.LENGTH_LONG).show();
            }
        });
    }
    
    @Override
    public void initSDKs() {
        BaseFrameworkSettings.DEBUGMODE = true;
        BaseFrameworkSettings.BETA_PLAN = true;
        try {
            Thread.sleep(8000);
        }catch (Exception e){}
        
        Preferences.getInstance().set(me,"test","testB",true);
        String test = Preferences.getInstance().getString(me,"test","testB");
        log("testB:" + test);
    }
}
