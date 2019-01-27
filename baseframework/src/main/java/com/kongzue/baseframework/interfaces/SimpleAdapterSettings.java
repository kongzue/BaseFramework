package com.kongzue.baseframework.interfaces;

import android.view.View;

import com.kongzue.baseframework.BaseAdapter;

public interface SimpleAdapterSettings<V, D> {
    V setViewHolder(View convertView);

    void setData(V viewHolder, D data, int index);
}
