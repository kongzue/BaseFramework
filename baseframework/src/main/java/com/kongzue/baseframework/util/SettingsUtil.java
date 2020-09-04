package com.kongzue.baseframework.util;

import android.graphics.Bitmap;
import android.support.annotation.Nullable;
import android.util.Base64;

import com.kongzue.baseframework.BaseApp;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * @author: Kongzue
 * @github: https://github.com/kongzue/
 * @homepage: http://kongzue.com/
 * @mail: myzcxhh@live.cn
 * @createTime: 2020/8/18 12:39
 */
public class SettingsUtil {
    public static void init(Preferences.ChangeSharedPreferencesPathCallBack changeSharedPreferencesPathCallBack) {
        Preferences.getInstance().setChangeSharedPreferencesPathCallBack(changeSharedPreferencesPathCallBack);
    }
    
    private String path;
    
    public SettingsUtil(String path) {
        this.path = path;
    }
    
    public static void set(String path, String key, String value) {
        Preferences.getInstance().set(BaseApp.getPrivateInstance(), path, key, value);
    }
    
    public static void set(String path, String key, boolean value) {
        Preferences.getInstance().set(BaseApp.getPrivateInstance(), path, key, value);
    }
    
    public static void set(String path, String key, int value) {
        Preferences.getInstance().set(BaseApp.getPrivateInstance(), path, key, value);
    }
    
    public static void set(String path, String key, float value) {
        Preferences.getInstance().set(BaseApp.getPrivateInstance(), path, key, value);
    }
    
    public static void set(String path, String key, long value) {
        Preferences.getInstance().set(BaseApp.getPrivateInstance(), path, key, value);
    }
    
    public static void set(String path, String key, double value) {
        Preferences.getInstance().set(BaseApp.getPrivateInstance(), path, key, value);
    }
    
    public static void set(String path, String key, Bitmap value) {
        PictureCache.saveInBackground(BaseApp.getPrivateInstance(), path, key, value, null);
    }
    
    public static boolean set(String path, String key, Serializable value) {
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeObject(value);
            byte[] byteArray = byteArrayOutputStream.toByteArray();
            byte[] base64ByteArray = Base64.encode(byteArray, Base64.DEFAULT);
            String data = new String(base64ByteArray);
            Preferences.getInstance().set(BaseApp.getPrivateInstance(), path, key, data);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    public static void commit(String path, String key, String value) {
        Preferences.getInstance().commit(BaseApp.getPrivateInstance(), path, key, value);
    }
    
    public static void commit(String path, String key, boolean value) {
        Preferences.getInstance().commit(BaseApp.getPrivateInstance(), path, key, value);
    }
    
    public static void commit(String path, String key, int value) {
        Preferences.getInstance().commit(BaseApp.getPrivateInstance(), path, key, value);
    }
    
    public static void commit(String path, String key, float value) {
        Preferences.getInstance().commit(BaseApp.getPrivateInstance(), path, key, value);
    }
    
    public static void commit(String path, String key, long value) {
        Preferences.getInstance().commit(BaseApp.getPrivateInstance(), path, key, value);
    }
    
    public static void commit(String path, String key, double value) {
        Preferences.getInstance().commit(BaseApp.getPrivateInstance(), path, key, value);
    }
    
    public static void commit(String path, String key, Bitmap value) {
        PictureCache.save(BaseApp.getPrivateInstance(), path, key, value);
    }
    
    public static boolean commit(String path, String key, Serializable value) {
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeObject(value);
            byte[] byteArray = byteArrayOutputStream.toByteArray();
            byte[] base64ByteArray = Base64.encode(byteArray, Base64.DEFAULT);
            String data = new String(base64ByteArray);
            Preferences.getInstance().commit(BaseApp.getPrivateInstance(), path, key, data);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    public static Bitmap getBitmap(String path, String key) {
        return PictureCache.read(BaseApp.getPrivateInstance(), path, key).getBitmap();
    }
    
    public static String getString(String path, String key) {
        return Preferences.getInstance().getString(BaseApp.getPrivateInstance(), path, key);
    }
    
    public static String getString(String path, String key, String defaultValue) {
        return Preferences.getInstance().getString(BaseApp.getPrivateInstance(), path, key, defaultValue);
    }
    
    public static boolean getBoolean(String path, String key) {
        return Preferences.getInstance().getBoolean(BaseApp.getPrivateInstance(), path, key);
    }
    
    public static boolean getBoolean(String path, String key, boolean defaultValue) {
        return Preferences.getInstance().getBoolean(BaseApp.getPrivateInstance(), path, key, defaultValue);
    }
    
    public static int getInt(String path, String key) {
        return Preferences.getInstance().getInt(BaseApp.getPrivateInstance(), path, key);
    }
    
    public static int getInt(String path, String key, int defaultValue) {
        return Preferences.getInstance().getInt(BaseApp.getPrivateInstance(), path, key, defaultValue);
    }
    
    public static float getFloat(String path, String key) {
        return Preferences.getInstance().getFloat(BaseApp.getPrivateInstance(), path, key);
    }
    
    public static float getFloat(String path, String key, float defaultValue) {
        return Preferences.getInstance().getFloat(BaseApp.getPrivateInstance(), path, key, defaultValue);
    }
    
    public static long getLong(String path, String key) {
        return Preferences.getInstance().getLong(BaseApp.getPrivateInstance(), path, key);
    }
    
    public static long getLong(String path, String key, long defaultValue) {
        return Preferences.getInstance().getLong(BaseApp.getPrivateInstance(), path, key, defaultValue);
    }
    
    public static double getDouble(String path, String key) {
        return Preferences.getInstance().getDouble(BaseApp.getPrivateInstance(), path, key);
    }
    
    public static double getDouble(String path, String key, double defaultValue) {
        return Preferences.getInstance().getDouble(BaseApp.getPrivateInstance(), path, key, defaultValue);
    }
    
    public static <T> T getObject(String path, String key, Class<T> tClass) {
        try {
            String data = getString(path, key);
            byte[] byteArray = data.getBytes();
            byte[] base64ByteArray = Base64.decode(byteArray, Base64.DEFAULT);
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(base64ByteArray);
            ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
            return (T) objectInputStream.readObject();
        } catch (Exception e) {
            return null;
        }
    }
    
    public static void clean(String path) {
        Preferences.getInstance().cleanAll(BaseApp.getPrivateInstance(), path);
        PictureCache.clean(BaseApp.getPrivateInstance(), path);
    }
    
    public void set(String key, String value) {
        SettingsUtil.set(path, key, value);
    }
    
    public void set(String key, boolean value) {
        SettingsUtil.set(path, key, value);
    }
    
    public void set(String key, int value) {
        SettingsUtil.set(path, key, value);
    }
    
    public void set(String key, float value) {
        SettingsUtil.set(path, key, value);
    }
    
    public void set(String key, long value) {
        SettingsUtil.set(path, key, value);
    }
    
    public void set(String key, double value) {
        SettingsUtil.set(path, key, value);
    }
    
    public void set(String key, Bitmap value) {
        SettingsUtil.set(path, key, value);
    }
    
    public void set(String key, Serializable value) {
        SettingsUtil.set(path, key, value);
    }
    
    public void commit(String key, String value) {
        SettingsUtil.commit(path, key, value);
    }
    
    public void commit(String key, boolean value) {
        SettingsUtil.commit(path, key, value);
    }
    
    public void commit(String key, int value) {
        SettingsUtil.commit(path, key, value);
    }
    
    public void commit(String key, float value) {
        SettingsUtil.commit(path, key, value);
    }
    
    public void commit(String key, long value) {
        SettingsUtil.commit(path, key, value);
    }
    
    public void commit(String key, double value) {
        SettingsUtil.commit(path, key, value);
    }
    
    public void commit(String key, Serializable value) {
        SettingsUtil.commit(path, key, value);
    }
    
    public String getString(String key) {
        return getString(path, key);
    }
    
    public String getStringWithDefaultValue(String key, String defaultValue) {
        return getString(path, key, defaultValue);
    }
    
    public Bitmap getBitmap(String key) {
        return getBitmap(path, key);
    }
    
    public boolean getBoolean(String key) {
        return getBoolean(path, key);
    }
    
    public boolean getBoolean(String key, boolean defaultValue) {
        return getBoolean(path, key, defaultValue);
    }
    
    public int getInt(String key) {
        return getInt(path, key);
    }
    
    public int getInt(String key, int defaultValue) {
        return getInt(path, key, defaultValue);
    }
    
    public float getFloat(String key) {
        return getFloat(path, key);
    }
    
    public float getFloat(String key, float defaultValue) {
        return getFloat(path, key, defaultValue);
    }
    
    public long getLong(String key) {
        return getLong(path, key);
    }
    
    public long getLong(String key, long defaultValue) {
        return getLong(path, key, defaultValue);
    }
    
    public double getDouble(String key) {
        return getDouble(path, key);
    }
    
    public double getDouble(String key, double defaultValue) {
        return getDouble(path, key, defaultValue);
    }
    
    public <T> T getObject(String key, Class<T> tClass) {
        return getObject(path, key, tClass);
    }
    
    public void clean() {
        clean(path);
    }
}
