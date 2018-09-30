# 日志文件内容样例

包含Activity基本生命周期、使用log(...)语句输出的、使用toast(...)语句建立提示的、以及崩溃信息

```
App.Start===============
packageName>>>com.kongzue.baseframeworkdemo
appVer>>>1.0(1)
manufacturer>>>xiaomi
model>>>mi 6
os-ver>>>8.0.0
androidId>>>b5866dbad8c46282

Log.Start===============

MainActivity>>>onCreate
log>>>-12149793

MainActivity>>>onResume

MainActivity>>>onPause

JumpActivity>>>onCreate

JumpActivity>>>onResume

JumpActivity>>>onPause

MainActivity>>>onResume

JumpActivity>>>onDestroy

Error>>>
java.lang.NullPointerException: This is a exception for test
	at com.kongzue.baseframeworkdemo.MainActivity.doTestError(MainActivity.java:298)
	at com.kongzue.baseframeworkdemo.MainActivity.access$000(MainActivity.java:39)
	at com.kongzue.baseframeworkdemo.MainActivity$3.onClick(MainActivity.java:129)
	at android.view.View.performClick(View.java:6281)
	at android.view.View$PerformClick.run(View.java:24745)
	at android.os.Handler.handleCallback(Handler.java:793)
	at android.os.Handler.dispatchMessage(Handler.java:98)
	at android.os.Looper.loop(Looper.java:176)
	at android.app.ActivityThread.main(ActivityThread.java:6701)
	at java.lang.reflect.Method.invoke(Native Method)
	at com.android.internal.os.Zygote$MethodAndArgsCaller.run(Zygote.java:246)
	at com.android.internal.os.ZygoteInit.main(ZygoteInit.java:783)


```