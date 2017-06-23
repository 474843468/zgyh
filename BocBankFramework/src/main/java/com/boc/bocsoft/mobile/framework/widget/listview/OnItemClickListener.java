package com.boc.bocsoft.mobile.framework.widget.listview;

import android.view.View;

/**
 * 用于RecycleView的Item点击监听
 */
public interface OnItemClickListener {
    /**
     * Itemview点击事件,传入当前Itemview及position
     * @param itemView
     * @param position
     */
    void onItemClick(View itemView, int position);
}