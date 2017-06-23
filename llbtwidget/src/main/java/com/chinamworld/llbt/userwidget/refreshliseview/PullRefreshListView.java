package com.chinamworld.llbt.userwidget.refreshliseview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import com.chinamworld.llbt.model.IFunc;


/**
 * 实现ListView的上拉刷新
 * Created by Administrator on 2016/8/28.
 */
public class PullRefreshListView extends PullToRefreshLayout {
    public PullRefreshListView(Context context) {
        super(context);
        initView();
    }

    public PullRefreshListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public PullRefreshListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView();
    }

    public int recordNum = 0;
    public int currentIndex = 0;
    private void initView(){
        super.setOnRefreshListener(new IRefreshLayoutListener() {
            @Override
            public void onLoadMore(View pullToRefreshLayout) {
                if (baseAdapter.getCount() < recordNum) {
                    loadMoreCallBack.callBack(currentIndex);
                } else {
                    PullRefreshListView.this.loadmoreCompleted(RefreshDataStatus.NoMoreData);
                }
            }
        });
    }
    
    IFunc<Boolean> loadMoreCallBack;
    public void initData(int recordNum,int currentIndex,IFunc<Boolean> loadMoreCallBack){
        this.loadMoreCallBack = loadMoreCallBack;
        this.recordNum = recordNum;
        this.currentIndex = currentIndex;
    }


    @Override
    public void setOnRefreshListener(IRefreshLayoutListener listener) {

    }

}
