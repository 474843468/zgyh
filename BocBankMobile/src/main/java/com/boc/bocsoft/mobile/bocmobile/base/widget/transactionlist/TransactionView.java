package com.boc.bocsoft.mobile.bocmobile.base.widget.transactionlist;

import android.content.Context;
import android.util.AttributeSet;

import com.boc.bocsoft.mobile.bocmobile.base.widget.refreshliseview.PullableListView;

import java.util.List;

/**
 * 查询列表公共组件
 * Created by liuweidong on 2016/6/1.
 */
public class TransactionView extends PullableListView {
    private Context mContext;
    private TransactionAdapter mAdapter;
    public static final int TITLE_DATE_TYPE = 0;
    public static final int TITLE_DATE_TYPE1 = 1;
    public static final int TITLE_BILL_TYPE = 3;
    public static final int TITLE_ATM_DRAW_TYPE = 5;
    public static final int TITLE_BILL_TYPE1 = 8;

    public TransactionView(Context context) {
        this(context, null);
    }

    public TransactionView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TransactionView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        setDividerHeight(0);
    }

    /**
     * 设置数据
     *
     * @param list
     */
    public void setData(List<TransactionBean> list) {
        mAdapter.setData(list);
        mAdapter.notifyDataSetChanged();
    }

    /**
     * 设置适配器
     */
    public void setAdapter() {
        setAdapter(getTransactionAdapter());
    }

    /**
     * 获取适配器
     *
     * @return
     */
    public TransactionAdapter getTransactionAdapter() {
        if (mAdapter == null)
            mAdapter = new TransactionAdapter(mContext);
        return mAdapter;
    }

    /**
     * 监听接口回调
     */
    public interface ClickListener {
        void onItemClickListener(int position);
    }

    /**
     * 设置监听事件
     *
     * @param listener
     */
    public void setListener(ClickListener listener) {
        getTransactionAdapter().setOnClickListener(listener);
    }
}
