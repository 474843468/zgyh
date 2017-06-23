package com.boc.bocsoft.mobile.bocmobile.buss.fund.common.fundstatement.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

/**
 * Created by huixiaobo on 15/8/22.
 * 悬浮嵌套组件
 */
public class StatementScrollView extends ScrollView {

    public OnScrollListener onScrollListener;

    public StatementScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setOnScrollListener(OnScrollListener onScrollListener) {
        this.onScrollListener = onScrollListener;
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (onScrollListener != null){
            onScrollListener.onScrollchanged(t);
        }
    }

    /**
     * 由垂直方向滚动条代表的所有垂直范围，缺省的范围是当前视图的画图高度。
     */
    public int computeVerticalScrollRange(){
        return super.computeVerticalScrollRange();
    }

    public interface OnScrollListener {
        public void onScrollchanged(int t);

        public void onTouchUp();

        public void onTouchDown();
    }




}
