# BaseFramework 更新日志

v6.7.9:
- 修复了加载顺序导致的加载逻辑问题；
- 支持了 @OnClicks 注解，可以绑定多个组件的点击事件；
- 新增了 onBack() 重写事件，若当前 Activity 正在显示 Fragment，会优先询问正在显示的 Fragment 是否拦截返回事件；
- 修改了 @SwipeBack 注解的生效时间，当不存在该注解时不增加 SwipeBack 侧滑返回布局；
- 修改了输入法显示和隐藏相关逻辑，showIME不再强制显示输入法（之前会导致输入法在Activity关闭后无法正常自动关闭）；
- setResponse 逻辑修改，现在可以更正确的在返回原 Activity 后执行回调逻辑；
- 新增 lazyInit(JumpParameter) 重写事件，用于在界面渲染完毕后执行的加载逻辑；
- 新增 runOnResume(Runnable) 方法，可以在 Activity 处于暂停状态时为其设置当该 Activity 恢复显示时执行的逻辑，若该 Activity 当前处于前台，则会直接在主线程执行。另外额外增加了 deleteResumeRunnable(Runnable) 删除指定待执行逻辑以及 cleanResumeRunnable() 清空所有待执行逻辑；
- 新增 startActivityForResult(Intent, ActivityResultCallback) 方法，调用外部 Intent 时轻松获得回调数据；
- AppManager 新增 getActivityInstance(instanceKey) 方法以快速获得 BaseActivity 实例，instanceKey 可根据 BaseActivity 的 getInstanceKey() 方法获得；
- FragmentChangeUtil 新增 getFragment(instanceKey) 方法以快速获得 BaseFragment 实例，instanceKey 可根据 BaseFragment 的 getInstanceKey() 方法获得；
- @FragmentLayout(...) 注解支持了 ViewPager 布局，使用 ViewPager 布局时 BaseFragment 自动绑定至 ViewPager；

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
