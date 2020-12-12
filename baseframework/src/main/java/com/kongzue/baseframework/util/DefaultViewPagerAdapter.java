package com.kongzue.baseframework.util;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.kongzue.baseframework.BaseFragment;

import java.util.List;

/**
 * @author: Kongzue
 * @github: https://github.com/kongzue/
 * @homepage: http://kongzue.com/
 * @mail: myzcxhh@live.cn
 * @createTime: 2020/11/20 13:22
 */
public class DefaultViewPagerAdapter extends FragmentPagerAdapter {
    
    private List<BaseFragment> viewList;
    
    public DefaultViewPagerAdapter(FragmentManager fm, List<BaseFragment> viewList) {
        super(fm);
        this.viewList = viewList;
    }
    
    @Override
    public int getCount() {
        return viewList.size();
    }
    
    @Override
    public Fragment getItem(int position) {
        BaseFragment fragment = viewList.get(position);
        return fragment;
    }
}