package com.boc.bocsoft.mobile.bocmobile.buss.system.home.ui.view;

import android.content.Context;
import android.support.v4.widget.NestedScrollView;
import android.util.AttributeSet;

/**
 * 首页滑动视图
 * Created by lxw on 2016/9/24 0024.
 */
public class HomeScrollView extends NestedScrollView {

    private OnScrollChangedListener onScrollChangedListener;
    public HomeScrollView(Context context) {
        super(context);
    }

    public HomeScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public HomeScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (onScrollChangedListener != null) {
            onScrollChangedListener.onScrollChanged(l, t, oldl, oldt);
        }
    }

    public interface OnScrollChangedListener{
        void onScrollChanged(int l, int t, int oldl, int oldt);
    }

    public void setOnScrollChangedListener(OnScrollChangedListener onScrollChangedListener){
        this.onScrollChangedListener = onScrollChangedListener;
    }
}
