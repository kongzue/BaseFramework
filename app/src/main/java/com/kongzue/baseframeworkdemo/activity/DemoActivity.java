package com.kongzue.baseframeworkdemo.activity;

import android.content.Intent;
import android.view.View;

import androidx.annotation.Nullable;

import com.kongzue.baseframework.BaseActivity;
import com.kongzue.baseframework.BaseFragment;
import com.kongzue.baseframework.interfaces.ActivityResultCallback;
import com.kongzue.baseframework.interfaces.BindView;
import com.kongzue.baseframework.interfaces.DarkNavigationBarTheme;
import com.kongzue.baseframework.interfaces.DarkStatusBarTheme;
import com.kongzue.baseframework.interfaces.FragmentLayout;
import com.kongzue.baseframework.interfaces.Layout;
import com.kongzue.baseframework.interfaces.NavigationBarBackgroundColor;
import com.kongzue.baseframework.interfaces.NavigationBarBackgroundColorRes;
import com.kongzue.baseframework.interfaces.OnClicks;
import com.kongzue.baseframework.interfaces.OnFragmentChangeListener;
import com.kongzue.baseframework.util.FragmentChangeUtil;
import com.kongzue.baseframework.util.JumpParameter;
import com.kongzue.baseframeworkdemo.R;
import com.kongzue.baseframeworkdemo.fragment.AboutFragment;
import com.kongzue.baseframeworkdemo.fragment.FunctionFragment;
import com.kongzue.baseframeworkdemo.fragment.IntroductionFragment;
import com.kongzue.tabbar.Tab;
import com.kongzue.tabbar.TabBarView;
import com.kongzue.tabbar.interfaces.OnTabChangeListener;

import java.util.ArrayList;
import java.util.List;

@Layout(R.layout.activity_demo)
@DarkStatusBarTheme(false)
@NavigationBarBackgroundColorRes(R.color.colorWhite)
@DarkNavigationBarTheme(true)
@FragmentLayout(R.id.viewPager)
public class DemoActivity extends BaseActivity {
    
    private IntroductionFragment introductionFragment = new IntroductionFragment();
    private FunctionFragment functionFragment = new FunctionFragment();
    private AboutFragment aboutFragment = new AboutFragment();
    
    @BindView(R.id.tabbar)
    private TabBarView tabbar;
    
    @Override
    public void initViews() {
        tabbar = findViewById(R.id.tabbar);
        
        List<Tab> tabs = new ArrayList<>();
        tabs.add(new Tab(this, getString(R.string.introduction), R.mipmap.img_tab_introduction));
        tabs.add(new Tab(this, getString(R.string.function), R.mipmap.img_maintab_function));
        tabs.add(new Tab(this, getString(R.string.github), R.mipmap.img_maintab_me));
        tabbar.setTab(tabs);
    }
    
    @Override
    public void initDatas(JumpParameter parameter) {
    }
    
    @Override
    public void initFragment(FragmentChangeUtil fragmentChangeUtil) {
        fragmentChangeUtil.addFragment(introductionFragment);
        fragmentChangeUtil.addFragment(functionFragment);
        fragmentChangeUtil.addFragment(aboutFragment);
        
        getFragmentChangeUtil().setOnFragmentChangeListener(new OnFragmentChangeListener() {
            @Override
            public void onChange(int index, BaseFragment fragment) {
                log("index:" + index);
                tabbar.setNormalFocusIndex(index);
            }
        });
        
        changeFragment(0);
    }
    
    @Override
    public void setEvents() {
        tabbar.setOnTabChangeListener(new OnTabChangeListener() {
            @Override
            public void onTabChanged(View v, int index) {
                changeFragment(index);
            }
        });
    }
}
