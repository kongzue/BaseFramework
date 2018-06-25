package com.kongzue.baseframework.interfaces;

import android.view.View;

import com.kongzue.baseframework.BaseAdapter;

public interface SimpleAdapterSettings {
    Object setViewHolder(View convertView);
    
    void setData(Object viewHolder, BaseAdapter.BaseDataBean dataBean);
}
