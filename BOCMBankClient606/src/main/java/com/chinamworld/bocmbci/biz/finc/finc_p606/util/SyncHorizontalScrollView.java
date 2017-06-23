package com.chinamworld.bocmbci.biz.finc.finc_p606.util;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.HorizontalScrollView;

/**
 * @Description:水平滑动控件
 */
public class SyncHorizontalScrollView extends HorizontalScrollView {

    private View mView;

    public SyncHorizontalScrollView(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
    }

    public SyncHorizontalScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
    }

    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        // 设置控件滚动监听，得到滚动的距离，然后让传进来的view也设置相同的滚动具体
        if (mView != null) {
            mView.scrollTo(l, t);
        }
    }

    /**
     * 重绘控件，解决listview只显示一行问题
     */
    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }

    /**
     * 设置跟它联动的view
     *
     * @param view
     */
    public void setScrollView(View view) {
        mView = view;
    }
}