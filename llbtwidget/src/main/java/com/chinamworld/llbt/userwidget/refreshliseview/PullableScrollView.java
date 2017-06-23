package com.chinamworld.llbt.userwidget.refreshliseview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.ScrollView;

/**
 * 继承于scrollView的下拉刷新
 * Created by Administrator on 2016/9/6.
 */
public class PullableScrollView extends ScrollView implements  IPullable{
    public PullableScrollView(Context context) {
        super(context);
        this.setFillViewport(true);
    }

    public PullableScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public PullableScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView(){
        this.setFillViewport(true);
        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        this.setLayoutParams(lp);
    }

    @Override
    public boolean canPull() {
        if(this.getScrollY() <= 0)
            return true;
        return false;
    }
}
