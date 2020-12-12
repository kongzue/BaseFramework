package com.kongzue.baseframework.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;

import com.kongzue.baseframework.BaseActivity;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;

/**
 * @author xiaohaibin
 * @link https://xiaohaibin.github.io/
 * @email： xhb_199409@163.com
 * @github: https://github.com/xiaohaibin
 * @describe: Activity管理工具类
 */
public class AppManager {
    
    private static OnActivityStatusChangeListener onActivityStatusChangeListener;
    private static Stack<BaseActivity> activityStack;
    private static AppManager instance;
    private static WeakReference<BaseActivity> activeActivity;
    
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
        if (onActivityStatusChangeListener != null) {
            onActivityStatusChangeListener.onActivityCreate(activity);
        }
    }
    
    /**
     * 获取当前Activity（堆栈中最后一个压入的）
     */
    public BaseActivity currentActivity() {
        if (activityStack == null) {
            return null;
        }
        BaseActivity activity = null;
        if (!activityStack.empty()) {
            activity = activityStack.lastElement();
        }
        return activity;
    }
    
    /**
     * 获取当前处于活动状态的Activity
     *
     * @return
     */
    public BaseActivity getActiveActivity() {
        if (activeActivity == null) {
            return null;
        }
        return activeActivity.get();
    }
    
    /**
     * 结束当前Activity（堆栈中最后一个压入的）
     */
    public void finishActivity() {
        if (activityStack == null) {
            return;
        }
        BaseActivity activity = activityStack.lastElement();
        killActivity(activity);
    }
    
    /**
     * 结束指定的Activity
     */
    public void killActivity(BaseActivity activity) {
        if (activity != null && activityStack != null) {
            if (activity != null) {
                activity.finishActivity();
            }
            activityStack.remove(activity);
            activity = null;
        }
    }
    
    /**
     * 结束指定类名的Activity
     */
    public void killActivity(Class<?> cls) {
        if (activityStack == null) {
            return;
        }
        Iterator<BaseActivity> iterator = activityStack.iterator();
        BaseActivity temp = null;
        while (iterator.hasNext()) {
            BaseActivity activity = iterator.next();
            if (activity != null && activity.getClass().equals(cls)) {
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
    public void exit(Context context) {
        try {
            killAllActivity();
            android.app.ActivityManager activityMgr = (android.app.ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            activityMgr.killBackgroundProcesses(context.getPackageName());
            System.exit(0);
        } catch (Exception e) {
        }
    }
    
    @Deprecated
    public void AppExit(Context context) {
        exit(context);
    }
    
    public void deleteActivity(BaseActivity activity) {
        try {
            if (activity != null && activityStack != null) {
                activityStack.remove(activity);
                if (onActivityStatusChangeListener != null) {
                    onActivityStatusChangeListener.onActivityDestroy(activity);
                    if (activityStack.isEmpty()) {
                        onActivityStatusChangeListener.onAllActivityClose();
                    }
                }
            }
        } catch (Exception e) {
        
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
            Stack<BaseActivity> activityStackCache = new Stack<>();
            activityStackCache.addAll(activityStack);
            Iterator<BaseActivity> iterator = activityStackCache.iterator();
            while (iterator.hasNext()) {
                BaseActivity activity = iterator.next();
                if (activity != null && excludeList.contains(activity.getClass())) {
                    continue;
                }
                iterator.remove();
                if (activity != null) {
                    activity.finish();
                }
            }
        }
    }
    
    /**
     * 根据类名获取堆栈中最后一个 activity 实例
     *
     * @param cls activity
     */
    public BaseActivity getActivityInstance(Class<?> cls) {
        if (activityStack == null) {
            return null;
        }
        if (!activityStack.empty()) {
            Stack<BaseActivity> reverseList = new Stack<BaseActivity>();
            reverseList.addAll(activityStack);
            Collections.reverse(reverseList);
            Iterator<BaseActivity> iterator = reverseList.iterator();
            BaseActivity temp = null;
            while (iterator.hasNext()) {
                BaseActivity activity = iterator.next();
                if (activity != null && activity.getClass().equals(cls)) {
                    temp = activity;
                }
            }
            if (temp != null) {
                return temp;
            }
        }
        return null;
    }
    
    public BaseActivity getActivityInstance(String instanceKey) {
        if (activityStack == null) {
            return null;
        }
        if (!activityStack.empty()) {
            Stack<BaseActivity> reverseList = new Stack<BaseActivity>();
            reverseList.addAll(activityStack);
            Collections.reverse(reverseList);
            Iterator<BaseActivity> iterator = reverseList.iterator();
            BaseActivity temp = null;
            while (iterator.hasNext()) {
                BaseActivity activity = iterator.next();
                if (activity != null && activity.getInstanceKey().equals(instanceKey)) {
                    temp = activity;
                }
            }
            if (temp != null) {
                return temp;
            }
        }
        return null;
    }
    
    public void onDestroy() {
        activityStack = new Stack<>();
    }
    
    public static Stack<BaseActivity> getActivityStack() {
        return activityStack;
    }
    
    public static OnActivityStatusChangeListener getOnActivityStatusChangeListener() {
        return onActivityStatusChangeListener;
    }
    
    public static void setOnActivityStatusChangeListener(OnActivityStatusChangeListener onActivityStatusChangeListener) {
        AppManager.onActivityStatusChangeListener = onActivityStatusChangeListener;
    }
    
    public interface OnActivityStatusChangeListener {
        void onActivityCreate(BaseActivity activity);
        
        void onActivityDestroy(BaseActivity activity);
        
        void onAllActivityClose();
    }
    
    public static void setActiveActivity(BaseActivity activeActivity) {
        AppManager.activeActivity = new WeakReference<>(activeActivity);
    }
}