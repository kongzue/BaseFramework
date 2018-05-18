package com.kongzue.baseframeworkdemo;

import android.annotation.SuppressLint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.kongzue.baseframework.BaseActivity;
import com.kongzue.baseframework.interfaces.Layout;
import com.kongzue.baseframework.util.Parameter;

@Layout(R.layout.activity_response)
public class ResponseActivity extends BaseActivity {

    private TextView txtTitle;
    private TextView txtP1;
    private Button btnSet;
    private Button btnClose;

    @Override
    public void initViews() {
        txtTitle = findViewById(R.id.txt_title);
        txtP1 = findViewById(R.id.txt_p1);
        btnSet = findViewById(R.id.btn_set);
        btnClose = findViewById(R.id.btn_close);
    }

    @Override
    public void initDatas() {

    }

    @Override
    public void setEvents() {
        btnSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((boolean) getParameter().get("needResponse") == true) {
                    setResponse(new Parameter().put("返回数据1", "测试成功"));
                    toast("已设置返回数据：key=返回数据1  value=测试成功");
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
