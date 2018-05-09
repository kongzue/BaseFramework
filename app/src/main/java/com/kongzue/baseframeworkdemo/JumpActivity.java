package com.kongzue.baseframeworkdemo;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.kongzue.baseframework.BaseActivity;

public class JumpActivity extends BaseActivity {

    private TextView txtP1;
    private ImageView imgP2;
    private Button btnClose;

    @SuppressLint("MissingSuperCall")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.activity_jump);
    }

    @Override
    public void initViews() {
        txtP1 = findViewById(R.id.txt_p1);
        imgP2 = findViewById(R.id.img_p2);
        btnClose = findViewById(R.id.btn_close);
    }

    @Override
    public void initDatas() {
        String parameter1 = (String) getParameter().get("参数1");
        if (!isNull(parameter1)) txtP1.setText("第一个参数读取到的值为：" + parameter1);

        Bitmap parameter2 = (Bitmap) getParameter().get("参数2");
        if (parameter2 != null) imgP2.setImageBitmap(parameter2);
    }

    @Override
    public void setEvents() {
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

}
