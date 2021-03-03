package com.kongzue.baseframework.interfaces;

import android.content.Intent;
import android.support.annotation.Nullable;

/**
 * @author: Kongzue
 * @github: https://github.com/kongzue/
 * @homepage: http://kongzue.com/
 * @mail: myzcxhh@live.cn
 * @createTime: 2021/1/19 15:06
 */
public abstract class ActivityResultCallback {
    
    public ActivityResultCallback(int resultId) {
        this.resultId = resultId;
    }
    
    public ActivityResultCallback() {
    }
    
    public int resultId;
    
    public int getResultId() {
        return resultId;
    }
    
    public ActivityResultCallback setResultId(int resultId) {
        this.resultId = resultId;
        return this;
    }
    
    public abstract void onActivityResult(int requestCode, int resultCode, @Nullable Intent data);
}