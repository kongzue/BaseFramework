package com.kongzue.baseframework;

import android.content.Context;
import androidx.annotation.LayoutRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kongzue.baseframework.interfaces.MultipleAdapterSettings;
import com.kongzue.baseframework.interfaces.MultipleMapAdapterSettings;
import com.kongzue.baseframework.interfaces.SimpleAdapterSettings;
import com.kongzue.baseframework.interfaces.SimpleMapAdapterSettings;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BaseAdapter<V,D> extends android.widget.BaseAdapter {

    public static final int TYPE_SIMPLE_DATA = 1;
    public static final int TYPE_MULTIPLE_DATA = 2;
    public static final int TYPE_MAP_DATA = 3;

    private LayoutInflater mInflater;
    private List<D> simpleDatas;
    private List<? extends BaseDataBean> multipleDatas;
    private List<Map<String, Object>> mapDatas;
    private Context context;
    private SimpleAdapterSettings<V,D> simpleAdapterSettings;
    private SimpleMapAdapterSettings simpleMapAdapterSettings;
    private MultipleAdapterSettings multipleAdapterSettings;
    private MultipleMapAdapterSettings multipleMapAdapterSettings;
    private int dataType = 0;

    private int layoutResId = -1;
    private Map<Integer, Integer> layoutResIdMap;

    private int typeCount = 1;

    public BaseAdapter(Context context, List<? extends BaseDataBean> datas, Map<Integer, Integer> layoutResId, MultipleAdapterSettings multipleAdapterSettings) {
        this.multipleDatas = datas;
        this.mapDatas = null;
        this.simpleDatas = null;
        this.context = context;
        this.layoutResIdMap = layoutResId;
        this.layoutResId = -1;
        this.multipleAdapterSettings = multipleAdapterSettings;
        dataType = TYPE_MULTIPLE_DATA;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        typeCount = layoutResIdMap.size();
    }

    public BaseAdapter(Context context, List<Map<String, Object>> datas, Map<Integer, Integer> layoutResId, MultipleMapAdapterSettings multipleMapAdapterSettings) {
        this.mapDatas = datas;
        this.simpleDatas = null;
        this.multipleDatas = null;
        this.context = context;
        this.layoutResIdMap = layoutResId;
        this.layoutResId = -1;
        this.multipleMapAdapterSettings = multipleMapAdapterSettings;
        dataType = TYPE_MAP_DATA;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        typeCount = layoutResIdMap.size();
    }

    public BaseAdapter(Context context, List<D> datas, @LayoutRes int layoutResId, SimpleAdapterSettings<V,D> simpleAdapterSettings) {
        this.simpleDatas = datas;
        this.mapDatas = null;
        this.multipleDatas = null;
        this.context = context;
        this.layoutResIdMap = null;
        this.layoutResId = layoutResId;
        this.simpleAdapterSettings = simpleAdapterSettings;
        dataType = TYPE_SIMPLE_DATA;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        typeCount = 1;
    }

    public BaseAdapter(Context context, List<Map<String, Object>> datas, @LayoutRes int layoutResId, SimpleMapAdapterSettings simpleMapAdapterSettings) {
        this.mapDatas = datas;
        this.simpleDatas = null;
        this.multipleDatas = null;
        this.context = context;
        this.layoutResIdMap = null;
        this.layoutResId = layoutResId;
        this.simpleMapAdapterSettings = simpleMapAdapterSettings;
        dataType = TYPE_MAP_DATA;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        typeCount = 1;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        switch (dataType) {
            case TYPE_MULTIPLE_DATA:
                return multipleDatas.get(position).getType();
            case TYPE_SIMPLE_DATA:
                return 1;
            case TYPE_MAP_DATA:
                Object type = mapDatas.get(position).get("type");
                if (type == null) type = 0;
                return (int) type;
            default:
                return 1;
        }
    }

    @Override
    public int getCount() {
        switch (dataType) {
            case TYPE_MULTIPLE_DATA:
                return multipleDatas.size();
            case TYPE_SIMPLE_DATA:
                return simpleDatas.size();
            case TYPE_MAP_DATA:
                return mapDatas.size();
            default:
                return 0;
        }
    }

    @Override
    public Object getItem(int i) {
        switch (dataType) {
            case TYPE_MULTIPLE_DATA:
                return multipleDatas.get(i);
            case TYPE_SIMPLE_DATA:
                return simpleDatas.get(i);
            case TYPE_MAP_DATA:
                return mapDatas.get(i);
            default:
                return null;
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
        switch (dataType) {
            case TYPE_MULTIPLE_DATA:
                BaseDataBean multipleData = multipleDatas.get(i);

                Object multipleViewHolder = null;
                int layoutId = layoutResIdMap.get(multipleData.getType());
                if (convertView == null) {
                    convertView = mInflater.inflate(layoutId, null);
                    multipleViewHolder = multipleAdapterSettings.setViewHolder(multipleData.getType(), convertView);
                    convertView.setTag(multipleViewHolder);
                } else {
                    multipleViewHolder = convertView.getTag();
                }
                multipleAdapterSettings.setData(multipleData.getType(), multipleViewHolder, multipleData, i);
                return convertView;
            case TYPE_SIMPLE_DATA:
                D simpleData = simpleDatas.get(i);

                if (layoutResId == -1) {
                    new Exception("请设置layoutResId");
                    return null;
                } else {
                    V simpleViewHolder;
                    if (convertView == null) {
                        convertView = mInflater.inflate(layoutResId, null);
                        simpleViewHolder = simpleAdapterSettings.setViewHolder(convertView);
                        convertView.setTag(simpleViewHolder);
                    } else {
                        simpleViewHolder = (V) convertView.getTag();
                    }
                    simpleAdapterSettings.setData(simpleViewHolder, simpleData, i);
                }
                return convertView;
            case TYPE_MAP_DATA:
                Map<String, Object> mapData = mapDatas.get(i);

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
                        simpleMapAdapterSettings.setData(viewHolder, mapData, i);
                    }
                } else {
                    Object viewHolder = null;
                    int mapLayoutId = layoutResIdMap.get((Integer) mapData.get("type"));
                    if (convertView == null) {
                        convertView = mInflater.inflate(mapLayoutId, null);
                        viewHolder = multipleMapAdapterSettings.setViewHolder((Integer) mapData.get("type"), convertView);
                        convertView.setTag(viewHolder);
                    } else {
                        viewHolder = convertView.getTag();
                    }
                    multipleMapAdapterSettings.setData((Integer) mapData.get("type"), viewHolder, mapData, i);
                }
                return convertView;
            default:
                return null;
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

    public void refreshSimpleDataChanged(List<D> newDatas) {
        if (simpleDatas != null) {
            simpleDatas = new ArrayList<>();
            simpleDatas.addAll(newDatas);
            notifyDataSetChanged();
        }
    }

    public void refreshMapDataChanged(List<Map<String, Object>> newDatas) {
        if (mapDatas != null) {
            mapDatas = new ArrayList<>();
            mapDatas.addAll(newDatas);
            notifyDataSetChanged();
        }
    }

    public void refreshMultipleDataChanged(ArrayList<? extends BaseDataBean> newDatas) {
        if (multipleDatas != null) {
            multipleDatas = new ArrayList<>();
            multipleDatas = newDatas;
            notifyDataSetChanged();
        }
    }

}
