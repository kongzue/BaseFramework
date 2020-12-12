package com.kongzue.baseframeworkdemo.activity;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.kongzue.baseframework.BaseActivity;
import com.kongzue.baseframework.interfaces.Layout;
import com.kongzue.baseframework.interfaces.NavigationBarBackgroundColorRes;
import com.kongzue.baseframework.interfaces.SwipeBack;
import com.kongzue.baseframework.util.JumpParameter;
import com.kongzue.baseframeworkdemo.App;
import com.kongzue.baseframeworkdemo.R;

//使用 @Layout 注解直接绑定要显示的布局
@Layout(R.layout.activity_response)
//支持侧滑返回
@SwipeBack(true)
//设置底部导航栏背景颜色，此外还可以使用 @NavigationBarBackgroundColor 来指定 argb 颜色
@NavigationBarBackgroundColorRes(R.color.colorWhite)
public class ResponseActivity extends BaseActivity {

    private TextView txtTitle;
    private TextView txtP1;
    private Button btnSet;
    private Button btnClose;

    @Override
    //此处用于绑定布局组件，你也可以使用 @BindView(resId) 来初始化组件
    public void initViews() {
        txtTitle = findViewById(R.id.txt_title);
        txtP1 = findViewById(R.id.txt_p1);
        btnSet = findViewById(R.id.btn_set);
        btnClose = findViewById(R.id.btn_close);
    }

    @Override
    //请在此编写初始化操作，例如读取数据等，以及对 UI 组件进行赋值
    public void initDatas(JumpParameter parameter) {
    
    }

    @Override
    //此处为组件绑定功能事件、回调等方法
    public void setEvents() {
        btnSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((boolean) getParameter().get("needResponse") == true) {
                    setResponse(new JumpParameter().put("返回数据1", "测试成功"));
                    btnSet.setText("已设置返回数据：key=返回数据1  value=测试成功");
                } else {
                    toast("needResponse = false：不需要返回数据给上一个界面");
                }
            }
        });

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
