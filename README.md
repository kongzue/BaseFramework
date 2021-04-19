# BaseFramework(AndroidX)

## BaseFramework 是什么？
BaseFramework框架包含沉浸式适配、对 Activity、Fragment 以及 Adapter 的封装，并提供了一些诸如权限申请、跳转、延时操作、提示、日志输出等小工具，以方便快速构建 Android App；

<a href="https://github.com/kongzue/BaseFramework/">
<img src="https://img.shields.io/badge/Kongzue-BaseFramework-green.svg" alt="Kongzue BaseFramework">
</a> 
<a href="https://jitpack.io/#kongzue/BaseFramework">
<img src="https://jitpack.io/v/kongzue/BaseFramework.svg" alt="Jitpack.io">
</a> 
<a href="http://www.apache.org/licenses/LICENSE-2.0">
<img src="https://img.shields.io/badge/License-Apache%202.0-red.svg" alt="License">
</a> 
<a href="http://www.kongzue.com">
<img src="https://img.shields.io/badge/Homepage-Kongzue.com-brightgreen.svg" alt="Homepage">
</a> 

Demo预览图如下：

![BaseFramework](https://github.com/kongzue/Res/raw/master/app/src/main/res/mipmap-xxxhdpi/img_baseframework_671.jpg)

试用版和使用方法 Demo 可以前往 <http://beta.kongzue.com/basefw> 下载

<div align=center><img src="https://github.com/kongzue/Res/raw/master/app/src/main/res/mipmap-xxxhdpi/download_baseframework.png" alt="Kongzue's BaseFramework" width="250" height="250" /></div>

## 使用前的约定与须知

- 更轻松！在 BaseActivity 中，约定关键词**me**代替**Activity.this**，因此您在编写代码时，在异步线程中可以轻松使用me关键字直接引用当前的父Activity。

- 请忘掉重写你的 onCreate 吧！在新版本的 BaseFramework 中，请在您的 Activity 的 class 上使用注解 @Layout(R.layout.xxx) ，剩下的事情我们会自动帮您完成！

- 规范化！无论是在 BaseActivity 还是 BaseFragment ，默认都有 initViews()、initDatas()、setEvents() 三个方法，他们分别代表加载组件、初始化数据、组件绑定事件三个步骤，因其执行顺序是固定的，且为了代码规范化，这三个方法必须重写，也建议将相关业务逻辑写在对应方法中，以方便维护和管理。

- 骚操作！提供大量更为好用和快捷的常用方法工具，大幅度减少因参数、引用造成的额外代码量，更好用的日志输出方便甩锅，不死的小强更能保证你的 App 不再因为异常而闪退。

## Maven仓库或Gradle的引用方式

### 引入方式

1) 前往 build.gradle(project) 添加 jitpack 仓库：
```
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}
```
2) 引入 BaseFramework：

最新版本：
<a href="https://jitpack.io/#kongzue/BaseFramework">
<img src="https://jitpack.io/v/kongzue/BaseFramework.svg" alt="Jitpack.io">
</a> 

```
dependencies {
        implementation 'com.github.kongzue:BaseFramework:latest.release'
}
```

#### Support版本

由于 Google Android Support 的支持原因，Support 版本将不再更新，要使用最后的 Support 支持版本，请前往：

[Kongzue BaseFramework(Support)](https://github.com/kongzue/BaseFramework-Support)

## 使用提示

以下文档为各功能模块的介绍，额外的，强烈建议阅读 [DemoActivity.java](https://github.com/kongzue/BaseFramework/blob/master/app/src/main/java/com/kongzue/baseframeworkdemo/activity/DemoActivity.java) 和 [FunctionFragment.java](https://github.com/kongzue/BaseFramework/blob/master/app/src/main/java/com/kongzue/baseframeworkdemo/fragment/FunctionFragment.java) 两个范例代码内容以了解更详细的开发流程和功能说明。

# 目录

· <a href="#1">**BaseActivity功能**</a>

···· <a href="#1-0">沉浸式</a>

···· <a href="#1-1">绑定 Layout 布局</a>

···· <a href="#1-2">跳转、Activity间通讯</a>

···· <a href="#1-3">更简单的跳转后返回数据</a>

···· <a href="#1-4">权限申请</a>

···· <a href="#1-5">BaseActivity提供的小工具</a>

···· <a href="#1-6">BaseActivity的生命周期</a>

···· <a href="#1-7">侧滑返回</a>

···· <a href="#1-8">布局绑定和事件绑定</a>

· <a href="#2">**BaseFragment功能**</a>

···· <a href="#2-0">BaseFragment 是什么</a>

···· <a href="#2-1">绑定 Layout 布局</a>

···· <a href="#2-2">FragmentChangeUtil</a>

···· <a href="#2-3">BaseFragment 最佳实践</a>

···· <a href="#2-4">BaseFragment 间的数据传递和回调</a>

· <a href="#3">**设置、属性值的存储读取工具 Settings**</a>

· <a href="#4">**AppManager**</a>

· <a href="#5">**异步或同步（演示操作）**</a>

· <a href="#6">**BaseAdapter**</a>

···· <a href="#6-1">JavaBean 适配方式</a>

···· <a href="#6-2">Map 适配方式</a>

···· <a href="#6-3">多种布局的绑定方式</a>

···· <a href="#6-4">数据刷新方法</a>

· <a href="#8">**增强型日志**</a>

· <a href="#7">**行为与日志监听 + 不死的小强**</a>

· <a href="#9">**语言变更工具**</a>

· <a href="#10">**BaseApp功能**</a>

···· <a href="#10-1">BaseApp提供的小工具</a>

## <a name="1">BaseActivity功能</a>

### <a name="1-0">沉浸式</a>

在 BaseActivity 中，您还可以使用以下注解对沉浸式进行控制：
```
@DarkStatusBarTheme(true)           //开启顶部状态栏图标、文字暗色模式
@DarkNavigationBarTheme(true)       //开启底部导航栏按钮暗色模式
@NavigationBarBackgroundColor(a = 255,r = 255,g = 255,b = 255)      //设置底部导航栏背景颜色（a = 0,r = 0,g = 0,b = 0可透明）
@NavigationBarBackgroundColorInt(-16777216)                         //设置底部导航栏背景颜色（颜色色值）
@NavigationBarBackgroundColorRes(R.color.black)                     //设置底部导航栏背景颜色（color资源Id）
@NavigationBarBackgroundColorHex("#FFFFFF")                         //设置底部导航栏背景颜色（颜色代码）

//也可从代码中进行控制：
setDarkStatusBarTheme(true);            //开启顶部状态栏图标、文字暗色模式
setDarkNavigationBarTheme(true);        //开启底部导航栏按钮暗色模式
setNavigationBarBackgroundColor(Color.argb(255,255,255,255));       //设置底部导航栏背景颜色（a = 0,r = 0,g = 0,b = 0可透明）
```

建议直接使用无 ActionBar 的 Activity 样式：
```
<!-- 在 res/values/styles.xml 中修改继承关系为：Theme.AppCompat.Light.NoActionBar -->
<style name="AppTheme" parent="Theme.AppCompat.Light.NoActionBar">
    <item name="colorPrimary">@color/colorPrimary</item>
    <item name="colorPrimaryDark">@color/colorPrimaryDark</item>
    <item name="colorAccent">@color/colorAccent</item>
</style>
```

### <a name="1-1">绑定 Layout 布局</a>

BaseActivity 默认使用注解来绑定布局：
```
@Layout(R.layout.activity_demo)
public class DemoActivity extends BaseActivity {
    ...
```

不建议重写 onCreate 方法，根据约定，请无需关心绑定布局的过程，你只需要在 initView() 方法中绑定、加载 View 组件，initData() 方法中加载数据，在 setEvents() 方法中绑定事件即可。

除了注解绑定外，你也可以使用 resetLayoutResId() 方法重载布局资源，例如实现自定义主题：
```
@Override
protected int resetLayoutResId() {
    if (darkMode){
        return R.layout.activity_demo_dark;
    }else{
        return super.resetLayoutResId();
    }
}
```

额外的，你还可以拦截绑定过程，暂时不绑定布局（可能会出现错误，仅用于特殊情况）：
```
@Override
public boolean interceptSetContentView() {
    return true;            //拦截绑定布局的过程
}
```

绑定 View 组件请参考 <a href="#1-8">布局绑定和事件绑定</a> 章节

### <a name="1-2">跳转、Activity间通讯（带自定义参数的跳转）</a>
Android 默认的 Intent无法支持自定义类型参数的跳转，BaseActivity 通过自有的数据通道允许传输自定义类型的数据给要跳转到的另一个 BaseActivity：

跳转代码范例：
```
Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.mipmap.img_bkg);
jump(JumpActivity.class, new JumpParameter()
        .put("参数1", "这是一段文字参数")
        .put("参数2", bmp)
);
```

接收数据代码范例：
```
String parameter1 = (String) getParameter().get("参数1");
if (!isNull(parameter1)) txtP1.setText("第一个参数读取到的值为：" + parameter1);
Bitmap parameter2 = (Bitmap) getParameter().get("参数2");
if (parameter2 != null) imgP2.setImageBitmap(parameter2);
```

### <a name="1-3">更简单的跳转后返回数据</a>
以往我们需要通过重写实现 onActivityResult 来实现回传数据，但在 BaseActivity 中，你只需要一个监听器：

跳转代码范例：
```
jump(ResponseActivity.class, new OnJumpResponseListener() {
    @Override
    public void OnResponse(JumpParameter parameter) {
        if (parameter == null) {
            toast("未返回任何数据");
        } else {
            toast("收到返回数据，参数“返回数据1”中的值为：" + parameter.get("返回数据1"));
        }
    }
});
```

亦可选用同时带参数+返回值的跳转：
```
jump(ResponseActivity.class,new JumpParameter()
                .put("参数1", "这是一段文字参数")
                .put("参数2", "这是一段文字参数")
        , new OnJumpResponseListener() {
    @Override
    public void OnResponse(JumpParameter parameter) {
        if (parameter==null){
            toast("未返回任何数据");
        }else{
            toast("收到返回数据，参数“返回数据1”中的值为：" + parameter.get("返回数据1"));
        }
    }
});
```

返回数据范例：
```
if ((boolean) getParameter().get("needResponse") == true) {
    setResponse(new Parameter().put("返回数据1", "测试成功"));
}

//或也可以使用：
if ((boolean) getParameter().get("needResponse") == true) {
    returnParameter(new Parameter().put("返回数据1", "测试成功"));
}
```

### <a name="1-4">权限申请</a>
要进行权限申请也变得更加简单，只需要实现相应的回调 OnPermissionResponseListener 即可：
```
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
```

### <a name="1-5">BaseActivity提供的小工具</a>
```
//快速调用 Toast：
toast(Obj);

//简易Log打印日志（BaseFrameworkSettings.DEBUGMODE = false关闭，注意此开关是同时影响 BaseActivity 和 BaseFragment的）：
log(Obj);

//软键盘开关：
setIMMStatus(boolean show, EditText editText);

//dip与像素px转换：
dip2px(float dpValue);

//像素px与dip转换：
dip2px(float dpValue);

//属性动画：
moveAnimation(Object obj, String perference, float aimValue, long time, long delay);

//数据判空（适合网络返回值判断处理，即便为字符串“null”也为空）：
isNull(String);

//跳转动画（参数为您的动画资源id）：
jumpAnim(int enterAnim, int exitAnim)

//使用默认浏览器打开链接
openUrl(String url)

//打开指定App
openApp(String packageName)

//检测App是否已安装
isInstallApp(String packageName)

//获取IMEI
//请预先在 AndroidManifest.xml 中声明：<uses-permission android:name="android.permission.READ_PHONE_STATE"/>
getIMEI()

//获取AndroidID
getAndroidId()

//获取Mac地址
//请预先在 AndroidManifest.xml 中声明：<uses-permission android:name="android.permission.ACCESS_WIFI_STATE"/>
getMacAddress()

//获取根布局
getRootView()

//获取状态栏的高度
getStatusBarHeight()

//获取屏幕宽度
getDisplayWidth()

//获取屏幕可用部分高度（屏幕高度-状态栏高度-屏幕底栏高度）
getDisplayHeight()

//获取底栏高度
getNavbarHeight()

//获取真实的屏幕高度，注意判断非0
getRootHeight()

//返回按键事件拦截（重写此方法，return true为拦截返回按键事件）
onBack()
```
另外，为方便开发，从 6.7.2 版本起，会自动对布局中使用“back”作为 id 的 View 会自动绑定返回事件（可重写）

### <a name="1-6">BaseActivity的生命周期</a>

通常的，您可以通过重写 onCreate()、 onResume()、 onPause()、 onDestroy() 四个方法来监控 Activity 的生命周期，但因为每个方法为独立的方法函数，可能在较多代码时不容易寻找他们的存在。

在 BaseActivity 中，您可以通过 setLifeCircleListener(LifeCircleListener LifeCircleListener) 来直接添加一个生命周期监听器，其中就包含了上述的四个方法，进行统一管理：

```
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
```

如果你要监控所有 BaseActivity 的生命周期，在 6.5.6 版本起新增了 setGlobalLifeCircleListener(GlobalLifeCircleListener globalLifeCircleListener) 用于对所有 BaseActivity 进行统一管理：
```
BaseActivity.setGlobalLifeCircleListener(new GlobalLifeCircleListener() {
    @Override
    public void onCreate(BaseActivity me, String className) {

    }
    @Override
    public void onResume(BaseActivity me, String className) {

    }
    @Override
    public void onPause(BaseActivity me, String className) {

    }
    @Override
    public void onDestroy(BaseActivity me, String className) {

    }
});
```
注意此方法为静态的，要使用它建议在 Application 中对它进行管理。

从 6.6.9 版本起新增了 setOnActivityStatusChangeListener(...) 用于监听 Activity 的创建、关闭以及全部 Activity 退出的状态：
```
AppManager.setOnActivityStatusChangeListener(new AppManager.OnActivityStatusChangeListener() {
    @Override
    public void onActivityCreate(BaseActivity activity) {
    
    }
    @Override
    public void onActivityDestroy(BaseActivity activity) {
    
    }
    @Override
    public void onAllActivityClose() {
        Log.e(">>>", "所有Activity已经关闭");
    }
});
```

### <a name="1-7">侧滑返回</a>

从 6.5.8 版本起，您可以对 BaseActivity 进行注解，来实现侧滑返回：
```
@SwipeBack(true)        //开启侧滑返回
public class YourActivity extends BaseActivity {
//...
```

效果如下：

![SwipeBack](https://github.com/kongzue/Res/raw/master/app/src/main/res/mipmap-xxxhdpi/baseframework_swipeback.png)

此效果使用到的框架来源于开源的 @ikew0ng 的 SwipeBackLayout(<https://github.com/ikew0ng/SwipeBackLayout>) 开源协议为 Apache License2.0

### <a name="1-8">布局绑定和事件绑定</a>

在 BaseActivity 和 BaseFragment 中均可使用事件绑定

使用注解 @BindView(int id) 以替代 findViewById(int id) 方法：
```
@BindView(R.id.btn_ok)
private Button okButton;
```

使用注解 @BindViews(int id) 以替代 findViewById(int id) 方法：
```
@BindViews({R.id.key1, R.id.key2, R.id.key3})
private List<Button> keyboardButtons;
```

使用注解 @OnClick(int id) 来代替 setOnClickListener(listener)：
```
@OnClick(R.id.btn_ok)
public void startTest(){
    //点击方法
}

//也可接收 View：
@OnClick(R.id.btn_ok)
public void startTest(View btnOk){
    //点击方法
}
```

## <a name="2">BaseFragment功能</a>

### <a name="2-0">BaseFragment 是什么</a>
BaseFragment 与普通的 Fragment 有什么区别？

首先，创建它变得异常的简单，你只需要在class上注解 `@Layout(你的布局资源文件id，例如R.layout.xxx)` 即可，剩下的事情BaseFragment会自动帮你完成。

在构建 BaseFragment 时，建议直接使用泛型指定你要绑定的 BaseActivity，然后你就可以使用 `me.` 来调用该 BaseActivity 中 public 的方法和元素了，例如：
```
@Layout(R.layout.fragment_demo)
public class FragmentDemo extends BaseFragment<MainActivity> {      //此处约定泛型
   
    @Override
    public void setEvents() {
        me.changeFragment(settingFragment);             //此处 me 代替已实例化的 MainActivity，且 changeFragment(...) 是 MainActivity 中的方法。
    }
}
```
若不想约定，可将泛型设置为 BaseActivity。

除此之外，我们还支持了直接使用 `findViewById` ，而不需要额外的找到根布局 rootView，再 rootView.findViewById(...)，查看代码了解更多

BaseFragment 同样支持 BaseActivity 同款 <a href="#1-5">小工具和组件</a>，例如便捷的 `toast(...)`、`log(...)`，您可以轻松使用它们。

BaseFragment 也支持生命周期集中管理，您同样可以在 BaseFragment 中通过 `setLifeCircleListener(LifeCircleListener LifeCircleListener)` 监控 BaseFragment 的生命周期。

6.6.8 版本起，BaseFragment 新增一个可重写方法 onShow，之前的 onLoad 会在首次显示该 Fragment 时触发，而 onShow 是用来替代 onResume 的，但不会在预加载时触发。

另外，onShow 的参数 isSwitchFragment(boolean) 用于判断是否是从其他 Fragment 切换到此界面。


### <a name="2-1">绑定 Layout 布局</a>

BaseFragment 默认使用注解来绑定布局：
```
@Layout(R.layout.activity_demo)
public class FunctionFragment extends BaseFragment<DemoActivity> {
    ...
```
BaseFragment 默认有一个泛型（非必需），请传入你要绑定的目标 Activity，这样就可以直接在 BaseFragment 中通过 me. 来直接调用绑定 Activity 的 public 成员或方法。

不建议重写 onCreate 方法，根据约定，请无需关心绑定布局的过程，你只需要在 initView() 方法中绑定、加载 View 组件，initData() 方法中加载数据，在 setEvents() 方法中绑定事件即可。

除了注解绑定外，你也可以使用 resetLayoutResId() 方法重载布局资源，例如实现自定义主题：
```
@Override
protected int resetLayoutResId() {
    if (darkMode){
        return R.layout.activity_demo_dark;
    }else{
        return super.resetLayoutResId();
    }
}
```

额外的，你还可以拦截绑定过程，暂时不绑定布局（可能会出现错误，仅用于特殊情况）：
```
@Override
public boolean interceptSetContentView() {
    return true;            //拦截绑定布局的过程
}
```

与 BaseActivity 一样，你也可以通过 @BindView(...)、@OnClick(...) 等方法绑定组件布局和设置点击事件，详情请参考 BaseActivity 的 <a href="#1-8">布局绑定和事件绑定</a> 章节。

### <a name="2-2">FragmentChangeUtil</a>

6.7.1 版本起，BaseActivity 中已集成此组件，无需自定义 FragmentChangeUtil，详情见 <a href="#2-3">BaseFragment 最佳实践</a>

6.6.4 版本起新增 FragmentChangeUtil 工具便于在 BaseActivity 中轻松进行 Fragment 的绑定和切换，使用方法如下：

1) 初始化
```
//参数 BaseActivity 为要绑定到的 BaseActivity，参数 frameLayoutResId 为在该 Activity 中显示 Fragment 的容器，一般使用 FrameLayout 即可
FragmentChangeUtil util = new FragmentChangeUtil(BaseActivity me, int frameLayoutResId);
```

2) 添加 Fragment 到管理工具
```
//普通添加方式：
util.addFragment(new HomeFragment());
util.addFragment(new MessageFragment());
util.addFragment(new MeFragment());

//预加载添加方式（initViews、initDatas、setEvents、onResume 都会被触发）
util.addFragment(new HomeFragment(), true);
util.addFragment(new MessageFragment(), true);
util.addFragment(new MeFragment(), true);
```

3) 切换到指定 Fragment
```
//使用对象
util.show(fragment);

//或使用索引：
util.show(int index);
```
* 索引即已添加的 Fragment 的编号。

4) FragmentChangeUtil 的额外方法：
```
//获取目前有几个已添加的 Fragment
util.getCount();    

//获取当前正在显示的 Fragment 对象
util.getFocusFragment();

//获取当前正在显示的 Fragment 对象的索引编号
util.getFocusFragmentIndex();

//删除对象
util.remove(fragment);

//隐藏当前 Fragment
util.hideNow();

//隐藏指定索引的 Fragment
util.hide(int index);

//隐藏指定 Fragment
util.hide(fragment);

//获得指定 Fragment
util.getFragment(int index);

//使用Class来获得堆栈中最后的指定 Fragment
util.getFragment(Class fragmentClass);

//返回已添加的 Fragment 数量
util.size();

//添加切换动画（请在show(...)方法前执行）
util.anim(int enterAnimResId, int exitAnimResId)

//返回按键事件拦截（重写此方法，return true为拦截返回按键事件）
onBack()
```

FragmentChangeUtil 现在提供两种 add 方式，一种是默认参数的 addFragment(BaseFragment fragment)，不再执行预加载，也就是说，执行后，仅添加了 Fragment 而不会执行任何事件。

另一种 addFragment(BaseFragment fragment,boolean isPreload)，第二个参数为 true 时会预加载，initViews、initDatas、setEvents、onResume 都会被触发，这个和之前是一样的。

另外，为方便开发，从 6.7.2 版本起，会自动对布局中使用“back”作为 id 的 View 会自动绑定返回事件（可重写）

### <a name="2-3">BaseFragment 最佳实践</a>

⚠ 此章节内容仅限 6.7.1 版本以上使用。

Fragment 依赖于 Activity 来进行显示，在 BaseFramework 中，您可以在 BaseActivity 中创建实例化一个 FrameLayout 布局作为存放 Fragment 的容器：
```
<FrameLayout
    android:id="@+id/frame"
    android:layout_width="match_parent"
    android:layout_height="match_parent" />
```

然后在 BaseActivity 上添加注解：
```
@FragmentLayout(R.id.frame)
public class DemoActivity extends BaseActivity {

    //已准备好的 Fragment 
    private IntroductionFragment introductionFragment = new IntroductionFragment();
    private FunctionFragment functionFragment = new FunctionFragment();
    private AboutFragment aboutFragment = new AboutFragment();

    ...
}
```

在 DemoActivity 中重写方法 initFragment(...)：
```
@Override
public void initFragment(FragmentChangeUtil fragmentChangeUtil) {
    //在此处将准备好的 Fragment 添加到 fragmentChangeUtil
    fragmentChangeUtil.addFragment(introductionFragment);
    fragmentChangeUtil.addFragment(functionFragment);
    fragmentChangeUtil.addFragment(aboutFragment);
    
    //默认显示第一个 Fragment
    changeFragment(0);
}
```
使用 changeFragment(int index) 或 changeFragment(baseFragment) 可以快速完成 Fragment 的切换步骤。

使用 getFragmentChangeUtil() 可以获取已实例化的 fragmentChangeUtil 对象。

添加切换动画：
```
//根据指定角标切换到相应 Fragment
changeFragment(int index, int enterAnimResId, int exitAnimResId);

//根据对象切换至相应 Fragment
changeFragment(BaseFragment fragment, int enterAnimResId, int exitAnimResId);
```

### <a name="2-4">BaseFragment 间的数据传递和回调</a>

⚠ 此章节内容仅限 6.7.1 版本以上使用，使用此方法的前提请参考 <a href="#2-3">BaseFragment 最佳实践</a>。

在同一个 BaseActivity 中的多个 BaseFragment 实例的场景中，BaseFragment 可以使用自带的 jump(...) 方法实现与其他 BaseFragment 间的跳转和数据传输，具体方法如下：
```
//跳转到指定角标的 Fragment，角标即在 BaseActivity 中 fragmentChangeUtil 添加 BaseFragment 时的顺序值
jump(int index);

//跳转到指定 Fragment
jump(BaseFragment fragment);
```

携带参数跳转：
```
jump(1, new JumpParameter()
                .put("参数1", "这是一段文字参数")
                .put("参数2", "这是一段文字参数")
);

//或
jump(functionFragment, new JumpParameter()
                .put("参数1", "这是一段文字参数")
                .put("参数2", "这是一段文字参数")
);
```

获得返回值：
```
jump(1, new OnJumpResponseListener() {
    @Override
    public void OnResponse(JumpParameter parameter) {
        if (parameter == null) {
            toast("未返回任何数据");
        } else {
            toast("收到返回数据，参数“返回数据1”中的值为：" + parameter.get("返回数据1"));
        }
    }
});

//或
jump(functionFragment, new OnJumpResponseListener() {
    @Override
    public void OnResponse(JumpParameter parameter) {
        if (parameter == null) {
            toast("未返回任何数据");
        } else {
            toast("收到返回数据，参数“返回数据1”中的值为：" + parameter.get("返回数据1"));
        }
    }
});
```

带参数且获得返回值：
```
jump(1, new JumpParameter()
                .put("参数1", "这是一段文字参数")
                .put("参数2", "这是一段文字参数")
        , new OnJumpResponseListener() {
    @Override
    public void OnResponse(JumpParameter parameter) {
        if (parameter==null){
            toast("未返回任何数据");
        }else{
            toast("收到返回数据，参数“返回数据1”中的值为：" + parameter.get("返回数据1"));
        }
    }
});

//或
jump(functionFragment, new JumpParameter()
                .put("参数1", "这是一段文字参数")
                .put("参数2", "这是一段文字参数")
        , new OnJumpResponseListener() {
    @Override
    public void OnResponse(JumpParameter parameter) {
        if (parameter==null){
            toast("未返回任何数据");
        }else{
            toast("收到返回数据，参数“返回数据1”中的值为：" + parameter.get("返回数据1"));
        }
    }
});
```

回传返回值的方法：
```
setFragmentResponse(JumpParameter parameter);
```

## <a name="3">设置、属性值的存储读取工具 Settings</a>

如果您编写了 BaseApp 的实现类 App（具体请参考章节<a href="#10">《BaseApp功能》</a>），您可以使用以下方法进行设置的读写：
```
//参数 "path" 为分区名，之后您可以单独清除某个分区的存储信息而不影响其他已存储的信息
//写设置：
App.Settings("path").set("key", "value");
App.Settings.set("path", "key", "value");

//读设置
String value = App.Settings("path").getString("key");
String value2 = App.Settings.getString("path", "key");
```

额外的，您可以直接将 bitmap 进行缓存：
```
//图像读写：
Bitmap bm;      //可直接存储 Bitmap  
App.Settings("path").set("bitmap", bm);
Bitmap readBmp = App.Settings("path").getBitmap("bitmap");
//备注：图像读写本质上是借助 PictureCache 类在缓存区进行的文件存储操作，你也可以手动使用 PictureCache 进行读写操作。
```

对分区数据进行清除：
```
//数据清除
App.Settings("path").clean();
```

对于序列化对象，也可以直接使用 Settings 进行存取：

例如，对于序列化对象 User 进行读写即：
```
//存储
User user = new User("张三", 18, "192.168.1.1");
App.user.set("userInfo", user);

//读取（请注意结果可能为 null）
User user = App.user.getObject("userInfo", User.class);
```

为了更容易区分和编写数据存储逻辑，您可以自行编写存储类继承 SettingsUtil 来实现存储读写。

例如 Demo 中，我们编写了用于存储用户信息的 USER 存储类：
```
public class USER extends SettingsUtil {
    
    public USER() {
        //初始化方法用于确定存储分区（path），之后您可以单独清除某个分区的存储信息而不影响其他已存储的信息
        super("user");
    }
}
```
然后在 App 类中编写了 USER 的实现：
```
public static USER user = new USER();
```

接下来，即可在应用的任意地方使用 App.user 来访问用户信息的读写操作，无需再次传入分区名作为参数
```
 App.user.set("userName", "张三");
```

### 自定义存储器实现实例（非必需操作）
SettingsUtil 的底层是 SharedPreferences 的封装，而 SharedPreferences 是可以使用第三方实现的。

默认不设置则使用系统默认的 SharedPreferences 实现方式。

要使用第三方实现，可以在 SettingsUtil.init(...) 回调中给出第三方实现的实例化对象即可：
```
//可选：自定义 SharedPreferences 实例（可选，默认不设置即使用系统 SharedPreferences 实例）
SettingsUtil.init(new Preferences.ChangeSharedPreferencesPathCallBack() {
    @Override
    public SharedPreferences onPathChange(String path) {
        //sharedPreferences: 可以使用第三方的 SharedPreferences 实例，例如来自腾讯的 MMKV 等
        return sharedPreferences;
    }
});
```

## <a name="4">AppManager</a>
AppManager 是 BaseActivity 的管理工具类，原工具是由 @xiaohaibin(<https://github.com/xiaohaibin>) 所开发，经同意集成在 BaseFramework 中，此处略加修改更适合 BaseActivity 的管理工作。

提供如下方法：
```
getActiveActivity()             //获取当前处于活跃的BaseActivity（注意可能为null）
killActivity(baseActivity)      //结束指定BaseActivity
killAllActivity()               //结束所有BaseActivity
AppExit()                       //退出App
```
其他方法，例如 pushActivity 添加Activity到堆栈，都是自动执行的，不需要手动调用。

## <a name="5">异步或同步</a>
有时我们需要等待一段时间执行事务，也有时我们需要从异步线程返回主线程进行 UI 等操作，这时往往需要在线程间进行切换进行操作，但偶尔也会因为执行过程中因 Activity 被关闭等问题出现空指针异常。

此时可以使用 BaseFramework 自带的异步同步方法轻松跳跃线程进行操作，此方法包含在 BaseActivity 和 BaseFragment 中：

1) 回到主线程操作：
```
runOnMain(new Runnable(){
    //此处可切换回主线程进行操作
});
```

2) 创建延迟在主线程执行操作：
```
runOnMainDelayed(new Runnable(){
    //此处可切换回主线程进行延迟操作
}, time);       //time 即延迟时间，毫秒单位
```

3) 创建延迟操作（用于代替 new Handler().postDelayed(Runnable, time)）：
```
runDelayed(new Runnable(){
    //此处可进行延迟操作（可能在异步线程）
}, time);       //time 即延迟时间，毫秒单位
```

## <a name="6">BaseAdapter</a>
注意，此处的 BaseAdapter 特指 com.kongzue.baseframework.BaseAdapter。

![Kongzue's BaseAdapter](https://github.com/kongzue/Res/raw/master/app/src/main/res/mipmap-xxxhdpi/download_baseadapter.png)

通常由系统提供的的 android.widget.BaseAdapter 和 android.widget.SimpleAdapter 虽然都能满足我们的日常所需，但面对各种自定义需求以及多种布局的需求时往往捉襟见肘，以至于我们不得不重写 Adapter 来满足需求。

但再重写过程中实际上是有很多重复性的代码，导致我们的项目臃肿不堪，从 v6.4.8 版本起，新增了 BaseAdapter 来实现各种自定义布局适配器的需求：

### <a name="6-1">JavaBean 适配方式</a>

使用此方式需要先创建继承自 BaseAdapter.BaseDataBean 的 JavaBean 数据集合来封装数据，例如在我们 Demo 中的：
```
List<CustomDatas> datas = new ArrayList();
datas.add(new CustomDatas().setTitle("我是布局1"));
datas.add(new CustomDatas().setTitle("我是布局2"));
datas.add(new CustomDatas().setTitle("我是布局3"));
```
其中 CustomDatas 的具体代码为：
```
private class CustomDatas extends BaseAdapter.BaseDataBean {
    String title;

    @Override
    public CustomDatas setType(int type) {
        this.type = type;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public CustomDatas setTitle(String title) {
        this.title = title;
        return this;
    }
}
```
它是一个典型的 JavaBean，其中所有属性请根据实际业务需求添加，并建议生成相应的 get、set 方法。

接下来创建适配器并绑定在相应组件上：
```
baseAdapter = new BaseAdapter(me, datas, R.layout.item_list_layout1, new SimpleAdapterSettings() {
    @Override
    public Object setViewHolder(View convertView) {
        ViewHolder1 viewHolder1 = new ViewHolder1();
        viewHolder1.txtTitle = convertView.findViewById(R.id.txt_title);
        return viewHolder1;
    }

    @Override
    public void setData(Object viewHolder, BaseAdapter.BaseDataBean dataBean) {
        CustomDatas data = (CustomDatas) dataBean;
        ViewHolder1 viewHolder1 = (ViewHolder1) viewHolder;
        viewHolder1.txtTitle.setText(data.getTitle());
    }
});
list.setAdapter(baseAdapter);
```
SimpleAdapterSettings 是一个适配器控制器的回调接口，在其中重写 setViewHolder 和 setData方法，其中 setViewHolder 需要您在此处根据父布局 convertView 创建布局管理组件 ViewHolder，并回传您的 ViewHolder。接下来会在 setData 中将 ViewHolder 和 相对应的数据 dataBean给出，请在此方法中对组件进行赋值和事件绑定。
注意在此方法中您可以将 dataBean 强转为您的 JavaBean 类，viewHolder 也可以强转为您的 ViewHolder。

### <a name="6-2">Map 适配方式</a>

应对复杂多变的数据我们可能会选择使用 Map 来存储我们的需要展现的数据，BaseAdapter 亦支持此方式的数据，与上述方法类似，您可以轻松完成数据的绑定和组件的展现：
```
List<Map<String, Object>> datas = new ArrayList<>();
Map<String, Object> map = new HashMap<>();
map.put("title", "我是布局1");
datas.add(map);
map = new HashMap<>();
map.put("title", "我是布局2");
datas.add(map);
map = new HashMap<>();
map.put("title", "我是布局3");
datas.add(map);
baseAdapter = new BaseAdapter(me, datas, R.layout.item_list_layout1, new SimpleMapAdapterSettings() {
    @Override
    public Object setViewHolder(View convertView) {
        ViewHolder1 viewHolder1 = new ViewHolder1();
        viewHolder1.txtTitle = convertView.findViewById(R.id.txt_title);
        return viewHolder1;
    }

    @Override
    public void setData(Object viewHolder, Map<String, Object> data) {
        ViewHolder1 viewHolder1 = (ViewHolder1) viewHolder;
        viewHolder1.txtTitle.setText(data.get("title") + "");
    }
});
list.setAdapter(baseAdapter);
```

### <a name="6-3">多种布局的绑定方式</a>

根据实际业务需求，我们可能需要在一个组件中展现多种布局，此时您首先需要对您的布局进行编号，从0开始，依次往后，并将他们添加为一个 Map 集合，其中键值对：id对应布局资源id（LayoutResId）：
```
Map<Integer, Integer> layoutResIdMap = new HashMap<>();
layoutResIdMap.put(0, R.layout.item_list_layout1);
layoutResIdMap.put(1, R.layout.item_list_layout2);
layoutResIdMap.put(2, R.layout.item_list_layout3);
```
接下来，您需要将数据存储为一个集合，此处展示的是 JavaBean 形式的存储方式，您亦可以使用 Map 作为数据的存储器，最后将它打包为一个 List即可：
```
List<CustomDatas> datas = new ArrayList();
datas.add(new CustomDatas().setTitle("我是布局1").setType(0));
datas.add(new CustomDatas().setTitle("我是布局2").setType(1));
datas.add(new CustomDatas().setTitle("我是布局3").setType(2));
```
需要注意的是，之前提到过，您的 JavaBean（CustomDatas）需要继承自 BaseAdapter.BaseDataBean，而在 BaseAdapter.BaseDataBean 中，我们默认实现了一个属性“type”，它是 int 整数型，用于存储与布局对应的编号 id。

如果您默认使用 Map 的方式存储数据，您需要手动 put("type", 对应布局编号id ) 以保证能够和布局资源相匹配。

最后，创建适配器和绑定展示组件：
```
baseAdapter = new BaseAdapter(me, datas, layoutResIdMap, new MultipleAdapterSettings() {
    @Override
    public Object setViewHolder(int type, View convertView) {
        switch (type) {
            case 0:
                ViewHolder1 viewHolder1 = new ViewHolder1();
                viewHolder1.txtTitle = convertView.findViewById(R.id.txt_title);
                return viewHolder1;
            case 1:
                ViewHolder2 viewHolder2 = new ViewHolder2();
                viewHolder2.txtTitle = convertView.findViewById(R.id.txt_title);
                return viewHolder2;
            case 2:
                ViewHolder3 viewHolder3 = new ViewHolder3();
                viewHolder3.txtTitle = convertView.findViewById(R.id.txt_title);
                return viewHolder3;
            default:
                return null;
        }
    }

    @Override
    public void setData(int type, Object viewHolder, BaseAdapter.BaseDataBean dataBean) {
        CustomDatas data = (CustomDatas) dataBean;
        switch (type) {
            case 0:
                ViewHolder1 viewHolder1 = (ViewHolder1) viewHolder;
                viewHolder1.txtTitle.setText(data.getTitle());
                break;
            case 1:
                ViewHolder2 viewHolder2 = (ViewHolder2) viewHolder;
                viewHolder2.txtTitle.setText(data.getTitle());
                break;
            case 2:
                ViewHolder3 viewHolder3 = (ViewHolder3) viewHolder;
                viewHolder3.txtTitle.setText(data.getTitle());
                break;
        }
    }
});
list.setAdapter(baseAdapter);
```
从上述代码中可以看到，回调函数中出现了一个 type 的值，在这里您可以根据不同的值绑定不同的布局，设置不同的数据和事件。

以上就是关于 BaseAdapter 的简单介绍了。您还可以通过文档前半部分的二维码下载 Demo ，其中会为您展现关于 BaseAdapter 全部的绑定方式。

### <a name="6-4">数据刷新方法</a>

从 v6.5.4 版本起，我们添加了 refreshDataChanged(...) 用于代替 notifyDataSetChanged() 刷新数据，该方法主要目的为解决 notifyDataSetChanged() 对于某些内容变化不敏感的问题。

使用方法为：
```
baseAdapter.refreshDataChanged(List<Map<String, Object>> newDatas);
//或
baseAdapter.refreshDataChanged(ArrayList<? extends BaseDataBean> newDatas);
```

## <a name="8">增强型日志</a>

![Kongzue's log](https://github.com/kongzue/Res/raw/master/app/src/main/res/mipmap-xxxhdpi/baseframework_newlog.png)

从 6.5.8 版本起使用 log(...) 方法输出的日志已升级为增强型日志。

在您使用 BaseFramework 时可以在 Logcat 的筛选中使用字符 “>>>” 对日志进行筛选（Logcat日志界面上方右侧的搜索输入框）。

1.在 Activity 启动和关闭时会有自动的提示：

```
D/>>>: MainActivity:onCreate
...
D/>>>: MainActivity:onDestroy
```
您可以在 Android Studio 的 File -> Settings 的 Editor -> Color Scheme -> Android Logcat 中调整各类型的 log 颜色，我们推荐如下图方式设置颜色：

![Kongzue's log settings](https://github.com/kongzue/Res/raw/master/app/src/main/res/mipmap-xxxhdpi/baseframework_logsettings.png)

2.对json进行自动格式化

使用 log(...) 方法输出日志内容时，若内容是 json 字符串，会自动格式化输出，方便查看。


## <a name="7">行为与日志监听 + 不死的小强</a>

![Kongzue's Beta Plan](https://github.com/kongzue/Res/raw/master/app/src/main/res/mipmap-xxxhdpi/betaplan_baseframework.jpg)

从 6.5.7 版本起新增了行为与日志监听功能，此功能默认是关闭的，此功能旨在帮助开发者进行良好的排错，包含Activity基本生命周期、使用log(...)语句输出的、使用toast(...)语句建立提示的、以及崩溃信息，在开启BETA_PLAN模式的情况下将按照App启动时间生成活动日志文件，以方便将App移交测试组进行测试。

此功能的优势在于：

1.可记录设备信息、系统版本信息、软件版本信息、设备ID等；

2.可根据需要重点记录一些日志信息：注意，此工具仅限记录各Activity启停状态、使用BaseActivity、BaseFragment的log、toast方法生成的信息以及崩溃信息）而不会记录其他方式生成的日Logcat信息；

3.可根据日志顺序按流程追溯用户操作步骤；

您可以前往此处查看<a href="BUGREPORT.md">日志文件内容样例</a>

从 6.7.5 版本起可通过重写 BaseApp 作为 Application 并通过以下代码直接配置 CrashListener：

```
setOnCrashListener(new OnBugReportListener() {
    @Override
    public boolean onCrash(Exception e, final File crashLogFile) {
        //TODO: 请在这里处理异常信息
        //return true时，会在执行完上述代码后关闭 App，但 return false，则会拦截此错误，App 不会闪退，继续运行
        return false;   
    }
});
```
通过 OnBugReportListener 的 onCrash(...) 方法返回值，可拦截 App 运行过程中的异常，而不让 App 闪退。

您可以在这里弹出一个对话框用于提示用户是否反馈错误信息，并选择继续保持 App 的运行，具体请参考代码：<a href="https://github.com/kongzue/BaseFramework/blob/master/app/src/main/java/com/kongzue/baseframeworkdemo/App.java">BaseApp Demo</a>

### 开启功能

开启所有日志记录功能，包含 Activity 基本生命周期、使用 log(...) 语句输出的、使用 toast(...) 输出的信息：
```
BaseFrameworkSettings.BETA_PLAN = true;
```

开启崩溃日志监控功能：
```
//除了可以在 BaseApp 中开启此功能外，也可通过 BaseFrameworkSettings 单独设置：
BaseFrameworkSettings.turnOnReadErrorInfoPermissions(context, new OnBugReportListener() {

    @Override
    public boolean onCrash(Exception e, final File crashLogFile) {
        //TODO: 请在这里处理异常信息
        Log.v(">>>", "onReporter: "+crashLogFile.getAbsolutePath());    //crashLogFile 为闪退信息存储的文件
        return false;   
    }
});
```
当发生崩溃时，会执行此回调，此监听器中返回错误信息及发生崩溃的整个 App 运行周期的日志文件（含崩溃信息）

崩溃日志监控功能可以在不开启 BETA_PLAN 的情况下单独使用。

注：获取的日志文件为 .bfl 格式的文本文件，可通过任意文本编辑器打开。

### 建议
建议在 OnBugReportListener 中接收到日志文件后，显示对话框提示用户是否愿意帮助改进App，并在用户同意后上传文件到您的服务器。

## <a name="9">语言变更工具</a>

从 6.6.2 版本起新增了语言变更工具，如果需要多语言支持，您可以先在 string.xml 中配置多国语言，然后使用如下方法进行语言变更：

```
BaseFrameworkSettings.selectLocale = Locale.ENGLISH;                //强制变成为英语
BaseFrameworkSettings.selectLocale = Locale.CHINA;                  //强制变成为汉语
BaseFrameworkSettings.selectLocale = Locale.SIMPLIFIED_CHINESE;     //强制变成为简体中文
BaseFrameworkSettings.selectLocale = Locale.TRADITIONAL_CHINESE;    //强制变成为繁体中文
```

建议您在 Application 启动时设置此属性。

需注意的是，修改此属性后并不会立即生效，需要重启您的 Activity 才可以生效，您也可以选择设置后重启当前 Activity 使其生效：

```
//注意先保存所有数据，然后使用如下语句重启当前Activity：
restartMe();
```

## <a name="10">BaseApp功能</a>

6.7.2 版本起新增 BaseApp 作为 Application 的基础类。

BaseApp 除了提供类似于 BaseActivity 以及 BaseFragment 的 log(...)、toast(...) 等快捷方法和 me 关键字外，提供两个重写方法 init() 以及 initSDKs() 分别用于初始化 App 数据以及 SDK。

init() 会在主线程执行，用于取代 onCreate() 方法，initSDKs() 则会在异步线程执行，用于初始化可能耗时较大的 SDK，且可以通过 setOnSDKInitializedCallBack(OnSDKInitializedCallBack) 方法获得SDK 初始化完成后的回调。

以下代码是 BaseApp 的实现范例：
```
public class App extends BaseApp<App> {     //此处泛型 App 是用于将 关键词 me 映射成您的 App 类，以方便通过 me 关键词使用和访问 App 中的公开方法及成员变量。
    
    @Override
    public void init() {
        setOnSDKInitializedCallBack(new OnSDKInitializedCallBack() {
            @Override
            public void onInitialized() {
                log("onInitialized: ");
                Toast.makeText(me, "SDK已加载完毕", Toast.LENGTH_LONG).show();
            }
        });
    }
    
    @Override
    public void initSDKs() {
        BaseFrameworkSettings.DEBUGMODE = true;
        BaseFrameworkSettings.BETA_PLAN = true;
        try {
            Thread.sleep(8000);             //模拟大负载SDK加载缓慢的过程
        }catch (Exception e){}
    }
}
```

运行后可查看结果，App 启动不会受 initSDKs() 时等待的 8 秒影响，另外也可通过 isInitializedSDKs() 方法判断 SDK 是否已经加载完毕。 

另外，OnSDKInitializedCallBack 回调方法 onInitialized() 是自动回到主线程执行的，无需额外处理。

从 6.7.5 版本起，新增 Settings 方法/静态类用于直接取代设置、属性值的存储读取工具 Preferences，具体请参考 <a href="#3">**设置、属性值的存储读取工具 Preferences**</a> 章节。

额外的，可通过如下方法直接关闭 App：
```
.exit();    //退出 App
```

### <a name="10-1">BaseApp功能提供的小工具</a>
```
//快速调用 Toast：
toast(Obj);

//简易Log打印日志（BaseFrameworkSettings.DEBUGMODE = false关闭，注意此开关是同时影响 BaseActivity 和 BaseFragment的）：
log(Obj);

//简易Log打印日志（E级别）
errorLog(...)

//dip与像素px转换：
dip2px(float dpValue);

//像素px与dip转换：
dip2px(float dpValue);

//数据判空（适合网络返回值判断处理，即便为字符串“null”也为空）：
isNull(String);

//打开指定App
openApp(String packageName)

//检测App是否已安装
isInstallApp(String packageName)

//获取状态栏的高度
getStatusBarHeight()

//获取屏幕宽度
getDisplayWidth()

//获取屏幕可用部分高度（屏幕高度-状态栏高度-屏幕底栏高度）
getDisplayHeight()

//获取底栏高度
getNavbarHeight()

//获取真实的屏幕高度，注意判断非0
getRootHeight()
```

## 开源协议
```
Copyright BaseFramework

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

  http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```

## <a name="about">更新日志</a>：

v6.7.8:

- 新增了@OnClicks(...)注解可以绑定多个组件的点击事件；
- 修复了在initDatas()、setEvents()方法中调用jump并设置返回监听器时出现返回监听器直接空值返回的问题。
- @FragmentLayout(...)新增ViewPager支持；
- FragmentChangeUtil 增加了 show 方法可选参数 `autoHideOldFragment`，现在可以自定义是否隐藏正在显示的 Fragment；
- 修复了在 API-29 以上时使用 Activity 透明主题可能出现无法透明的问题；

v6.7.7:

- 取消了强制`@Layout`设置布局的形式，现在还可以通过重写`resetLayoutResId()`方法来绑定布局；
- 在 BaseActivity 和 BaseFragment 中提供了全新的可重写`onBack()`方法，以取代BaseActivity的onBackPressed()方法，当此方法return值为true时，将拦截返回事件，若当前Activity正在显示一个BaseFragment，那么会优先询问正在显示的BaseFragment是否拦截；
- 在 BaseActivity 和 BaseFragment 中提供了可重写`lazyInit()`方法，此方法执行过程是在当前 Activity 或 Fragment 完成渲染后执行。
- BaseFragment 提供了`jump(int index, OnJumpResponseListener onJumpResponseListener)`可直接跳转到其他 BaseFragment 并在返回后获得返回数据。
- 修复了 BaseFrameworkSettings.log(...) 重复打印日志的 bug；

v6.7.6:

- 为 Demo 项目提供了完整的注释信息；
- Settings 提供了 JavaBean 的读写支持；
- BaseApp 提供 getInstance(Class) 方法获取已实例化的 App 对象；
- BaseActivity 和 BaseFragment 新增实验性的 click(view, OnClickListener) 防重复点击的方法；
- BaseActivity 和 BaseFragment 新增 showIME(@notNull editText) 和 hideIME(@nullable editText) 方法用于快速显示/隐藏输入法；
- 新增 PictureCache 工具类，BaseApp 提供 Settings 提供 bitmap 的缓存读写；
- 修复 DebugLogG 在创建日志文件时出现的空指针问题；
- 修复调用 AppManager.getActiveActivity() 的空指针问题；

v6.7.5:
- LogG 统一日志打印流程；
- BaseActivity 和 BaseFragment 提供可重写的方法 interceptSetContentView() 可阻止默认初始化布局的流程；
- BaseActivity 和 BaseFragment 提供可重写的方法 resetLayoutResId() 用于重定向布局资源 id（可用于主题换服等操作）；
- BaseApp 提供 Settings 方法及静态类用于快速完成设置键值的读写；
- BaseApp 提供 setOnCrashListener(...) 可设置崩溃闪退拦截，可通过回调 onCrash 的 return 值操作是否因闪退关闭 App；
- BaseApp 提供 exit() 方法可直接退出程序；
- 修复可能因系统资源回收导致 Fragment 切换失效的bug；

v6.7.4:
- AppManager 新增方法 getActiveActivity() 方法获取当前活跃的 BaseActivity 对象；
- 修复 AppManager 的 getActivityInstance(...)、deleteActivity(...)、killActivity(...)、finishActivity()、currentActivity()方法可能引发的空指针异常；
- 修改 FragmentChangeUtil 默认 getFocusFragment() 方法返回 BaseFragment()；
- 完善 Preferences 自定义 SharedPreferences 对象的方法及新增切换 Path 的回调方法；
- 完善 BaseActivity 中使用绑定 FragmentChangeUtil 在部分内存不足导致 Activity 被释放的情况下导致 Fragment 无法加载的问题；
- BaseActivity 和 BaseFragment 的 error(...) 输出错误日志的方法被修改为 errorLog(...)；

v6.7.3:
- 新增 @NavigationBarBackgroundColorHex 直接设置HEX颜色值设置底栏背景颜色；
- @NavigationBarBackgroundColorInt 和 @NavigationBarBackgroundColorRes 设置时取消 key；
- 新增注解 @OnClick 和 @BindView、@BindViews 注解以绑定布局和点击事件；
- 修复 AppManager 可能存在的 deleteActivity 方法空指针 bug；
- FragmentChangeUtil 新增 getFragment(Class) 方法获取已添加的 Fragment 实例化对象；
- FragmentChangeUtil 新增 anim(int enterAnimResId, int exitAnimResId) 可在 show() 方法前设置切换动画；
- BaseActivity 新增 changeFragment(..., int enterAnimResId, int exitAnimResId) 切换已绑定的 Fragment 时的切换动画；
- BaseActivity 更新获取底栏高度方法；

v6.7.2:
- 新增 BaseApp，详情请查看 <a href="#10">BaseApp功能</a>；
- BaseActivity 和 BaseFragment 中布局使用“back”作为 id 的 View 会自动绑定返回事件（可重写）；
- BaseActivity 新增注解 @NavigationBarBackgroundColorInt(colorInt) 以及 @NavigationBarBackgroundColorRes(colorResId) 可使用 ColorInt 以及颜色资源 ID 设置底部导航栏背景颜色；
- Preferences 新增 initSharedPreferences(SharedPreferences) 初始化方法可使用其他 SharedPreferences 实例；
- FragmentChangeUtil 新增 size() 可获取当前绑定的 Fragment 数量；
- 修复非处于活动状态的 BaseFragment 的 onShow 事件在 BaseActivity 的 resume 过程中被触发的问题；

v6.7.1:
- BaseActivity 默认集成 FragmentChangeUtil，可使用 @FragmentLayout(layoutId) 注解，方便一键绑定 Fragment 布局和 FragmentChangeUtil 管理器；
- FragmentChangeUtil 新增 getFragment(index) 方法可获取已添加的 BaseFragment；
- FragmentChangeUtil 新增 OnFragmentChangeListener 可设置 BaseFragment 显示时监听器；
- BaseFragment 新增可重写的 onHide() 方法，以判断当 Fragment 被切换时触发；
- BaseFragment 新增 jump(BaseFragment) 相关方法，包含参数传递和回调方法，用于跳转至同一 BaseActivity 中其他已绑定的 BaseFragment，并传递信息；
- BaseFragment 新增 setFragmentResponse(JumpParameter) 方法用于设置回传数据；
- 修复了 BaseFragment 生命周期 onPause 存在的调用错误 bug；
- 修复崩溃提醒失效的 bug；
- 新增部分方法注释信息；
- 更新 Demo APP；

v6.7.0:
- 代码规范化提升；
- 修复了 AppManager 中可能存在的空指针问题；
- Preferences 提供 commit 方法与 set 方法并存存储属性值，前者会立即保存，而后者更节省资源；

v6.6.9:
- 提升代码规范化；
- AppManager 新增 setOnActivityStatusChangeListener(...) 用于监听 Activity 的创建、关闭以及全部 Activity 退出的状态；

v6.6.8:
- FragmentChangeUtil 新增 hideNow() 方法与 remove(fragment) 方法；
- FragmentChangeUtil 提供普通添加方式和预加载方式；
- BaseFragment 提供可重写方法 onShow 用于取代 onResume；

v6.6.7:
- 新增 error(...) 以代替快速调用 Log.e(...) ;
- BaseFragment 新增方法 onLoad 以处理只在首次显示时执行的事务。
- FragmentChangeUtil 新增 hide(...) 方法用于隐藏显示的 Fragment；

v6.6.6:
- BaseFragment 现已可使用泛型，来直接访问父 Activity 中的 public 方法和元素； 
- 尝试性的提供了 Toast 的兼容模式，兼容解决部分设备因关闭“悬浮窗权限”导致 Toast 无法正常使用的问题，请使用 toastS(Object) 来调用此功能，或者使用 Toaster 类相关方法提供更多功能和可玩性。
- BaseActivity 新增 getRootView() 方法可直接获取根布局；
- 修复了 FragmentChangeUtil 在切换时错误调用未初始化状态的子 Fragment.onResume() 的问题；

v6.6.5:
- AppManager 新增排除结束方法 killOtherActivityExclude(class) 可排除指定 Activity 并结束之外的所有 Activity；
- BaseActivity 以及 BaseFragment 新增 getColorS(resId) 以替代系统提供且不让修改的半残方法 getColor(resId)；

v6.6.4:
- 修复了属性动画存在的bug；
- BaseAdapter 新增泛型功能；
- 修复了 BaseFragment 重复加载导致组件指针绑定问题；
- 新增 FragmentChangeUtil 管理工具，以便于在 BaseActivity 中轻松进行 Fragment 的绑定和切换；

v6.6.3:
- 修复了获取导航栏高度值错误的问题；
- BaseFragment 中属性动画方法更新；

v6.6.2:
- 新增语言变更工具，具体请参照<a href="#9">语言变更工具</a>；

v6.6.1:
- BaseActivity 新增方法：获取设备IMEI：getIMEI()、获取设备AndroidID：getAndroidId()、获取Mac地址：getMacAddress()；
- BaseFragment 新增方法 getStatusBarHeight()、getDisplayWidth()、getDisplayHeight()、getNavbarHeight()、getRootHeight()、getIMEI()、getAndroidId()、getMacAddress()；

v6.6.0:
- 组件升级至兼容 API-28；

v6.5.9:
- 修复了全屏注解 @FullScreen(true) 和侧滑返回可能存在冲突的问题；
- BaseActivity 和 BaseFragment 新增成员变量 savedInstanceState 即 onCreate 发生时传递的 Bundle；

v6.5.8:
- BaseActivity、BaseFramework 新增 openUrl(...) 可直接打开使用默认浏览器打开 url 地址；
- BaseActivity、BaseFramework 新增 openApp(...) 可直接打开指定包名的 App；
- BaseActivity、BaseFramework 新增 isInstallApp(...) 可直接判断指定包名的 App 是否已安装；
- log(...) 方法增强，全新的日志表现形式；
- log(...) 输出 json 时会自动格式化 json 语法；
- BaseActivity 新增注解 @SwipeBack(true) 可标记当前 Activity 支持侧滑返回；

v6.5.7.2:
- 新增判空规则，支持iOS可能传递的“(null)”文本；

v6.5.7.1:
- 修复无法引用的bug；

v6.5.7:
- 修复了 AppManager 中 killActivity(Class) 可能引发崩溃的bug；
- 修改，将 BaseActivity.DEBUGMODE 移动到了 BaseFrameworkSettings.DEBUGMODE，原 BaseActivity.DEBUGMODE 不再使用；
- 新增<a href="#7">行为与日志监听</a>功能；

v6.5.6.1:
- 修复 JumpParameter 空指针问题；

v6.5.6:
- BaseActivity 新增全局生命周期管理 GlobalLifeCircleListener；
- 回传数据方法 setResponse(...) 现新增更符合直觉的 returnParameter(...)；
- JumpParameter 可直接解析更多数据类型，例如 double、float、long、short 等；
- initDatas(JumpParameter parameter) 中的参数 parameter 不再需要非空校验了；

v6.5.5.3:
- 修复遗留问题，BaseFragment的dip2px和px2dip将无需context参数；
- 修复遗留问题，getDisplayWidth()函数名已被修改正确；

v6.5.5.2:
- 修复 lifeCircleListener.onCreate() 无效的问题；

v6.5.5.1:
- 警告：因命名冲突，6.5.5版本起，跳转回调参数 OnResponseListener 改名为 OnJumpResponseListener；
- 可以使用bigLog(...)打印更长的日志了；
- dip2px和px2dip不再需要context参数；
- 显示/关闭键盘的方法setIMMStatus(...)将被更名为showIME(...)，目前新旧方法都可以使用；
- 更新getNavbarHeight()方法，可以获取到更为准确的底栏高度；
- 新增getRootHeight()方法，可以获取准确真正的屏幕高度（含底栏和状态栏）；
- 新增BaseActivity注解 @FullScreen(true)可以直接使Activity全屏；
- 修正了 @NavigationBarBackgroundColor(a,r,g,b)的默认值；
- jump支持了共享元素，只需要在原参数末尾增加共享元素view，例如jump(Class<?> cls, View transitionView)；

详细的更新说明请阅读：《BaseFramework 6.5.5.1版本更新报告》：<https://www.jianshu.com/p/9c2e0039aca1>

v6.5.4:
- 增加 BaseActivity 与 BaseFragment 一键管理生命周期监听器，可在 BaseActivity 的子类中使用 setLifeCircleListener(LifeCircleListener);
- BaseAdapter 增加了 refreshDataChanged(...) 用于代替 notifyDataSetChanged() 刷新数据，新方法对于内容的变化也很敏感；

v6.5.3:
- 可以使用 runOnMain(Runnable) 来执行需要在主线程执行的事务，该方法与 runOnUiThread() 的不同点在于会自动判断当前 BaseActivity 是否处于存活状态，无须担心因此出现的空指针问题；
- 可以使用 runOnMainDelayed(Runnable, time) 来执行需要在主线程延迟执行的事务；
- 可以使用 runDelayed(Runnable, time) 来替代 new Handler().postDelayed(Runnable, time) 执行延迟事务；

v6.5.2:
- 修复 setIMMStatus(boolean, Edittext) 开关输入法方法中，Edittext 可能为 NULL 导致空指针的问题；
- 跳转到应用设置方法 startAppSettings() 不再是私有的，他现在可以公开调用；

v6.5.1:
- 修复 bug；

v6.5.0:
- 修复 BaseFragment 中日志 log 打印不受 BaseActivity.DEBUGMODE 控制的问题；

v6.5.0:
- 跳转参数 JumpParameter 新增 getBoolean、getInt 和 getString 三个基础方法，从此方法获取数据不需要判断是否为空（null）以及进行强转类型；

v6.4.9:
- 为避免与 BaseOkHttp 框架冲突修改 com.kongzue.baseframework.util.Parameter 类名为 com.kongzue.baseframework.util.JumpParameter;
- initDatas() 现已改为携带参数的 initDatas(JumpParameter paramer); 可以直接获取使用jump方法跳转时所携带的跳转参数，请注意非空判断；

v6.4.8:
- 新增BaseAdapter；

v6.4.7:
- 修复一些bug；

v6.4.6:
- 新增AppManager管理器；
- 新增DarkNavigationBarTheme、DarkStatusBarTheme、NavigationBarBackgroundColor注解；

v6.4.0:
- 集成Preferences（SharedPreferences的封装，仅使用简单的get、set方法即可）

v6.3.0:
- 直接使用注解的方式绑定布局资源（@Layout）
- 为BaseFragment增添支持新的jump(...)跳转方法；

v6.2.0:
- 支持新的jump(...)跳转方法；
- 更新BaseFragment；

v6.1.0:
- 合并 BaseActivity 与 BaseFragment 为 BaseFramework 总框架；
