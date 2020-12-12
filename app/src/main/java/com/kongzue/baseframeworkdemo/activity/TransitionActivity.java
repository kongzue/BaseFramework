package com.kongzue.baseframeworkdemo.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.kongzue.baseframework.BaseActivity;
import com.kongzue.baseframework.interfaces.Layout;
import com.kongzue.baseframework.interfaces.NavigationBarBackgroundColorRes;
import com.kongzue.baseframework.interfaces.SwipeBack;
import com.kongzue.baseframework.util.JumpParameter;
import com.kongzue.baseframeworkdemo.R;

//使用 @Layout 注解直接绑定要显示的布局
@Layout(R.layout.activity_transition)
//支持侧滑返回
@SwipeBack(true)
//设置底部导航栏背景颜色，此外还可以使用 @NavigationBarBackgroundColor 来指定 argb 颜色
@NavigationBarBackgroundColorRes(R.color.colorPrimary)
public class TransitionActivity extends BaseActivity {
    
    private Button btnTransition;
    
    @Override
    //此处用于绑定布局组件，你也可以使用 @BindView(resId) 来初始化组件
    public void initViews() {
        btnTransition = findViewById(R.id.btn_transition);
    }
    
    @Override
    //请在此编写初始化操作，例如读取数据等，以及对 UI 组件进行赋值
    public void initDatas(JumpParameter paramer) {
    
    }
    
    @Override
    //此处为组件绑定功能事件、回调等方法
    public void setEvents() {
        btnTransition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}
