package com.kongzue.baseframework.util;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.kongzue.baseframework.BaseActivity;
import com.kongzue.baseframework.BaseFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: @Kongzue
 * Github: https://github.com/kongzue/
 * Homepage: http://kongzue.com/
 * Mail: myzcxhh@live.cn
 * CreateTime: 2019/2/20 18:17
 */
public class FragmentChangeUtil {
    
    private BaseActivity me;
    private List<BaseFragment> fragmentList;
    private BaseFragment focusFragment;
    
    private int frameLayoutResId;
    
    public FragmentChangeUtil(BaseActivity me, int frameLayoutResId) {
        build(me, frameLayoutResId);
    }
    
    public FragmentChangeUtil build(BaseActivity me, int frameLayoutResId) {
        if (me.getSavedInstanceState() == null) {
            this.me = me;
            this.frameLayoutResId = frameLayoutResId;
            fragmentList = new ArrayList<>();
        }
        return this;
    }
    
    public FragmentChangeUtil addFragment(BaseFragment fragment) {
        if (me == null || frameLayoutResId == 0 || fragmentList == null) {
            me.log("错误：请先执行 build(...) 方法初始化 FragmentChangeUtil");
            return null;
        }
        me.getSupportFragmentManager().beginTransaction().add(frameLayoutResId, fragment).commit();
        me.getSupportFragmentManager().beginTransaction().hide(fragment).commit();
        fragmentList.add(fragment);
        return this;
    }
    
    public FragmentChangeUtil show(BaseFragment fragment) {
        if (me == null || frameLayoutResId == 0 || fragmentList == null) {
            me.log("错误：请先执行build(...)方法初始化FragmentChangeUtil");
            return null;
        }
        if (!fragmentList.contains(fragment)) {
            me.log("错误：请先执行 addFragment(fragment) 方法将此 Fragment 添加进 FragmentChangeUtil");
            return null;
        }
        FragmentTransaction transaction = me.getSupportFragmentManager().beginTransaction();
        if (focusFragment != null) transaction.hide(focusFragment);
        transaction.show(fragment);
        transaction.commit();
        fragment.callResume();
        focusFragment = fragment;
        return this;
    }
    
    public FragmentChangeUtil show(int index) {
        if (me == null || frameLayoutResId == 0 || fragmentList == null) {
            me.log("错误：请先执行build(...)方法初始化FragmentChangeUtil");
            return null;
        }
        FragmentTransaction transaction = me.getSupportFragmentManager().beginTransaction();
        if (focusFragment != null) transaction.hide(focusFragment);
        transaction.show(fragmentList.get(index));
        transaction.commit();
        fragmentList.get(index).callResume();
        focusFragment = fragmentList.get(index);
        return this;
    }
    
    public FragmentChangeUtil hide(int index) {
        if (me == null || frameLayoutResId == 0 || fragmentList == null) {
            me.log("错误：请先执行build(...)方法初始化FragmentChangeUtil");
            return null;
        }
        FragmentTransaction transaction = me.getSupportFragmentManager().beginTransaction();
        transaction.hide(fragmentList.get(index));
        transaction.commit();
        return this;
    }
    
    public FragmentChangeUtil hide(BaseFragment fragment) {
        if (me == null || frameLayoutResId == 0 || fragmentList == null) {
            me.log("错误：请先执行build(...)方法初始化FragmentChangeUtil");
            return null;
        }
        FragmentTransaction transaction = me.getSupportFragmentManager().beginTransaction();
        transaction.hide(fragment);
        transaction.commit();
        return this;
    }
    
    public int getCount() {
        if (fragmentList == null) {
            me.log("错误：请先执行build(...)方法初始化FragmentChangeUtil");
            return -1;
        }
        return fragmentList.size();
    }
    
    public Fragment getFocusFragment() {
        return focusFragment;
    }
    
    public int getFocusFragmentIndex() {
        if (fragmentList == null) {
            me.log("错误：请先执行build(...)方法初始化FragmentChangeUtil");
            return -1;
        }
        return fragmentList.indexOf(focusFragment);
    }
}
