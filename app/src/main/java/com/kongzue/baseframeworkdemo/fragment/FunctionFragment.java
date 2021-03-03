package com.kongzue.baseframeworkdemo.fragment;

import android.Manifest;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.kongzue.baseframework.BaseFragment;
import com.kongzue.baseframework.BaseFrameworkSettings;
import com.kongzue.baseframework.interfaces.BindView;
import com.kongzue.baseframework.interfaces.Layout;
import com.kongzue.baseframework.interfaces.OnClick;
import com.kongzue.baseframework.util.CycleRunner;
import com.kongzue.baseframework.util.JumpParameter;
import com.kongzue.baseframework.util.OnJumpResponseListener;
import com.kongzue.baseframework.util.OnPermissionResponseListener;
import com.kongzue.baseframeworkdemo.App;
import com.kongzue.baseframeworkdemo.activity.AdapterTestActivity;
import com.kongzue.baseframeworkdemo.activity.DemoActivity;
import com.kongzue.baseframeworkdemo.activity.JumpActivity;
import com.kongzue.baseframeworkdemo.R;
import com.kongzue.baseframeworkdemo.activity.ResponseActivity;
import com.kongzue.baseframeworkdemo.activity.TransitionActivity;
import com.kongzue.baseframeworkdemo.util.User;

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

//使用 @Layout 注解直接绑定要显示的布局
@Layout(R.layout.fragment_function)
/**
 * 此处泛型是用于约束绑定目标 Activity 的，是可选操作，
 * 如果你指定了目标绑定目标 Activity，则使用“me.”关键词可直接调用该 Activity 中的 public 成员或方法
 */
public class FunctionFragment extends BaseFragment<DemoActivity> {
    
    //使用 @BindView(resId) 来初始化组件
    @BindView(R.id.textView)
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
    //此处用于绑定布局组件，你也可以使用 @BindView(resId) 来初始化组件
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
    //请在此编写初始化操作，例如读取数据等，以及对 UI 组件进行赋值
    public void initDatas() {
    }
    
    private int time;
    private CycleRunner runner;
    
    @Override
    //此处为组件绑定功能事件、回调等方法
    public void setEvents() {
        //功能：主线程计时器
        btnTimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //此功能将演示在主线程循环执行的计时器，你可以在回调中直接执行 UI 操作，适用于发送验证码倒计时等场景
                setFragmentResponse(new JumpParameter().put("function", ((Button) v).getText().toString()));
    
                //如果有，先取消之前的计时器
                if (runner!=null){
                    runner.cancel();
                }
                
                runner = runOnMainCycle(new Runnable() {
                    @Override
                    public void run() {
                        time++;
                        btnTimer.setText("t:" + time);
                    }
                }, 1000, 1000);
            }
        });
        
        //功能：兼容性 Toast 提示，在部分因未开启悬浮窗权限导致无法正常弹出一般 Toast 的情况下会自动使用 PopupWindow 替代
        btnToast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFragmentResponse(new JumpParameter().put("function", ((Button) v).getText().toString()));
                
                toastS("test!");
            }
        });
        
        //功能：切换语言（会重启界面）
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
        
        //功能：获取 IMEI
        btnGetImei.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toast(getIMEI());
            }
        });
        
        //功能：打印 Map（Json 形式）
        btnPrintMapLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFragmentResponse(new JumpParameter().put("function", ((Button) v).getText().toString()));
                
                Map<String, Object> map = new HashMap<>();
                map.put("name", "kongzue");
                map.put("email", "myzcxhh@live.cn");
                map.put("website", "kongzue.com");
                map.put("times", 20);
                map.put("signed", true);
                log(map);
                
                AlertDialog.Builder builder = new AlertDialog.Builder(me);
                builder.setTitle("提示");
                builder.setMessage("此功能需要连接Android Studio的Logcat查看输出结果");
                builder.setPositiveButton("知道了", null);
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    
        //功能：打印 List（Json 形式）
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
    
        //功能：打印 Json
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
        
        //功能：触发一次闪退
        btnError.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFragmentResponse(new JumpParameter().put("function", ((Button) v).getText().toString()));
                
                doTestError();
            }
        });
    
        //功能：共享元素跳转
        btnTransition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFragmentResponse(new JumpParameter().put("function", ((Button) v).getText().toString()));
                
                jump(TransitionActivity.class, btnTransition);
            }
        });
        
        //功能：基础适配器演示
        intentToBaseAdapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFragmentResponse(new JumpParameter().put("function", ((Button) v).getText().toString()));
                
                jump(AdapterTestActivity.class);
            }
        });
        
        //功能：申请权限
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
        
        //功能，跳转回调
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
    //你也可以使用 @OnClick 注解直接绑定点击事件
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
    
    //功能：触发一个错误
    private void doTestError() throws NullPointerException {
        throw new NullPointerException("This is a exception for test");
    }
    
    @Override
    public void onParameterReceived(JumpParameter parameter) {
        toast(parameter.getString("tip"));
    }
    
    
    @Override
    /**
     * 进入 Fragment 时调用此方法，isSwitchFragment 标记说明了是否为从别的 Fragment 切换至此 Fragment 的，
     * 若为 false，则有可能是从后台切换至前台触发
     */
    public void onShow(boolean isSwitchFragment) {
        log("FunctionFragment: onShow");
        super.onShow(isSwitchFragment);
        
        //演示一个从 App 类中写入的序列化对象的读取实现
        User user = App.user.getObject("userInfo", User.class);
        log("userLoad: "+user);
    }
    
    @Override
    public void onHide() {
        log("FunctionFragment: onHide");
        super.onHide();
    }
    
    @Override
    public boolean onBack() {
        toastS("此界面不允许返回退出\n这个拦截操作是在 BaseFragment 中进行的");
        return true;
    }
}
