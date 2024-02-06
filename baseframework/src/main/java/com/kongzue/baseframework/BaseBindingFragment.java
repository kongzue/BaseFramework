package com.kongzue.baseframework;

import android.view.LayoutInflater;
import android.view.View;

import androidx.viewbinding.ViewBinding;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;

public abstract class BaseBindingFragment<ME extends BaseActivity, VB extends ViewBinding> extends BaseFragment<ME> {

    public BaseBindingFragment(VB binding) {
        this.binding = binding;
    }

    public BaseBindingFragment() {
    }

    protected VB binding;

    @Override
    public void initViews() {

    }

    @Override
    public View resetContentView() {
        return userDataBindingCreateLayout();
    }

    @Override
    public abstract void initDatas();

    @Override
    public abstract void setEvents();

    private View userDataBindingCreateLayout() {
        if (binding == null) {
            String bindingClassName = getViewBindClassName();
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
        } else {
            return binding.getRoot();
        }
    }

    private String getViewBindClassName() {
        ParameterizedType genericSuperclass = (ParameterizedType) getClass().getGenericSuperclass();
        String type = genericSuperclass.getActualTypeArguments()[1].toString();
        if (type.contains(" ")) {
            String[] splitType = type.split(" ");
            type = splitType[splitType.length - 1];
        }
        return type;
    }
}
