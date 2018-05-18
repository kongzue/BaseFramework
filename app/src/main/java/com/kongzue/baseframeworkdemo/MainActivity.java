package com.kongzue.baseframeworkdemo;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.net.Uri;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kongzue.baseframework.BaseActivity;
import com.kongzue.baseframework.interfaces.Layout;
import com.kongzue.baseframework.util.OnPermissionResponseListener;
import com.kongzue.baseframework.util.OnResponseListener;
import com.kongzue.baseframework.util.Parameter;

@Layout(R.layout.activity_main)
public class MainActivity extends BaseActivity {

    private FragmentDemo fragmentDemo;

    private TextView txtTitle;
    private TextView linkHome;
    private Button intentToFragment;
    private Button btnJump;
    private Button btnResult;
    private Button btnPermission;
    private TextView linkBokhttp;
    private TextView linkBvolley;
    private TextView linkUpdate;
    private TextView linkDialog;
    private FrameLayout frame;

    @Override
    public void initViews() {
        //此处加载组件
        txtTitle = findViewById(R.id.txt_title);
        linkHome = findViewById(R.id.link_home);
        intentToFragment = findViewById(R.id.intentToFragment);
        btnJump = findViewById(R.id.btn_jump);
        btnResult = findViewById(R.id.btn_result);
        btnPermission = findViewById(R.id.btn_permission);
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
        btnPermission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestPermission(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, new OnPermissionResponseListener() {
                    @Override
                    public void onSuccess(String[] permissions) {
                        toast("申请权限成功");
                    }

                    @Override
                    public void onFail() {
                        toast("申请权限失败");
                    }
                });
            }
        });

        btnResult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("提示");
                builder.setMessage("跳转到下一个界面后，点击“SET返回数据”按钮即可设定返回数据，当返回此界面时会显示该数据");
                builder.setPositiveButton("知道了", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        jump(ResponseActivity.class, new OnResponseListener() {
                            @Override
                            public void OnResponse(Parameter parameter) {
                                if (parameter == null) {
                                    toast("未返回任何数据");
                                } else {
                                    toast("收到返回数据，参数“返回数据1”中的值为：" + parameter.get("返回数据1"));
                                }
                            }
                        });

                        //亦可选用同时带参数+返回值的跳转
                        //jump(ResponseActivity.class,new Parameter()
                        //                .put("参数1", "这是一段文字参数")
                        //                .put("参数2", "这是一段文字参数")
                        //        , new OnResponseListener() {
                        //    @Override
                        //    public void OnResponse(Parameter parameter) {
                        //        if (parameter==null){
                        //            toast("未返回任何数据");
                        //        }else{
                        //            toast("收到返回数据，参数“返回数据1”中的值为：" + parameter.get("返回数据1"));
                        //        }
                        //    }
                        //});
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        btnJump.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("提示");
                builder.setMessage("接下来会创建一个Bitmap，并通过BaseActivity的通道传输给下一个BaseActivity，在其中会通过相应的方法接收到这个Bitmap。\n通过BaseActivity自带的jump(...)方法可以传输任何类型的参数给下一个界面。");
                builder.setPositiveButton("知道了", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.mipmap.img_bkg);
                        jump(JumpActivity.class, new Parameter()
                                .put("参数1", "这是一段文字参数")
                                .put("参数2", bmp)
                        );
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

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