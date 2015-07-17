package com.bigfat.draggedviewpager.view;

import android.animation.LayoutTransition;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import java.util.ArrayList;

/**
 * Created by yueban on 16/7/15.
 */
public class MDA_PageListLayout<T> extends LinearLayout {
    private int itemLayoutResId;
    private ArrayList<T> data;
    private int dragPosition = -1;
    private int pageIndex;//页面索引

    public MDA_PageListLayout(Context context, int itemLayoutResId) {
        this(context, null, itemLayoutResId);
    }

    public MDA_PageListLayout(Context context, AttributeSet attrs, int itemLayoutResId) {
        this(context, attrs, 0, itemLayoutResId);
    }

    public MDA_PageListLayout(Context context, AttributeSet attrs, int defStyleAttr, int itemLayoutResId) {
        super(context, attrs, defStyleAttr);
        init();
        this.itemLayoutResId = itemLayoutResId;
    }

    private void init() {
        //设置布局方向
        setOrientation(VERTICAL);
        //设置动画
        LayoutTransition layoutTransition = new LayoutTransition();
        layoutTransition.setAnimator(LayoutTransition.APPEARING, null);
        layoutTransition.setAnimator(LayoutTransition.DISAPPEARING, null);
        setLayoutTransition(layoutTransition);
    }

    public void initView() {
        removeAllViews();
        if (data != null) {
            for (int i = 0; i < data.size(); i++) {
                View itemView = LayoutInflater.from(getContext()).inflate(itemLayoutResId, this, false);
                bindItemData(itemView, data.get(i));

                itemView.setTag(pageIndex);
                if (dragPosition == i) {
                    itemView.setVisibility(INVISIBLE);
                }
                addView(itemView);
            }
        }
    }

    public void bindItemData(View itemView, T t) {
        getParentHorizontalScrollView().getController().bindItemData(itemView, t);
    }

    public ArrayList<T> getData() {
        return data;
    }

    public void setData(ArrayList<T> data) {
        this.data = data;
        initView();
    }

    public int getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
        for (int i = 0; i < getChildCount(); i++) {
            getChildAt(i).setTag(pageIndex);
        }
    }

    public void notifyDataRemoved(int position) {
        removeViewAt(position);
    }

    public void notifyDataInserted(View itemView, int position) {
        bindItemData(itemView, data.get(position));
        addView(itemView, position);
    }

    public MDA_DragViewPager getParentHorizontalScrollView() {
        return getParentHorizontalScrollView(this);
    }

    private MDA_DragViewPager getParentHorizontalScrollView(View view) {
        if (view.getParent() instanceof MDA_DragViewPager) {
            return (MDA_DragViewPager) view.getParent();
        } else {
            return getParentHorizontalScrollView((View) view.getParent());
        }
    }

    public void setDragPosition(int dragPosition) {
        this.dragPosition = dragPosition;
    }

    public void refreshVisibility() {
        for (int i = 0; i < getChildCount(); i++) {
            if (dragPosition == i) {
                getChildAt(i).setVisibility(INVISIBLE);
            } else {
                getChildAt(i).setVisibility(VISIBLE);
            }
        }
    }
}
