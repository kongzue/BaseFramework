package com.kongzue.baseframework.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;

import com.kongzue.baseframework.BaseActivity;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;

/**
 * @link https://xiaohaibin.github.io/
 * @email： xhb_199409@163.com
 * @github: https://github.com/xiaohaibin
 * @describe: Activity管理工具类
 */
public class AppManager {
    private static Stack<BaseActivity> activityStack;
    private static AppManager instance;
    
    private AppManager() {
    }
    
    /**
     * @return 获取activity管理实例
     */
    public static AppManager getInstance() {
        if (instance == null) {
            instance = new AppManager();
        }
        return instance;
    }
    
    /**
     * 添加Activity到堆栈
     */
    public void pushActivity(BaseActivity activity) {
        if (activityStack == null) {
            activityStack = new Stack<BaseActivity>();
        }
        activityStack.add(activity);
    }
    
    /**
     * 获取当前Activity（堆栈中最后一个压入的）
     */
    public BaseActivity currentActivity() {
        BaseActivity activity = null;
        if (!activityStack.empty())
            activity = activityStack.lastElement();
        return activity;
    }
    
    /**
     * 结束当前Activity（堆栈中最后一个压入的）
     */
    public void finishActivity() {
        BaseActivity activity = activityStack.lastElement();
        killActivity(activity);
    }
    
    /**
     * 结束指定的Activity
     */
    public void killActivity(BaseActivity activity) {
        if (activity != null) {
            activity.finishActivity();
            activityStack.remove(activity);
            activity = null;
        }
    }
    
    /**
     * 结束指定类名的Activity
     */
    public void killActivity(Class<?> cls) {
        Iterator<BaseActivity> iterator = activityStack.iterator();
        BaseActivity temp = null;
        while (iterator.hasNext()) {
            BaseActivity activity = iterator.next();
            if (activity.getClass().equals(cls)) {
                temp = activity;
            }
        }
        if (temp != null) {
            temp.finish();
        }
    }
    
    /**
     * 结束所有Activity
     */
    public void killAllActivity() {
        if (activityStack != null) {
            while (!activityStack.empty()) {
                BaseActivity activity = currentActivity();
                killActivity(activity);
            }
            activityStack.clear();
        }
    }
    
    /**
     * 退出应用程序
     */
    @SuppressLint("MissingPermission")
    public void AppExit(Context context) {
        try {
            killAllActivity();
            android.app.ActivityManager activityMgr = (android.app.ActivityManager) context
                    .getSystemService(Context.ACTIVITY_SERVICE);
            activityMgr.killBackgroundProcesses(context.getPackageName());
            System.exit(0);
        } catch (Exception e) {
        }
    }
    
    public void deleteActivity(BaseActivity activity) {
        if (activity != null) {
            activityStack.remove(activity);
        }
    }
    
    /**
     * 关闭所有 {@link Activity},排除指定的 {@link Activity}
     *
     * @param cls activity
     */
    public void killOtherActivityExclude(Class<?>... cls) {
        List<Class<?>> excludeList = Arrays.asList(cls);
        if (activityStack != null) {
            Iterator<BaseActivity> iterator = activityStack.iterator();
            while (iterator.hasNext()) {
                Activity activity = iterator.next();
                if (excludeList.contains(activity.getClass())) {
                    continue;
                }
                iterator.remove();
                activity.finish();
            }
        }
    }
    
    /**
     * 根据类名获取堆栈中最后一个 activity 实例
     *
     * @param cls activity
     */
    public BaseActivity getActivityInstance(Class<?> cls) {
        if (!activityStack.empty()) {
            Stack<BaseActivity> reverseList = new Stack<BaseActivity>();
            reverseList.addAll(activityStack);
            Collections.reverse(reverseList);
            Iterator<BaseActivity> iterator = reverseList.iterator();
            BaseActivity temp = null;
            while (iterator.hasNext()) {
                BaseActivity activity = iterator.next();
                if (activity.getClass().equals(cls)) {
                    temp = activity;
                }
            }
            if (temp != null) {
                return temp;
            }
        }
        return null;
    }
}
