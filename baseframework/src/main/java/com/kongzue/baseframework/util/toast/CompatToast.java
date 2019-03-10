package com.kongzue.baseframework.util.toast;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.kongzue.baseframework.R;

import java.util.Timer;
import java.util.TimerTask;

import static com.kongzue.baseframework.util.toast.Toaster.DURATION;

public class CompatToast extends BaseToast {
    
    private Context context;
    private View contentView;
    private PopupWindow popupWindow;
    private Timer timer;
    
    private int animation = android.R.style.Animation_Toast;
    private int gravity = Gravity.BOTTOM | Gravity.CENTER;
    
    private int xOffset;
    private int yOffset = dip2px(80);
    
    public CompatToast(Context context) {
        this.context = context;
    }
    
    @Override
    public BaseToast show(String msg) {
        Toaster.cancel();
        popupWindow = new PopupWindow(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        
        LayoutInflater inflate = (LayoutInflater)
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflate.inflate(R.layout.layout_system_toast, null);
        TextView tv = v.findViewById(R.id.message);
        tv.setText(msg);
        
        popupWindow.setContentView(v);
        popupWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));
        popupWindow.setOutsideTouchable(false);
        popupWindow.setClippingEnabled(false);
        popupWindow.setFocusable(false);
        
        popupWindow.setAnimationStyle(animation);
        
        popupWindow.showAtLocation(((Activity) context).getWindow().getDecorView(), Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM, xOffset, yOffset);
        
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                ((Activity) context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toaster.cancel();
                    }
                });
            }
        }, DURATION == Toast.LENGTH_SHORT ? 2000 : 3500);
    
        Toaster.nowToast = this;
        return this;
    }
    
    @Override
    public BaseToast show(int layoutResId) {
        Toaster.cancel();
        popupWindow = new PopupWindow(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.contentView = layoutInflater.inflate(layoutResId, null);
        
        popupWindow.setContentView(contentView);
        popupWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));
        popupWindow.setOutsideTouchable(false);
        popupWindow.setClippingEnabled(false);
        popupWindow.setFocusable(false);
    
        popupWindow.setAnimationStyle(animation);
    
        popupWindow.showAtLocation(((Activity) context).getWindow().getDecorView(), Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM, xOffset, yOffset);
    
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                ((Activity) context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toaster.cancel();
                    }
                });
            }
        }, DURATION == Toast.LENGTH_SHORT ? 2000 : 3500);
    
        Toaster.nowToast = this;
        return this;
    }
    
    @Override
    public BaseToast cancel() {
        Log.i(".>>", "cancel: ");
        if (popupWindow != null) popupWindow.dismiss();
        return this;
    }
    
    public int dip2px(float dpValue) {
        return (int) (0.5f + dpValue * Resources.getSystem().getDisplayMetrics().density);
    }
    
    public int getAnimation() {
        return animation;
    }
    
    public BaseToast setAnimation(int animation) {
        this.animation = animation;
        return this;
    }
    
    public int getGravity() {
        return gravity;
    }
    
    public BaseToast setGravity(int gravity) {
        this.gravity = gravity;
        return this;
    }
    
    public int getxOffset() {
        return xOffset;
    }
    
    public BaseToast setxOffset(int xOffset) {
        this.xOffset = xOffset;
        return this;
    }
    
    public int getyOffset() {
        return yOffset;
    }
    
    public BaseToast setyOffset(int yOffset) {
        this.yOffset = yOffset;
        return this;
    }
}
