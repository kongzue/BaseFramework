package com.kongzue.baseframeworkdemo.activity;

import android.graphics.Bitmap;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.kongzue.baseframework.BaseActivity;
import com.kongzue.baseframework.interfaces.EnterAnim;
import com.kongzue.baseframework.interfaces.ExitAnim;
import com.kongzue.baseframework.interfaces.Layout;
import com.kongzue.baseframework.interfaces.NavigationBarBackgroundColorRes;
import com.kongzue.baseframework.interfaces.SwipeBack;
import com.kongzue.baseframework.util.JumpParameter;
import com.kongzue.baseframeworkdemo.R;

//使用 @Layout 注解直接绑定要显示的布局
@Layout(R.layout.activity_jump)
//支持侧滑返回
@SwipeBack(true)
//设置底部导航栏背景颜色，此外还可以使用 @NavigationBarBackgroundColor 来指定 argb 颜色
@NavigationBarBackgroundColorRes(R.color.colorPrimary)
//入场动画
@EnterAnim(enterAnimResId = R.anim.fade, holdAnimResId = R.anim.hold)
//出场动画
@ExitAnim(holdAnimResId = R.anim.hold, exitAnimResId = R.anim.back)
public class JumpActivity extends BaseActivity {
    
    private TextView txtP1;
    private ImageView imgP2;
    private Button btnClose;
    
    @Override
    //此处用于绑定布局组件，你也可以使用 @BindView(resId) 来初始化组件
    public void initViews() {
        txtP1 = findViewById(R.id.txt_p1);
        imgP2 = findViewById(R.id.img_p2);
        btnClose = findViewById(R.id.btn_close);
    }
    
    @Override
    //请在此编写初始化操作，例如读取数据等，以及对 UI 组件进行赋值
    public void initDatas(JumpParameter parameter) {
        String parameter1 = parameter.get("参数1");
        if (!isNull(parameter1)) txtP1.setText("第一个参数读取到的值为：\n" + parameter1);
        
        Bitmap parameter2 = parameter.get("参数2");
        if (parameter2 != null) imgP2.setImageBitmap(parameter2);
    }
    
    @Override
    //此处为组件绑定功能事件、回调等方法
    public void setEvents() {
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    
}