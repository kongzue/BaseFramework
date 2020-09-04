package com.kongzue.baseframeworkdemo.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.kongzue.baseframework.BaseActivity;
import com.kongzue.baseframework.BaseAdapter;
import com.kongzue.baseframework.interfaces.FullScreen;
import com.kongzue.baseframework.interfaces.Layout;
import com.kongzue.baseframework.interfaces.MultipleAdapterSettings;
import com.kongzue.baseframework.interfaces.MultipleMapAdapterSettings;
import com.kongzue.baseframework.interfaces.SimpleAdapterSettings;
import com.kongzue.baseframework.interfaces.SimpleMapAdapterSettings;
import com.kongzue.baseframework.interfaces.SwipeBack;
import com.kongzue.baseframework.util.JumpParameter;
import com.kongzue.baseframeworkdemo.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//使用 @Layout 注解直接绑定要显示的布局
@Layout(R.layout.activity_adapter_test)
//支持侧滑返回
@SwipeBack(true)
//全屏显示此界面
@FullScreen(true)
public class AdapterTestActivity extends BaseActivity {
    
    private TextView txtTitle;
    private Button btnSimpleBean;
    private Button btnSimpleMap;
    private Button btnMultipleBean;
    private Button btnMultipleMap;
    private TextView txtCode;
    private ListView list;
    
    @Override
    //此处用于绑定布局组件，你也可以使用 @BindView(resId) 来初始化组件
    public void initViews() {
        txtTitle = findViewById(R.id.txt_title);
        btnSimpleBean = findViewById(R.id.btn_simple_bean);
        btnSimpleMap = findViewById(R.id.btn_simple_map);
        btnMultipleBean = findViewById(R.id.btn_multiple_bean);
        btnMultipleMap = findViewById(R.id.btn_multiple_map);
        txtCode = findViewById(R.id.txt_code);
        list = findViewById(R.id.list);
    }
    
    private BaseAdapter baseAdapter;
    
    @Override
    //请在此编写初始化操作，例如读取数据等，以及对 UI 组件进行赋值
    public void initDatas(JumpParameter parameter) {
//        initMultipleBeanTest();
//        initMultipleMapTest();
//        initSimpleMapTest();
//        initSimpleBeanTest();
        
        initExperimentalTest();
    }
    
    @Override
    //此处为组件绑定功能事件、回调等方法
    public void setEvents() {
        btnSimpleBean.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtCode.setText(getString(R.string.adapter_simple_bean));
                initSimpleBeanTest();
            }
        });
        
        btnSimpleMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtCode.setText(getString(R.string.adapter_simple_map));
                initSimpleMapTest();
            }
        });
        
        btnMultipleBean.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtCode.setText(getString(R.string.adapter_multiple_bean));
                initMultipleBeanTest();
            }
        });
        
        btnMultipleMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtCode.setText(getString(R.string.adapter_multiple_map));
                initMultipleMapTest();
            }
        });
    }
    
    //添加演示数据
    private void initExperimentalTest() {
        List<com.kongzue.baseframework.experimental.BaseAdapter.SimpleData> dataList = new ArrayList<>();
        CustomData customData = new CustomData(R.layout.item_list_layout1,"测试");
        dataList.add(customData);
        customData = new CustomData(R.layout.item_list_layout2,"测试2");
        dataList.add(customData);
        customData = new CustomData(R.layout.item_list_layout3,"测试3");
        dataList.add(customData);
        customData = new CustomData(R.layout.item_list_layout1,"测试1");
        dataList.add(customData);
        customData = new CustomData(R.layout.item_list_layout1,"测试1");
        dataList.add(customData);
        customData = new CustomData(R.layout.item_list_layout2,"测试2");
        dataList.add(customData);
        customData = new CustomData(R.layout.item_list_layout2,"测试2");
        dataList.add(customData);
        customData = new CustomData(R.layout.item_list_layout3,"测试3");
        dataList.add(customData);
        customData = new CustomData(R.layout.item_list_layout3,"测试3");
        dataList.add(customData);
        customData = new CustomData(R.layout.item_list_layout1,"测试1");
        dataList.add(customData);
        customData = new CustomData(R.layout.item_list_layout1,"测试1");
        dataList.add(customData);
        customData = new CustomData(R.layout.item_list_layout1,"测试1");
        dataList.add(customData);
        customData = new CustomData(R.layout.item_list_layout2,"测试2");
        dataList.add(customData);
        customData = new CustomData(R.layout.item_list_layout2,"测试2");
        dataList.add(customData);
        customData = new CustomData(R.layout.item_list_layout2,"测试2");
        dataList.add(customData);
        customData = new CustomData(R.layout.item_list_layout3,"测试3");
        dataList.add(customData);
        customData = new CustomData(R.layout.item_list_layout3,"测试3");
        dataList.add(customData);
        customData = new CustomData(R.layout.item_list_layout3,"测试3");
        dataList.add(customData);
        
        com.kongzue.baseframework.experimental.BaseAdapter<CustomData> baseAdapter = new com.kongzue.baseframework.experimental.BaseAdapter(me,dataList);
        baseAdapter.setBindView(new com.kongzue.baseframework.experimental.BaseAdapter.BindView<CustomData>() {
            @Override
            public void onBind(View view, CustomData data) {
                switch (data.layoutResId()){
                    case R.layout.item_list_layout1:
                        TextView txtTitle = view.findViewById(R.id.txt_title);
                        txtTitle.setText(data.getContent());
                        break;
                    case R.layout.item_list_layout2:
                        TextView txtTitle2 = view.findViewById(R.id.txt_title);
                        txtTitle2.setText(data.getContent());
                        break;
                    case R.layout.item_list_layout3:
                        TextView txtTitle3 = view.findViewById(R.id.txt_title);
                        txtTitle3.setText(data.getContent());
                        break;
                }
            }
        });
        
        list.setAdapter(baseAdapter);
    }
    
    class CustomData implements com.kongzue.baseframework.experimental.BaseAdapter.SimpleData{
    
        private int layoutId;
        private String content;
    
        public CustomData(int layoutId, String content) {
            this.layoutId = layoutId;
            this.content = content;
        }
    
        public String getContent() {
            return content;
        }
    
        public CustomData setContent(String content) {
            this.content = content;
            return this;
        }
    
        public int getLayoutId() {
            return layoutId;
        }
    
        public CustomData setLayoutId(int layoutId) {
            this.layoutId = layoutId;
            return this;
        }
    
        @Override
        public int layoutResId() {
            return layoutId;
        }
    }
    
    private void initSimpleMapTest() {
        List<Map<String, Object>> datas = new ArrayList<>();
        Map<String, Object> map = new HashMap<>();
        map.put("title", "我是布局1");
        datas.add(map);
        map = new HashMap<>();
        map.put("title", "我是布局2");
        datas.add(map);
        map = new HashMap<>();
        map.put("title", "我是布局3");
        datas.add(map);
        
        baseAdapter = new BaseAdapter(me, datas, R.layout.item_list_layout1, new SimpleMapAdapterSettings() {
            @Override
            public Object setViewHolder(View convertView) {
                ViewHolder1 viewHolder1 = new ViewHolder1();
                viewHolder1.txtTitle = convertView.findViewById(R.id.txt_title);
                return viewHolder1;
            }
    
            @Override
            public void setData(Object viewHolder, Map data, List dataList, int index) {
                ViewHolder1 viewHolder1 = (ViewHolder1) viewHolder;
                viewHolder1.txtTitle.setText(data.get("title") + "");
            }
            
        });
        
        list.setAdapter(baseAdapter);
    }
    
    private void initSimpleBeanTest() {
        List<CustomDatas> datas = new ArrayList();
        datas.add(new CustomDatas().setTitle("我是布局1"));
        datas.add(new CustomDatas().setTitle("我是布局2"));
        datas.add(new CustomDatas().setTitle("我是布局3"));
        
        baseAdapter = new BaseAdapter(me, datas, R.layout.item_list_layout1, new SimpleAdapterSettings() {
            @Override
            public Object setViewHolder(View convertView) {
                ViewHolder1 viewHolder1 = new ViewHolder1();
                viewHolder1.txtTitle = convertView.findViewById(R.id.txt_title);
                return viewHolder1;
            }
    
            @Override
            public void setData(Object viewHolder, Object d, List dataList, int index) {
                CustomDatas data = (CustomDatas) d;
                ViewHolder1 viewHolder1 = (ViewHolder1) viewHolder;
                viewHolder1.txtTitle.setText(data.getTitle());
            }
        });
        
        list.setAdapter(baseAdapter);
    }
    
    private void initMultipleMapTest() {
        List<Map<String, Object>> datas = new ArrayList<>();
        Map<String, Object> map = new HashMap<>();
        map.put("type", 0);
        map.put("title", "我是布局1");
        datas.add(map);
        map = new HashMap<>();
        map.put("type", 1);
        map.put("title", "我是布局2");
        datas.add(map);
        map = new HashMap<>();
        map.put("type", 2);
        map.put("title", "我是布局3");
        datas.add(map);
        
        Map<Integer, Integer> layoutResIdMap = new HashMap<>();
        layoutResIdMap.put(0, R.layout.item_list_layout1);
        layoutResIdMap.put(1, R.layout.item_list_layout2);
        layoutResIdMap.put(2, R.layout.item_list_layout3);
        
        baseAdapter = new BaseAdapter(me, datas, layoutResIdMap, new MultipleMapAdapterSettings() {
            @Override
            public Object setViewHolder(int type, View convertView) {
                switch (type) {
                    case 0:
                        ViewHolder1 viewHolder1 = new ViewHolder1();
                        viewHolder1.txtTitle = convertView.findViewById(R.id.txt_title);
                        return viewHolder1;
                    case 1:
                        ViewHolder2 viewHolder2 = new ViewHolder2();
                        viewHolder2.txtTitle = convertView.findViewById(R.id.txt_title);
                        return viewHolder2;
                    case 2:
                        ViewHolder3 viewHolder3 = new ViewHolder3();
                        viewHolder3.txtTitle = convertView.findViewById(R.id.txt_title);
                        return viewHolder3;
                    default:
                        return null;
                }
            }
    
            @Override
            public void setData(int type, Object vh, Map<String, Object> data, List<Map<String, Object>> dataList, int index) {
                switch (type) {
                    case 0:
                        ViewHolder1 viewHolder1 = (ViewHolder1) vh;
                        viewHolder1.txtTitle.setText((String) data.get("title"));
                        break;
                    case 1:
                        ViewHolder2 viewHolder2 = (ViewHolder2) vh;
                        viewHolder2.txtTitle.setText((String) data.get("title"));
                        break;
                    case 2:
                        ViewHolder3 viewHolder3 = (ViewHolder3) vh;
                        viewHolder3.txtTitle.setText((String) data.get("title"));
                        break;
                }
            }
        });
        
        list.setAdapter(baseAdapter);
    }
    
    private void initMultipleBeanTest() {
        List<CustomDatas> datas = new ArrayList();
        datas.add(new CustomDatas().setTitle("我是布局1").setType(0));
        datas.add(new CustomDatas().setTitle("我是布局2").setType(1));
        datas.add(new CustomDatas().setTitle("我是布局3").setType(2));
        
        Map<Integer, Integer> layoutResIdMap = new HashMap<>();
        layoutResIdMap.put(0, R.layout.item_list_layout1);
        layoutResIdMap.put(1, R.layout.item_list_layout2);
        layoutResIdMap.put(2, R.layout.item_list_layout3);
        
        baseAdapter = new BaseAdapter(me, datas, layoutResIdMap, new MultipleAdapterSettings() {
            @Override
            public Object setViewHolder(int type, View convertView) {
                switch (type) {
                    case 0:
                        ViewHolder1 viewHolder1 = new ViewHolder1();
                        viewHolder1.txtTitle = convertView.findViewById(R.id.txt_title);
                        return viewHolder1;
                    case 1:
                        ViewHolder2 viewHolder2 = new ViewHolder2();
                        viewHolder2.txtTitle = convertView.findViewById(R.id.txt_title);
                        return viewHolder2;
                    case 2:
                        ViewHolder3 viewHolder3 = new ViewHolder3();
                        viewHolder3.txtTitle = convertView.findViewById(R.id.txt_title);
                        return viewHolder3;
                    default:
                        return null;
                }
            }
    
            @Override
            public void setData(int type, Object vh, Object d, List dataList, int index) {
                CustomDatas data = (CustomDatas) d;
                switch (type) {
                    case 0:
                        ViewHolder1 viewHolder1 = (ViewHolder1) vh;
                        viewHolder1.txtTitle.setText(data.getTitle());
                        break;
                    case 1:
                        ViewHolder2 viewHolder2 = (ViewHolder2) vh;
                        viewHolder2.txtTitle.setText(data.getTitle());
                        break;
                    case 2:
                        ViewHolder3 viewHolder3 = (ViewHolder3) vh;
                        viewHolder3.txtTitle.setText(data.getTitle());
                        break;
                }
            }
        });
        
        list.setAdapter(baseAdapter);
    }
    
    private class ViewHolder1 {
        TextView txtTitle;
    }
    
    private class ViewHolder2 {
        TextView txtTitle;
    }
    
    private class ViewHolder3 {
        TextView txtTitle;
    }
    
    private class CustomDatas extends BaseAdapter.BaseDataBean {
        String title;
        
        @Override
        public CustomDatas setType(int type) {
            this.type = type;
            return this;
        }
        
        public String getTitle() {
            return title;
        }
        
        public CustomDatas setTitle(String title) {
            this.title = title;
            return this;
        }
    }
}
