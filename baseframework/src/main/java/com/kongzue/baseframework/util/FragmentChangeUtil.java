package com.kongzue.baseframework.util;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

import com.kongzue.baseframework.BaseActivity;
import com.kongzue.baseframework.BaseFragment;
import com.kongzue.baseframework.interfaces.OnFragmentChangeListener;

import java.util.ArrayList;
import java.util.List;

import static com.kongzue.baseframework.BaseActivity.isNull;
import static com.kongzue.baseframework.BaseFrameworkSettings.DEBUGMODE;

/**
 * Author: @Kongzue
 * Github: https://github.com/kongzue/
 * Homepage: http://kongzue.com/
 * Mail: myzcxhh@live.cn
 * CreateTime: 2019/2/20 18:17
 */
public class FragmentChangeUtil {
    
    private OnFragmentChangeListener onFragmentChangeListener;
    
    private BaseActivity me;
    private List<BaseFragment> fragmentList;
    private BaseFragment focusFragment;
    private int enterAnimResId;
    private int exitAnimResId;
    
    private int frameLayoutResId;
    private View containerView;
    
    private FragmentPagerAdapter fragmentPagerAdapter;
    
    public FragmentChangeUtil(BaseActivity me, int frameLayoutResId) {
        build(me, frameLayoutResId);
    }
    
    public FragmentChangeUtil build(BaseActivity me, int frameLayoutResId) {
        this.me = me;
        this.frameLayoutResId = frameLayoutResId;
        containerView = me.findViewById(frameLayoutResId);
        fragmentList = new ArrayList<>();
        return this;
    }
    
    public FragmentPagerAdapter getFragmentPagerAdapter() {
        return fragmentPagerAdapter;
    }
    
    public FragmentChangeUtil addFragment(BaseFragment fragment) {
        addFragment(fragment, true);
        return this;
    }
    
    public FragmentChangeUtil addFragment(BaseFragment fragment, boolean isPreload) {
        if (me == null || frameLayoutResId == 0 || fragmentList == null) {
            log("错误：请先执行 build(...) 方法初始化 FragmentChangeUtil");
            return null;
        }
        if (isPreload) {
            if (containerView instanceof FrameLayout) {
                me.getSupportFragmentManager().beginTransaction().add(frameLayoutResId, fragment).commit();
                me.getSupportFragmentManager().beginTransaction().hide(fragment).commit();
                fragment.setAdded(true);
            }
        }
        fragmentList.add(fragment);
        if (containerView instanceof ViewPager) {
            ViewPager viewPager = (ViewPager) containerView;
            if (fragmentPagerAdapter == null) {
                focusFragment = fragmentList.get(0);
                fragmentPagerAdapter = new DefaultViewPagerAdapter(me.getSupportFragmentManager(), fragmentList);
                viewPager.setAdapter(fragmentPagerAdapter);
            } else {
                fragmentPagerAdapter.notifyDataSetChanged();
            }
            viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                
                }
                
                @Override
                public void onPageSelected(int position) {
                    if (focusFragment != null) {
                        focusFragment.onHide();
                    }
                    if (onFragmentChangeListener != null) {
                        onFragmentChangeListener.onChange(position, fragmentList.get(position));
                    }
                    fragmentList.get(position).callShow();
                    focusFragment = fragmentList.get(position);
                }
                
                @Override
                public void onPageScrollStateChanged(int state) {
                
                }
            });
        }
        return this;
    }
    
    public FragmentChangeUtil show(BaseFragment fragment) {
        show(fragment, true);
        return this;
    }
    
    public FragmentChangeUtil show(BaseFragment fragment, boolean autoHideOldFragment) {
        if (me == null || frameLayoutResId == 0 || fragmentList == null) {
            log("错误：请先执行build(...)方法初始化FragmentChangeUtil");
            return null;
        }
        if (!fragmentList.contains(fragment)) {
            log("错误：请先执行 addFragment(fragment) 方法将此 Fragment 添加进 FragmentChangeUtil");
            return null;
        }
        if (containerView instanceof FrameLayout) {
            FragmentTransaction transaction = me.getSupportFragmentManager().beginTransaction();
            if (enterAnimResId != 0 && exitAnimResId != 0) {
                transaction.setCustomAnimations(enterAnimResId, exitAnimResId);
                enterAnimResId = 0;
                exitAnimResId = 0;
            }
            
            if (autoHideOldFragment && focusFragment != null) {
                focusFragment.onHide();
                transaction.hide(focusFragment);
            }
            
            if (!fragment.isAddedCompat()) {
                transaction.add(frameLayoutResId, fragment);
            } else {
                transaction.show(fragment);
                if (onFragmentChangeListener != null) {
                    onFragmentChangeListener.onChange(fragmentList.indexOf(fragment), fragment);
                }
            }
            transaction.commit();
            fragment.callShow();
            focusFragment = fragment;
        }
        if (containerView instanceof ViewPager) {
            if (getFocusFragment() == fragment) {
                if (onFragmentChangeListener != null || focusFragment == null) {
                    onFragmentChangeListener.onChange(fragmentList.indexOf(fragment), fragment);
                }
            }
            ViewPager viewPager = (ViewPager) containerView;
            viewPager.setCurrentItem(fragmentList.indexOf(fragment));
        }
        return this;
    }
    
    public FragmentChangeUtil show(int index) {
        show(index, true);
        return this;
    }
    
    public FragmentChangeUtil show(int index, boolean autoHideOldFragment) {
        if (me == null || frameLayoutResId == 0 || fragmentList == null) {
            log("错误：请先执行build(...)方法初始化FragmentChangeUtil");
            return null;
        }
        if (containerView instanceof FrameLayout) {
            FragmentTransaction transaction = me.getSupportFragmentManager().beginTransaction();
            if (enterAnimResId != 0 && exitAnimResId != 0) {
                transaction.setCustomAnimations(enterAnimResId, exitAnimResId);
                enterAnimResId = 0;
                exitAnimResId = 0;
            }
            
            if (autoHideOldFragment && focusFragment != null) {
                focusFragment.onHide();
                transaction.hide(focusFragment);
            }
            
            if (!fragmentList.get(index).isAddedCompat()) {
                transaction.add(frameLayoutResId, fragmentList.get(index));
            } else {
                if (onFragmentChangeListener != null) {
                    onFragmentChangeListener.onChange(index, fragmentList.get(index));
                }
                transaction.show(fragmentList.get(index));
            }
            
            transaction.commit();
            fragmentList.get(index).callShow();
            focusFragment = fragmentList.get(index);
        }
        if (containerView instanceof ViewPager) {
            if (getFocusFragmentIndex() == index || focusFragment == null) {
                if (onFragmentChangeListener != null) {
                    onFragmentChangeListener.onChange(index, fragmentList.get(index));
                }
            }
            ViewPager viewPager = (ViewPager) containerView;
            viewPager.setCurrentItem(index);
        }
        return this;
    }
    
    public FragmentChangeUtil hide(int index) {
        if (me == null || frameLayoutResId == 0 || fragmentList == null) {
            log("错误：请先执行build(...)方法初始化FragmentChangeUtil");
            return null;
        }
        if (containerView instanceof FrameLayout) {
            FragmentTransaction transaction = me.getSupportFragmentManager().beginTransaction();
            if (enterAnimResId != 0 && exitAnimResId != 0) {
                transaction.setCustomAnimations(enterAnimResId, exitAnimResId);
                enterAnimResId = 0;
                exitAnimResId = 0;
            }
            
            if (!fragmentList.get(index).isAddedCompat()) {
                transaction.add(frameLayoutResId, fragmentList.get(index));
            }
            fragmentList.get(index).onHide();
            transaction.hide(fragmentList.get(index));
            
            transaction.commit();
        }
        if (containerView instanceof ViewPager) {
            ViewPager viewPager = (ViewPager) containerView;
            viewPager.setCurrentItem(0);
        }
        return this;
    }
    
    public FragmentChangeUtil hideNow() {
        if (me == null || frameLayoutResId == 0 || fragmentList == null) {
            log("错误：请先执行build(...)方法初始化FragmentChangeUtil");
            return null;
        }
        if (containerView instanceof FrameLayout) {
            FragmentTransaction transaction = me.getSupportFragmentManager().beginTransaction();
            if (enterAnimResId != 0 && exitAnimResId != 0) {
                transaction.setCustomAnimations(enterAnimResId, exitAnimResId);
                enterAnimResId = 0;
                exitAnimResId = 0;
            }
            
            focusFragment.onHide();
            transaction.hide(focusFragment);
            
            transaction.commit();
        }
        if (containerView instanceof ViewPager) {
            ViewPager viewPager = (ViewPager) containerView;
            viewPager.setCurrentItem(0);
        }
        return this;
    }
    
    public FragmentChangeUtil hide(BaseFragment fragment) {
        if (me == null || frameLayoutResId == 0 || fragmentList == null) {
            log("错误：请先执行build(...)方法初始化FragmentChangeUtil");
            return null;
        }
        if (containerView instanceof FrameLayout) {
            FragmentTransaction transaction = me.getSupportFragmentManager().beginTransaction();
            if (enterAnimResId != 0 && exitAnimResId != 0) {
                transaction.setCustomAnimations(enterAnimResId, exitAnimResId);
                enterAnimResId = 0;
                exitAnimResId = 0;
            }
            
            if (!fragment.isAddedCompat()) {
                transaction.add(frameLayoutResId, fragment);
            }
            fragment.onHide();
            transaction.hide(fragment);
            
            transaction.commit();
        }
        if (containerView instanceof ViewPager) {
            ViewPager viewPager = (ViewPager) containerView;
            viewPager.setCurrentItem(0);
        }
        return this;
    }
    
    public FragmentChangeUtil remove(BaseFragment fragment) {
        if (me == null || frameLayoutResId == 0 || fragmentList == null) {
            log("错误：请先执行build(...)方法初始化FragmentChangeUtil");
            return null;
        }
        if (containerView instanceof FrameLayout) {
            FragmentTransaction transaction = me.getSupportFragmentManager().beginTransaction();
            if (enterAnimResId != 0 && exitAnimResId != 0) {
                transaction.setCustomAnimations(enterAnimResId, exitAnimResId);
                enterAnimResId = 0;
                exitAnimResId = 0;
            }
            
            if (fragment.isAddedCompat()) {
                transaction.remove(fragment);
            }
            fragment.setAdded(false);
            fragmentList.remove(fragment);
            
            transaction.commit();
        }
        if (containerView instanceof ViewPager) {
            fragmentList.remove(fragment);
            fragmentPagerAdapter.notifyDataSetChanged();
        }
        return this;
    }
    
    public int getCount() {
        if (fragmentList == null) {
            log("错误：请先执行build(...)方法初始化FragmentChangeUtil");
            return -1;
        }
        return fragmentList.size();
    }
    
    public BaseFragment getFocusFragment() {
        return focusFragment;
    }
    
    public FragmentChangeUtil setFocusFragment(BaseFragment fragment) {
        focusFragment = fragment;
        return this;
    }
    
    public int getFocusFragmentIndex() {
        if (fragmentList == null) {
            log("错误：请先执行build(...)方法初始化FragmentChangeUtil");
            return -1;
        }
        return fragmentList.indexOf(focusFragment);
    }
    
    //简易Log
    public void log(final Object obj) {
        try {
            if (DEBUGMODE) {
                String msg = obj.toString();
                if (isNull(msg)) {
                    return;
                }
                Log.v(">>>>>>", msg);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public BaseFragment getFragment(int index) {
        if (me == null || frameLayoutResId == 0 || fragmentList == null) {
            log("错误：请先执行build(...)方法初始化FragmentChangeUtil");
            return null;
        }
        if (fragmentList.size() < (index + 1)) {
            log("错误：要获取的 index=" + index + " 超出了已添加的列表范围：" + fragmentList.size());
            return null;
        }
        return fragmentList.get(index);
    }
    
    public BaseFragment getFragment(String instanceKey) {
        if (me == null || frameLayoutResId == 0 || fragmentList == null) {
            log("错误：请先执行build(...)方法初始化FragmentChangeUtil");
            return null;
        }
        for (BaseFragment fragment : fragmentList) {
            if (fragment.getInstanceKey().equals(instanceKey)) {
                return fragment;
            }
        }
        return null;
    }
    
    public BaseFragment getFragment(Class c) {
        if (me == null || frameLayoutResId == 0 || fragmentList == null) {
            log("错误：请先执行build(...)方法初始化FragmentChangeUtil");
            return null;
        }
        for (BaseFragment b : fragmentList) {
            if (b.getClass().equals(c)) {
                return b;
            }
        }
        return null;
    }
    
    public OnFragmentChangeListener getOnFragmentChangeListener() {
        return onFragmentChangeListener;
    }
    
    public FragmentChangeUtil setOnFragmentChangeListener(OnFragmentChangeListener onFragmentChangeListener) {
        this.onFragmentChangeListener = onFragmentChangeListener;
        return this;
    }
    
    public int size() {
        if (fragmentList == null) {
            log("错误：请先执行build(...)方法初始化FragmentChangeUtil");
            return 0;
        }
        return fragmentList.size();
    }
    
    public FragmentChangeUtil anim(int enterAnimResId, int exitAnimResId) {
        this.enterAnimResId = enterAnimResId;
        this.exitAnimResId = exitAnimResId;
        return this;
    }
}