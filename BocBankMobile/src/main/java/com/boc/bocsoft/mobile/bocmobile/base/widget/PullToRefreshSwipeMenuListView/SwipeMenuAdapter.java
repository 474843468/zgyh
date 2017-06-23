package com.boc.bocsoft.mobile.bocmobile.base.widget.PullToRefreshSwipeMenuListView;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.WrapperListAdapter;

import com.boc.bocsoft.mobile.bocmobile.buss.equickpay.model.hcecardlist.HceCardListQueryViewModel;


/**
 * Created by gengjunying on 2016/12/12.
 * 滑动菜单适配器
 */
public class SwipeMenuAdapter implements WrapperListAdapter, SwipeMenuView.OnSwipeItemClickListener {
    private ListAdapter mAdapter;
    private Context mContext;
    private PullToRefreshSwipeMenuListView.OnMenuItemClickListener onMenuItemClickListener;

    public SwipeMenuAdapter(Context context, ListAdapter adapter) {
        mAdapter = adapter;
        mContext = context;
    }

    @Override
    public int getCount() {
        return mAdapter.getCount();
    }

    @Override
    public Object getItem(int position) {
        return mAdapter.getItem(position);
    }

    @Override
    public long getItemId(int position) {
        return mAdapter.getItemId(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        SwipeMenuLayout layout = null;
        if (convertView == null) {
            View contentView = mAdapter.getView(position, convertView, parent);
            SwipeMenu menu = new SwipeMenu(mContext);
            menu.setViewType(mAdapter.getItemViewType(position));
            createMenu(menu,(HceCardListQueryViewModel)getItem(position));
            SwipeMenuView menuView = new SwipeMenuView(menu, (PullToRefreshSwipeMenuListView) parent);
            menuView.setOnSwipeItemClickListener(this);
            PullToRefreshSwipeMenuListView listView = (PullToRefreshSwipeMenuListView) parent;
            layout = new SwipeMenuLayout(contentView, menuView, listView.getCloseInterpolator(),
                    listView.getOpenInterpolator());
            layout.setPosition(position);
        } else {
            layout = (SwipeMenuLayout) convertView;
            layout.closeMenu();
            layout.setPosition(position);
            mAdapter.getView(position, layout.getContentView(), parent);
        }
        return layout;
    }

    public void createMenu(SwipeMenu menu, HceCardListQueryViewModel data) {

    }

    @Override
    public void onItemClick(SwipeMenuView view, SwipeMenu menu, int index) {
        if (onMenuItemClickListener != null) {
            onMenuItemClickListener.onMenuItemClick(view.getPosition(), menu, index);
        }
    }

    public void setOnMenuItemClickListener(PullToRefreshSwipeMenuListView.OnMenuItemClickListener onMenuItemClickListener) {
        this.onMenuItemClickListener = onMenuItemClickListener;
    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {
        mAdapter.registerDataSetObserver(observer);
    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {
        mAdapter.unregisterDataSetObserver(observer);
    }

    @Override
    public boolean areAllItemsEnabled() {
        return mAdapter.areAllItemsEnabled();
    }

    @Override
    public boolean isEnabled(int position) {
        return mAdapter.isEnabled(position);
    }

    @Override
    public boolean hasStableIds() {
        return mAdapter.hasStableIds();
    }

    @Override
    public int getItemViewType(int position) {
        return mAdapter.getItemViewType(position);
    }

    @Override
    public int getViewTypeCount() {
        return mAdapter.getViewTypeCount();
    }

    @Override
    public boolean isEmpty() {
        return mAdapter.isEmpty();
    }

    @Override
    public ListAdapter getWrappedAdapter() {
        return mAdapter;
    }
}
