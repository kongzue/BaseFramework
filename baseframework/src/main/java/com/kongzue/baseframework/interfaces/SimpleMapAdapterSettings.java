package com.kongzue.baseframework.interfaces;

import android.view.View;

import com.kongzue.baseframework.BaseAdapter;

import java.util.Map;

public interface SimpleMapAdapterSettings<E> {
    E setViewHolder(View convertView);
    
    void setData(E viewHolder, Map<String, Object> data,int index);
}
