package com.chinamworld.bocmbci.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.chinamworld.bocmbci.widget.adapter.AdapterForLinearLayout;
/**
 * 
 * @author xiabaoying
 *
 * 2012-12-11
 *
 */
public class LinearLayoutForListView extends LinearLayout {

    private AdapterForLinearLayout adapter;
    private OnClickListener onClickListener = null;

    /**
     * 绑定布局
     */
    public void bindLinearLayout() {
        int count = adapter.getCount();
        for (int i = 0; i < count; i++) {
            View v = adapter.getView(i, null, null);

            addView(v, i);
        }
    }

    public LinearLayoutForListView(Context context) {
        super(context);

    }

    public LinearLayoutForListView(Context context, AttributeSet attrs) {
        super(context, attrs);

    }

    /**
     * 获取Adapter
     * 
     * @return adapter
     */
    public AdapterForLinearLayout getAdpater() {
        return adapter;
    }

    /**
     * 设置数据
     * 
     * @param adpater
     */
    public void setAdapter(AdapterForLinearLayout adpater) {
        this.adapter = adpater;
        bindLinearLayout();
    }

    /**
     * 获取点击事件
     * 
     * @return
     */
    public OnClickListener getOnclickListner() {
        return onClickListener;
    }

    /**
     * 设置点击事件
     * 
     * @param onClickListener
     */
    public void setOnclickLinstener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

}
