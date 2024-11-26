package com.kongzue.baseframework;

import android.view.LayoutInflater;
import android.view.View;

import androidx.viewbinding.ViewBinding;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

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
        Type superclass = getClass().getGenericSuperclass();
        if (superclass instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) superclass;
            Type type = parameterizedType.getActualTypeArguments()[0];
            try {
                Class<VB> clazz = (Class<VB>) type;
                LayoutInflater layoutInflater = LayoutInflater.from(getContext());
                binding = (VB) clazz.getMethod("inflate", LayoutInflater.class).invoke(null, layoutInflater);
                return binding.getRoot();
            } catch (Exception e) {
                throw new RuntimeException("ViewBinding creation failed", e);
            }
        }
        return null;
    }
}
