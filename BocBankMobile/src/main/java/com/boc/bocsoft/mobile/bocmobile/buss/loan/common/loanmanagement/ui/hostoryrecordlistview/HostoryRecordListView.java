package com.boc.bocsoft.mobile.bocmobile.buss.loan.common.loanmanagement.ui.hostoryrecordlistview;

import android.content.Context;
import android.util.AttributeSet;

import com.boc.bocsoft.mobile.bocmobile.base.widget.refreshliseview.PullableListView;

import java.util.List;

/**
 * 剩余还款列表公共组件
 */
public class HostoryRecordListView extends PullableListView {
    private Context mContext;
    private HostoryRecordAdapter adapter;

    public HostoryRecordListView(Context context) {
        super(context);
        mContext = context;
    }

    public HostoryRecordListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        setDividerHeight(0);
    }

    public HostoryRecordListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
    }


    /**
     * 设置数据
     * @param list
     */
    public void setData(List<HostoryRecordBean> list) {
    	if(adapter == null){
            adapter = new HostoryRecordAdapter(mContext);
            setAdapter(adapter);
        }
        getTransactionAdapter().setData(list);
        adapter.notifyDataSetChanged();
    }

    private HostoryRecordAdapter getTransactionAdapter() {
        return adapter;
    }

}
