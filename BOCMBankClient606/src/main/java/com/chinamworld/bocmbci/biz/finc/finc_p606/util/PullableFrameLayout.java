package com.chinamworld.bocmbci.biz.finc.finc_p606.util;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.ScrollView;

import com.chinamworld.bocmbci.R;
import com.chinamworld.llbt.userwidget.refreshliseview.IPullable;

/**
 * Created by Administrator on 2016/10/21 0021.
 */
public class PullableFrameLayout extends FrameLayout implements IPullable {
    private ListView listview;
    public PullableFrameLayout(Context context) {
        super(context);
    }

    public PullableFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

    }

    public PullableFrameLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    /** 当前ListView上层的第一个ScrollView */
    private ScrollView mScrollView;
    public void setCurListView(ListView listView){
        this.listview = listView;
        View tmp = listView;
        while(tmp.getParent() != null){
            if(tmp.getParent() instanceof  ScrollView){
                mScrollView = (ScrollView)tmp.getParent();
                return;
            }
            tmp = (View)tmp.getParent();
        }
    }

    @Override
    public boolean canPull() {
        if(mScrollView == null)
            return false;
        if (listview.getCount() == 0) {
            // 没有item的时候也可以上拉加载
            return true;
        }
        double d1 = mScrollView.getScrollY();
        double d2 = mScrollView.getMeasuredHeight();
        double d3 = mScrollView.getHeight();
        if(d1 + d2  >= listview.getHeight()){
            return true;
        }
        return false;
//        boolean flag = false;
//        if (listview.getCount() == 0) {
//            // 没有item的时候也可以上拉加载
//            flag = true;
//        } else if (listview.getLastVisiblePosition() == (listview.getCount() - 1)) {
//            // 滑到底部了
//            if (listview.getChildAt(listview.getLastVisiblePosition() - listview.getFirstVisiblePosition()) != null
//                    && listview.getChildAt(listview.getLastVisiblePosition() - listview.getFirstVisiblePosition()).getBottom() <= listview.getMeasuredHeight())
//                flag = true;
//        }
//
//        return flag;
    }
}
