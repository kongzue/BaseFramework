# BaseFramework
BaseFramework框架是我对之前编程开发的一些总结，目的是以最快的方式完成项目开发，因此将一些常用的小工具，例如简易吐司、简易log等放在了基础类中，只需要将您项目中的Activity或Fragment继承本框架中的BaseActivity以及BaseFragment，即可使用。
除此之外BaseActivity还提供沉浸式适配，您可以查看Demo的源代码来了解更多。
本界面就是基于BaseActivity实现的Demo。

<a href="https://github.com/kongzue/BaseFramework/">
<img src="https://img.shields.io/badge/BaseFramework-6.2.0-green.svg" alt="Kongzue Dialog">
</a> 
<a href="https://bintray.com/myzchh/maven/BaseFramework/6.2.0/link">
<img src="https://img.shields.io/badge/Maven-6.2.0-blue.svg" alt="Maven">
</a> 
<a href="http://www.apache.org/licenses/LICENSE-2.0">
<img src="https://img.shields.io/badge/License-Apache%202.0-red.svg" alt="Maven">
</a> 
<a href="http://www.kongzue.com">
<img src="https://img.shields.io/badge/Homepage-Kongzue.com-brightgreen.svg" alt="Maven">
</a> 

Demo预览图如下：

![BaseFramework](https://github.com/kongzue/Res/raw/master/app/src/main/res/mipmap-xxxhdpi/BaseFramework.png)

试用版可以前往 http://kongzue.com/open_source/BaseFramework.apk 下载

![Kongzue's Dialog Demo](https://github.com/kongzue/Res/raw/master/app/src/main/res/mipmap-xxxhdpi/download_baseframework.png)

## 使用前的约定与须知
- 在 BaseActivity 中，约定关键词me代替Activity.this，因此您在编写代码时，在异步线程中可以轻松使用me关键字直接引用当前Activity。

- 无论是在 BaseActivity 还是 BaseFragment ，默认都有 initViews()、initDatas()、setEvents() 三个方法，他们分别代表加载组件、初始化数据、组件绑定事件三个步骤，因其执行顺序是固定的，且为了代码规范化，这三个方法必须重写，也建议将相关业务逻辑写在对应方法中，以方便维护和管理。

## Maven仓库或Gradle的引用方式
Maven仓库：
```
<dependency>
  <groupId>com.kongzue.baseframework</groupId>
  <artifactId>baseframework</artifactId>
  <version>6.2.0</version>
  <type>pom</type>
</dependency>
```
Gradle：
在dependencies{}中添加引用：
```
implementation 'com.kongzue.baseframework:baseframework:6.2.0'
```

## BaseActivity功能

#### 带自定义参数的跳转
Android 默认的 Intent无法支持自定义类型参数的跳转，BaseActivity 通过自有的数据通道允许传输自定义类型的数据给要跳转到的另一个 BaseActivity：

跳转代码范例：
```
Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.mipmap.img_bkg);
jump(JumpActivity.class, new Parameter()
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

#### 更简单的跳转后返回数据
以往我们需要通过重写实现 onActivityResult 来实现回传数据，但在 BaseActivity 中，你只需要一个监听器：

跳转代码范例：
```
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
```

亦可选用同时带参数+返回值的跳转：
```
jump(ResponseActivity.class,new Parameter()
                .put("参数1", "这是一段文字参数")
                .put("参数2", "这是一段文字参数")
        , new OnResponseListener() {
    @Override
    public void OnResponse(Parameter parameter) {
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
```

#### 权限申请
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

#### 除此之外，BaseActivity还支持以下小工具
//简易吐司：
toast(Obj);
//简易Log打印日志：
log(Obj);
//软键盘开关：
setIMMStatus(boolean show, EditText editText);
//dip与像素px转换：
dip2px(Context context, float dpValue);
//像素px与dip转换：
dip2px(Context context, float dpValue);
//属性动画：
moveAnimation(Object obj, String perference, float aimValue, long time, long delay);
//数据判空（适合网络返回值判断处理，即便为字符串“null”也为空）：
isNull(String);

## BaseFragment功能
BaseFragment 与普通的 Fragment 有什么区别？

首先，创建它变得异常的简单，你只需要重写 getLayout() ，并 return 你的 layout 的资源ID（R.layout.xxx）即可，剩下的事情 BaseFragment 会自动帮你完成。

除此之外，我们还支持了直接使用 findViewById ，而不需要额外的找到根布局 rootView，再 rootView.findViewById(...)，查看代码了解更多

BaseFragment 同样支持 BaseActivity 的一些小工具和组件，您可以轻松使用它们。

## 更新日志：
v6.2.0:
- 支持新的jump(...)跳转方法；
- 更新BaseFragment；

v6.1.0:
- 合并 BaseActivity 与 BaseFragment 为 BaseFramework 总框架；
