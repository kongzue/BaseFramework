package com.kongzue.baseframework;

import android.view.LayoutInflater;
import android.view.View;

import androidx.viewbinding.ViewBinding;

import com.kongzue.baseframework.util.JumpParameter;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public abstract class BaseBindingActivity<VB extends ViewBinding> extends BaseActivity {

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
        Type superclass = getClass().getGenericSuperclass();
        if (superclass instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) superclass;
            Type type = parameterizedType.getActualTypeArguments()[0];
            try {
                Class<VB> clazz = (Class<VB>) type;
                LayoutInflater layoutInflater = LayoutInflater.from(this);
                binding = (VB) clazz.getMethod("inflate", LayoutInflater.class).invoke(null, layoutInflater);
                return binding.getRoot();
            } catch (Exception e) {
                throw new RuntimeException("ViewBinding creation failed", e);
            }
        }
        return null;
    }
}
