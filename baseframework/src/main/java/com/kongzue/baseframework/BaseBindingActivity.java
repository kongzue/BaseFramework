package com.kongzue.baseframework;

import android.view.LayoutInflater;
import android.view.View;

import androidx.viewbinding.ViewBinding;

import com.kongzue.baseframework.util.JumpParameter;

import java.lang.reflect.Method;

public abstract class BaseBindingActivity<VB extends ViewBinding> extends BaseActivity{

    protected VB binding;

    @Override
    public void initViews() {

    }

    @Override
    public abstract void initDatas(JumpParameter parameter);

    @Override
    public abstract void setEvents();

    @Override
    public View resetContentView() {
        return userDataBindingCreateLayout();
    }

    private View userDataBindingCreateLayout() {
        String className = getClass().getSimpleName();
        if (className.endsWith("Activity")) {
            className = className.substring(0, className.length() - 8);
        }
        String bindingClassName = getPackageName() + ".databinding.Activity" + className + "Binding";

        try {
            // 通过反射实例化Binding对象
            Class<?> bindingClass = Class.forName(bindingClassName);
            Method inflateMethod = bindingClass.getMethod("inflate", LayoutInflater.class);
            binding = (VB) inflateMethod.invoke(null, getLayoutInflater());

            return binding.getRoot();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
