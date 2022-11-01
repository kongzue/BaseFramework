package com.kongzue.baseframework.util;

import android.Manifest;

import com.kongzue.baseframework.BaseActivity;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * @author: Kongzue
 * @github: https://github.com/kongzue/
 * @homepage: http://kongzue.com/
 * @mail: myzcxhh@live.cn
 * @createTime: 2022/11/2 2:13
 */
public class Permission {
    
    public static Permission build() {
        return new Permission();
    }
    
    private List<String> permissions = new ArrayList<>();
    
    public void get() {
        BaseActivity activity = AppManager.getInstance().getActiveActivity();
        if (activity != null) {
            activity.requestPermission(permissions.toArray(new String[permissions.size()]), null);
        }
    }
    
    public void get(OnPermissionResponseListener onPermissionResponseListener) {
        if (onPermissionResponseListener instanceof OnActivityPermissionCallBack) {
            try {
                Class<?> parameterizedTypeReferenceSubclass = onPermissionResponseListener.getClass();
                Type type = parameterizedTypeReferenceSubclass.getGenericSuperclass();
                ParameterizedType parameterizedType = (ParameterizedType) type;
                Class typeClass = (Class<?>) parameterizedType.getActualTypeArguments()[0];
                BaseActivity activity = AppManager.getInstance().getActivityInstance(typeClass);
                if (activity != null) {
                    activity.requestPermission(permissions.toArray(new String[permissions.size()]), onPermissionResponseListener);
                    return;
                }
            } catch (Exception e) {
            }
        }
        BaseActivity activity = AppManager.getInstance().getActiveActivity();
        if (activity != null) {
            activity.requestPermission(permissions.toArray(new String[permissions.size()]), onPermissionResponseListener);
        }
        
    }
    
    public Permission CALENDAR() {
        permissions.add(Manifest.permission.READ_CALENDAR);
        permissions.add(Manifest.permission.WRITE_CALENDAR);
        return this;
    }
    
    public Permission EXTERNAL_STORAGE() {
        permissions.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        return this;
    }
    
    public Permission STORAGE() {
        permissions.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        return this;
    }
    
    public Permission CAMERA() {
        permissions.add(Manifest.permission.CAMERA);
        return this;
    }
    
    public Permission CONTACTS() {
        permissions.add(Manifest.permission.READ_CONTACTS);
        permissions.add(Manifest.permission.WRITE_CONTACTS);
        permissions.add(Manifest.permission.GET_ACCOUNTS);
        return this;
    }
    
    public Permission LOCATION() {
        permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
        permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        return this;
    }
    
    public Permission MICROPHONE() {
        permissions.add(Manifest.permission.RECORD_AUDIO);
        return this;
    }
    
    public Permission PHONE_STATE() {
        permissions.add(Manifest.permission.READ_PHONE_STATE);
        return this;
    }
    
    public Permission CALL() {
        permissions.add(Manifest.permission.CALL_PHONE);
        return this;
    }
    
    public Permission CALL_LOG() {
        permissions.add(Manifest.permission.READ_CALL_LOG);
        permissions.add(Manifest.permission.WRITE_CALL_LOG);
        return this;
    }
    
    public Permission VOICEMAIL() {
        permissions.add(Manifest.permission.ADD_VOICEMAIL);
        return this;
    }
    
    public Permission SIP() {
        permissions.add(Manifest.permission.USE_SIP);
        return this;
    }
    
    public Permission OUTGOING_CALLS() {
        permissions.add(Manifest.permission.PROCESS_OUTGOING_CALLS);
        return this;
    }
    
    public Permission SENSORS() {
        permissions.add(Manifest.permission.BODY_SENSORS);
        return this;
    }
    
    public Permission SMS() {
        permissions.add(Manifest.permission.SEND_SMS);
        return this;
    }
    
    public Permission SMS_RECEIVE() {
        permissions.add(Manifest.permission.RECEIVE_SMS);
        return this;
    }
    
    public Permission RECEIVE_SMS() {
        permissions.add(Manifest.permission.RECEIVE_SMS);
        return this;
    }
    
    public Permission SMS_READ() {
        permissions.add(Manifest.permission.READ_SMS);
        return this;
    }
    
    public Permission READ_SMS() {
        permissions.add(Manifest.permission.READ_SMS);
        return this;
    }
    
    public Permission RECEIVE_WAP_PUSH() {
        permissions.add(Manifest.permission.RECEIVE_WAP_PUSH);
        return this;
    }
    
    public Permission RECEIVE_MMS() {
        permissions.add(Manifest.permission.RECEIVE_MMS);
        return this;
    }
}
