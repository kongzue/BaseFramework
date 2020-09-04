package com.kongzue.baseframeworkdemo;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import com.kongzue.baseframework.BaseActivity;
import com.kongzue.baseframework.BaseApp;
import com.kongzue.baseframework.BaseFrameworkSettings;
import com.kongzue.baseframework.interfaces.OnBugReportListener;
import com.kongzue.baseframework.interfaces.OnSDKInitializedCallBack;
import com.kongzue.baseframework.util.AppManager;
import com.kongzue.baseframework.util.SettingsUtil;
import com.kongzue.baseframeworkdemo.util.User;

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

//        Bitmap bm = null;       //随便写的 Demo
//        App.cache.set("key","value");
//        App.cache.set("bitmap",bm);
//        App.cache.clean();
        
        User user = new User("张三", 18, "192.168.1.1");
        App.user.set("userInfo", user);
    }
    
    public static USER user = new USER();
    
    public static class USER extends SettingsUtil {
        
        public USER() {
            super("user");
        }
    }
    
    public static CACHE cache;
    
    public static class CACHE extends SettingsUtil {
        
        public CACHE() {
            super("cache");
        }
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
