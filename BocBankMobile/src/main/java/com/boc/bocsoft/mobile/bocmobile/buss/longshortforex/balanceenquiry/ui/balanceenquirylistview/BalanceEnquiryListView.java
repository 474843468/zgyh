package com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.balanceenquiry.ui.balanceenquirylistview;

import android.content.Context;
import android.util.AttributeSet;

import com.boc.bocsoft.mobile.bocmobile.base.widget.refreshliseview.PullableListView;

/**
 * 余额查询列表公共组件
 * Created by wjk7118 on 2016/11/29.
 */
public class BalanceEnquiryListView extends PullableListView {

    private Context mContext;
    private BalanceEnquiryAdapter adapter;

    public BalanceEnquiryListView(Context context){
        super(context);
        mContext = context;
    }

    public BalanceEnquiryListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        setDividerHeight(0);
    }

    public BalanceEnquiryListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
    }
    @Override

    protected void onMeasure(int widthMeasureSpec,int heightMeasureSpec){
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE>>2,MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec,expandSpec);
    }
}
