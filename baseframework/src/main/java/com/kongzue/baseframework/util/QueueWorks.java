package com.kongzue.baseframework.util;

import android.util.Log;

import com.kongzue.baseframework.BaseActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Author: @Kongzue
 * Github: https://github.com/kongzue/
 * Homepage: http://kongzue.com/
 * Mail: myzcxhh@live.cn
 * CreateTime: 2024/3/7 03:15
 */
public abstract class QueueWorks<A extends BaseActivity> {

    private static Map<String, List<QueueWorks>> workBag = new HashMap<>();
    private static Set<String> working = new HashSet<>();

    public static void addWork(Class<? extends BaseActivity> clazz, QueueWorks works) {
        List<QueueWorks> worksList = workBag.get(clazz.getName());
        if (worksList == null) {
            worksList = new ArrayList<>();
        }
        worksList.add(works);
        workBag.put(clazz.getName(), worksList);
        doNextWork(clazz);
    }

    public static void doNextWork(Class<? extends BaseActivity> clazz) {
        BaseActivity activity = AppManager.getInstance().getActivityInstance(clazz);
        List<QueueWorks> worksList = workBag.get(clazz.getName());
        if (activity != null && worksList != null && !worksList.isEmpty()) {
            if (working.contains(clazz.getName())) {
                return;
            }
            working.add(clazz.getName());
            QueueWorks works = worksList.get(0);
            worksList.remove(works);
            if (activity.isActive) {
                activity.runOnMain(new Runnable() {
                    @Override
                    public void run() {
                        works.callRun(activity);
                    }
                });
            } else {
                activity.runOnResume(new Runnable() {
                    @Override
                    public void run() {
                        works.callRun(activity);
                    }
                });
            }
        }
    }

    Class<? extends BaseActivity> runOnClass;

    private void callRun(A activity) {
        this.runOnClass = activity.getClass();
        run(activity);
    }

    public abstract void run(A me);

    public void finishWork() {
        working.remove(runOnClass.getName());
        doNextWork(runOnClass);
    }

    Object data;

    public <D> D getData() {
        return (D) data;
    }

    public QueueWorks<A> setData(Object data) {
        this.data = data;
        return this;
    }
}
