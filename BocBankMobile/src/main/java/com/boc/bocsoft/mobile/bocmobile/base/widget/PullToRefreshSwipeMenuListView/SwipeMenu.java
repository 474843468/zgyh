package com.boc.bocsoft.mobile.bocmobile.base.widget.PullToRefreshSwipeMenuListView;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gengjunying on 2016/12/12.
 * 滑动菜单
 */
public class SwipeMenu {
    private Context mContext;

    public List<SwipeMenuItem> getmItems() {
        return mItems;
    }

    public void setmItems(List<SwipeMenuItem> mItems) {
        this.mItems = mItems;
    }

    private List<SwipeMenuItem> mItems;
    private int mViewType;

    public SwipeMenu(Context context) {
        mContext = context;
        mItems = new ArrayList<SwipeMenuItem>();
    }

    public Context getContext() {
        return mContext;
    }

    public void addMenuItem(SwipeMenuItem item) {
        mItems.add(item);
    }

    public void removeMenuItem(SwipeMenuItem item) {
        mItems.remove(item);
    }

    public List<SwipeMenuItem> getMenuItems() {
        return mItems;
    }

    public SwipeMenuItem getMenuItem(int index) {
        return mItems.get(index);
    }

    public int getViewType() {
        return mViewType;
    }

    public void setViewType(int viewType) {
        this.mViewType = viewType;
    }
}
