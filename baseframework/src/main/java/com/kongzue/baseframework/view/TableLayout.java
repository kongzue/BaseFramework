package com.kongzue.baseframework.view;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.kongzue.baseframework.R;

import java.util.ArrayList;
import java.util.List;

/**
 * TableLayout 2.0
 * Author: @Kongzue
 * Github: https://github.com/kongzue/
 * Homepage: http://kongzue.com/
 * Mail: myzcxhh@live.cn
 * CreateTime: 2019/4/24 15:51
 */
public class TableLayout extends ViewGroup {
    
    private int maxColumn = 2;
    private int itemMargin = 0;
    private int setChildHeight = dp2px(150);
    
    private Context context;
    
    public TableLayout(Context context) {
        super(context);
        this.context = context;
        loadAttrs(context, null);
    }
    
    public TableLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        
        loadAttrs(context, attrs);
    }
    
    public TableLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        
        loadAttrs(context, attrs);
    }
    
    private void loadAttrs(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.TableLayout);
        
        itemMargin = typedArray.getDimensionPixelOffset(R.styleable.TableLayout_marginDp, 0);
        setChildHeight = typedArray.getDimensionPixelOffset(R.styleable.TableLayout_itemHeight, dp2px(150));
        maxColumn = typedArray.getInteger(R.styleable.TableLayout_column, 2);
        
        typedArray.recycle();
    }
    
    private List<View> items;
    private int newHeight = 0;
    
    private int[] childTop, childLeft, childRight, childBottom;
    
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int maxWidth = resolveSize(Integer.MAX_VALUE, widthMeasureSpec) + itemMargin;
        int itemWidth = maxWidth / maxColumn - itemMargin;
        int itemHeight = resolveSize(setChildHeight, heightMeasureSpec);
        
        childTop = new int[getChildCount()];
        childLeft = new int[getChildCount()];
        childRight = new int[getChildCount()];
        childBottom = new int[getChildCount()];
        
        int l = 0, t = 0, r = 0, b = 0;
        
        items = new ArrayList<>();
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            if (child.getVisibility() == VISIBLE) {
                items.add(getChildAt(i));
            }
        }
        
        //调用每个子view的onMeasure测量子view
        for (int i = 0; i < items.size(); i++) {
            View child = items.get(i);
            //LayoutParams lp = child.getLayoutParams();
            
            int childWidthSpec = MeasureSpec.makeMeasureSpec(itemWidth, MeasureSpec.EXACTLY);
            int childHeightSpec = MeasureSpec.makeMeasureSpec(itemHeight, MeasureSpec.EXACTLY);
            
            if (i != 0 && i % maxColumn == 0) {
                l = 0;
                t = t + itemHeight + itemMargin;
            }
            r = l + itemWidth;
            b = t + itemHeight;
            
            childLeft[i] = l;
            childTop[i] = t;
            childRight[i] = r;
            childBottom[i] = b;
            
            l = l + itemWidth + itemMargin;
            newHeight = t + itemHeight;
            
            child.measure(childWidthSpec, childHeightSpec);
        }
        
        int mWidth = resolveSize(itemWidth, widthMeasureSpec);
        int mHeight = resolveSize(itemHeight, heightMeasureSpec);
        
        //setMeasuredDimension(mWidth, mHeight);
        
        //refreshViews(widthMeasureSpec, heightMeasureSpec);
        
        setMeasuredDimension(mWidth, newHeight);//设置宽高
    }
    
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        for (int i = 0; i < items.size(); i++) {
            View child = items.get(i);
            child.layout(childLeft[i], childTop[i], childRight[i], childBottom[i]);
        }
    }
    
    @Override
    public void addView(View child) {
        super.addView(child);
    }
    
    @Override
    public void addView(View child, int index) {
        super.addView(child, index);
    }
    
    @Override
    public void addView(View child, int width, int height) {
        super.addView(child, width, height);
    }
    
    @Override
    public void addView(View child, LayoutParams params) {
        super.addView(child, params);
    }
    
    @Override
    public void addView(View child, int index, LayoutParams params) {
        super.addView(child, index, params);
    }
    
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }
    
    private int dp2px(float dpValue) {
        return (int) (0.5f + dpValue * Resources.getSystem().getDisplayMetrics().density);
    }
    
    public int getMaxColumn() {
        return maxColumn;
    }
    
    public TableLayout setMaxColumn(int maxColumn) {
        this.maxColumn = maxColumn;
        return this;
    }
    
    public int getItemMargin() {
        return itemMargin;
    }
    
    public TableLayout setItemMargin(int itemMargin) {
        this.itemMargin = itemMargin;
        return this;
    }
    
    public int getItemHeight() {
        return setChildHeight;
    }
    
    public TableLayout setItemHeight(int itemHeight) {
        this.setChildHeight = itemHeight;
        return this;
    }
}
