package com.kongzue.baseframework.util;

import com.kongzue.baseframework.BaseActivity;

/**
 * @author: Kongzue
 * @github: https://github.com/kongzue/
 * @homepage: http://kongzue.com/
 * @mail: myzcxhh@live.cn
 * @createTime: 2022/11/2 2:26
 */
public abstract class OnActivityPermissionCallBack<A extends BaseActivity> implements OnPermissionResponseListener {
    
    A activity;
    
    public abstract void onSuccess(A activity, String[] permissions);
    
    public void onFail(A activity) {
    
    }
    
    public A getActivity() {
        return activity;
    }
    
    public OnActivityPermissionCallBack<A> setActivity(A activity) {
        this.activity = activity;
        return this;
    }
    
    @Override
    @Deprecated
    public void onSuccess(String[] permissions) {
        onSuccess(activity, permissions);
    }
    
    @Override
    @Deprecated
    public void onFail() {
        onFail(activity);
    }
}
