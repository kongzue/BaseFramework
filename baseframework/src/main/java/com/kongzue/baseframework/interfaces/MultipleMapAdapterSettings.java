package com.kongzue.baseframework.interfaces;

import android.view.View;

import com.kongzue.baseframework.BaseAdapter;

import java.util.Map;

public interface MultipleMapAdapterSettings<E> {
    E setViewHolder(int type, View convertView);
    
    void setData(int type, E vh, Map<String, Object> data);
}