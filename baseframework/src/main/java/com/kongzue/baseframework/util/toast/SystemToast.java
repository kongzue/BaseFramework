package com.kongzue.baseframework.util.toast;

import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import androidx.annotation.NonNull;
import androidx.core.app.NotificationManagerCompat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import static com.kongzue.baseframework.BaseActivity.isMIUI;
import static com.kongzue.baseframework.util.toast.Toaster.isAndroidO;

public class SystemToast extends BaseToast {
    
    private Toast toast;
    private Context context;
    private View contentView;
    
    private int animation = android.R.style.Animation_Toast;
    private int gravity = Gravity.BOTTOM | Gravity.CENTER;
    
    private int xOffset;
    private int yOffset= dip2px(80);
    
    private int duration = Toaster.DURATION;
    
    public SystemToast(Context context) {
        this.context = context;
    }
    
    @Override
    public SystemToast show(String msg) {
        Toaster.cancel();
        
        toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
        hookToast(toast);
        customToast(toast);
        hookINotificationManager(toast, context);
        
        Toaster.nowToast = this;
        
        toast.show();
        return this;
    }
    
    public SystemToast show(int layoutResId) {
        Toaster.cancel();
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.contentView = layoutInflater.inflate(layoutResId, null);
        
        toast = Toast.makeText(context, "", Toast.LENGTH_SHORT);
        hookToast(toast);
        customToast(toast);
        hookINotificationManager(toast, context);
        
        Toaster.nowToast = this;
        
        toast.show();
        return this;
    }
    
    @Override
    public BaseToast cancel() {
        toast.cancel();
        return this;
    }
    
    public SystemToast setGravity(int gravity, int xOffset, int yOffset) {
        this.gravity = gravity;
        this.xOffset = xOffset;
        this.yOffset = yOffset;
        return this;
    }
    
    public SystemToast setGravity(int gravity) {
        setGravity(gravity, 0, 0);
        return this;
    }
    
    public int getAnimation() {
        return animation;
    }
    
    public SystemToast setAnimation(int animation) {
        this.animation = animation;
        return this;
    }
    
    public int getGravity() {
        return gravity;
    }
    
    public int getxOffset() {
        return xOffset;
    }
    
    public SystemToast setxOffset(int xOffset) {
        this.xOffset = xOffset;
        return this;
    }
    
    public int getyOffset() {
        return yOffset;
    }
    
    public SystemToast setyOffset(int yOffset) {
        this.yOffset = yOffset;
        return this;
    }
    
    public int getDuration() {
        return duration;
    }
    
    public SystemToast setDuration(int duration) {
        this.duration = duration;
        return this;
    }
    
    private void hookToast(Toast toast) {
        if (toast == null || Build.VERSION.SDK_INT >= 26) return;
        try {
            Field sField_TN = Toast.class.getDeclaredField("mTN");
            sField_TN.setAccessible(true);
            Field sField_TN_Handler = sField_TN.getType().getDeclaredField("mHandler");
            sField_TN_Handler.setAccessible(true);
            
            Object tn = sField_TN.get(toast);
            Handler preHandler = (Handler) sField_TN_Handler.get(tn);
            sField_TN_Handler.set(tn, new SafelyHandlerWrapper(preHandler));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void customToast(Toast toast) {
        if (toast == null) return;
        if (contentView != null) {
            toast.setView(this.contentView);
        }
        toast.setGravity(this.gravity, xOffset, yOffset);
        try {
            Object mTN = getField(toast, "mTN");
            if (mTN != null) {
                Object mParams = getField(mTN, "mParams");
                if (mParams instanceof WindowManager.LayoutParams) {
                    WindowManager.LayoutParams params = (WindowManager.LayoutParams) mParams;
                    params.windowAnimations = animation;
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        toast.setDuration(duration);
    }
    
    private static Object getField(Object object, String fieldName) throws Exception {
        Field field = object.getClass().getDeclaredField(fieldName);
        if (field != null) {
            field.setAccessible(true);
            return field.get(object);
        }
        return null;
    }
    
    class SafelyHandlerWrapper extends Handler {
        private Handler impl;
        
        public SafelyHandlerWrapper(Handler impl) {
            this.impl = impl;
        }
        
        @Override
        public void dispatchMessage(Message msg) {
            try {
                impl.dispatchMessage(msg);
            } catch (Exception e) {
            }
        }
        
        @Override
        public void handleMessage(Message msg) {
            impl.handleMessage(msg);//需要委托给原Handler执行
        }
    }
    
    private static Object iNotificationManagerProxy;
    
    private static void hookINotificationManager(Toast toast, @NonNull Context mContext) {
        if (toast == null) return;
        if (NotificationManagerCompat.from(mContext).areNotificationsEnabled() || isMIUI())
            return;
        if (isAndroidO()) {
            if (iNotificationManagerProxy != null) return;//代理不为空说明之前已设置成功
            try {
                //生成INotificationManager代理
                Method getServiceMethod = Toast.class.getDeclaredMethod("getService");
                getServiceMethod.setAccessible(true);
                final Object iNotificationManagerObj = getServiceMethod.invoke(null);
                
                Class iNotificationManagerCls = Class.forName("android.app.INotificationManager");
                iNotificationManagerProxy = Proxy.newProxyInstance(toast.getClass().getClassLoader(), new Class[]{iNotificationManagerCls}, new InvocationHandler() {
                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        if ("enqueueToast".equals(method.getName()) || "enqueueToastEx".equals(method.getName())//华为p20 pro上为enqueueToastEx
                                || "cancelToast".equals(method.getName())
                        ) {
                            args[0] = "android";
                        }
                        return method.invoke(iNotificationManagerObj, args);
                    }
                });
                
                //使INotificationManager代理生效
                Field sServiceFiled = Toast.class.getDeclaredField("sService");
                sServiceFiled.setAccessible(true);
                sServiceFiled.set(toast, iNotificationManagerProxy);
            } catch (Exception e) {
                iNotificationManagerProxy = null;
                
            }
        }
    }
    
    public int dip2px(float dpValue) {
        return (int) (0.5f + dpValue * Resources.getSystem().getDisplayMetrics().density);
    }
    
}
