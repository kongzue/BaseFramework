package com.kongzue.baseframework.experimental;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: Kongzue
 * @github: https://github.com/kongzue/
 * @homepage: http://kongzue.com/
 * @mail: myzcxhh@live.cn
 * @createTime: 2019/9/30 12:36
 */
public class BaseAdapter<D extends BaseAdapter.SimpleData> extends android.widget.BaseAdapter {
    
    private List<D> dataList;
    private LayoutInflater mInflater;
    private BindView<D> bindView;
    
    public BaseAdapter(Context context,List<D> dataList) {
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.dataList = dataList;
    }
    
    @Override
    public int getCount() {
        return dataList.size();
    }
    
    @Override
    public D getItem(int position) {
        return dataList.get(position);
    }
    
    @Override
    public long getItemId(int position) {
        return position;
    }
    
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        D data = dataList.get(position);
        if (convertView == null) {
            View childView = mInflater.inflate(data.layoutResId(), null);
            convertView = childView;
        }
        if (bindView != null) {
            bindView.onBind(convertView, data);
        }
        return convertView;
    }
    
    public BaseAdapter<D> setBindView(BindView<D> bindView) {
        this.bindView = bindView;
        return this;
    }
    
    public interface BindView<D> {
        void onBind(View view, D data);
    }
    
    public interface SimpleData {
        int layoutResId();
    }
}