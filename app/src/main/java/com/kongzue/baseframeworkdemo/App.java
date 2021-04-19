package com.kongzue.baseframeworkdemo;

import android.content.DialogInterface;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.kongzue.baseframework.BaseApp;
import com.kongzue.baseframework.BaseFrameworkSettings;
import com.kongzue.baseframework.interfaces.OnBugReportListener;
import com.kongzue.baseframework.interfaces.OnSDKInitializedCallBack;
import com.kongzue.baseframework.util.AppManager;

import java.io.File;

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
        
        setOnCrashListener(new OnBugReportListener() {
            @Override
            public boolean onCrash(Exception e, final File crashLogFile) {
                if (AppManager.getInstance().getActiveActivity() == null || !AppManager.getInstance().getActiveActivity().isActive) {
                    return false;
                }
                AlertDialog.Builder builder = new AlertDialog.Builder(AppManager.getInstance().getActiveActivity());
                builder.setTitle("Ops！发生了一次崩溃！");
                builder.setMessage("您是否愿意帮助我们改进程序以修复此Bug？");
                builder.setPositiveButton("愿意", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        toast("请对file进行处理：" + crashLogFile.getAbsolutePath());
                    }
                });
                builder.setNegativeButton("不了", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    
                    }
                });
                builder.setCancelable(false);
                AlertDialog dialog = builder.create();
                dialog.show();
                
                return false;
            }
        });
        
        int[] a = {1, 3, 5, 7, 9};
        log(a);
        CharSequence[] s = {"s", "b", "t", "m"};
        log(s);
    }
    
    @Override
    public void initSDKs() {
        BaseFrameworkSettings.DEBUGMODE = true;
        BaseFrameworkSettings.BETA_PLAN = true;
        try {
            Thread.sleep(8000);
        } catch (Exception e) {
        }
    }
}
