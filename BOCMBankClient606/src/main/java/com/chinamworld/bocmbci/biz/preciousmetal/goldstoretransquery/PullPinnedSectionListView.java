package com.chinamworld.bocmbci.biz.preciousmetal.goldstoretransquery;

import android.content.Context;
import android.util.AttributeSet;

import com.chinamworld.llbt.userwidget.refreshliseview.IPullable;
import com.chinamworld.llbt.userwidget.refreshliseview.PullToRefreshLayout;

/**
 * 带悬浮标题栏的上滑 刷新 控件
 * Created by linyl on 2016/9/17.
 */
public class PullPinnedSectionListView extends PinnedSectionListView implements IPullable {

    public PullPinnedSectionListView(Context context) {
        super(context);
    }

    public PullPinnedSectionListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PullPinnedSectionListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean canPull() {
        if (getCount() == 0) {
            // 没有item的时候也可以上拉加载
            return true;
        } else if (getLastVisiblePosition() == (getCount() - 1)) {
            // 滑到底部了
            if (getChildAt(getLastVisiblePosition() - getFirstVisiblePosition()) != null
                    && getChildAt(getLastVisiblePosition() - getFirstVisiblePosition()).getBottom() <= getMeasuredHeight())
                return true;
        }
        return false;
    }
}