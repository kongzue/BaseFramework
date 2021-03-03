package com.kongzue.baseframework.util;

import java.util.HashMap;
import java.util.Map;
/**
 * @Version: 6.7.0
 * @Author: Kongzue
 * @github: https://github.com/kongzue/BaseFramework
 * @link: http://kongzue.com/
 */

public class JumpParameter {
    
    private Map<String, Object> datas;
    
    public JumpParameter put(String key, Object value) {
        if (datas == null) {
            datas = new HashMap<>();
        }
        datas.put(key, value);
        return this;
    }
    
    public JumpParameter cleanAll() {
        datas = new HashMap<>();
        return this;
    }
    
    public Object get(String key) {
        if (datas == null) {
            return null;
        }
        return datas.get(key);
    }
    
    public boolean getBoolean(String key) {
        if (datas == null || datas.get(key) == null) {
            return false;
        }
        return (boolean) datas.get(key);
    }
    
    public boolean getBoolean(String key, boolean defaultValue) {
        if (datas == null || datas.get(key) == null) {
            return defaultValue;
        }
        return (boolean) datas.get(key);
    }
    
    public int getInt(String key) {
        if (datas == null || datas.get(key) == null) {
            return 0;
        }
        return (int) datas.get(key);
    }
    
    public int getInt(String key, int defaultValue) {
        if (datas == null || datas.get(key) == null) {
            return defaultValue;
        }
        return (int) datas.get(key);
    }
    
    public String getString(String key) {
        if (datas == null || datas.get(key) == null) {
            return "";
        }
        return (String) datas.get(key);
    }
    
    public String getString(String key, String defaultValue) {
        if (datas == null || datas.get(key) == null) {
            return defaultValue;
        }
        return (String) datas.get(key);
    }
    
    public double getDouble(String key) {
        if (datas == null || datas.get(key) == null) {
            return 0;
        }
        return (double) datas.get(key);
    }
    
    public double getDouble(String key, double defaultValue) {
        if (datas == null || datas.get(key) == null) {
            return defaultValue;
        }
        return (double) datas.get(key);
    }
    
    public long getLong(String key) {
        if (datas == null || datas.get(key) == null) {
            return 0;
        }
        return (long) datas.get(key);
    }
    
    public long getLong(String key, long defaultValue) {
        if (datas == null || datas.get(key) == null) {
            return defaultValue;
        }
        return (long) datas.get(key);
    }
    
    public short getShort(String key) {
        if (datas == null || datas.get(key) == null) {
            return 0;
        }
        return (short) datas.get(key);
    }
    
    public short getShort(String key, short defaultValue) {
        if (datas == null || datas.get(key) == null) {
            return defaultValue;
        }
        return (short) datas.get(key);
    }
    
    public float getFloat(String key) {
        if (datas == null || datas.get(key) == null) {
            return 0;
        }
        return (float) datas.get(key);
    }
    
    public float getFloat(String key, float defaultValue) {
        if (datas == null || datas.get(key) == null) {
            return defaultValue;
        }
        return (float) datas.get(key);
    }
    
    public Map<String, Object> getAll() {
        return datas;
    }
}
