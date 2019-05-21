package com.kongzue.baseframework;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.ColorRes;
import androidx.annotation.IdRes;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.kongzue.baseframework.interfaces.LifeCircleListener;
import com.kongzue.baseframework.interfaces.Layout;
import com.kongzue.baseframework.util.JumpParameter;
import com.kongzue.baseframework.util.OnPermissionResponseListener;
import com.kongzue.baseframework.util.OnJumpResponseListener;
import com.kongzue.baseframework.util.toast.Toaster;

import static com.kongzue.baseframework.BaseFrameworkSettings.DEBUGMODE;

/**
 * @Version: 6.5.5
 * @Author: Kongzue
 * @github: https://github.com/kongzue/BaseFrameworkSettings
 * @link: http://kongzue.com/
 * @describe: 自动化代码流水线作业，以及对原生安卓、MIUI、flyme的透明状态栏显示灰色图标文字的支持，同时提供一些小工具简化开发难度，详细说明文档：https://github.com/kongzue/BaseFramework
 */

public abstract class BaseFragment<ME extends BaseActivity> extends Fragment {
    
    public int layoutResId = -1;
    
    private LifeCircleListener lifeCircleListener;          //快速管理生命周期
    
    private Bundle savedInstanceState;
    
    public ME me;                                                 //绑定的BaseActivity
    
    public void setActivity(ME activity) {
        this.me = activity;
    }
    
    public BaseFragment() {
    }
    
    public View rootView;       //根布局，可以直接用
    
    @Deprecated
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        me = (ME) getActivity();
        this.savedInstanceState = savedInstanceState;
        try {
            Layout layout = getClass().getAnnotation(Layout.class);
            if (layout == null) {
                layoutResId = getLayout();
            } else {
                if (layout.value() != -1) {
                    layoutResId = layout.value();
                } else {
                    throw new Exception("请在您的Fragment的Class上注解：@Layout(你的layout资源id)");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (rootView == null) {
            rootView = LayoutInflater.from(getActivity()).inflate(layoutResId, container, false);
            rootView.setClickable(true);        //防止点击穿透
        }
        
        if (lifeCircleListener != null) lifeCircleListener.onCreate();
        
        initViews();
        initDatas();
        setEvents();
        return rootView;
    }
    
    public void setLifeCircleListener(LifeCircleListener lifeCircleListener) {
        this.lifeCircleListener = lifeCircleListener;
    }
    
    //不再推荐使用，建议直接在Fragment上注解：@Layout(你的layout资源id)
    @Deprecated
    public int getLayout() {
        return layoutResId;
    }
    
    public abstract void initViews();
    
    public abstract void initDatas();
    
    public abstract void setEvents();
    
    public abstract void onLoad();
    
    public final <T extends View> T findViewById(@IdRes int id) {
        return rootView.findViewById(id);
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
    
    public void error(final Object obj) {
        me.error(obj);
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
        if (s == null || s.trim().isEmpty() || s.equals("null")) {
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
        int version = Integer.valueOf(android.os.Build.VERSION.SDK);
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
    
    //复制文本到剪贴板
    public boolean copy(String s) {
        return me.copy(s);
    }
    
    //软键盘打开与收起
    public void setIMMStatus(boolean show, EditText editText) {
        me.setIMMStatus(show, editText);
    }
    
    @Override
    public void onResume() {
        super.onResume();
        if (lifeCircleListener != null) lifeCircleListener.onResume();
    }
    
    @Override
    public void onPause() {
        if (lifeCircleListener != null) lifeCircleListener.onResume();
        super.onPause();
    }
    
    @Override
    public void onDestroy() {
        if (lifeCircleListener != null) lifeCircleListener.onDestroy();
        super.onDestroy();
    }
    
    //使用默认浏览器打开链接
    public boolean openUrl(String url) {
        try {
            Uri uri = Uri.parse(url);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
            return true;
        } catch (Exception e) {
            if (DEBUGMODE) e.printStackTrace();
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
    
    private boolean isFirstResume = false;
    
    public void callResume() {
        if (rootView != null) {
            onResume();
            if (!isFirstResume) {
                onLoad();
                isFirstResume = true;
            }
        }
    }
}

