package com.kongzue.baseframework.interfaces;

import android.view.View;

import com.kongzue.baseframework.BaseAdapter;

import java.util.Map;

public interface SimpleMapAdapterSettings {
    Object setViewHolder(View convertView);
    
    void setData(Object viewHolder, Map<String, Object> data);
}
