package com.kongzue.baseframework.util;

import android.util.Log;

import com.kongzue.baseframework.BaseFrameworkSettings;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @Version: 6.7.0
 * @Author: Kongzue
 * @github: https://github.com/kongzue/BaseFramework
 * @link: http://kongzue.com/
 */

public class JumpParameter {

    private Map<String, Object> dataMap;

    public JumpParameter(Map<String, Object> dataMap) {
        this.dataMap = dataMap;
    }

    public JumpParameter() {
    }

    public JumpParameter(String jsonStr) {
        try {
            JSONObject jsonObject = new JSONObject(jsonStr);
            Iterator keys = jsonObject.keys();
            while (keys.hasNext()) {
                String key = keys.next() + "";
                String value = jsonObject.optString(key);
                put(key, value);
            }
        } catch (Exception e) {
            if (BaseFrameworkSettings.DEBUGMODE) {
                Log.e(">>>", "JumpParameter.create: Error json: " + jsonStr);
            }
        }
    }

    public JumpParameter put(String key, Object value) {
        if (dataMap == null) {
            dataMap = new HashMap<>();
        }
        dataMap.put(key, value);
        return this;
    }

    public JumpParameter set(String key, Object value) {
        if (dataMap == null) {
            dataMap = new HashMap<>();
        }
        dataMap.put(key, value);
        return this;
    }

    public JumpParameter cleanAll() {
        dataMap = new HashMap<>();
        return this;
    }

    public <T> T get(String key) {
        if (dataMap == null) {
            return null;
        }
        return (T) dataMap.get(key);
    }

    public boolean getBoolean(String key) {
        if (dataMap == null || dataMap.get(key) == null) {
            return false;
        }
        return (boolean) dataMap.get(key);
    }

    public boolean getBoolean(String key, boolean defaultValue) {
        if (dataMap == null || dataMap.get(key) == null) {
            return defaultValue;
        }
        return (boolean) dataMap.get(key);
    }

    public int getInt(String key) {
        if (dataMap == null || dataMap.get(key) == null) {
            return 0;
        }
        return (int) dataMap.get(key);
    }

    public int getInt(String key, int defaultValue) {
        if (dataMap == null || dataMap.get(key) == null) {
            return defaultValue;
        }
        return (int) dataMap.get(key);
    }

    public String getString(String key) {
        if (dataMap == null || dataMap.get(key) == null) {
            return "";
        }
        return (String) dataMap.get(key);
    }

    public String getString(String key, String defaultValue) {
        if (dataMap == null || dataMap.get(key) == null) {
            return defaultValue;
        }
        return (String) dataMap.get(key);
    }

    public double getDouble(String key) {
        if (dataMap == null || dataMap.get(key) == null) {
            return 0;
        }
        return (double) dataMap.get(key);
    }

    public double getDouble(String key, double defaultValue) {
        if (dataMap == null || dataMap.get(key) == null) {
            return defaultValue;
        }
        return (double) dataMap.get(key);
    }

    public long getLong(String key) {
        if (dataMap == null || dataMap.get(key) == null) {
            return 0;
        }
        return (long) dataMap.get(key);
    }

    public long getLong(String key, long defaultValue) {
        if (dataMap == null || dataMap.get(key) == null) {
            return defaultValue;
        }
        return (long) dataMap.get(key);
    }

    public short getShort(String key) {
        if (dataMap == null || dataMap.get(key) == null) {
            return 0;
        }
        return (short) dataMap.get(key);
    }

    public short getShort(String key, short defaultValue) {
        if (dataMap == null || dataMap.get(key) == null) {
            return defaultValue;
        }
        return (short) dataMap.get(key);
    }

    public float getFloat(String key) {
        if (dataMap == null || dataMap.get(key) == null) {
            return 0;
        }
        return (float) dataMap.get(key);
    }

    public float getFloat(String key, float defaultValue) {
        if (dataMap == null || dataMap.get(key) == null) {
            return defaultValue;
        }
        return (float) dataMap.get(key);
    }

    public Map<String, Object> getAll() {
        return dataMap;
    }

    public String toJsonString() {
        return new JSONObject(dataMap).toString();
    }
}