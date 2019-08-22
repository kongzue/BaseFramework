package com.kongzue.baseframework.interfaces;

import android.view.View;

import com.kongzue.baseframework.BaseAdapter;

import java.util.List;
import java.util.Map;

public interface SimpleMapAdapterSettings<V> {
    V setViewHolder(View convertView);
    
    void setData(V viewHolder, Map<String, Object> data, List<Map<String, Object>> dataList, int index);
}