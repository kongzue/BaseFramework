package com.kongzue.baseframework.interfaces;

import android.view.View;

import com.kongzue.baseframework.BaseAdapter;

public interface SimpleAdapterSettings<E> {
    E setViewHolder(View convertView);
    
    void setData(E viewHolder, BaseAdapter.BaseDataBean dataBean,int index);
}
