package com.kongzue.baseframework.util;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.kongzue.baseframework.BaseFrameworkSettings.DEBUGMODE;

/**
 * Author: @Kongzue
 * Github: https://github.com/kongzue/
 * Homepage: http://kongzue.com/
 * Mail: myzcxhh@live.cn
 * CreateTime: 2018/11/9 22:06
 */
public class JsonFormat {
    
    public static final String LINE_SEPARATOR = System.getProperty("line.separator");
    
    public static boolean formatJson(String msg) {
        if (!DEBUGMODE) return false;
        String message;
        try {
            if (msg.startsWith("{")) {
                JSONObject jsonObject = new JSONObject(msg);
                message = jsonObject.toString(4);//最重要的方法，就一行，返回格式化的json字符串，其中的数字4是缩进字符数
            } else if (msg.startsWith("[")) {
                JSONArray jsonArray = new JSONArray(msg);
                message = jsonArray.toString(4);
            } else {
                return false;
            }
        } catch (JSONException e) {
            return false;
        }
        
        String[] lines = message.split(LINE_SEPARATOR);
        for (String line : lines) {
            Log.i(">>>>>>", line);
        }
        return true;
    }
    
}
