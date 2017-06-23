package com.boc.bocsoft.mobile.framework.widget.listview;

import android.view.View;

/**
 * Created by XieDu on 2016/7/7.
 */
public class ListAdapterHelper {
    /**
     * 给列表项的某个子视图绑定点击监听器
     */
    public static <T> void setOnClickChildViewInItemItf(int position, T item, View childView,
            OnClickChildViewInItemItf<T> onClickChildViewInItemItf) {
        childView.setOnClickListener(
                new OnClickChildViewInItemListener<T>(position, item, onClickChildViewInItemItf));
    }

    /**
     * 用于监听点击列表某项内部的子View
     *
     * @author xdy4486
     */
    public static class OnClickChildViewInItemListener<T> implements View.OnClickListener {

        private int position;
        private T item;
        private OnClickChildViewInItemItf<T> onClickChildViewInItemItf;

        public OnClickChildViewInItemListener(int position, T item,
                OnClickChildViewInItemItf<T> onClickChildViewInItemItf) {
            this.position = position;
            this.item = item;
            this.onClickChildViewInItemItf = onClickChildViewInItemItf;
        }

        public void onClick(View childView) {
            if (onClickChildViewInItemItf == null) {
                return;
            }
            onClickChildViewInItemItf.onClickChildViewInItem(position, item, childView);
        }
    }

    /**
     * 用于监听点击列表某项内部的子View
     *
     * @author xdy4486
     */
    public interface OnClickChildViewInItemItf<T> {
        /**
         * @param position 列表下标
         * @param item 列表数据对象
         * @param childView 子视图
         */
        public void onClickChildViewInItem(int position, T item, View childView);
    }
}
