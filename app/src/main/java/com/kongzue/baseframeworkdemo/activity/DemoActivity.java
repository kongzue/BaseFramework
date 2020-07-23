package com.kongzue.baseframeworkdemo.activity;

import android.view.View;

import com.kongzue.baseframework.BaseActivity;
import com.kongzue.baseframework.BaseFragment;
import com.kongzue.baseframework.interfaces.BindView;
import com.kongzue.baseframework.interfaces.DarkNavigationBarTheme;
import com.kongzue.baseframework.interfaces.DarkStatusBarTheme;
import com.kongzue.baseframework.interfaces.FragmentLayout;
import com.kongzue.baseframework.interfaces.Layout;
import com.kongzue.baseframework.interfaces.NavigationBarBackgroundColor;
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
@NavigationBarBackgroundColor(a = 255, r = 255, g = 255, b = 255)
@DarkNavigationBarTheme(true)
@FragmentLayout(R.id.frame)
public class DemoActivity extends BaseActivity {
    
    private IntroductionFragment introductionFragment = new IntroductionFragment();
    private FunctionFragment functionFragment = new FunctionFragment();
    private AboutFragment aboutFragment = new AboutFragment();
    
    @BindView(R.id.tabbar)
    private TabBarView tabbar;
    
    @Override
    public void initViews() {
        tabbar = findViewById(R.id.tabbar);
    }
    
    @Override
    public void initDatas(JumpParameter parameter) {
        List<Tab> tabs = new ArrayList<>();
        tabs.add(new Tab(this, getString(R.string.introduction), R.mipmap.img_tab_introduction));
        tabs.add(new Tab(this, getString(R.string.function), R.mipmap.img_maintab_function));
        tabs.add(new Tab(this, getString(R.string.github), R.mipmap.img_maintab_me));
        tabbar.setTab(tabs);
    }
    
    @Override
    public void initFragment(FragmentChangeUtil fragmentChangeUtil) {
        fragmentChangeUtil.addFragment(introductionFragment);
        fragmentChangeUtil.addFragment(functionFragment);
        fragmentChangeUtil.addFragment(aboutFragment);
        
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
        
        getFragmentChangeUtil().setOnFragmentChangeListener(new OnFragmentChangeListener() {
            @Override
            public void onChange(int index, BaseFragment fragment) {
                tabbar.setNormalFocusIndex(index);
            }
        });
    }
}
