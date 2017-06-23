package com.boc.bocsoft.mobile.bocmobile.buss.fund.fundproduct.home.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.HorizontalScrollView;

import java.util.List;

/**
 * @Description:水平滑动控件
 */
public class SyncHorizontalScrollView extends HorizontalScrollView {

    private List<SyncHorizontalScrollView> mViewList;

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
        if (mViewList != null) {
            for(int i = 0; i < mViewList.size(); i ++){
                View curView = mViewList.get(i);
                if(curView != this){
                    curView.scrollTo(l, t);
                }
            }
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
     * @param listView
     */
    public void setScrollView(List<SyncHorizontalScrollView> listView) {
        mViewList = listView;
    }
}