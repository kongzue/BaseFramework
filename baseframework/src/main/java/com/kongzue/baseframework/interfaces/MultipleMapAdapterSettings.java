package com.kongzue.baseframework.interfaces;

import android.view.View;

import com.kongzue.baseframework.BaseAdapter;

import java.util.Map;

public interface MultipleMapAdapterSettings {
    Object setViewHolder(int type, View convertView);
    
    void setData(int type, Object vh, Map<String, Object> data,int index);
}