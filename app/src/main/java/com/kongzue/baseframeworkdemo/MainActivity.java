package com.kongzue.baseframeworkdemo;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kongzue.baseframework.BaseActivity;

public class MainActivity extends BaseActivity {

    private FragmentDemo fragmentDemo;

    private TextView txtTitle;
    private TextView linkHome;
    private Button intentToFragment;
    private TextView linkBokhttp;
    private TextView linkBvolley;
    private TextView linkUpdate;
    private TextView linkDialog;
    private FrameLayout frame;

    @SuppressLint("MissingSuperCall")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.activity_main);
    }

    @Override
    public void initViews() {
        //此处加载组件
        txtTitle = findViewById(R.id.txt_title);
        linkHome = findViewById(R.id.link_home);
        intentToFragment = findViewById(R.id.intentToFragment);
        linkBokhttp = findViewById(R.id.link_bokhttp);
        linkBvolley = findViewById(R.id.link_bvolley);
        linkUpdate = findViewById(R.id.link_update);
        linkDialog = findViewById(R.id.link_dialog);
        frame = findViewById(R.id.frame);
    }

    @Override
    public void initDatas() {
        //此处编写初始化代码
        linkHome.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        linkBokhttp.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        linkBvolley.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        linkUpdate.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        linkDialog.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
    }

    @Override
    public void setEvents() {
        //此处为组件绑定事件
        linkHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("https://github.com/kongzue/BaseFramework");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });

        intentToFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                if (fragmentDemo != null) {
                    transaction.hide(fragmentDemo);
                } else {
                    fragmentDemo = new FragmentDemo();
                    transaction.add(R.id.frame, fragmentDemo);
                }
                fragmentDemo.setMainActivity((MainActivity) me);
                transaction.show(fragmentDemo);
                transaction.commit();
            }
        });

        linkUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("https://github.com/kongzue/KongzueUpdateSDK");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });

        linkDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("https://github.com/kongzue/Dialog");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });

        linkBvolley.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("https://github.com/kongzue/BaseVolley");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });

        linkBokhttp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("https://github.com/kongzue/BaseOkHttp");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });
    }

    public void hideFragment() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        if (fragmentDemo != null) {
            transaction.hide(fragmentDemo);
        }
        transaction.commit();
    }
}