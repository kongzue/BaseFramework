package com.kongzue.baseframeworkdemo.fragment;

import android.Manifest;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.kongzue.baseframework.BaseFragment;
import com.kongzue.baseframework.BaseFrameworkSettings;
import com.kongzue.baseframework.interfaces.Layout;
import com.kongzue.baseframework.interfaces.OnClick;
import com.kongzue.baseframework.util.JumpParameter;
import com.kongzue.baseframework.util.OnJumpResponseListener;
import com.kongzue.baseframework.util.OnPermissionResponseListener;
import com.kongzue.baseframeworkdemo.activity.AdapterTestActivity;
import com.kongzue.baseframeworkdemo.activity.DemoActivity;
import com.kongzue.baseframeworkdemo.activity.JumpActivity;
import com.kongzue.baseframeworkdemo.R;
import com.kongzue.baseframeworkdemo.activity.ResponseActivity;
import com.kongzue.baseframeworkdemo.activity.TransitionActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * @author: Kongzue
 * @github: https://github.com/kongzue/
 * @homepage: http://kongzue.com/
 * @mail: myzcxhh@live.cn
 * @createTime: 2019/8/20 21:57
 */
@Layout(R.layout.fragment_function)
public class FunctionFragment extends BaseFragment<DemoActivity> {
    
    private TextView textView;
    private Button intentToBaseAdapter;
    private Button btnJump;
    private Button btnResult;
    private Button btnTransition;
    private Button btnPermission;
    private Button btnError;
    private Button btnPrintJsonLog;
    private Button btnPrintMapLog;
    private Button btnPrintListLog;
    private Button btnGetImei;
    private Button btnChangeLng;
    private Button btnToast;
    private Button btnTimer;
    
    @Override
    public void initViews() {
        textView = findViewById(R.id.textView);
        intentToBaseAdapter = findViewById(R.id.intentToBaseAdapter);
        btnJump = findViewById(R.id.btn_jump);
        btnResult = findViewById(R.id.btn_result);
        btnTransition = findViewById(R.id.btn_transition);
        btnPermission = findViewById(R.id.btn_permission);
        btnError = findViewById(R.id.btn_error);
        btnPrintJsonLog = findViewById(R.id.btn_printJsonLog);
        btnPrintMapLog = findViewById(R.id.btn_printMapLog);
        btnPrintListLog = findViewById(R.id.btn_printListLog);
        btnGetImei = findViewById(R.id.btn_getImei);
        btnChangeLng = findViewById(R.id.btn_changeLng);
        btnToast = findViewById(R.id.btn_toast);
        btnTimer = findViewById(R.id.btn_timer);
    }
    
    @Override
    public void initDatas() {
    
    }
    
    private int time;
    
    //此处为组件绑定事件
    @Override
    public void setEvents() {
        btnTimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFragmentResponse(new JumpParameter().put("function", ((Button) v).getText().toString()));
    
                runOnMainCycle(new Runnable() {
                    @Override
                    public void run() {
                        time++;
                        btnTimer.setText("t:" + time);
                    }
                }, 1000, 1000);
            }
        });
        
        btnToast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFragmentResponse(new JumpParameter().put("function", ((Button) v).getText().toString()));
    
                toastS("test!");
            }
        });
        
        btnChangeLng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFragmentResponse(new JumpParameter().put("function", ((Button) v).getText().toString()));
    
                if (BaseFrameworkSettings.selectLocale == Locale.ENGLISH) {
                    BaseFrameworkSettings.selectLocale = Locale.CHINA;
                } else {
                    BaseFrameworkSettings.selectLocale = Locale.ENGLISH;
                }
                me.restartMe();
            }
        });
        
        btnGetImei.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toast(getIMEI());
            }
        });
        
        btnPrintMapLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFragmentResponse(new JumpParameter().put("function", ((Button) v).getText().toString()));
                
                Map<String,Object> map = new HashMap<>();
                map.put("name","kongzue");
                map.put("email","myzcxhh@live.cn");
                map.put("website","kongzue.com");
                map.put("times",20);
                map.put("signed",true);
                log(map);
    
                AlertDialog.Builder builder = new AlertDialog.Builder(me);
                builder.setTitle("提示");
                builder.setMessage("此功能需要连接Android Studio的Logcat查看输出结果");
                builder.setPositiveButton("知道了", null);
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
        
        btnPrintListLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFragmentResponse(new JumpParameter().put("function", ((Button) v).getText().toString()));
    
                List list = new ArrayList();
                list.add("listItem 1");
                list.add("listItem 2");
                list.add(true);
                list.add(25.6);
                
                log(list);
    
                AlertDialog.Builder builder = new AlertDialog.Builder(me);
                builder.setTitle("提示");
                builder.setMessage("此功能需要连接Android Studio的Logcat查看输出结果");
                builder.setPositiveButton("知道了", null);
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
        
        btnPrintJsonLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFragmentResponse(new JumpParameter().put("function", ((Button) v).getText().toString()));
    
                log("{\"employees\": [{ \"firstName\":\"Bill\" , \"lastName\":\"Gates\" },{ \"firstName\":\"George\" , \"lastName\":\"Bush\" },{ \"firstName\":\"Thomas\" , \"lastName\":\"Carter\" }]}");
                
                AlertDialog.Builder builder = new AlertDialog.Builder(me);
                builder.setTitle("提示");
                builder.setMessage("此功能需要连接Android Studio的Logcat查看输出结果");
                builder.setPositiveButton("知道了", null);
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
        
        btnError.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFragmentResponse(new JumpParameter().put("function", ((Button) v).getText().toString()));
    
                doTestError();
            }
        });
        
        btnTransition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFragmentResponse(new JumpParameter().put("function", ((Button) v).getText().toString()));
    
                jump(TransitionActivity.class, btnTransition);
            }
        });
        
        intentToBaseAdapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFragmentResponse(new JumpParameter().put("function", ((Button) v).getText().toString()));
    
                jump(AdapterTestActivity.class);
            }
        });
        
        btnPermission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFragmentResponse(new JumpParameter().put("function", ((Button) v).getText().toString()));
    
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
                setFragmentResponse(new JumpParameter().put("function", ((Button) v).getText().toString()));
    
                AlertDialog.Builder builder = new AlertDialog.Builder(me);
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
    }
    
    @OnClick(R.id.btn_jump)
    public void jumpFunction() {
        setFragmentResponse(new JumpParameter().put("function", btnJump.getText().toString()));
        AlertDialog.Builder builder = new AlertDialog.Builder(me);
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
    
    private void doTestError() throws NullPointerException {
        throw new NullPointerException("This is a exception for test");
    }
    
    @Override
    public void onParameterReceived(JumpParameter parameter) {
        toast(parameter.getString("tip"));
    }
    
    
    @Override
    public void onShow(boolean isSwitchFragment) {
        log("FunctionFragment: onShow");
        super.onShow(isSwitchFragment);
    }
    
    @Override
    public void onHide() {
        log("FunctionFragment: onHide");
        super.onHide();
    }
}
