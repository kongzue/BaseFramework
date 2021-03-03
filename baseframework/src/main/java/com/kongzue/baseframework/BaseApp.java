package com.kongzue.baseframework;

import android.app.Application;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Point;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowInsets;
import android.view.WindowManager;
import android.widget.Toast;

import com.kongzue.baseframework.interfaces.OnBugReportListener;
import com.kongzue.baseframework.interfaces.OnSDKInitializedCallBack;
import com.kongzue.baseframework.util.AppManager;
import com.kongzue.baseframework.util.DebugLogG;
import com.kongzue.baseframework.util.SettingsUtil;
import com.kongzue.baseframework.util.toast.Toaster;

import java.lang.reflect.Field;

import static com.kongzue.baseframework.BaseFrameworkSettings.DEBUGMODE;

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
    
    public static <App extends BaseApp> App getInstance(Class<App> appClass) {
        return (App) instance;
    }
    
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
    public static int dip2px(float dpValue) {
        if (getPrivateInstance() == null) {
            return (int) (dpValue * (Resources.getSystem().getDisplayMetrics().density) + 0.5f);
        }
        final float scale = getPrivateInstance().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
    
    //用于进行px和dip转换
    public static int px2dip(float pxValue) {
        if (getPrivateInstance() == null) {
            return (int) (pxValue / (Resources.getSystem().getDisplayMetrics().density) + 0.5f);
        }
        final float scale = getPrivateInstance().getResources().getDisplayMetrics().density;
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
    public static boolean openApp(String packageName) {
        if (getPrivateInstance() == null) {
            return false;
        }
        PackageManager packageManager = getPrivateInstance().getPackageManager();
        if (isInstallApp(packageName)) {
            try {
                Intent intent = packageManager.getLaunchIntentForPackage(packageName);
                getPrivateInstance().startActivity(intent);
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
    public static boolean isInstallApp(String packageName) {
        PackageInfo packageInfo = null;
        try {
            packageInfo = getPrivateInstance().getPackageManager().getPackageInfo(packageName, 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return packageInfo != null;
    }
    
    public static Settings Settings(String path) {
        return new Settings(path);
    }
    
    public static class Settings extends SettingsUtil {
        
        public Settings(String path) {
            super(path);
        }
    }
    
    /**
     * 此方法用于主动设置实例化 application 对象以实现一些需要 context 的场合下不需要传参，减少工作量，
     * 在您有实现 App extends BaseApp 的情况下无需调用，
     * 请勿手动调用此方法，此接口仅用于 BaseActivity 以固定逻辑调用。
     *
     * @param context Application 上下文
     * @hide
     */
    public static void setPrivateInstance(Application context) {
        BaseApp.instance = context;
    }
    
    /**
     * 此方法用于主动获取实例化的 application 对象以实现一些需要 context 的场合下不需要传参，减少工作量，
     * 如需要使用 App 的实例化对象，请勿使用本方法，
     * 请自行实现 getInstance() 方法，或使用 {@link #getInstance(Class)} 来获取。
     *
     * @return 例化的 application 对象
     */
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