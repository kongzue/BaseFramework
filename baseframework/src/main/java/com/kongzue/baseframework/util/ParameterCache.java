package com.kongzue.baseframework.util;

import android.util.Log;

import java.util.HashMap;
import java.util.Map;

/**
 * 数据传递的中间人
 * 本类负责缓存数据，承担 BaseActivity、BaseFragment 之间数据传输的缓存角色
 */
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
        if (parameterList == null) {
            return null;
        }
        return parameterList.get(className);
    }

    public void set(String className, JumpParameter jumpParameter) {
        if (parameterList == null) {
            parameterList = new HashMap<>();
        }
        parameterList.put(className, jumpParameter);
    }


    private Map<String, JumpParameter> parameterResponseList;

    public JumpParameter getResponse(String className) {
        if (parameterResponseList == null) {
            return null;
        }
        return parameterResponseList.get(className);
    }

    public void setResponse(String className, JumpParameter jumpParameter) {
        if (parameterResponseList == null) {
            parameterResponseList = new HashMap<>();
        }
        parameterResponseList.put(className, jumpParameter);
    }

    public void cleanResponse(String className) {
        if (parameterResponseList == null) {
            return;
        }
        parameterResponseList.put(className, null);
    }
    
    public void cleanParameter(String className) {
        if (parameterList == null) {
            return;
        }
        parameterList.put(className, null);
    }
}
