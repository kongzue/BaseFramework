package com.kongzue.baseframework.util;

import java.util.HashMap;
import java.util.Map;

public class ParameterCache {

    private static ParameterCache parameterCache;

    private ParameterCache() {
    }

    public static ParameterCache getInstance() {
        if (parameterCache == null) {
            synchronized (ParameterCache.class) {
                if (parameterCache == null) {
                    parameterCache = new ParameterCache();
                }
            }
        }
        return parameterCache;
    }

    private Map<String, JumpParameter> parameterList;

    public JumpParameter get(String className) {
        if (parameterList == null) return null;
        return parameterList.get(className);
    }

    public void set(String className, JumpParameter jumpParameter) {
        if (parameterList == null) parameterList = new HashMap<>();
        parameterList.put(className, jumpParameter);
    }


    private Map<String, JumpParameter> parameterResponseList;

    public JumpParameter getResponse(String className) {
        if (parameterResponseList == null) return null;
        return parameterResponseList.get(className);
    }

    public void setResponse(String className, JumpParameter jumpParameter) {
        if (parameterResponseList == null) parameterResponseList = new HashMap<>();
        parameterResponseList.put(className, jumpParameter);
    }

    public void cleanResponse(String className) {
        if (parameterResponseList == null) return;
        parameterResponseList.put(className, null);
    }


}
