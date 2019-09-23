package com.kongzue.baseframeworkdemo.activity;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

import com.kongzue.baseframework.BaseActivity;
import com.kongzue.baseframework.BaseFragment;
import com.kongzue.baseframework.BaseFrameworkSettings;
import com.kongzue.baseframework.interfaces.DarkNavigationBarTheme;
import com.kongzue.baseframework.interfaces.DarkStatusBarTheme;
import com.kongzue.baseframework.interfaces.FragmentLayout;
import com.kongzue.baseframework.interfaces.Layout;
import com.kongzue.baseframework.interfaces.NavigationBarBackgroundColor;
import com.kongzue.baseframework.interfaces.OnBugReportListener;
import com.kongzue.baseframework.interfaces.OnFragmentChangeListener;
import com.kongzue.baseframework.util.FragmentChangeUtil;
import com.kongzue.baseframework.util.JumpParameter;
import com.kongzue.baseframework.util.Preferences;
import com.kongzue.baseframeworkdemo.R;
import com.kongzue.baseframeworkdemo.fragment.AboutFragment;
import com.kongzue.baseframeworkdemo.fragment.FunctionFragment;
import com.kongzue.baseframeworkdemo.fragment.IntroductionFragment;
import com.kongzue.tabbar.Tab;
import com.kongzue.tabbar.TabBarView;
import com.kongzue.tabbar.interfaces.OnTabChangeListener;

import java.io.File;
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
    
    private TabBarView tabbar;
    
    @Override
    public void initViews() {
        tabbar = findViewById(R.id.tabbar);
    }
    
    @Override
    public void initDatas(JumpParameter parameter) {
        BaseFrameworkSettings.turnOnReadErrorInfoPermissions(this, new OnBugReportListener() {
            @Override
            public void onReporter(final File file) {
                runOnMain(new Runnable() {
                    @Override
                    public void run() {
                        AlertDialog.Builder builder = new AlertDialog.Builder(me);
                        builder.setTitle("Ops！发生了一次崩溃！");
                        builder.setMessage("您是否愿意帮助我们改进程序以修复此Bug？");
                        builder.setPositiveButton("愿意", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                toast("请对file进行处理：" + file.getAbsolutePath());
                            }
                        });
                        builder.setNegativeButton("不了", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            
                            }
                        });
                        AlertDialog dialog = builder.create();
                        dialog.show();
                    }
                });
            }
        });
        
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
