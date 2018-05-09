package com.kongzue.baseframework.util;

public interface OnPermissionResponseListener {
    void onSuccess(String[] permissions);
    void onFail();
}
