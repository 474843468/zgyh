package com.chinamworld.llbt.userwidget.refreshliseview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * 继承于ListView的上拉刷新
 * Created by Administrator on 2016/5/27.
 */
public class PullableListView extends ListView implements IPullable {

    public PullableListView(Context context) {
        super(context);
    }

    public PullableListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PullableListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
    private boolean isPullUp = true;

    /**
     * 设置当前列表为上拉刷新，还是下拉刷新
     * @param flag ：true:上拉刷新 ；false:下拉刷新
     */
    public void setIsPullUp(boolean flag){
        isPullUp = flag;
    }
    @Override
    public boolean canPull() {
        if (getCount() == 0) {
            // 没有item的时候也可以上拉加载
            return true;
        }
        if(isPullUp == false){
            if(getFirstVisiblePosition() == 0 && getChildAt(0) != null && getChildAt(0).getTop() == 0){
                return true;
            }
            return false;
        }
        if (getLastVisiblePosition() == (getCount() - 1)) {
            // 滑到底部了
            if (getChildAt(getLastVisiblePosition() - getFirstVisiblePosition()) != null
                    && getChildAt(getLastVisiblePosition() - getFirstVisiblePosition()).getBottom() <= getMeasuredHeight())
                return true;
        }
        return false;
    }

    /**
     * 设置控件的隐藏滑动滚动条
     */
    public void setScrollBars(){
        this.setVerticalScrollBarEnabled(false);
        this.setFastScrollEnabled(false);
    }
}
