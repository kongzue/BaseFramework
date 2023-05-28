package com.kongzue.baseframework.util;

import static com.kongzue.baseframework.BaseApp.isNull;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.asynclayoutinflater.view.AsyncLayoutInflater;

import com.kongzue.baseframework.BaseActivity;
import com.kongzue.baseframework.BaseApp;
import com.kongzue.baseframework.BaseFragment;
import com.kongzue.baseframework.interfaces.Layout;

import java.util.HashMap;
import java.util.Map;

public class AsyncActivityLayoutLoader {
    static Map<String, ViewPackage> cachedView;

    private static void preCreateActivityLayoutCache(String baseActivityName, int layoutId) {
        if (layoutId == 0 || isNull(baseActivityName)) {
            return;
        }
        if (cachedView == null) {
            cachedView = new HashMap<>();
        }
        new AsyncLayoutInflater(BaseApp.getPrivateInstance()).inflate(layoutId, null, new AsyncLayoutInflater.OnInflateFinishedListener() {
            @Override
            public void onInflateFinished(@NonNull View view, int resId, @Nullable ViewGroup parent) {
                cachedView.put(baseActivityName, new ViewPackage(view, resId));
            }
        });
    }

    public static void preCreateActivityLayoutCache(Class baseActivityClass) {
        String baseActivityName = baseActivityClass.getName();
        Layout layout = (Layout) baseActivityClass.getAnnotation(Layout.class);
        if (layout != null) {
            if (layout.value() != -1) {
                preCreateActivityLayoutCache(baseActivityName, layout.value());
            }
        }
    }

    public static View getActivityLayout(String baseActivityName) {
        ViewPackage pkg = cachedView.get(baseActivityName);
        if (pkg != null && pkg.getView() != null) {
            View view = pkg.getView();
            cachedView.remove(baseActivityName);
            preCreateActivityLayoutCache(baseActivityName, pkg.resId);
            pkg.cleanAll();
            return view;
        }
        return null;
    }

    public static void clearActivityLayoutCache() {
        cachedView.clear();
        cachedView = null;
    }

    static class ViewPackage {
        View view;
        int resId;

        ViewPackage(View view, int resId) {
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
