package com.boc.bocsoft.mobile.bocmobile.base.widget.transactionlist;

import android.content.Context;
import android.util.AttributeSet;

import com.boc.bocsoft.mobile.bocmobile.base.widget.transactionlist.TransactionView;

/**
 * @author wangyang
 *         16/8/22 23:00
 */
public class FullTransactionListView extends TransactionView{

    public FullTransactionListView(Context context) {
        super(context);
    }

    public FullTransactionListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FullTransactionListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
