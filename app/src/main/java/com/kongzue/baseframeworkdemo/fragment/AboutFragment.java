package com.kongzue.baseframeworkdemo.fragment;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.kongzue.baseframework.BaseFragment;
import com.kongzue.baseframework.interfaces.Layout;
import com.kongzue.baseframeworkdemo.activity.DemoActivity;
import com.kongzue.baseframeworkdemo.R;

/**
 * @author: Kongzue
 * @github: https://github.com/kongzue/
 * @homepage: http://kongzue.com/
 * @mail: myzcxhh@live.cn
 * @createTime: 2019/8/20 22:18
 */
//使用 @Layout 注解直接绑定要显示的布局
@Layout(R.layout.fragment_about)
/**
 * 此处泛型是用于约束绑定目标 Activity 的，是可选操作，
 * 如果你指定了目标绑定目标 Activity，则使用“me.”关键词可直接调用该 Activity 中的 public 成员或方法
 */
public class AboutFragment extends BaseFragment<DemoActivity> {
    
    private WebView webView;
    
    @Override
    //此处用于绑定布局组件，你也可以使用 @BindView(resId) 来初始化组件
    public void initViews() {
        webView = findViewById(R.id.webView);
    }
    
    @Override
    //请在此编写初始化操作，例如读取数据等，以及对 UI 组件进行赋值
    public void initDatas() {
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setSupportZoom(false);
        webSettings.setAllowFileAccess(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setLoadsImagesAutomatically(true);
        webSettings.setDefaultTextEncodingName("utf-8");
    
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(intent);
                } catch (ActivityNotFoundException e) {
                    e.printStackTrace();
                }
                return true;
            }
        
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }
        });
        
        webView.loadUrl("https://github.com/kongzue/BaseFramework/");
    }
    
    @Override
    //此处为组件绑定功能事件、回调等方法
    public void setEvents() {
    
    }
    
    @Override
    /**
     * 进入 Fragment 时调用此方法，isSwitchFragment 标记说明了是否为从别的 Fragment 切换至此 Fragment 的，
     * 若为 false，则有可能是从后台切换至前台触发
     */
    public void onShow(boolean isSwitchFragment) {
        log("AboutFragment: onShow");
        super.onShow(isSwitchFragment);
    }
    
    @Override
    public void onHide() {
        log("AboutFragment: onHide");
        super.onHide();
    }
}
