package com.kongzue.baseframework;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.kongzue.baseframework.interfaces.Layout;
import com.kongzue.baseframework.util.OnPermissionResponseListener;
import com.kongzue.baseframework.util.OnResponseListener;
import com.kongzue.baseframework.util.Parameter;
import com.kongzue.baseframework.util.ParameterCache;

/**
 * @Version:    6.4.5
 * @Author:     Kongzue
 * @github:     https://github.com/kongzue/BaseFramework
 * @link:       http://kongzue.com/
 * @describe:   自动化代码流水线作业，以及对原生安卓、MIUI、flyme的透明状态栏显示灰色图标文字的支持，同时提供一些小工具简化开发难度，详细说明文档：https://github.com/kongzue/BaseFramework
 */

public abstract class BaseFragment extends Fragment {

    public int layoutResId = -1;

    public BaseActivity me;

    public void setActivity(BaseActivity activity) {
        this.me = activity;
    }

    public BaseFragment() {
    }

    public View rootView;       //根布局，可以直接用

    @Deprecated
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
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
        rootView = LayoutInflater.from(getActivity()).inflate(layoutResId, container, false);
        me = (BaseActivity) getActivity();
        initViews();
        initDatas();
        setEvents();
        return rootView;
    }

    //不再推荐使用，建议直接在Fragment上注解：@Layout(你的layout资源id)
    @Deprecated
    public int getLayout() {
        return layoutResId;
    }

    public abstract void initViews();

    public abstract void initDatas();

    public abstract void setEvents();

    public final <T extends View> T findViewById(@IdRes int id) {
        return rootView.findViewById(id);
    }

    private Toast toast;

    protected void runOnMain(Runnable runnable) {
        me.runOnUiThread(runnable);
    }

    protected final static String NULL = "";

    //简易吐司
    public void toast(final Object obj) {
        try {
            runOnMain(new Runnable() {

                @Override
                public void run() {
                    if (toast == null)
                        toast = Toast.makeText(me, NULL, Toast.LENGTH_SHORT);
                    toast.setText(obj.toString());
                    toast.show();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void log(final Object var1) {
        try {
            runOnMain(new Runnable() {
                public void run() {
                    Log.i("log", var1.toString());
                }
            });
        } catch (Exception var3) {
            var3.printStackTrace();
        }
    }

    //位移动画
    public void moveAnimation(Object obj, String perference, float aimValue, long time, long delay) {
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(obj, perference, aimValue);
        objectAnimator.setDuration(time);
        objectAnimator.setStartDelay(delay);
        objectAnimator.start();
    }

    public void moveAnimation(Object obj, String perference, float aimValue, long time) {
        moveAnimation(obj, perference, aimValue, time, 0);
    }

    public void moveAnimation(Object obj, String perference, float aimValue) {
        moveAnimation(obj, perference, aimValue, 300, 0);
    }

    //用于进行dip和px转换
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    //用于进行px和dip转换
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
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
    public boolean jump(Class<?> cls, Parameter parameter) {
        return me.jump(cls, parameter);
    }

    //带返回值的跳转
    public boolean jump(Class<?> cls, OnResponseListener onResponseListener) {
        return me.jump(cls, onResponseListener);
    }

    //带返回值的跳转
    public boolean jump(Class<?> cls, Parameter parameter, OnResponseListener onResponseListener) {
        return me.jump(cls, parameter, onResponseListener);
    }

    //目标Activity：设定要返回的数据
    public void setResponse(Parameter parameter) {
        me.setResponse(parameter);
    }

    //获取跳转参数
    public Parameter getParameter() {
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
}

