package com.kongzue.baseframework.util.swipeback;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.kongzue.baseframework.util.swipeback.util.SwipeBackActivityBase;
import com.kongzue.baseframework.util.swipeback.util.SwipeBackActivityHelper;
import com.kongzue.baseframework.util.swipeback.util.SwipeBackLayout;
import com.kongzue.baseframework.util.swipeback.util.SwipeBackUtil;

/**
 * Author: @Isaac Wang
 * Github: https://github.com/ikew0ng/SwipeBackLayout
 * License: Apache License
 */
public class SwipeBackActivity extends AppCompatActivity implements SwipeBackActivityBase {
    private SwipeBackActivityHelper mHelper;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mHelper = new SwipeBackActivityHelper(this);
        mHelper.onActivityCreate();
    }
    
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mHelper.onPostCreate();
    }
    
    @Override
    public SwipeBackLayout getSwipeBackLayout() {
        return mHelper.getSwipeBackLayout();
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
}
