package com.kongzue.baseframework.util;

import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.constraintlayout.widget.ConstraintLayout;

public class ViewController {

    View view;
    ViewGroup.LayoutParams lp;

    public static ViewController of(View v) {
        return new ViewController(v);
    }

    public ViewController(View view) {
        this.view = view;
        if (view.getLayoutParams() != null) {
            lp = view.getLayoutParams();
        } else {
            if (view.getParent() instanceof RelativeLayout) {
                lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                view.setLayoutParams(lp);
            } else if (view.getParent() instanceof LinearLayout) {
                lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                view.setLayoutParams(lp);
            } else if (view.getParent() instanceof ConstraintLayout) {
                lp = new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.WRAP_CONTENT, ConstraintLayout.LayoutParams.WRAP_CONTENT);
                view.setLayoutParams(lp);
            } else if (view.getParent() instanceof AbsListView) {
                lp = new AbsListView.LayoutParams(AbsListView.LayoutParams.WRAP_CONTENT, AbsListView.LayoutParams.WRAP_CONTENT);
                view.setLayoutParams(lp);
            } else {
                lp = new ViewGroup.MarginLayoutParams(ViewGroup.MarginLayoutParams.WRAP_CONTENT, ViewGroup.MarginLayoutParams.WRAP_CONTENT);
                view.setLayoutParams(lp);
            }
        }
    }

    public <T extends ViewGroup.LayoutParams> T getLayoutParams() {
        return (T) lp;
    }

    public ViewController setWidth(int width) {
        if (lp != null) {
            lp.width = width;
            view.requestLayout();
        }
        return this;
    }

    public ViewController setHeight(int height) {
        if (lp != null) {
            lp.height = height;
            view.requestLayout();
        }
        return this;
    }

    public int getWidth() {
        return view.getMeasuredWidth();
    }

    public int getHeight() {
        return view.getMeasuredHeight();
    }

    public View getView() {
        return view;
    }

    public ViewController setPadding(int padding) {
        view.setPadding(padding, padding, padding, padding);
        return this;
    }

    public ViewController setPadding(int left, int top, int right, int bottom) {
        view.setPadding(left, top, right, bottom);
        return this;
    }

    public int getPaddingTop() {
        return view.getPaddingTop();
    }

    public int getPaddingBottom() {
        return view.getPaddingBottom();
    }

    public int getPaddingLeft() {
        return view.getPaddingLeft();
    }

    public int getPaddingRight() {
        return view.getPaddingRight();
    }

    public ViewController setPaddingTop(int paddingTop) {
        view.setPadding(getPaddingLeft(), paddingTop, getPaddingRight(), getPaddingBottom());
        return this;
    }

    public ViewController setPaddingBottom(int paddingBottom) {
        view.setPadding(getPaddingLeft(), getPaddingTop(), getPaddingRight(), paddingBottom);
        return this;
    }

    public ViewController setPaddingLeft(int paddingLeft) {
        view.setPadding(paddingLeft, getPaddingTop(), getPaddingRight(), getPaddingBottom());
        return this;
    }

    public ViewController setPaddingRight(int paddingRight) {
        view.setPadding(getPaddingLeft(), getPaddingTop(), paddingRight, getPaddingBottom());
        return this;
    }

    public ViewController setPaddingHorizontal(int paddingHorizontal) {
        view.setPadding(paddingHorizontal, getPaddingTop(), paddingHorizontal, getPaddingBottom());
        return this;
    }

    public ViewController setPaddingVertical(int paddingVertical) {
        view.setPadding(getPaddingLeft(), paddingVertical, getPaddingRight(), paddingVertical);
        return this;
    }

    public int getMarginLeft() {
        if (lp instanceof ViewGroup.MarginLayoutParams) {
            return ((ViewGroup.MarginLayoutParams) lp).leftMargin;
        }
        return 0;
    }

    public int getMarginTop() {
        if (lp instanceof ViewGroup.MarginLayoutParams) {
            return ((ViewGroup.MarginLayoutParams) lp).topMargin;
        }
        return 0;
    }

    public int getMarginRight() {
        if (lp instanceof ViewGroup.MarginLayoutParams) {
            return ((ViewGroup.MarginLayoutParams) lp).rightMargin;
        }
        return 0;
    }

    public int getMarginBottom() {
        if (lp instanceof ViewGroup.MarginLayoutParams) {
            return ((ViewGroup.MarginLayoutParams) lp).bottomMargin;
        }
        return 0;
    }

    public ViewController setMarginLeft(int marginLeft) {
        if (lp instanceof ViewGroup.MarginLayoutParams) {
            ((ViewGroup.MarginLayoutParams) lp).leftMargin = marginLeft;
            view.requestLayout();
        }
        return this;
    }

    public ViewController setMarginTop(int marginTop) {
        if (lp instanceof ViewGroup.MarginLayoutParams) {
            ((ViewGroup.MarginLayoutParams) lp).topMargin = marginTop;
            view.requestLayout();
        }
        return this;
    }

    public ViewController setMarginRight(int marginRight) {
        if (lp instanceof ViewGroup.MarginLayoutParams) {
            ((ViewGroup.MarginLayoutParams) lp).rightMargin = marginRight;
            view.requestLayout();
        }
        return this;
    }

    public ViewController setMarginBottom(int marginBottom) {
        if (lp instanceof ViewGroup.MarginLayoutParams) {
            ((ViewGroup.MarginLayoutParams) lp).bottomMargin = marginBottom;
            view.requestLayout();
        }
        return this;
    }

    public ViewController setMargin(int margin) {
        if (lp instanceof ViewGroup.MarginLayoutParams) {
            ((ViewGroup.MarginLayoutParams) lp).leftMargin = margin;
            ((ViewGroup.MarginLayoutParams) lp).topMargin = margin;
            ((ViewGroup.MarginLayoutParams) lp).rightMargin = margin;
            ((ViewGroup.MarginLayoutParams) lp).bottomMargin = margin;
            view.requestLayout();
        }
        return this;
    }

    public ViewController setMarginHorizontal(int marginHorizontal) {
        if (lp instanceof ViewGroup.MarginLayoutParams) {
            ((ViewGroup.MarginLayoutParams) lp).leftMargin = marginHorizontal;
            ((ViewGroup.MarginLayoutParams) lp).rightMargin = marginHorizontal;
            view.requestLayout();
        }
        return this;
    }

    public ViewController setMarginVertical(int marginVertical) {
        if (lp instanceof ViewGroup.MarginLayoutParams) {
            ((ViewGroup.MarginLayoutParams) lp).topMargin = marginVertical;
            ((ViewGroup.MarginLayoutParams) lp).bottomMargin = marginVertical;
            view.requestLayout();
        }
        return this;
    }

    public ViewController setMargin(int left, int top, int right, int bottom) {
        if (lp instanceof ViewGroup.MarginLayoutParams) {
            ((ViewGroup.MarginLayoutParams) lp).leftMargin = left;
            ((ViewGroup.MarginLayoutParams) lp).topMargin = top;
            ((ViewGroup.MarginLayoutParams) lp).rightMargin = right;
            ((ViewGroup.MarginLayoutParams) lp).bottomMargin = bottom;
            view.requestLayout();
        }
        return this;
    }

    public ViewController setWeight(float weight) {
        if (lp instanceof LinearLayout.LayoutParams) {
            ((LinearLayout.LayoutParams) lp).weight = weight;
            view.requestLayout();
        }
        return this;
    }

    public float getWeight() {
        if (lp instanceof LinearLayout.LayoutParams) {
            return ((LinearLayout.LayoutParams) lp).weight;
        }
        return 0;
    }

    public ViewController setGravity(int gravity) {
        if (lp instanceof LinearLayout.LayoutParams) {
            ((LinearLayout.LayoutParams) lp).gravity = gravity;
            view.requestLayout();
        }
        return this;
    }

    public int getGravity() {
        if (lp instanceof LinearLayout.LayoutParams) {
            return ((LinearLayout.LayoutParams) lp).gravity;
        }
        return 0;
    }

    public ViewController setWidthAndHeight(int width, int height) {
        setWidth(width);
        setHeight(height);
        return this;
    }

    public ViewController addRule(int relativeLayoutRule) {
        if (lp instanceof RelativeLayout.LayoutParams) {
            ((RelativeLayout.LayoutParams) lp).addRule(relativeLayoutRule);
            view.requestLayout();
        }
        return this;
    }

    public ViewController addRule(int relativeLayoutRule, int subject) {
        if (lp instanceof RelativeLayout.LayoutParams) {
            ((RelativeLayout.LayoutParams) lp).addRule(relativeLayoutRule, subject);
            view.requestLayout();
        }
        return this;
    }

    public int getRule(int relativeLayoutRule) {
        if (lp instanceof RelativeLayout.LayoutParams) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                return ((RelativeLayout.LayoutParams) lp).getRule(relativeLayoutRule);
            }
        }
        return 0;
    }

    public int[] getRules(){
        if (lp instanceof RelativeLayout.LayoutParams) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                return ((RelativeLayout.LayoutParams) lp).getRules();
            }
        }
        return null;
    }
}
