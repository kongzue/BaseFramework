package com.kongzue.baseframework;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

/**
 * Ver.6.1.0
 * 自动化代码流水线作业
 * 超级简单的布局绑定
 * 同时提供一些小工具简化开发难度
 * 详细说明文档：https://github.com/kongzue/BaseFramework
 */

public abstract class BaseFragment extends Fragment {

    public BaseActivity me;

    public void setActivity(BaseActivity activity) {
        this.me = activity;
    }

    public BaseFragment() {
    }

    public View rootView;

    @Deprecated
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        int resId = getLayout();
        if (resId == 0) {
            new Exception("getLayout()需要被重写");
        } else {
            rootView = LayoutInflater.from(getActivity()).inflate(resId, container, false);
        }
        me = (BaseActivity) getActivity();
        initViews();
        initDatas();
        setEvents();
        return rootView;
    }

    //可被重写的接口
    public abstract int getLayout();

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
}

