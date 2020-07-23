package com.kongzue.baseframeworkdemo.fragment;

import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.kongzue.baseframework.BaseApp;
import com.kongzue.baseframework.BaseFragment;
import com.kongzue.baseframework.interfaces.Layout;
import com.kongzue.baseframework.interfaces.OnClick;
import com.kongzue.baseframework.util.JumpParameter;
import com.kongzue.baseframework.util.OnJumpResponseListener;
import com.kongzue.baseframework.util.Preferences;
import com.kongzue.baseframeworkdemo.App;
import com.kongzue.baseframeworkdemo.activity.DemoActivity;
import com.kongzue.baseframeworkdemo.R;

/**
 * @author: Kongzue
 * @github: https://github.com/kongzue/
 * @homepage: http://kongzue.com/
 * @mail: myzcxhh@live.cn
 * @createTime: 2019/8/20 21:41
 */
@Layout(R.layout.fragment_introduction)
public class IntroductionFragment extends BaseFragment<DemoActivity> {
    
    private TextView linkHome;
    private FloatingActionButton btnFab;
    
    @Override
    public void initViews() {
        linkHome = findViewById(R.id.link_home);
        btnFab = findViewById(R.id.btn_fab);
    }
    
    @Override
    public void initDatas() {
        linkHome.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
    }
    
    @Override
    public void setEvents() {
        linkHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("https://github.com/kongzue/BaseFramework");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });
    }
    
    @OnClick(R.id.btn_fab)
    public void startTest() {
        jump(1, new JumpParameter().put("tip", "开始尝试功能！"), new OnJumpResponseListener() {
            @Override
            public void OnResponse(JumpParameter jumpParameter) {
                toast("你刚刚使用了：" + jumpParameter.getString("function"));
            }
        });
    }
    
    @Override
    public void onShow(boolean isSwitchFragment) {
        log("IntroductionFragment: onShow");
        super.onShow(isSwitchFragment);
    }
    
    @Override
    public void onHide() {
        log("IntroductionFragment: onHide");
        super.onHide();
    }
}
