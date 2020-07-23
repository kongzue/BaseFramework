package com.kongzue.baseframework;

import android.app.Application;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.WindowInsets;
import android.view.WindowManager;
import android.widget.Toast;

import com.kongzue.baseframework.interfaces.OnBugReportListener;
import com.kongzue.baseframework.interfaces.OnSDKInitializedCallBack;
import com.kongzue.baseframework.util.AppManager;
import com.kongzue.baseframework.util.DebugLogG;
import com.kongzue.baseframework.util.JsonFormat;
import com.kongzue.baseframework.util.Preferences;
import com.kongzue.baseframework.util.toast.Toaster;

import java.io.File;
import java.io.Serializable;
import java.lang.ref.WeakReference;
import java.lang.reflect.Field;

import static com.kongzue.baseframework.BaseFrameworkSettings.BETA_PLAN;
import static com.kongzue.baseframework.BaseFrameworkSettings.DEBUGMODE;
import static com.kongzue.baseframework.BaseFrameworkSettings.setNavigationBarHeightZero;

/**
 * @author: Kongzue
 * @github: https://github.com/kongzue/
 * @homepage: http://kongzue.com/
 * @mail: myzcxhh@live.cn
 * @createTime: 2019/9/23 12:43
 */
public abstract class BaseApp<YourApp extends BaseApp> extends Application {
    
    private Handler mainHandler = new Handler(Looper.getMainLooper());
    private OnSDKInitializedCallBack onSDKInitializedCallBack;
    
    private boolean isInitializedSDKs;
    
    public abstract void init();
    
    public void initSDKs() {
    }
    
    public void initSDKInitialized() {
    }
    
    public YourApp me;
    private static Application instance;
    
    @Override
    @Deprecated
    public void onCreate() {
        super.onCreate();
        
        me = (YourApp) this;
        instance = me;
        
        init();
        new Thread() {
            @Override
            public void run() {
                synchronized (me) {
                    isInitializedSDKs = false;
                    initSDKs();
                    
                    if (onSDKInitializedCallBack != null) {
                        mainHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                initSDKInitialized();
                                onSDKInitializedCallBack.onInitialized();
                                isInitializedSDKs = true;
                            }
                        });
                    }
                }
            }
        }.start();
    }
    
    public boolean isInitializedSDKs() {
        return isInitializedSDKs;
    }
    
    public BaseApp<YourApp> setOnSDKInitializedCallBack(OnSDKInitializedCallBack onSDKInitializedCallBack) {
        this.onSDKInitializedCallBack = onSDKInitializedCallBack;
        return this;
    }
    
    public void log(final Object obj) {
        DebugLogG.LogI(obj);
    }
    
    public void error(final Object obj) {
        DebugLogG.LogE(obj);
    }
    
    public void bigLog(String msg) {
        DebugLogG.bigLog(msg, false);
    }
    
    private void logG(String tag, Object o) {
        DebugLogG.LogG(tag + ">>>" + o.toString());
    }
    
    protected final static String NULL = "";
    private Toast toast;
    
    public void toast(final Object obj) {
        try {
            mainHandler.post(new Runnable() {
                @Override
                public void run() {
                    logG("toast", obj.toString());
                    if (toast == null) {
                        toast = Toast.makeText(BaseApp.this, NULL, Toast.LENGTH_SHORT);
                    }
                    toast.setText(obj.toString());
                    toast.show();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void toastS(final Object obj) {
        Toaster.build(me).show(obj.toString());
    }
    
    public void runOnMain(Runnable runnable) {
        mainHandler.post(new Runnable() {
            @Override
            public void run() {
                runnable.run();
            }
        });
    }
    
    public void runOnMainDelayed(Runnable runnable, long time) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                runOnMain(runnable);
            }
        }, time);
    }
    
    public void runDelayed(Runnable runnable, long time) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                runnable.run();
            }
        }, time);
    }
    
    //用于进行dip和px转换
    public int dip2px(float dpValue) {
        final float scale = getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
    
    //用于进行px和dip转换
    public int px2dip(float pxValue) {
        final float scale = getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }
    
    
    //启动当前应用设置页面
    public void startAppSettings() {
        Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:" + getPackageName()));
        startActivity(intent);
    }
    
    //获取状态栏的高度
    public int getStatusBarHeight() {
        try {
            Class<?> c = Class.forName("com.android.internal.R$dimen");
            Object obj = c.newInstance();
            Field field = c.getField("status_bar_height");
            int x = Integer.parseInt(field.get(obj).toString());
            return getResources().getDimensionPixelSize(x);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
    
    //获取屏幕宽度
    public int getDisplayWidth() {
        WindowManager windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        Display disp = windowManager.getDefaultDisplay();
        Point outP = new Point();
        disp.getSize(outP);
        return outP.x;
    }
    
    //获取屏幕可用部分高度（屏幕高度-状态栏高度-屏幕底栏高度）
    public int getDisplayHeight() {
        WindowManager windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        Display disp = windowManager.getDefaultDisplay();
        Point outP = new Point();
        disp.getSize(outP);
        return outP.y;
    }
    
    //获取底栏高度
    public int getNavbarHeight() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            try {
                WindowInsets windowInsets = null;
                windowInsets = AppManager.getInstance().currentActivity().getWindow().getDecorView().getRootView().getRootWindowInsets();
                if (windowInsets != null) {
                    return windowInsets.getStableInsetBottom();
                }
            } catch (Exception e) {
            }
        }
        int resourceId = 0;
        int rid = getResources().getIdentifier("config_showNavigationBar", "bool", "android");
        if (rid != 0) {
            resourceId = getResources().getIdentifier("navigation_bar_height", "dimen", "android");
            return getResources().getDimensionPixelSize(resourceId);
        } else {
            return 0;
        }
    }
    
    //获取真实的屏幕高度，注意判断非0
    public int getRootHeight() {
        WindowManager windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        int diaplayHeight = 0;
        Display display = windowManager.getDefaultDisplay();
        Point point = new Point();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            display.getRealSize(point);
            diaplayHeight = point.y;
        } else {
            DisplayMetrics dm = new DisplayMetrics();
            windowManager.getDefaultDisplay().getMetrics(dm);
            diaplayHeight = dm.heightPixels; //得到高度```
        }
        return diaplayHeight;
    }
    
    public boolean copy(String s) {
        if (isNull(s)) {
            log("要复制的文本为空");
            return false;
        }
        ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData mClipData = ClipData.newPlainText("Label", s);
        cm.setPrimaryClip(mClipData);
        return true;
    }
    
    public static boolean isNull(String s) {
        if (s == null || s.trim().isEmpty() || "null".equals(s) || "(null)".equals(s)) {
            return true;
        }
        return false;
    }
    
    //打开指定App
    public boolean openApp(String packageName) {
        PackageManager packageManager = getPackageManager();
        if (isInstallApp(packageName)) {
            try {
                Intent intent = packageManager.getLaunchIntentForPackage(packageName);
                startActivity(intent);
                return true;
            } catch (Exception e) {
                if (DEBUGMODE) {
                    e.printStackTrace();
                }
                return false;
            }
        } else {
            return false;
        }
    }
    
    //检测App是否已安装
    public boolean isInstallApp(String packageName) {
        PackageInfo packageInfo = null;
        try {
            packageInfo = getPackageManager().getPackageInfo(packageName, 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return packageInfo != null;
    }
    
    public static Settings Settings(String path) {
        return new Settings(path);
    }
    
    /**
     * 代理属性存储
     */
    public static class Settings {
        
        public static void init(com.kongzue.baseframework.util.Preferences.ChangeSharedPreferencesPathCallBack changeSharedPreferencesPathCallBack) {
            com.kongzue.baseframework.util.Preferences.getInstance().setChangeSharedPreferencesPathCallBack(changeSharedPreferencesPathCallBack);
        }
        
        private String path;
        
        public Settings(String path) {
            this.path = path;
        }
        
        public static void set(String path, String key, String value) {
            com.kongzue.baseframework.util.Preferences.getInstance().set(instance, path, key, value);
        }
        
        public static void set(String path, String key, boolean value) {
            com.kongzue.baseframework.util.Preferences.getInstance().set(instance, path, key, value);
        }
        
        public static void set(String path, String key, int value) {
            com.kongzue.baseframework.util.Preferences.getInstance().set(instance, path, key, value);
        }
        
        public static void set(String path, String key, float value) {
            com.kongzue.baseframework.util.Preferences.getInstance().set(instance, path, key, value);
        }
        
        public static void set(String path, String key, long value) {
            com.kongzue.baseframework.util.Preferences.getInstance().set(instance, path, key, value);
        }
        
        public static void set(String path, String key, double value) {
            com.kongzue.baseframework.util.Preferences.getInstance().set(instance, path, key, value);
        }
        
        public static void commit(String path, String key, String value) {
            com.kongzue.baseframework.util.Preferences.getInstance().commit(instance, path, key, value);
        }
        
        public static void commit(String path, String key, boolean value) {
            com.kongzue.baseframework.util.Preferences.getInstance().commit(instance, path, key, value);
        }
        
        public static void commit(String path, String key, int value) {
            com.kongzue.baseframework.util.Preferences.getInstance().commit(instance, path, key, value);
        }
        
        public static void commit(String path, String key, float value) {
            com.kongzue.baseframework.util.Preferences.getInstance().commit(instance, path, key, value);
        }
        
        public static void commit(String path, String key, long value) {
            com.kongzue.baseframework.util.Preferences.getInstance().commit(instance, path, key, value);
        }
        
        public static void commit(String path, String key, double value) {
            com.kongzue.baseframework.util.Preferences.getInstance().commit(instance, path, key, value);
        }
        
        public static String getString(String path, String key) {
            return com.kongzue.baseframework.util.Preferences.getInstance().getString(instance, path, key);
        }
        
        public static String getString(String path, String key, String defaultValue) {
            return com.kongzue.baseframework.util.Preferences.getInstance().getString(instance, path, key, defaultValue);
        }
        
        public static boolean getBoolean(String path, String key) {
            return com.kongzue.baseframework.util.Preferences.getInstance().getBoolean(instance, path, key);
        }
        
        public static boolean getBoolean(String path, String key, boolean defaultValue) {
            return com.kongzue.baseframework.util.Preferences.getInstance().getBoolean(instance, path, key, defaultValue);
        }
        
        public static int getInt(String path, String key) {
            return com.kongzue.baseframework.util.Preferences.getInstance().getInt(instance, path, key);
        }
        
        public static int getInt(String path, String key, int defaultValue) {
            return com.kongzue.baseframework.util.Preferences.getInstance().getInt(instance, path, key, defaultValue);
        }
        
        public static float getFloat(String path, String key) {
            return com.kongzue.baseframework.util.Preferences.getInstance().getFloat(instance, path, key);
        }
        
        public static float getFloat(String path, String key, float defaultValue) {
            return com.kongzue.baseframework.util.Preferences.getInstance().getFloat(instance, path, key, defaultValue);
        }
        
        public static long getLong(String path, String key) {
            return com.kongzue.baseframework.util.Preferences.getInstance().getLong(instance, path, key);
        }
        
        public static long getLong(String path, String key, long defaultValue) {
            return com.kongzue.baseframework.util.Preferences.getInstance().getLong(instance, path, key, defaultValue);
        }
        
        public static double getDouble(String path, String key) {
            return com.kongzue.baseframework.util.Preferences.getInstance().getDouble(instance, path, key);
        }
        
        public static double getDouble(String path, String key, double defaultValue) {
            return com.kongzue.baseframework.util.Preferences.getInstance().getDouble(instance, path, key, defaultValue);
        }
        
        public static void clean(String path) {
            com.kongzue.baseframework.util.Preferences.getInstance().cleanAll(instance, path);
        }
        
        public void set(String key, String value) {
            Settings.set(path, key, value);
        }
        
        public void set(String key, boolean value) {
            Settings.set(path, key, value);
        }
        
        public void set(String key, int value) {
            Settings.set(path, key, value);
        }
        
        public void set(String key, float value) {
            Settings.set(path, key, value);
        }
        
        public void set(String key, long value) {
            Settings.set(path, key, value);
        }
        
        public void set(String key, double value) {
            Settings.set(path, key, value);
        }
        
        public void commit(String key, String value) {
            Settings.commit(path, key, value);
        }
        
        public void commit(String key, boolean value) {
            Settings.commit(path, key, value);
        }
        
        public void commit(String key, int value) {
            Settings.commit(path, key, value);
        }
        
        public void commit(String key, float value) {
            Settings.commit(path, key, value);
        }
        
        public void commit(String key, long value) {
            Settings.commit(path, key, value);
        }
        
        public void commit(String key, double value) {
            Settings.commit(path, key, value);
        }
        
        public String getString(String key) {
            return getString(path, key);
        }
        
        public String getStringWithDefaultValue(String key, String defaultValue) {
            return getString(path, key, defaultValue);
        }
        
        public boolean getBoolean(String key) {
            return getBoolean(path, key);
        }
        
        public boolean getBoolean(String key, boolean defaultValue) {
            return getBoolean(path, key, defaultValue);
        }
        
        public int getInt(String key) {
            return getInt(path, key);
        }
        
        public int getInt(String key, int defaultValue) {
            return getInt(path, key, defaultValue);
        }
        
        public float getFloat(String key) {
            return getFloat(path, key);
        }
        
        public float getFloat(String key, float defaultValue) {
            return getFloat(path, key, defaultValue);
        }
        
        public long getLong(String key) {
            return getLong(path, key);
        }
        
        public long getLong(String key, long defaultValue) {
            return getLong(path, key, defaultValue);
        }
        
        public double getDouble(String key) {
            return getDouble(path, key);
        }
        
        public double getDouble(String key, double defaultValue) {
            return getDouble(path, key, defaultValue);
        }
        
        public void clean() {
            clean(path);
        }
    }
    
    public static void setPrivateInstance(Application context) {
        BaseApp.instance = context;
    }
    
    public static Application getPrivateInstance() {
        if (instance == null) {
            return null;
        }
        return instance;
    }
    
    public void setOnCrashListener(OnBugReportListener onBugReportListener) {
        BaseFrameworkSettings.turnOnReadErrorInfoPermissions(this, onBugReportListener);
    }
    
    public void exit() {
        BaseFrameworkSettings.exitApp();
    }
}
