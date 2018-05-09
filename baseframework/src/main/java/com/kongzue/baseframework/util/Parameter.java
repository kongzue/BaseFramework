package com.kongzue.baseframework.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Parameter {

    private Map<String, Object> datas;

    public Parameter put(String key, Object value) {
        if (datas == null) datas = new HashMap<>();
        datas.put(key, value);
        return this;
    }

    public Parameter cleanAll() {
        datas = new HashMap<>();
        return this;
    }

    public Object get(String key){
        return datas.get(key);
    }

    public Map<String, Object> getAll(){
        return datas;
    }
}
