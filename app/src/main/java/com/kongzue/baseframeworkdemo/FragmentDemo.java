package com.kongzue.baseframeworkdemo;

import android.annotation.SuppressLint;
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
    
    private int index;
    
    private TextView txtTitle;
    private TextView info;
    private Button btnHide;
    
    public FragmentDemo() {
    }
    
    @SuppressLint("ValidFragment")
    public FragmentDemo(int index) {
        this.index = index;
    }
    
    @Override
    public void initViews() {
        //此处加载组件
        log("fragment " + index + " : initViews");
        txtTitle = findViewById(R.id.txt_title);
        info = findViewById(R.id.info);
        btnHide = findViewById(R.id.btn_hide);
    }
    
    @Override
    public void initDatas() {
        //此处编写初始化代码
        txtTitle.setText("BaseFragment " + index);
        log("fragment " + index + " : initDatas");
    }
    
    @Override
    public void setEvents() {
        //此处为组件绑定事件
        log("fragment " + index + " : setEvents");
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
        log("fragment " + index + " : onLoad");
    }
    
    @Override
    public void onResume() {
        super.onResume();
        log("fragment " + index + " : onResume");
    }
    
    @Override
    public void onShow(boolean isSwitchFragment) {
        log("fragment " + index + " : onShow");
    }
}
