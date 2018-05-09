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

    private Map<String, Parameter> parameterList;

    public Parameter get(String className) {
        if (parameterList == null) return null;
        return parameterList.get(className);
    }

    public void set(String className, Parameter parameter) {
        if (parameterList == null) parameterList = new HashMap<>();
        parameterList.put(className, parameter);
    }


    private Map<String, Parameter> parameterResponseList;

    public Parameter getResponse(String className) {
        if (parameterResponseList == null) return null;
        return parameterResponseList.get(className);
    }

    public void setResponse(String className, Parameter parameter) {
        if (parameterResponseList == null) parameterResponseList = new HashMap<>();
        parameterResponseList.put(className, parameter);
    }

    public void cleanResponse(String className) {
        if (parameterResponseList == null) return;
        parameterResponseList.put(className, null);
    }


}
