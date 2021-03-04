package com.kongzue.baseframeworkdemo.activity;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.view.View;

import com.kongzue.baseframework.BaseActivity;
import com.kongzue.baseframework.BaseFragment;
import com.kongzue.baseframework.interfaces.BindView;
import com.kongzue.baseframework.interfaces.DarkNavigationBarTheme;
import com.kongzue.baseframework.interfaces.DarkStatusBarTheme;
import com.kongzue.baseframework.interfaces.FragmentLayout;
import com.kongzue.baseframework.interfaces.NavigationBarBackgroundColorRes;
import com.kongzue.baseframework.interfaces.OnClicks;
import com.kongzue.baseframework.interfaces.Layout;
import com.kongzue.baseframework.interfaces.NavigationBarBackgroundColor;
import com.kongzue.baseframework.interfaces.NavigationBarBackgroundColorRes;
import com.kongzue.baseframework.interfaces.OnFragmentChangeListener;
import com.kongzue.baseframework.util.AppManager;
import com.kongzue.baseframework.util.FragmentChangeUtil;
import com.kongzue.baseframework.util.JumpParameter;
import com.kongzue.baseframeworkdemo.App;
import com.kongzue.baseframeworkdemo.R;
import com.kongzue.baseframeworkdemo.fragment.AboutFragment;
import com.kongzue.baseframeworkdemo.fragment.FunctionFragment;
import com.kongzue.baseframeworkdemo.fragment.IntroductionFragment;
import com.kongzue.tabbar.Tab;
import com.kongzue.tabbar.TabBarView;
import com.kongzue.tabbar.interfaces.OnTabChangeListener;

import java.util.ArrayList;
import java.util.List;

//使用 @Layout 注解直接绑定要显示的布局
@Layout(R.layout.activity_demo)
//设置不使用状态栏暗色文字图标样式
@DarkStatusBarTheme(false)
//设置底部导航栏背景颜色，此外还可以使用 @NavigationBarBackgroundColor 来指定 argb 颜色
@NavigationBarBackgroundColorRes(R.color.colorWhite)
//设置使用底部导航栏暗色图标样式
@DarkNavigationBarTheme(true)
//绑定子 Fragment 要显示的容器布局
@FragmentLayout(R.id.viewPager)
public class DemoActivity extends BaseActivity {
    
    //三个子 Fragment 布局（简介界面、功能界面、Github关于界面）
    private IntroductionFragment introductionFragment = new IntroductionFragment();
    private FunctionFragment functionFragment = new FunctionFragment();
    private AboutFragment aboutFragment = new AboutFragment();
    
    //使用 @BindView(resId) 来初始化组件
    @BindView(R.id.tabbar)
    private TabBarView tabbar;
    
    @Override
    //此处用于绑定布局组件，你也可以使用 @BindView(resId) 来初始化组件
    public void initViews() {
        tabbar = findViewById(R.id.tabbar);
    }
    
    @Override
    //请在此编写初始化操作，例如读取数据等，以及对 UI 组件进行赋值
    public void initDatas(JumpParameter parameter) {
        List<Tab> tabs = new ArrayList<>();
        tabs.add(new Tab(this, getString(R.string.introduction), R.mipmap.img_tab_introduction));
        tabs.add(new Tab(this, getString(R.string.function), R.mipmap.img_maintab_function));
        tabs.add(new Tab(this, getString(R.string.github), R.mipmap.img_maintab_me));
        tabbar.setTab(tabs);
    }
    
    @Override
    protected void lazyInit(JumpParameter parameter) {
    
    }
    
    private void doTestError() throws NullPointerException {
        throw new NullPointerException("This is a exception for test");
    }
    
    @Override
    //此处为添加子布局逻辑
    public void initFragment(FragmentChangeUtil fragmentChangeUtil) {
        fragmentChangeUtil.addFragment(introductionFragment);
        fragmentChangeUtil.addFragment(functionFragment);
        fragmentChangeUtil.addFragment(aboutFragment);
        
        //默认切换至第一个界面
        changeFragment(0);
    }
    
    @Override
    //此处为组件绑定功能事件、回调等方法
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
