package com.kongzue.baseframework.util;

import java.util.HashMap;
import java.util.Map;

public class JumpParameter {
    
    private Map<String, Object> datas;
    
    public JumpParameter put(String key, Object value) {
        if (datas == null) datas = new HashMap<>();
        datas.put(key, value);
        return this;
    }
    
    public JumpParameter cleanAll() {
        datas = new HashMap<>();
        return this;
    }
    
    public Object get(String key) {
        return datas.get(key);
    }
    
    public boolean getBoolean(String key) {
        if (datas.get(key) == null) {
            return false;
        }
        return (boolean) datas.get(key);
    }
    
    public int getInt(String key) {
        if (datas.get(key) == null) {
            return 0;
        }
        return (int) datas.get(key);
    }
    
    public String getString(String key) {
        if (datas.get(key) == null) {
            return "";
        }
        return (String) datas.get(key);
    }
    
    public Map<String, Object> getAll() {
        return datas;
    }
}
