package com.kongzue.baseframework.util;

import static com.kongzue.baseframework.BaseApp.isNull;

import android.content.Context;
import android.content.MutableContextWrapper;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.kongzue.baseframework.BaseApp;
import com.kongzue.baseframework.interfaces.Layout;

import java.util.HashMap;
import java.util.Map;

public class AsyncActivityLayoutLoader {

    static Map<String, ViewWrapper> cachedView;
    public static boolean showDebugLog = true;

    private static void preCreateActivityLayoutCache(Context context, String baseActivityName, int layoutId) {
        if (layoutId == 0 || isNull(baseActivityName)) {
            return;
        }
        if (cachedView == null) {
            cachedView = new HashMap<>();
        }
        new AsyncLayoutInflater(context).inflate(layoutId, null, new AsyncLayoutInflater.OnInflateFinishedListener() {
            @Override
            public void onInflateFinished(@NonNull View view, int resId, @Nullable ViewGroup parent) {
                cachedView.put(baseActivityName, new ViewWrapper(view, resId));
                if (showDebugLog)
                    Log.w(">>>", "AsyncActivityLayoutLoader: " + baseActivityName + " is already cached");
            }
        });
    }

    public static void preCreateActivityLayoutCache(Context context, Class baseActivityClass) {
        String baseActivityName = baseActivityClass.getName();
        Layout layout = (Layout) baseActivityClass.getAnnotation(Layout.class);
        if (layout != null) {
            if (layout.value() != -1) {
                preCreateActivityLayoutCache(context, baseActivityName, layout.value());
            }
        }
    }

    public static void preCreateActivityLayoutCache(Class baseActivityClass) {
        String baseActivityName = baseActivityClass.getName();
        Layout layout = (Layout) baseActivityClass.getAnnotation(Layout.class);
        if (layout != null) {
            if (layout.value() != -1) {
                preCreateActivityLayoutCache(new MutableContextWrapper(BaseApp.getPrivateInstance()), baseActivityName, layout.value());
            }
        }
    }

    public static View getActivityLayout(Context context, String baseActivityName) {
        if (cachedView == null) {
            return null;
        }
        ViewWrapper pkg = cachedView.get(baseActivityName);
        if (pkg != null && pkg.getView() != null) {
            View view = pkg.getView();
            if (view.getContext() instanceof MutableContextWrapper) {
                MutableContextWrapper contextWrapper = (MutableContextWrapper) view.getContext();
                contextWrapper.setBaseContext(context);
            }
            if (showDebugLog)
                Log.w(">>>", "AsyncActivityLayoutLoader: " + baseActivityName + " is used from cache");
            cachedView.remove(baseActivityName);
            preCreateActivityLayoutCache(context, baseActivityName, pkg.resId);
            pkg.cleanAll();
            return view;
        }
        return null;
    }

    public static void clearActivityLayoutCache() {
        cachedView.clear();
        cachedView = null;
    }

    static class ViewWrapper {
        View view;
        int resId;

        ViewWrapper(View view, int resId) {
            this.view = view;
            this.resId = resId;
        }

        View getView() {
            return view;
        }

        void cleanAll() {
            view = null;
            resId = 0;
        }
    }
}
