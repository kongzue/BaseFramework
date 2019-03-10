package com.kongzue.baseframework.util.toast;

/**
 * Author: @Kongzue
 * Github: https://github.com/kongzue/
 * Homepage: http://kongzue.com/
 * Mail: myzcxhh@live.cn
 * CreateTime: 2019/3/10 12:30
 */
public abstract class BaseToast {
    
    public abstract BaseToast show(String msg);
    
    public abstract BaseToast show(int layoutResId);
    
    public abstract BaseToast cancel();
    
}
