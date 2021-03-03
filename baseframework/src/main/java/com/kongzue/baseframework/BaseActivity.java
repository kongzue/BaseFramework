package com.kongzue.baseframework;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.app.SharedElementCallback;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Point;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.Settings;

import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Pair;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowInsets;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.kongzue.baseframework.interfaces.ActivityResultCallback;
import com.kongzue.baseframework.interfaces.BindView;
import com.kongzue.baseframework.interfaces.BindViews;
import com.kongzue.baseframework.interfaces.FragmentLayout;
import com.kongzue.baseframework.interfaces.FullScreen;
import com.kongzue.baseframework.interfaces.GlobalLifeCircleListener;
import com.kongzue.baseframework.interfaces.LifeCircleListener;
import com.kongzue.baseframework.interfaces.DarkNavigationBarTheme;
import com.kongzue.baseframework.interfaces.DarkStatusBarTheme;
import com.kongzue.baseframework.interfaces.Layout;
import com.kongzue.baseframework.interfaces.NavigationBarBackgroundColor;
import com.kongzue.baseframework.interfaces.NavigationBarBackgroundColorHex;
import com.kongzue.baseframework.interfaces.NavigationBarBackgroundColorInt;
import com.kongzue.baseframework.interfaces.NavigationBarBackgroundColorRes;
import com.kongzue.baseframework.interfaces.OnClick;
import com.kongzue.baseframework.interfaces.OnClicks;
import com.kongzue.baseframework.interfaces.SwipeBack;
import com.kongzue.baseframework.util.AppManager;
import com.kongzue.baseframework.util.CycleRunner;
import com.kongzue.baseframework.util.DebugLogG;
import com.kongzue.baseframework.util.FragmentChangeUtil;
import com.kongzue.baseframework.util.JsonFormat;
import com.kongzue.baseframework.util.JumpParameter;
import com.kongzue.baseframework.util.LanguageUtil;
import com.kongzue.baseframework.util.OnPermissionResponseListener;
import com.kongzue.baseframework.util.OnJumpResponseListener;
import com.kongzue.baseframework.util.ParameterCache;
import com.kongzue.baseframework.util.swipeback.util.SwipeBackActivityBase;
import com.kongzue.baseframework.util.swipeback.util.SwipeBackActivityHelper;
import com.kongzue.baseframework.util.swipeback.util.SwipeBackLayout;
import com.kongzue.baseframework.util.swipeback.util.SwipeBackUtil;
import com.kongzue.baseframework.util.toast.Toaster;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.TimerTask;

import static com.kongzue.baseframework.BaseFrameworkSettings.BETA_PLAN;
import static com.kongzue.baseframework.BaseFrameworkSettings.DEBUGMODE;
import static com.kongzue.baseframework.BaseFrameworkSettings.setNavigationBarHeightZero;

/**
 * @Version: 6.7.8
 * @Author: Kongzue
 * @github: https://github.com/kongzue/BaseFramework
 * @link: http://kongzue.com/
 * @describe: 自动化代码流水线作业，以及对原生安卓、MIUI、flyme的透明状态栏显示灰色图标文字的支持，同时提供一些小工具简化开发难度，详细说明文档：https://github.com/kongzue/BaseFramework
 */
public abstract class BaseActivity extends AppCompatActivity implements SwipeBackActivityBase {
    
    private LifeCircleListener lifeCircleListener;                          //快速管理生命周期
    private static GlobalLifeCircleListener globalLifeCircleListener;       //全局生命周期
    
    public boolean isActive = false;                                        //当前Activity是否处于前台
    public boolean isAlive = false;                                         //当前Activity是否处于存活状态
    
    public OnJumpResponseListener onResponseListener;                       //jump跳转回调
    private OnPermissionResponseListener onPermissionResponseListener;      //权限申请回调
    
    private FragmentChangeUtil fragmentChangeUtil;
    
    public BaseActivity me = this;
    
    private boolean isFullScreen = false;
    private boolean darkStatusBarThemeValue = false;
    private boolean darkNavigationBarThemeValue = false;
    private int navigationBarBackgroundColorValue = Color.BLACK;
    private int layoutResId = -1;
    private int fragmentLayoutId = -1;
    
    private Bundle savedInstanceState;
    private SwipeBackActivityHelper mHelper;
    
    @Override
    @Deprecated
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (BaseApp.getPrivateInstance() == null) {
            BaseApp.setPrivateInstance(getApplication());
        }
        SwipeBack swipeBack = getClass().getAnnotation(SwipeBack.class);
        if (swipeBack != null) {
            enableSwipeBack = swipeBack.value();
        }
        if (enableSwipeBack) mHelper = new SwipeBackActivityHelper(this);
        this.savedInstanceState = savedInstanceState;
        
        logG("\n" + me.getClass().getSimpleName(), "onCreate");
        info(2, me.getClass().getSimpleName() + ":onCreate");
        
        isAlive = true;
        
        initAttributes();
        
        if (!interceptSetContentView()) {
            layoutResId = resetLayoutResId();
            if (layoutResId == 0) {
                errorLog("请在您的Activity的Class上注解：@Layout(你的layout资源id)或重写resetLayoutResId()方法以设置布局");
            }
            setContentView(layoutResId);
        }
        
        if (isFullScreen) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_FULLSCREEN);
        } else {
            setTranslucentStatus(true);
        }
        AppManager.getInstance().pushActivity(me);
        
        initBindViewAndFunctions();
        initViews();
        initFragments();
        bindAutoEvent();
        initDatas(getParameter());
        setEvents();
        getRootView().post(new Runnable() {
            @Override
            public void run() {
                lazyInit(getParameter());
            }
        });
        
        if (lifeCircleListener != null) {
            lifeCircleListener.onCreate();
        }
        if (globalLifeCircleListener != null) {
            globalLifeCircleListener.onCreate(me, me.getClass().getName());
        }
    }
    
    public boolean interceptSetContentView() {
        return false;
    }
    
    protected int resetLayoutResId() {
        return layoutResId;
    }
    
    private void initFragments() {
        if (fragmentLayoutId != -1 && fragmentChangeUtil == null) {
            fragmentChangeUtil = new FragmentChangeUtil(this, fragmentLayoutId);
            initFragment(fragmentChangeUtil);
        }
    }
    
    protected void initBindViewAndFunctions() {
        try {
            Field[] fields = getClass().getDeclaredFields();
            for (Field field : fields) {
                if (field.isAnnotationPresent(BindView.class)) {
                    BindView bindView = field.getAnnotation(BindView.class);
                    if (bindView != null && bindView.value() != 0) {
                        field.setAccessible(true);
                        field.set(me, me.findViewById(bindView.value()));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            Field[] fields = getClass().getDeclaredFields();
            for (Field field : fields) {
                if (field.isAnnotationPresent(BindViews.class)) {
                    BindViews bindView = field.getAnnotation(BindViews.class);
                    if (bindView != null && bindView.value().length != 0) {
                        List<View> viewList = new ArrayList<>();
                        for (int id : bindView.value()) {
                            viewList.add(findViewById(id));
                        }
                        field.setAccessible(true);
                        field.set(me, viewList);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            Method[] methods = getClass().getDeclaredMethods();
            for (Method method : methods) {
                if (method.isAnnotationPresent(OnClick.class)) {
                    OnClick onClick = method.getAnnotation(OnClick.class);
                    if (onClick != null && onClick.value() != 0) {
                        View v = findViewById(onClick.value());
                        v.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                try {
                                    method.invoke(me, v);
                                } catch (Exception e) {
                                    try {
                                        method.invoke(me);
                                    } catch (Exception e1) {
                                    }
                                }
                            }
                        });
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            Method[] methods = getClass().getDeclaredMethods();
            for (Method method : methods) {
                if (method.isAnnotationPresent(OnClicks.class)) {
                    OnClicks onClicks = method.getAnnotation(OnClicks.class);
                    if (onClicks != null && onClicks.value().length != 0) {
                        for (int id : onClicks.value()) {
                            View v = findViewById(id);
                            v.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    try {
                                        method.invoke(me, v);
                                    } catch (Exception e) {
                                        try {
                                            method.invoke(me);
                                        } catch (Exception e1) {
                                        }
                                    }
                                }
                            });
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void bindAutoEvent() {
        View backView = findViewById(R.id.back);
        if (backView != null) {
            backView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
        }
    }
    
    /**
     * 不推荐使用此方法，推荐重写 onBack()，并使用 return 值确定是否拦截返回按键的事件
     */
    @Override
    @Deprecated
    public void onBackPressed() {
        if (!onBack()) {
            super.onBackPressed();
        }
    }
    
    /**
     * 根绝 return 值确定是否允许执行返回指令。
     * 若当前 Activity 正在显示 Fragment，会优先询问正在显示的 Fragment 是否拦截返回事件。
     *
     * @return true：拦截返回指令；false：执行返回指令。
     */
    public boolean onBack() {
        if (fragmentChangeUtil != null && fragmentChangeUtil.getFocusFragment() != null) {
            return fragmentChangeUtil.getFocusFragment().onBack();
        }
        return false;
    }
    
    public void setLifeCircleListener(LifeCircleListener lifeCircleListener) {
        this.lifeCircleListener = lifeCircleListener;
    }
    
    protected boolean enableSwipeBack;
    
    //加载注解设置
    private void initAttributes() {
        try {
            FullScreen fullScreen = getClass().getAnnotation(FullScreen.class);
            Layout layout = getClass().getAnnotation(Layout.class);
            FragmentLayout fragmentLayout = getClass().getAnnotation(FragmentLayout.class);
            DarkNavigationBarTheme darkNavigationBarTheme = getClass().getAnnotation(DarkNavigationBarTheme.class);
            DarkStatusBarTheme darkStatusBarTheme = getClass().getAnnotation(DarkStatusBarTheme.class);
            NavigationBarBackgroundColor navigationBarBackgroundColor = getClass().getAnnotation(NavigationBarBackgroundColor.class);
            NavigationBarBackgroundColorRes navigationBarBackgroundColorRes = getClass().getAnnotation(NavigationBarBackgroundColorRes.class);
            NavigationBarBackgroundColorInt navigationBarBackgroundColorInt = getClass().getAnnotation(NavigationBarBackgroundColorInt.class);
            NavigationBarBackgroundColorHex navigationBarBackgroundColorHex = getClass().getAnnotation(NavigationBarBackgroundColorHex.class);
            if (fullScreen != null) {
                isFullScreen = fullScreen.value();
                if (isFullScreen) {
                    requestWindowFeature(Window.FEATURE_NO_TITLE);
                }
            }
            
            if (enableSwipeBack) {
                mHelper.onActivityCreate();
                setSwipeBackEnable(enableSwipeBack);
            }
            if (layout != null) {
                if (layout.value() != -1) {
                    layoutResId = layout.value();
                }
            }
            if (fragmentLayout != null) {
                fragmentLayoutId = fragmentLayout.value();
            }
            if (darkStatusBarTheme != null) {
                darkStatusBarThemeValue = darkStatusBarTheme.value();
            }
            if (darkNavigationBarTheme != null) {
                darkNavigationBarThemeValue = darkNavigationBarTheme.value();
            }
            if (navigationBarBackgroundColor != null) {
                if (navigationBarBackgroundColor.a() != -1 && navigationBarBackgroundColor.r() != -1 && navigationBarBackgroundColor.g() != -1 && navigationBarBackgroundColor.b() != -1) {
                    navigationBarBackgroundColorValue = Color.argb(navigationBarBackgroundColor.a(), navigationBarBackgroundColor.r(), navigationBarBackgroundColor.g(), navigationBarBackgroundColor.b());
                }
            }
            if (navigationBarBackgroundColorRes != null) {
                if (navigationBarBackgroundColorRes.value() != -1) {
                    navigationBarBackgroundColorValue = getColorS(navigationBarBackgroundColorRes.value());
                }
            }
            if (navigationBarBackgroundColorInt != null) {
                navigationBarBackgroundColorValue = navigationBarBackgroundColorInt.value();
            }
            if (navigationBarBackgroundColorHex != null) {
                navigationBarBackgroundColorValue = Color.parseColor(navigationBarBackgroundColorHex.value());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public void finish() {
        AppManager.getInstance().killActivity(me);
    }
    
    public void finishActivity() {
        super.finish();
    }
    
    //可被重写的接口
    
    /**
     * initViews会在启动时首先执行，建议在此方法内进行布局绑定、View初始化等操作
     */
    public abstract void initViews();
    
    /**
     * initDatas会在布局加载后执行，建议在此方法内加载数据和处理布局显示数据
     *
     * @param parameter 从其他界面传入的数据，提供GET、SET方法获取这些数据
     */
    public abstract void initDatas(JumpParameter parameter);
    
    /**
     * setEvents会在数据加载后执行，建议在此方法内绑定设置监听器、设置执行回调事件等操作
     */
    public abstract void setEvents();
    
    /**
     * 若有 BaseFragment 切换需要，可以使用 @FragmentLayout(R.layout.frameLayout) 注解，初始化 FragmentChangeUtil，并在此方法内 addFragment
     *
     * @param fragmentChangeUtil BaseFragment管理和切换类
     */
    public void initFragment(FragmentChangeUtil fragmentChangeUtil) {
    
    }
    
    /**
     * 切换至指定 BaseFragment
     *
     * @param index 已添加的 Fragment 编号（角标）
     */
    public void changeFragment(int index) {
        if (fragmentChangeUtil != null) {
            fragmentChangeUtil.show(index);
        } else {
            initFragments();
        }
    }
    
    public void changeFragment(int index, int enterAnimResId, int exitAnimResId) {
        if (fragmentChangeUtil != null) {
            fragmentChangeUtil.anim(enterAnimResId, exitAnimResId).show(index);
        } else {
            initFragments();
        }
    }
    
    /**
     * 切换至指定 BaseFragment
     *
     * @param fragment 已添加的 Fragment 对象
     */
    public void changeFragment(BaseFragment fragment) {
        if (fragmentChangeUtil != null) {
            fragmentChangeUtil.show(fragment);
        }
    }
    
    public void changeFragment(BaseFragment fragment, int enterAnimResId, int exitAnimResId) {
        if (fragmentChangeUtil != null) {
            fragmentChangeUtil.anim(enterAnimResId, exitAnimResId).show(fragment);
        }
    }
    
    /**
     * 获取 FragmentChangeUtil 对象
     */
    public FragmentChangeUtil getFragmentChangeUtil() {
        return fragmentChangeUtil;
    }
    
    //主题相关设置方法
    public void setDarkStatusBarTheme(boolean value) {
        darkStatusBarThemeValue = value;
        setTranslucentStatus(true);
    }
    
    public void setDarkNavigationBarTheme(boolean value) {
        darkNavigationBarThemeValue = value;
        setTranslucentStatus(true);
    }
    
    public void setNavigationBarBackgroundColor(@ColorInt int color) {
        navigationBarBackgroundColorValue = color;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(navigationBarBackgroundColorValue);
        }
    }
    
    public void setNavigationBarBackgroundColor(int a, int r, int g, int b) {
        navigationBarBackgroundColorValue = Color.argb(a, r, g, b);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(navigationBarBackgroundColorValue);
        }
    }
    
    //状态栏主题
    protected void setTranslucentStatus(boolean on) {
        if (isMIUI()) {
            setStatusBarDarkModeInMIUI(darkStatusBarThemeValue, this);
        }
        if (isFlyme()) {
            setStatusBarDarkIconInFlyme(getWindow(), darkStatusBarThemeValue);
        }
        Window window = getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            
            if (darkStatusBarThemeValue) {
                if (darkNavigationBarThemeValue) {
                    window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
                    );
                } else {
                    window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    );
                }
            } else {
                if (darkNavigationBarThemeValue) {
                    window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
                    );
                } else {
                    window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    );
                }
            }
            
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WindowManager.LayoutParams winParams = window.getAttributes();
            final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
            if (on) {
                winParams.flags |= bits;
            } else {
                winParams.flags &= ~bits;
            }
            window.setAttributes(winParams);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setNavigationBarColor(navigationBarBackgroundColorValue);
        }
    }
    
    private void setStatusBarDarkModeInMIUI(boolean darkmode, Activity activity) {
        Class<? extends Window> clazz = activity.getWindow().getClass();
        try {
            int darkModeFlag = 0;
            Class<?> layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
            Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
            darkModeFlag = field.getInt(layoutParams);
            Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);
            extraFlagField.invoke(activity.getWindow(), darkmode ? darkModeFlag : 0, darkModeFlag);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private boolean setStatusBarDarkIconInFlyme(Window window, boolean dark) {
        boolean result = false;
        if (window != null) {
            try {
                WindowManager.LayoutParams lp = window.getAttributes();
                Field darkFlag = WindowManager.LayoutParams.class.getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON");
                Field meizuFlags = WindowManager.LayoutParams.class.getDeclaredField("meizuFlags");
                darkFlag.setAccessible(true);
                meizuFlags.setAccessible(true);
                int bit = darkFlag.getInt(null);
                int value = meizuFlags.getInt(lp);
                if (dark) {
                    value |= bit;
                } else {
                    value &= ~bit;
                }
                meizuFlags.setInt(lp, value);
                window.setAttributes(lp);
                result = true;
            } catch (Exception e) {
                Log.e("MeiZu", "setStatusBarDarkIcon: failed");
            }
        }
        return result;
    }
    
    private static final String KEY_MIUI_VERSION_CODE = "ro.miui.ui.version.code";
    private static final String KEY_MIUI_VERSION_NAME = "ro.miui.ui.version.name";
    private static final String KEY_MIUI_INTERNAL_STORAGE = "ro.miui.internal.storage";
    
    //MIUI判断
    public static boolean isMIUI() {
        try {
            final BuildProperties prop = BuildProperties.newInstance();
            return prop.getProperty(KEY_MIUI_VERSION_CODE, null) != null || prop.getProperty(KEY_MIUI_VERSION_NAME, null) != null || prop.getProperty(KEY_MIUI_INTERNAL_STORAGE, null) != null;
        } catch (final IOException e) {
            return false;
        }
    }
    
    //Flyme判断
    public static boolean isFlyme() {
        try {
            final Method method = Build.class.getMethod("hasSmartBar");
            
            return method != null;
        } catch (final Exception e) {
            return false;
        }
    }
    
    public static class BuildProperties {
        
        private final Properties properties;
        
        private BuildProperties() throws IOException {
            properties = new Properties();
            properties.load(new FileInputStream(new File(Environment.getRootDirectory(), "build.prop")));
        }
        
        public boolean containsKey(final Object key) {
            return properties.containsKey(key);
        }
        
        public boolean containsValue(final Object value) {
            return properties.containsValue(value);
        }
        
        public Set<Map.Entry<Object, Object>> entrySet() {
            return properties.entrySet();
        }
        
        public String getProperty(final String name) {
            return properties.getProperty(name);
        }
        
        public String getProperty(final String name, final String defaultValue) {
            return properties.getProperty(name, defaultValue);
        }
        
        public boolean isEmpty() {
            return properties.isEmpty();
        }
        
        public Enumeration<Object> keys() {
            return properties.keys();
        }
        
        public Set<Object> keySet() {
            return properties.keySet();
        }
        
        public int size() {
            return properties.size();
        }
        
        public Collection<Object> values() {
            return properties.values();
        }
        
        public static BuildProperties newInstance() throws IOException {
            return new BuildProperties();
        }
    }
    
    protected final static String NULL = "";
    private Toast toast;
    
    public void runOnMain(Runnable runnable) {
        if (!isAlive) {
            return;
        }
        runOnUiThread(new Runnable() {
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
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                runnable.run();
            }
        }, time);
    }
    
    private List<CycleRunner> cycleTimerList = new ArrayList<>();
    
    public CycleRunner runCycle(Runnable runnable, long firstDelay, long interval) {
        CycleRunner runner = new CycleRunner();
        runner.schedule(new TimerTask() {
            @Override
            public void run() {
                runnable.run();
            }
        }, firstDelay, interval);
        cycleTimerList.add(runner);
        return runner;
    }
    
    public CycleRunner runOnMainCycle(Runnable runnable, long firstDelay, long interval) {
        CycleRunner runner = new CycleRunner();
        runner.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnMain(runnable);
            }
        }, firstDelay, interval);
        cycleTimerList.add(runner);
        return runner;
    }
    
    //简易吐司
    public void toast(final Object obj) {
        try {
            runOnMain(new Runnable() {
                @Override
                public void run() {
                    logG("toast", obj.toString());
                    if (toast == null) {
                        toast = Toast.makeText(BaseActivity.this, NULL, Toast.LENGTH_SHORT);
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
    
    //简易Log
    public void log(final Object obj) {
        DebugLogG.LogI(obj);
    }
    
    public void errorLog(final Object obj) {
        DebugLogG.LogE(obj);
    }
    
    private void info(int level, String msg) {
        if (!DEBUGMODE) {
            return;
        }
        switch (level) {
            case 0:
                Log.v(">>>", msg);
                break;
            case 1:
                Log.i(">>>", msg);
                break;
            case 2:
                Log.d(">>>", msg);
                break;
            case 3:
                Log.e(">>>", msg);
                break;
        }
    }
    
    //软键盘打开与收起
    public void showIME(boolean show, EditText editText) {
        if (editText == null) {
            return;
        }
        if (show) {
            editText.requestFocus();
            editText.setFocusableInTouchMode(true);
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(editText, InputMethodManager.RESULT_UNCHANGED_SHOWN);
        } else {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(editText.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
    
    public void showIME(@NonNull EditText editText) {
        if (editText == null) {
            return;
        }
        editText.requestFocus();
        editText.setFocusableInTouchMode(true);
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(editText, InputMethodManager.RESULT_UNCHANGED_SHOWN);
    }
    
    public void hideIME(@Nullable EditText editText) {
        if (editText != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(editText.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        } else {
            View view = getCurrentFocus();
            if (view != null) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }
    
    //兼容用
    @Deprecated
    public void setIMMStatus(boolean show, EditText editText) {
        showIME(show, editText);
    }
    
    public static String StartFindWords = "";
    
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
    
    
    //权限相关
    private final String TAG = "PermissionsUtil";
    private int REQUEST_CODE_PERMISSION = 0x00099;
    
    /**
     * 请求权限
     * <p>
     * 警告：此处除了用户拒绝外，唯一可能出现无法获取权限或失败的情况是在AndroidManifest.xml中未声明权限信息
     * Android6.0+即便需要动态请求权限（重点）但不代表着不需要在AndroidManifest.xml中进行声明。
     *
     * @param permissions                  请求的权限
     * @param onPermissionResponseListener 回调监听器
     */
    public void requestPermission(String[] permissions, OnPermissionResponseListener onPermissionResponseListener) {
        this.onPermissionResponseListener = onPermissionResponseListener;
        if (checkPermissions(permissions)) {
            if (onPermissionResponseListener != null) {
                onPermissionResponseListener.onSuccess(permissions);
            }
        } else {
            List<String> needPermissions = getDeniedPermissions(permissions);
            ActivityCompat.requestPermissions(this, needPermissions.toArray(new String[needPermissions.size()]), REQUEST_CODE_PERMISSION);
        }
    }
    
    /**
     * 检测所有的权限是否都已授权
     *
     * @param permissions
     * @return
     */
    public boolean checkPermissions(String[] permissions) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) !=
                    PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }
    
    //单权限检查
    public boolean checkPermissions(String permission) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (ContextCompat.checkSelfPermission(this, permission) !=
                PackageManager.PERMISSION_GRANTED) {
            return false;
        }
        return true;
    }
    
    /**
     * 获取权限集中需要申请权限的列表
     *
     * @param permissions
     * @return
     */
    private List<String> getDeniedPermissions(String[] permissions) {
        List<String> needRequestPermissionList = new ArrayList<>();
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) !=
                    PackageManager.PERMISSION_GRANTED ||
                    ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
                needRequestPermissionList.add(permission);
            }
        }
        return needRequestPermissionList;
    }
    
    
    /**
     * 系统请求权限回调
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_PERMISSION) {
            if (verifyPermissions(grantResults)) {
                if (onPermissionResponseListener != null) {
                    onPermissionResponseListener.onSuccess(permissions);
                }
            } else {
                if (onPermissionResponseListener != null) {
                    onPermissionResponseListener.onFail();
                }
                showTipsDialog();
            }
        }
    }
    
    /**
     * 确认所有的权限是否都已授权
     *
     * @param grantResults
     * @return
     */
    private boolean verifyPermissions(int[] grantResults) {
        for (int grantResult : grantResults) {
            if (grantResult != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }
    
    /**
     * 显示提示对话框
     */
    private void showTipsDialog() {
        new AlertDialog.Builder(this)
                .setTitle("警告")
                .setMessage("需要必要的权限才可以正常使用该功能，您已拒绝获得该权限。\n如果需要重新授权，您可以点击“允许”按钮进入系统设置进行授权")
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startAppSettings();
                    }
                }).show();
    }
    
    //启动当前应用设置页面
    public void startAppSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
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
        Display disp = getWindowManager().getDefaultDisplay();
        Point outP = new Point();
        disp.getSize(outP);
        return outP.x;
    }
    
    //获取屏幕可用部分高度（屏幕高度-状态栏高度-屏幕底栏高度）
    public int getDisplayHeight() {
        Display disp = getWindowManager().getDefaultDisplay();
        Point outP = new Point();
        disp.getSize(outP);
        return outP.y;
    }
    
    //获取底栏高度
    public int getNavbarHeight() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            WindowInsets windowInsets = null;
            windowInsets = getWindow().getDecorView().getRootView().getRootWindowInsets();
            if (windowInsets != null) {
                return windowInsets.getStableInsetBottom();
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
        int diaplayHeight = 0;
        Display display = getWindowManager().getDefaultDisplay();
        Point point = new Point();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            display.getRealSize(point);
            diaplayHeight = point.y;
        } else {
            DisplayMetrics dm = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(dm);
            diaplayHeight = dm.heightPixels; //得到高度```
        }
        return diaplayHeight;
    }
    
    //位移动画
    public ObjectAnimator moveAnimation(Object obj, String perference, float aimValue, long time, long delay) {
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(obj, perference, aimValue);
        objectAnimator.setDuration(time);
        objectAnimator.setStartDelay(delay);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            objectAnimator.setAutoCancel(true);
        }
        objectAnimator.start();
        return objectAnimator;
    }
    
    public ObjectAnimator moveAnimation(Object obj, String perference, float aimValue, long time) {
        return moveAnimation(obj, perference, aimValue, time, 0);
    }
    
    public ObjectAnimator moveAnimation(Object obj, String perference, float aimValue) {
        return moveAnimation(obj, perference, aimValue, 300, 0);
    }
    
    //复制文本到剪贴板
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
    
    //网络传输文本判空规则
    public static boolean isNull(String s) {
        if (s == null || s.trim().isEmpty() || "null".equals(s) || "(null)".equals(s)) {
            return true;
        }
        return false;
    }
    
    //更好用的跳转方式
    public boolean jump(Class<?> cls) {
        try {
            startActivity(new Intent(me, cls));
        } catch (Exception e) {
            if (DEBUGMODE) {
                e.printStackTrace();
            }
            return false;
        }
        return true;
    }
    
    //可以传任何类型参数的跳转方式
    public boolean jump(Class<?> cls, JumpParameter jumpParameter) {
        try {
            if (jumpParameter != null) {
                ParameterCache.getInstance().set(cls.getName(), jumpParameter);
            }
            startActivity(new Intent(me, cls));
        } catch (Exception e) {
            if (DEBUGMODE) {
                e.printStackTrace();
            }
            return false;
        }
        return true;
    }
    
    //带返回值的跳转
    public boolean jump(Class<?> cls, OnJumpResponseListener onResponseListener) {
        return jump(cls, null, onResponseListener);
    }
    
    //带参数和返回值跳转
    public boolean jump(Class<?> cls, JumpParameter jumpParameter, OnJumpResponseListener onResponseListener) {
        try {
            startActivity(new Intent(me, cls));
            ParameterCache.getInstance().cleanResponse(me.getClass().getName());
            if (jumpParameter == null) {
                jumpParameter = new JumpParameter();
            }
            ParameterCache.getInstance().set(cls.getName(), jumpParameter
                    .put("needResponse", true)
                    .put("responseClassName", getInstanceKey())
            );
            this.onResponseListener = onResponseListener;
        } catch (Exception e) {
            if (DEBUGMODE) {
                e.printStackTrace();
            }
            return false;
        }
        return true;
    }
    
    //可使用共享元素的跳转方式
    public boolean jump(Class<?> cls, View transitionView) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                me.setExitSharedElementCallback(new SharedElementCallback() {
                    @Override
                    public void onSharedElementEnd(List<String> sharedElementNames, List<View> sharedElements, List<View> sharedElementSnapshots) {
                        super.onSharedElementEnd(sharedElementNames, sharedElements, sharedElementSnapshots);
                        for (View view : sharedElements) {
                            view.setVisibility(View.VISIBLE);
                        }
                    }
                });
                startActivity(new Intent(me, cls), ActivityOptions.makeSceneTransitionAnimation(me, transitionView, transitionView.getTransitionName()).toBundle());
            } else {
                startActivity(new Intent(me, cls));
            }
        } catch (Exception e) {
            if (DEBUGMODE) {
                e.printStackTrace();
            }
            return false;
        }
        return true;
    }
    
    public boolean jump(Class<?> cls, View... transitionViews) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                me.setExitSharedElementCallback(new SharedElementCallback() {
                    @Override
                    public void onSharedElementEnd(List<String> sharedElementNames, List<View> sharedElements, List<View> sharedElementSnapshots) {
                        super.onSharedElementEnd(sharedElementNames, sharedElements, sharedElementSnapshots);
                        for (View view : sharedElements) {
                            view.setVisibility(View.VISIBLE);
                        }
                    }
                });
                
                Pair<View, String>[] pairs = new Pair[transitionViews.length];
                int i = 0;
                for (View tv : transitionViews) {
                    Pair<View, String> pair = new Pair<>(tv, tv.getTransitionName());
                    pairs[i] = pair;
                    i++;
                }
                startActivity(new Intent(me, cls), ActivityOptions.makeSceneTransitionAnimation(me, pairs).toBundle());
            } else {
                startActivity(new Intent(me, cls));
            }
        } catch (Exception e) {
            if (DEBUGMODE) {
                e.printStackTrace();
            }
            return false;
        }
        return true;
    }
    
    //可使用共享元素的带参数跳转方式
    public boolean jump(Class<?> cls, JumpParameter jumpParameter, View transitionView) {
        try {
            if (jumpParameter != null) {
                ParameterCache.getInstance().set(cls.getName(), jumpParameter);
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                me.setExitSharedElementCallback(new SharedElementCallback() {
                    @Override
                    public void onSharedElementEnd(List<String> sharedElementNames, List<View> sharedElements, List<View> sharedElementSnapshots) {
                        super.onSharedElementEnd(sharedElementNames, sharedElements, sharedElementSnapshots);
                        for (View view : sharedElements) {
                            view.setVisibility(View.VISIBLE);
                        }
                    }
                });
                startActivity(new Intent(me, cls), ActivityOptions.makeSceneTransitionAnimation(me, transitionView, transitionView.getTransitionName()).toBundle());
            } else {
                startActivity(new Intent(me, cls));
            }
        } catch (Exception e) {
            if (DEBUGMODE) {
                e.printStackTrace();
            }
            return false;
        }
        return true;
    }
    
    public boolean jump(Class<?> cls, JumpParameter jumpParameter, View... transitionViews) {
        try {
            if (jumpParameter != null) {
                ParameterCache.getInstance().set(cls.getName(), jumpParameter);
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                me.setExitSharedElementCallback(new SharedElementCallback() {
                    @Override
                    public void onSharedElementEnd(List<String> sharedElementNames, List<View> sharedElements, List<View> sharedElementSnapshots) {
                        super.onSharedElementEnd(sharedElementNames, sharedElements, sharedElementSnapshots);
                        for (View view : sharedElements) {
                            view.setVisibility(View.VISIBLE);
                        }
                    }
                });
                
                Pair<View, String>[] pairs = new Pair[transitionViews.length];
                int i = 0;
                for (View tv : transitionViews) {
                    Pair<View, String> pair = new Pair<>(tv, tv.getTransitionName());
                    pairs[i] = pair;
                    i++;
                }
                
                startActivity(new Intent(me, cls), ActivityOptions.makeSceneTransitionAnimation(me, pairs).toBundle());
            } else {
                startActivity(new Intent(me, cls));
            }
        } catch (Exception e) {
            if (DEBUGMODE) {
                e.printStackTrace();
            }
            return false;
        }
        return true;
    }
    
    //可使用共享元素的带返回值的跳转
    public boolean jump(Class<?> cls, OnJumpResponseListener onResponseListener, View transitionView) {
        return jump(cls, null, onResponseListener, transitionView);
    }
    
    //可使用共享元素的带参数和返回值跳转
    public boolean jump(Class<?> cls, JumpParameter jumpParameter, OnJumpResponseListener onResponseListener, View transitionView) {
        try {
            ParameterCache.getInstance().cleanResponse(me.getClass().getName());
            if (jumpParameter == null) {
                jumpParameter = new JumpParameter();
            }
            ParameterCache.getInstance().set(cls.getName(), jumpParameter
                    .put("needResponse", true)
                    .put("responseClassName", getInstanceKey())
            );
            this.onResponseListener = onResponseListener;
            
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                me.setExitSharedElementCallback(new SharedElementCallback() {
                    @Override
                    public void onSharedElementEnd(List<String> sharedElementNames, List<View> sharedElements, List<View> sharedElementSnapshots) {
                        super.onSharedElementEnd(sharedElementNames, sharedElements, sharedElementSnapshots);
                        for (View view : sharedElements) {
                            view.setVisibility(View.VISIBLE);
                        }
                    }
                });
                startActivity(new Intent(me, cls), ActivityOptions.makeSceneTransitionAnimation(me, transitionView, transitionView.getTransitionName()).toBundle());
            } else {
                startActivity(new Intent(me, cls));
                
            }
        } catch (Exception e) {
            if (DEBUGMODE) {
                e.printStackTrace();
            }
            return false;
        }
        return true;
    }
    
    public void jumpAnim(int enterAnim, int exitAnim) {
        int version = Integer.valueOf(Build.VERSION.SDK_INT);
        if (version > 5) {
            overridePendingTransition(enterAnim, exitAnim);
        }
    }
    
    //目标Activity：设定要返回的数据
    public void setResponse(JumpParameter jumpParameter) {
        BaseActivity backResponseActivity = AppManager.getInstance().getActivityInstance(getParameter().getString("responseClassName"));
        if (backResponseActivity != null) {
            backResponseActivity.setResponseMessage(jumpParameter);
        }
    }
    
    private Runnable waitResponseRunnable;
    
    protected void setResponseMessage(JumpParameter jumpParameter) {
        log(getClass().getName() + ".setResponseMessage: " + jumpParameter);
        waitResponseRunnable = new Runnable() {
            @Override
            public void run() {
                if (onResponseListener != null) {
                    JumpParameter responseData = jumpParameter;
                    if (responseData == null) {
                        responseData = new JumpParameter();
                    }
                    onResponseListener.OnResponse(responseData);
                    onResponseListener = null;
                }
            }
        };
        if (isActive) {
            runOnMain(waitResponseRunnable);
            waitResponseRunnable = null;
        }
    }
    
    //目标Activity：设定要返回的数据，写法2
    public void returnParameter(JumpParameter parameter) {
        setResponse(parameter);
    }
    
    //获取跳转参数
    public JumpParameter getParameter() {
        JumpParameter jumpParameter = ParameterCache.getInstance().get(me.getClass().getName());
        if (jumpParameter == null) {
            jumpParameter = new JumpParameter();
        }
        return jumpParameter;
    }
    
    protected void lazyInit(JumpParameter parameter) {
    }
    
    @Override
    protected void onResume() {
        isActive = true;
        logG("\n" + me.getClass().getSimpleName(), "onResume");
        super.onResume();
        if (waitResponseRunnable != null) {
            runOnMain(waitResponseRunnable);
            waitResponseRunnable = null;
        }
        if (lifeCircleListener != null) {
            lifeCircleListener.onResume();
        }
        if (globalLifeCircleListener != null) {
            globalLifeCircleListener.onResume(me, me.getClass().getName());
        }
        AppManager.setActiveActivity(this);
        if (resumeRunnableList != null) {
            for (Runnable runnable : resumeRunnableList) {
                runOnMain(runnable);
            }
        }
    }
    
    private List<Runnable> resumeRunnableList;
    
    public void runOnResume(Runnable resumeRunnable) {
        if (resumeRunnableList == null) {
            resumeRunnableList = new ArrayList<>();
        }
        resumeRunnableList.add(resumeRunnable);
    }
    
    public void cleanResumeRunnable() {
        resumeRunnableList = new ArrayList<>();
    }
    
    public void deleteResumeRunnable(Runnable resumeRunnable) {
        if (resumeRunnableList != null) resumeRunnableList.remove(resumeRunnable);
    }
    
    @Override
    protected void onPause() {
        if (Toaster.isSupportToast) {
            Toaster.cancel();
        }
        isActive = false;
        logG("\n" + me.getClass().getSimpleName(), "onPause");
        if (lifeCircleListener != null) {
            lifeCircleListener.onPause();
        }
        if (globalLifeCircleListener != null) {
            globalLifeCircleListener.onPause(me, me.getClass().getName());
        }
        super.onPause();
    }
    
    @Override
    protected void onDestroy() {
        isAlive = false;
        logG("\n" + me.getClass().getSimpleName(), "onDestroy");
        info(2, me.getClass().getSimpleName() + ":onDestroy");
        if (getParameter() != null) {
            getParameter().cleanAll();
        }
        AppManager.getInstance().deleteActivity(me);
        if (lifeCircleListener != null) {
            lifeCircleListener.onDestroy();
        }
        if (globalLifeCircleListener != null) {
            globalLifeCircleListener.onDestroy(me, me.getClass().getName());
        }
        for (CycleRunner runnable : cycleTimerList) {
            if (!runnable.isCanceled()) {
                runnable.cancel();
            }
        }
        super.onDestroy();
    }
    
    //大型打印使用，Log默认是有字数限制的，如有需要打印更长的文本可以使用此方法
    public void bigLog(String msg) {
        DebugLogG.bigLog(msg, false);
    }
    
    public static GlobalLifeCircleListener getGlobalLifeCircleListener() {
        return globalLifeCircleListener;
    }
    
    public static void setGlobalLifeCircleListener(GlobalLifeCircleListener globalLifeCircleListener) {
        BaseActivity.globalLifeCircleListener = globalLifeCircleListener;
    }
    
    public static boolean DEBUGMODE() {
        return DEBUGMODE;
    }
    
    private void logG(String tag, Object o) {
        DebugLogG.LogG(tag + ">>>" + o.toString());
    }
    
    //使用默认浏览器打开链接
    public boolean openUrl(String url) {
        try {
            Uri uri = Uri.parse(url);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
            return true;
        } catch (Exception e) {
            if (DEBUGMODE) {
                e.printStackTrace();
            }
            return false;
        }
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
    
    public Bundle getSavedInstanceState() {
        return savedInstanceState;
    }
    
    //获取IMEI (请预先在 AndroidManifest.xml 中声明：<uses-permission android:name="android.permission.READ_PHONE_STATE"/>)
    @SuppressLint({"WrongConstant", "MissingPermission"})
    public String getIMEI() {
        String result = null;
        try {
            if (checkPermissions(new String[]{"android.permission.READ_PHONE_STATE"})) {
                TelephonyManager telephonyManager = (TelephonyManager) getSystemService("phone");
                if (telephonyManager != null) {
                    if (Build.VERSION.SDK_INT >= 26) {
                        try {
                            Method method = telephonyManager.getClass().getMethod("getImei", new Class[0]);
                            method.setAccessible(true);
                            result = (String) method.invoke(telephonyManager, new Object[0]);
                        } catch (Exception e) {
                        }
                        if (isNull(result)) {
                            result = telephonyManager.getDeviceId();
                        }
                    } else {
                        result = telephonyManager.getDeviceId();
                    }
                }
            } else {
                requestPermission(new String[]{"android.permission.READ_PHONE_STATE"}, new OnPermissionResponseListener() {
                    @Override
                    public void onSuccess(String[] permissions) {
                        getIMEI();
                    }
                    
                    @Override
                    public void onFail() {
                        if (BaseFrameworkSettings.DEBUGMODE) {
                            Log.e(">>>", "getIMEI(): 失败，用户拒绝授权READ_PHONE_STATE");
                        }
                    }
                });
            }
        } catch (Exception e) {
            if (BaseFrameworkSettings.DEBUGMODE) {
                e.printStackTrace();
            }
        }
        return result;
    }
    
    public String getAndroidId() {
        String androidID = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        return androidID;
    }
    
    //获取Mac地址 (请预先在 AndroidManifest.xml 中声明：<uses-permission android:name="android.permission.ACCESS_WIFI_STATE"/>)
    public String getMacAddress() {
        String macAddress = null;
        StringBuffer buf = new StringBuffer();
        NetworkInterface networkInterface = null;
        try {
            networkInterface = NetworkInterface.getByName("eth1");
            if (networkInterface == null) {
                networkInterface = NetworkInterface.getByName("wlan0");
            }
            if (networkInterface == null) {
                return "02:00:00:00:00:02";
            }
            byte[] addr = networkInterface.getHardwareAddress();
            for (byte b : addr) {
                buf.append(String.format("%02X:", b));
            }
            if (buf.length() > 0) {
                buf.deleteCharAt(buf.length() - 1);
            }
            macAddress = buf.toString();
        } catch (SocketException e) {
            e.printStackTrace();
            return "02:00:00:00:00:02";
        }
        return macAddress;
    }
    
    public void restartMe() {
        finish();
        jump(me.getClass());
        jumpAnim(R.anim.fade, R.anim.hold);
    }
    
    //以下不用管系列————
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        if (enableSwipeBack) mHelper.onPostCreate();
    }
    
    @Override
    public SwipeBackLayout getSwipeBackLayout() {
        return enableSwipeBack ? mHelper.getSwipeBackLayout() : null;
    }
    
    @Override
    public void setSwipeBackEnable(boolean enable) {
        getSwipeBackLayout().setEnableGesture(enable);
    }
    
    @Override
    public void scrollToFinishActivity() {
        SwipeBackUtil.convertActivityToTranslucent(this);
        getSwipeBackLayout().scrollToFinishActivity();
    }
    
    @Override
    protected void attachBaseContext(Context c) {
        super.attachBaseContext(LanguageUtil.wrap(c));
    }
    
    //支持最低SDK的getColor方法
    public int getColorS(@ColorRes int id) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return getResources().getColor(id, getTheme());
        } else {
            return getResources().getColor(id);
        }
    }
    
    public View getRootView() {
        return getWindow().getDecorView().findViewById(android.R.id.content);
    }
    
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (globalLifeCircleListener != null) {
            globalLifeCircleListener.windowFocus(me, me.getClass().getName(), hasFocus);
        }
    }
    
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.remove("android:support:fragments");
    }
    
    public void click(View v, View.OnClickListener onClickListener) {
        v.setFocusableInTouchMode(true);
        v.setOnTouchListener(new View.OnTouchListener() {
            int touchFlag = 0;
            
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        if (touchFlag == 0) {
                            touchFlag = 1;
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        if (touchFlag == 1) {
                            touchFlag = -1;
                            onClickListener.onClick(v);
                            runDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    touchFlag = 0;
                                }
                            }, 500);
                        }
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        if (touchFlag == 1) {
                            touchFlag = 0;
                        }
                        break;
                }
                return true;
            }
        });
    }
    
    public String getInstanceKey() {
        return getClass().getName() + "@" + Integer.toHexString(hashCode());
    }
    
    private List<ActivityResultCallback> activityResultCallbackList;
    
    public void startActivityForResult(Intent intent, ActivityResultCallback activityResultCallback) {
        if (activityResultCallbackList == null) activityResultCallbackList = new ArrayList<>();
        if (activityResultCallback.getResultId() == 0) {
            activityResultCallback.setResultId(100000 + activityResultCallbackList.size());
        }
        activityResultCallbackList.add(activityResultCallback);
        super.startActivityForResult(intent, activityResultCallback.getResultId());
    }
    
    public void startActivityForResult(Intent intent, ActivityResultCallback activityResultCallback, @Nullable Bundle options) {
        if (activityResultCallbackList == null) activityResultCallbackList = new ArrayList<>();
        if (activityResultCallback.getResultId() == 0) {
            activityResultCallback.setResultId(100 + activityResultCallbackList.size());
        }
        activityResultCallbackList.add(activityResultCallback);
        super.startActivityForResult(intent, activityResultCallback.getResultId(), options);
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (activityResultCallbackList != null) {
            List<ActivityResultCallback> runActivityResultCallback = new ArrayList<>();
            for (ActivityResultCallback callback : activityResultCallbackList) {
                if (callback.getResultId() == requestCode) {
                    callback.onActivityResult(requestCode, resultCode, data);
                    runActivityResultCallback.add(callback);
                }
            }
            activityResultCallbackList.removeAll(runActivityResultCallback);
        }
    }
}