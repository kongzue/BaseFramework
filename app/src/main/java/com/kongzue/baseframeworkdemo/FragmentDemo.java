package com.kongzue.baseframeworkdemo;

import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.kongzue.baseframework.BaseFragment;
import com.kongzue.baseframework.interfaces.Layout;
import com.kongzue.baseframework.util.JumpParameter;

@Layout(R.layout.fragment_demo)
public class FragmentDemo extends BaseFragment<MainActivity> {

    private TextView info;
    private Button btnHide;

    @Override
    public void initViews() {
        //此处加载组件
        log("initViews");
        info = findViewById(R.id.info);
        btnHide = findViewById(R.id.btn_hide);
    }
    
    @Override
    public void initDatas() {
        //此处编写初始化代码
        log("initDatas");
    }

    @Override
    public void setEvents() {
        //此处为组件绑定事件
        log("setEvents");
        btnHide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                me.hideFragment();
            }
        });
    }
    
    @Override
    public void onLoad() {
        //首次显示时执行
        log("onLoad");
    }
    
}
