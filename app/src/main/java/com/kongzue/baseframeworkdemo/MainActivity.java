package com.kongzue.baseframeworkdemo;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kongzue.baseframework.BaseActivity;
import com.kongzue.baseframework.BaseFrameworkSettings;
import com.kongzue.baseframework.interfaces.LifeCircleListener;
import com.kongzue.baseframework.interfaces.DarkNavigationBarTheme;
import com.kongzue.baseframework.interfaces.DarkStatusBarTheme;
import com.kongzue.baseframework.interfaces.Layout;
import com.kongzue.baseframework.interfaces.NavigationBarBackgroundColor;
import com.kongzue.baseframework.interfaces.OnBugReportListener;
import com.kongzue.baseframework.util.FragmentChangeUtil;
import com.kongzue.baseframework.util.OnPermissionResponseListener;
import com.kongzue.baseframework.util.OnJumpResponseListener;
import com.kongzue.baseframework.util.JumpParameter;
import com.kongzue.baseframework.util.toast.Toaster;

import java.io.File;
import java.util.Locale;

@Layout(R.layout.activity_main)
@DarkStatusBarTheme(false)
@NavigationBarBackgroundColor(a = 255, r = 255, g = 255, b = 255)
@DarkNavigationBarTheme(true)
public class MainActivity extends BaseActivity {
    
    private FragmentDemo fragmentDemo = new FragmentDemo();
    private FragmentChangeUtil fragmentChangeUtil;
    
    private TextView txtTitle;
    private TextView linkHome;
    private Button intentToFragment;
    private Button intentToBaseAdapter;
    private Button btnJump;
    private Button btnResult;
    private Button btnTransition;
    private Button btnPermission;
    private Button btnError;
    private Button btnPrintJsonLog;
    private Button btnGetImei;
    private Button btnChangeLng;
    private Button btnToast;
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
        intentToBaseAdapter = findViewById(R.id.intentToBaseAdapter);
        btnJump = findViewById(R.id.btn_jump);
        btnResult = findViewById(R.id.btn_result);
        btnTransition = findViewById(R.id.btn_transition);
        btnPermission = findViewById(R.id.btn_permission);
        btnError = findViewById(R.id.btn_error);
        btnPrintJsonLog = findViewById(R.id.btn_printJsonLog);
        btnGetImei = findViewById(R.id.btn_getImei);
        btnChangeLng = findViewById(R.id.btn_changeLng);
        btnToast = findViewById(R.id.btn_toast);
        linkBokhttp = findViewById(R.id.link_bokhttp);
        linkBvolley = findViewById(R.id.link_bvolley);
        linkUpdate = findViewById(R.id.link_update);
        linkDialog = findViewById(R.id.link_dialog);
        frame = findViewById(R.id.frame);
    }
    
    @Override
    public void initDatas(JumpParameter parameter) {
        BaseFrameworkSettings.turnOnReadErrorInfoPermissions(this, new OnBugReportListener() {
            @Override
            public void onReporter(final File file) {
                runOnMain(new Runnable() {
                    @Override
                    public void run() {
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        builder.setTitle("Ops！发生了一次崩溃！");
                        builder.setMessage("您是否愿意帮助我们改进程序以修复此Bug？");
                        builder.setPositiveButton("愿意", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                toast("请对file进行处理：" + file.getAbsolutePath());
                            }
                        });
                        builder.setNegativeButton("不了", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            
                            }
                        });
                        AlertDialog dialog = builder.create();
                        dialog.show();
                    }
                });
            }
        });
        
        log(Color.rgb(70, 155, 223));
        //此处编写初始化代码
        linkHome.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        linkBokhttp.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        linkBvolley.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        linkUpdate.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        linkDialog.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
    
        fragmentChangeUtil = new FragmentChangeUtil(me,R.id.frame);
        fragmentChangeUtil.addFragment(fragmentDemo);

//        setDarkStatusBarTheme(true);
//        setDarkNavigationBarThemeValue(true);
//        setNavigationBarBackgroundColorValue(Color.argb(255,255,255,255));
        
        setLifeCircleListener(new LifeCircleListener() {
            @Override
            public void onCreate() {
            
            }
            
            @Override
            public void onResume() {
            
            }
            
            @Override
            public void onPause() {
            
            }
            
            @Override
            public void onDestroy() {
            
            }
        });
    }
    
    @Override
    public void setEvents() {
        //此处为组件绑定事件
        btnToast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toastS("test!");
            }
        });
        
        btnChangeLng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (BaseFrameworkSettings.selectLocale == Locale.ENGLISH){
                    BaseFrameworkSettings.selectLocale = Locale.CHINA;
                }else{
                    BaseFrameworkSettings.selectLocale = Locale.ENGLISH;
                }
                restartMe();
            }
        });
        
        btnGetImei.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toast(getIMEI());
            }
        });
        
        btnPrintJsonLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                log("{\"employees\": [{ \"firstName\":\"Bill\" , \"lastName\":\"Gates\" },{ \"firstName\":\"George\" , \"lastName\":\"Bush\" },{ \"firstName\":\"Thomas\" , \"lastName\":\"Carter\" }]}");
            }
        });
        
        btnError.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doTestError();
            }
        });
        
        btnTransition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jump(TransitionActivity.class, btnTransition);
            }
        });
        
        intentToBaseAdapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jump(AdapterTestActivity.class);
            }
        });
        
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
                        jump(ResponseActivity.class, new OnJumpResponseListener() {
                            @Override
                            public void OnResponse(JumpParameter jumpParameter) {
                                if (jumpParameter == null) {
                                    toast("未返回任何数据");
                                } else {
                                    toast("收到返回数据，参数“返回数据1”中的值为：" + jumpParameter.get("返回数据1"));
                                }
                            }
                        });
                        
                        //亦可选用同时带参数+返回值的跳转
                        //jump(ResponseActivity.class,new JumpParameter()
                        //                .put("参数1", "这是一段文字参数")
                        //                .put("参数2", "这是一段文字参数")
                        //        , new OnResponseListener() {
                        //    @Override
                        //    public void OnResponse(JumpParameter parameter) {
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
                        jump(JumpActivity.class, new JumpParameter()
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
                Uri uri = Uri.parse("https://github.com/kongzue/BaseFrameworkSettings");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });
        
        intentToFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentChangeUtil.show(0);
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
        fragmentChangeUtil.hide(0);
    }
    
    private void doTestError() throws NullPointerException {
        throw new NullPointerException("This is a exception for test");
    }
}