package com.chinamworld.llbt.userwidget.refreshliseview;

import android.view.View;

/**
 * 刷新布局，数据刷新时，事件监听
 * Created by yuht on 2016/10/14.
 */
public interface IRefreshLayoutListener {
    void onLoadMore(View pullToRefreshLayout);

}
