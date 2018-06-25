package com.kongzue.baseframework;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kongzue.baseframework.interfaces.MultipleAdapterSettings;
import com.kongzue.baseframework.interfaces.MultipleMapAdapterSettings;
import com.kongzue.baseframework.interfaces.SimpleAdapterSettings;
import com.kongzue.baseframework.interfaces.SimpleMapAdapterSettings;

import java.util.List;
import java.util.Map;

public class BaseAdapter extends android.widget.BaseAdapter {
    
    private LayoutInflater mInflater;
    private List<? extends BaseDataBean> datas;
    private List<Map<String, Object>> mapDatas;
    private Context context;
    private SimpleAdapterSettings simpleAdapterSettings;
    private SimpleMapAdapterSettings simpleMapAdapterSettings;
    private MultipleAdapterSettings multipleAdapterSettings;
    private MultipleMapAdapterSettings multipleMapAdapterSettings;
    private boolean isMapDatas = false;
    
    private int layoutResId = -1;
    private Map<Integer, Integer> layoutResIdMap;
    
    private int typeCount = 1;
    
    public BaseAdapter(Context context, List<? extends BaseDataBean> datas, Map<Integer, Integer> layoutResId, MultipleAdapterSettings multipleAdapterSettings) {
        this.datas = datas;
        this.mapDatas = null;
        this.context = context;
        this.layoutResIdMap = layoutResId;
        this.layoutResId = -1;
        this.multipleAdapterSettings = multipleAdapterSettings;
        isMapDatas = false;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        
        typeCount = layoutResIdMap.size();
    }
    
    public BaseAdapter(Context context, List<Map<String, Object>> datas, Map<Integer, Integer> layoutResId, MultipleMapAdapterSettings multipleMapAdapterSettings) {
        this.mapDatas = datas;
        this.datas = null;
        this.context = context;
        this.layoutResIdMap = layoutResId;
        this.layoutResId = -1;
        this.multipleMapAdapterSettings = multipleMapAdapterSettings;
        isMapDatas = true;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        
        typeCount = layoutResIdMap.size();
    }
    
    public BaseAdapter(Context context, List<? extends BaseDataBean> datas, @LayoutRes int layoutResId, SimpleAdapterSettings simpleAdapterSettings) {
        this.datas = datas;
        this.context = context;
        this.layoutResIdMap = null;
        this.layoutResId = layoutResId;
        this.simpleAdapterSettings = simpleAdapterSettings;
        isMapDatas = false;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        
        typeCount = 1;
    }
    
    public BaseAdapter(Context context, List<Map<String, Object>> datas, @LayoutRes int layoutResId, SimpleMapAdapterSettings simpleMapAdapterSettings) {
        this.mapDatas = datas;
        this.context = context;
        this.layoutResIdMap = null;
        this.layoutResId = layoutResId;
        this.simpleMapAdapterSettings = simpleMapAdapterSettings;
        isMapDatas = true;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        
        typeCount = 1;
    }
    
    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }
    
    @Override
    public int getItemViewType(int position) {
        if (datas == null) {
            Object type = mapDatas.get(position).get("type");
            if (type == null) type = 0;
            return (int) type;
        } else {
            return datas.get(position).getType();
        }
    }
    
    @Override
    public int getCount() {
        if (datas == null) {
            return mapDatas.size();
        } else {
            return datas.size();
        }
    }
    
    @Override
    public Object getItem(int i) {
        if (datas == null) {
            return mapDatas.get(i);
        } else {
            return datas.get(i);
        }
    }
    
    @Override
    public long getItemId(int i) {
        return i;
    }
    
    @Override
    public int getViewTypeCount() {
        return typeCount;
    }
    
    @Override
    public View getView(final int i, View convertView, ViewGroup viewGroup) {
        if (isMapDatas) {
            Map<String, Object> obj = mapDatas.get(i);
            
            if (layoutResIdMap == null) {
                if (layoutResId == -1) {
                    new Exception("请设置layoutResId或layoutResIdMap，至少设置其一");
                    return null;
                } else {
                    Object viewHolder;
                    if (convertView == null) {
                        convertView = mInflater.inflate(layoutResId, null);
                        viewHolder = simpleMapAdapterSettings.setViewHolder(convertView);
                        convertView.setTag(viewHolder);
                    } else {
                        viewHolder = convertView.getTag();
                    }
                    simpleMapAdapterSettings.setData(viewHolder, obj);
                }
            } else {
                Object viewHolder = null;
                int layoutId = layoutResIdMap.get((Integer) obj.get("type"));
                if (convertView == null) {
                    convertView = mInflater.inflate(layoutId, null);
                    viewHolder = multipleMapAdapterSettings.setViewHolder((Integer) obj.get("type"), convertView);
                    convertView.setTag(viewHolder);
                } else {
                    viewHolder = convertView.getTag();
                }
                multipleMapAdapterSettings.setData((Integer) obj.get("type"), viewHolder, obj);
            }
            return convertView;
        } else {
            BaseDataBean obj = datas.get(i);
            
            if (layoutResIdMap == null) {
                if (layoutResId == -1) {
                    new Exception("请设置layoutResId或layoutResIdMap，至少设置其一");
                    return null;
                } else {
                    Object viewHolder;
                    if (convertView == null) {
                        convertView = mInflater.inflate(layoutResId, null);
                        viewHolder = simpleAdapterSettings.setViewHolder(convertView);
                        convertView.setTag(viewHolder);
                    } else {
                        viewHolder = convertView.getTag();
                    }
                    simpleAdapterSettings.setData(viewHolder, obj);
                }
            } else {
                Object viewHolder = null;
                int layoutId = layoutResIdMap.get(obj.getType());
                if (convertView == null) {
                    convertView = mInflater.inflate(layoutId, null);
                    viewHolder = multipleAdapterSettings.setViewHolder(obj.getType(), convertView);
                    convertView.setTag(viewHolder);
                } else {
                    viewHolder = convertView.getTag();
                }
                multipleAdapterSettings.setData(obj.getType(), viewHolder, obj);
            }
            return convertView;
        }
    }
    
    public static class BaseDataBean {
        public int type = 0;
        
        public int getType() {
            return type;
        }
        
        public BaseDataBean setType(int type) {
            this.type = type;
            return this;
        }
    }
    
}
