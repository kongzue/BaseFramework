package com.kongzue.baseframework;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.ColorRes;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.kongzue.baseframework.interfaces.ActivityResultCallback;
import com.kongzue.baseframework.interfaces.BindView;
import com.kongzue.baseframework.interfaces.BindViews;
import com.kongzue.baseframework.interfaces.LifeCircleListener;
import com.kongzue.baseframework.interfaces.Layout;
import com.kongzue.baseframework.interfaces.OnClick;
import com.kongzue.baseframework.interfaces.OnClicks;
import com.kongzue.baseframework.util.CycleRunner;
import com.kongzue.baseframework.util.FragmentChangeUtil;
import com.kongzue.baseframework.util.JumpParameter;
import com.kongzue.baseframework.util.OnPermissionResponseListener;
import com.kongzue.baseframework.util.OnJumpResponseListener;
import com.kongzue.baseframework.util.ParameterCache;
import com.kongzue.baseframework.util.toast.Toaster;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;

import static com.kongzue.baseframework.BaseFrameworkSettings.DEBUGMODE;

/**
 * @Version: 6.7.0
 * @Author: Kongzue
 * @github: https://github.com/kongzue/BaseFramework
 * @link: http://kongzue.com/
 * @describe: 自动化代码流水线作业，以及对原生安卓、MIUI、flyme的透明状态栏显示灰色图标文字的支持，同时提供一些小工具简化开发难度，详细说明文档：https://github.com/kongzue/BaseFramework
 */
public abstract class BaseFragment<ME extends BaseActivity> extends Fragment {
    
    public int layoutResId = -1;
    public boolean isActive = false;                                        //当前Fragment是否处于前台
    public int fromFragmentId = -1;
    
    /**
     * 快速管理生命周期
     */
    private LifeCircleListener lifeCircleListener;
    
    private Bundle savedInstanceState;
    
    public ME me;
    
    public BaseFragment THIS;
    
    public void setActivity(ME activity) {
        this.me = activity;
    }
    
    /**
     * 根布局，可以直接用
     */
    public View rootView;
    
    /**
     * 已完全取代 onCreateView 的使用，请勿重写此方法，要绑定布局请使用 @Layout(...) 注解
     *
     * @param inflater           忽略
     * @param container          忽略
     * @param savedInstanceState 忽略
     * @return 忽略
     */
    @Deprecated
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        me = (ME) getActivity();
        THIS = this;
        this.savedInstanceState = savedInstanceState;
        
        rootView = interceptSetContentView();
        if (rootView == null) {
            try {
                Layout layout = getClass().getAnnotation(Layout.class);
                if (layout != null && layout.value() != -1) {
                    layoutResId = layout.value();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            layoutResId = resetLayoutResId();
            if (layoutResId == -1) {
                errorLog("请在您的Fragment的Class上注解：@Layout(你的layout资源id)，或重写resetLayoutResId()方法以设置布局");
                return null;
            }
            if (rootView == null) {
                rootView = LayoutInflater.from(getActivity()).inflate(layoutResId, container, false);
            }
        }
        
        if (lifeCircleListener != null) {
            lifeCircleListener.onCreate();
        }
        
        initViews();
        bindAutoEvent();
        initBindViewAndFunctions();
        initDatas();
        setEvents();
        
        rootView.post(new Runnable() {
            @Override
            public void run() {
                lazyInit(getParameter());
            }
        });
        
        return rootView;
    }
    
    protected void lazyInit(JumpParameter parameter) {
    }
    
    public View interceptSetContentView() {
        return null;
    }
    
    protected int resetLayoutResId() {
        return layoutResId;
    }
    
    protected void initBindViewAndFunctions() {
        try {
            Field[] fields = getClass().getDeclaredFields();
            for (Field field : fields) {
                if (field.isAnnotationPresent(BindView.class)) {
                    BindView bindView = field.getAnnotation(BindView.class);
                    if (bindView != null && bindView.value() != 0) {
                        field.setAccessible(true);
                        field.set(THIS, THIS.findViewById(bindView.value()));
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
                        field.set(THIS, viewList);
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
                                    method.invoke(THIS, v);
                                } catch (Exception e) {
                                    try {
                                        method.invoke(THIS);
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
                    me.onBackPressed();
                }
            });
        }
    }
    
    public void setLifeCircleListener(LifeCircleListener lifeCircleListener) {
        this.lifeCircleListener = lifeCircleListener;
    }
    
    //不再推荐使用，建议直接在Fragment上注解：@Layout(你的layout资源id)
    @Deprecated
    public int getLayout() {
        return layoutResId;
    }
    
    /**
     * initViews会在启动时首先执行，建议在此方法内进行布局绑定、View初始化等操作
     */
    public abstract void initViews();
    
    /**
     * initDatas会在布局加载后执行，建议在此方法内加载数据和处理布局显示数据
     */
    public abstract void initDatas();
    
    /**
     * setEvents会在数据加载后执行，建议在此方法内绑定设置监听器、设置执行回调事件等操作
     */
    public abstract void setEvents();
    
    /**
     * 重写此方法以替代 {@link #onResume()} 方法
     * onLoad 会在首次进入此界面时执行
     */
    public void onLoad() {
    
    }
    
    /**
     * 重写此方法以替代 {@link #onResume()} 方法
     *
     * @param isSwitchFragment 是否是从其他 BaseFragment 切换至此界面
     */
    public void onShow(boolean isSwitchFragment) {
    
    }
    
    /**
     * 重写此方法以获取通过 jump(...) 跳转到此 BaseFragment 时携带的参数
     *
     * @param parameter 跳转时携带的参数
     */
    public void onParameterReceived(JumpParameter parameter) {
    
    }
    
    public final <T extends View> T findViewById(@IdRes int id) {
        return (T) rootView.findViewById(id);
    }
    
    private Toast toast;
    
    protected final static String NULL = "";
    
    //简易吐司
    public void toast(final Object obj) {
        me.toast(obj);
    }
    
    public void toastS(final Object obj) {
        Toaster.build(me).show(obj.toString());
    }
    
    public void log(final Object obj) {
        me.log(obj);
    }
    
    public void errorLog(final Object obj) {
        me.errorLog(obj);
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
    
    //用于进行dip和px转换
    public int dip2px(float dpValue) {
        final float scale = me.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
    
    //用于进行px和dip转换
    public int px2dip(float pxValue) {
        final float scale = me.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }
    
    public boolean isNull(String s) {
        if (s == null || s.trim().isEmpty() || "null".equals(s)) {
            return true;
        }
        return false;
    }
    
    //更好用的跳转方式
    public boolean jump(Class<?> cls) {
        return me.jump(cls);
    }
    
    //可以传任何类型参数的跳转方式
    public boolean jump(Class<?> cls, JumpParameter jumpParameter) {
        return me.jump(cls, jumpParameter);
    }
    
    //带返回值的跳转
    public boolean jump(Class<?> cls, OnJumpResponseListener onJumpResponseListener) {
        return me.jump(cls, onJumpResponseListener);
    }
    
    //带返回值的跳转
    public boolean jump(Class<?> cls, JumpParameter jumpParameter, OnJumpResponseListener onResponseListener) {
        return me.jump(cls, jumpParameter, onResponseListener);
    }
    
    //带共享元素的跳转方式
    public boolean jump(Class<?> cls, View transitionView) {
        return me.jump(cls, transitionView);
    }
    
    public boolean jump(Class<?> cls, JumpParameter jumpParameter, View transitionView) {
        return me.jump(cls, jumpParameter, transitionView);
    }
    
    public boolean jump(Class<?> cls, OnJumpResponseListener onJumpResponseListener, View transitionView) {
        return me.jump(cls, onJumpResponseListener, transitionView);
    }
    
    public boolean jump(Class<?> cls, JumpParameter jumpParameter, OnJumpResponseListener onJumpResponseListener, View transitionView) {
        return me.jump(cls, jumpParameter, onJumpResponseListener, transitionView);
    }
    
    /**
     * 跳转到绑定在同一 BaseActivity 下指定角标的 BaseFragment
     *
     * @param index 角标
     */
    public void jump(int index) {
        me.changeFragment(index);
    }
    
    public void jump(int index, int enterAnimResId, int exitAnimResId) {
        me.changeFragment(index, enterAnimResId, exitAnimResId);
    }
    
    /**
     * 跳转到绑定在同一 BaseActivity 下指定已实例化的 BaseFragment 对象
     *
     * @param baseFragment 指定的 BaseFragment 对象
     */
    public void jump(BaseFragment baseFragment) {
        me.changeFragment(baseFragment);
    }
    
    public void jump(BaseFragment baseFragment, int enterAnimResId, int exitAnimResId) {
        me.changeFragment(baseFragment, enterAnimResId, exitAnimResId);
    }
    
    /**
     * 转到绑定在同一 BaseActivity 下指定已实例化的 BaseFragment 对象，并携带要传递参数
     *
     * @param baseFragment 指定的 BaseFragment 对象
     * @param parameter    要传递参数
     */
    public void jump(BaseFragment baseFragment, JumpParameter parameter) {
        if (parameter != null) {
            ParameterCache.getInstance().set(baseFragment.getClass().getName(), parameter);
        }
        me.changeFragment(baseFragment);
    }
    
    /**
     * 跳转到绑定在同一 BaseActivity 下指定角标的 BaseFragment，并携带要传递参数
     *
     * @param index         角标
     * @param jumpParameter 要传递参数
     */
    public void jump(int index, JumpParameter jumpParameter) {
        if (jumpParameter != null) {
            ParameterCache.getInstance().set(me.getFragmentChangeUtil().getFragment(index).getClass().getName(), jumpParameter);
        }
        me.changeFragment(index);
    }
    
    private OnJumpResponseListener onJumpResponseListener;
    
    /**
     * 转到绑定在同一 BaseActivity 下指定已实例化的 BaseFragment 对象，并携带要传递参数和回调
     *
     * @param baseFragment           指定的 BaseFragment 对象
     * @param jumpParameter          要传递参数
     * @param onJumpResponseListener 回调
     */
    public void jump(BaseFragment baseFragment, JumpParameter jumpParameter, OnJumpResponseListener onJumpResponseListener) {
        ParameterCache.getInstance().cleanResponse(this.getClass().getName());
        if (jumpParameter == null) {
            jumpParameter = new JumpParameter();
        }
        this.onJumpResponseListener = onJumpResponseListener;
        ParameterCache.getInstance().set(baseFragment.getClass().getName(),
                jumpParameter.put("needResponse", true).put("responseClassName", getInstanceKey())
        );
        me.changeFragment(baseFragment);
    }
    
    /**
     * 跳转到绑定在同一 BaseActivity 下指定角标的 BaseFragment，并携带要传递参数和回调
     *
     * @param index                  角标
     * @param jumpParameter          要传递参数
     * @param onJumpResponseListener 回调
     */
    public void jump(int index, JumpParameter jumpParameter, OnJumpResponseListener onJumpResponseListener) {
        ParameterCache.getInstance().cleanResponse(this.getClass().getName());
        if (jumpParameter == null) {
            jumpParameter = new JumpParameter();
        }
        this.onJumpResponseListener = onJumpResponseListener;
        ParameterCache.getInstance().set(me.getFragmentChangeUtil().getFragment(index).getClass().getName(),
                jumpParameter.put("needResponse", true).put("responseClassName", getInstanceKey())
        );
        me.changeFragment(index);
    }
    
    public void jump(int index, OnJumpResponseListener onJumpResponseListener) {
        ParameterCache.getInstance().cleanResponse(this.getClass().getName());
        JumpParameter jumpParameter = new JumpParameter();
        this.onJumpResponseListener = onJumpResponseListener;
        ParameterCache.getInstance().set(me.getFragmentChangeUtil().getFragment(index).getClass().getName(),
                jumpParameter.put("needResponse", true).put("responseClassName", getInstanceKey())
        );
        me.changeFragment(index);
    }
    
    //目标Fragment：设定要返回的数据
    public void setFragmentResponse(JumpParameter jumpParameter) {
        FragmentChangeUtil fragmentChangeUtil = me.getFragmentChangeUtil();
        BaseFragment baseFragment = fragmentChangeUtil.getFragment(getFragmentParameter().getString("responseClassName"));
        if (baseFragment != null) {
            baseFragment.setResponseMessage(jumpParameter);
        }
    }
    
    private Runnable waitResponseRunnable;
    
    protected void setResponseMessage(JumpParameter jumpParameter) {
        log(getClass().getName() + ".setResponseMessage: " + jumpParameter);
        waitResponseRunnable = new Runnable() {
            @Override
            public void run() {
                if (onJumpResponseListener != null) {
                    JumpParameter responseData = jumpParameter;
                    if (responseData == null) {
                        responseData = new JumpParameter();
                    }
                    onJumpResponseListener.OnResponse(responseData);
                    onJumpResponseListener = null;
                }
            }
        };
        if (isActive) {
            runOnMain(waitResponseRunnable);
            waitResponseRunnable = null;
        }
    }
    
    public JumpParameter getFragmentParameter() {
        JumpParameter jumpParameter = ParameterCache.getInstance().get(this.getClass().getName());
        if (jumpParameter == null) {
            jumpParameter = new JumpParameter();
        }
        return jumpParameter;
    }
    
    //目标Fragment：设定要返回的数据，写法2
    public void returnFragmentParameter(JumpParameter parameter) {
        setFragmentResponse(parameter);
    }
    
    //大型打印使用，Log默认是有字数限制的，如有需要打印更长的文本可以使用此方法
    public void bigLog(String msg) {
        me.bigLog(msg);
    }
    
    //目标Activity：设定要返回的数据
    public void setResponse(JumpParameter jumpParameter) {
        me.setResponse(jumpParameter);
    }
    
    //获取跳转参数
    public JumpParameter getParameter() {
        return me.getParameter();
    }
    
    //跳转动画
    public void jumpAnim(int enterAnim, int exitAnim) {
        int version = Integer.valueOf(Build.VERSION.SDK_INT);
        if (version > 5) {
            me.overridePendingTransition(enterAnim, exitAnim);
        }
    }
    
    //权限申请
    public void requestPermission(String[] permissions, OnPermissionResponseListener onPermissionResponseListener) {
        me.requestPermission(permissions, onPermissionResponseListener);
    }
    
    //多权限检查
    public boolean checkPermissions(String[] permissions) {
        return me.checkPermissions(permissions);
    }
    
    //单权限检查
    public boolean checkPermissions(String permission) {
        return me.checkPermissions(permission);
    }
    
    public void runOnMain(Runnable runnable) {
        me.runOnMain(runnable);
    }
    
    public void runOnMainDelayed(Runnable runnable, long time) {
        me.runOnMainDelayed(runnable, time);
    }
    
    public void runDelayed(Runnable runnable, long time) {
        me.runDelayed(runnable, time);
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
    
    /**
     * 复制文本到剪贴板
     *
     * @param s 要复制的文本
     * @return 是否成功复制
     */
    public boolean copy(String s) {
        return me.copy(s);
    }
    
    /**
     * 软键盘打开与收起
     *
     * @param show     打开还是关闭
     * @param editText 必须依赖一个 EditText
     */
    public void setIMMStatus(boolean show, EditText editText) {
        me.showIME(show, editText);
    }
    
    public void showIME(@NonNull EditText editText) {
        me.showIME(editText);
    }
    
    public void hideIME(@Nullable EditText editText) {
        me.hideIME(editText);
    }
    
    /**
     * 不推荐使用 onResume
     * 建议使用 {@link #onLoad()} 或 {@link #onShow(boolean)} 代替。
     * {@link #onLoad()} 仅会在第一次显示时执行一次
     * {@link #onShow(boolean)} 会在每次恢复显示时执行，其参数 isSwitchFragment 用于判断是否是从其他 Fragment 切换到本界面
     */
    @Override
    @Deprecated
    public void onResume() {
        super.onResume();
        if (isCallShow) {
            if (rootView != null) {
                if (!isLoaded) {
                    isLoaded = true;
                    onLoad();
                }
            }
        }
        if (isLoaded) {
            if (isActive) {
                onShow(isCallShow);
            }
            if (isCallShow) {
                JumpParameter jumpParameter = ParameterCache.getInstance().get(this.getClass().getName());
                if (jumpParameter != null) {
                    onParameterReceived(jumpParameter);
                }
            }
        }
        if (waitResponseRunnable != null) {
            runOnMain(waitResponseRunnable);
            waitResponseRunnable = null;
        }
        isCallShow = false;
        if (lifeCircleListener != null) {
            lifeCircleListener.onResume();
        }
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
    public void onPause() {
        if (lifeCircleListener != null) {
            lifeCircleListener.onPause();
        }
        super.onPause();
    }
    
    @Override
    public void onDestroy() {
        if (lifeCircleListener != null) {
            lifeCircleListener.onDestroy();
        }
        for (CycleRunner runnable : cycleTimerList) {
            if (!runnable.isCanceled()) {
                runnable.cancel();
            }
        }
        super.onDestroy();
    }
    
    /**
     * 重写此方法以判断当 Fragment 被切换时触发，请注意此时界面可能被暂停
     */
    public void onHide() {
        isActive = false;
        ParameterCache.getInstance().cleanParameter(this.getClass().getName());
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
        return me.openApp(packageName);
    }
    
    //检测App是否已安装
    public boolean isInstallApp(String packageName) {
        return me.isInstallApp(packageName);
    }
    
    public Bundle getSavedInstanceState() {
        return savedInstanceState;
    }
    
    public int getStatusBarHeight() {
        return me.getStatusBarHeight();
    }
    
    //获取屏幕宽度
    public int getDisplayWidth() {
        return me.getDisplayWidth();
    }
    
    //获取屏幕可用部分高度（屏幕高度-状态栏高度-屏幕底栏高度）
    public int getDisplayHeight() {
        return me.getDisplayHeight();
    }
    
    //获取底栏高度
    public int getNavbarHeight() {
        return me.getNavbarHeight();
    }
    
    //获取真实的屏幕高度，注意判断非0
    public int getRootHeight() {
        return me.getRootHeight();
    }
    
    public String getIMEI() {
        return me.getIMEI();
    }
    
    public String getAndroidId() {
        return me.getAndroidId();
    }
    
    public String getMacAddress() {
        return me.getMacAddress();
    }
    
    //支持最低SDK的getColor方法
    public int getColorS(@ColorRes int id) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return getResources().getColor(id, me.getTheme());
        } else {
            return getResources().getColor(id);
        }
    }
    
    private boolean isLoaded = false;
    private boolean isCallShow = false;
    
    /**
     * 此方法是自动执行的，请勿调用
     */
    public void callShow() {
        isCallShow = true;
        isActive = true;
        if (rootView != null) {
            onResume();
        }
    }
    
    private boolean mAdded;
    
    public void setAdded(boolean isAdded) {
        mAdded = isAdded;
    }
    
    /**
     * 1.isAdded() 根本不靠谱，时而返回 false 搞事情...
     * 2.傻X Google竟然把 isAdded() 给final掉了，无奈改个名字吧
     *
     * @return 是否已经被绑定到 FragmentManager
     */
    public boolean isAddedCompat() {
        return mAdded;
    }
    
    public void click(View v, View.OnClickListener onClickListener) {
        me.click(v, onClickListener);
    }
    
    public boolean onBack() {
        return false;
    }
    
    public String getInstanceKey() {
        return getClass().getName() + "@" + Integer.toHexString(hashCode());
    }
    
    public void startActivityForResult(Intent intent, ActivityResultCallback activityResultCallback) {
        me.startActivityForResult(intent, activityResultCallback);
    }
    
    public void startActivityForResult(Intent intent, ActivityResultCallback activityResultCallback, @Nullable Bundle options) {
        me.startActivityForResult(intent, activityResultCallback, options);
    }
}