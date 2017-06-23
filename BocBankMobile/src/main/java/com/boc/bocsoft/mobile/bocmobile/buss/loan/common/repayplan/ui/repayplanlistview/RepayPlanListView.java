package com.boc.bocsoft.mobile.bocmobile.buss.loan.common.repayplan.ui.repayplanlistview;

import android.content.Context;
import android.util.AttributeSet;

import com.boc.bocsoft.mobile.bocmobile.base.widget.refreshliseview.PullableListView;

import java.util.List;

/**
 * 剩余还款列表公共组件
 * <p/>
 * Created by liuzc on 2016/8/10.
 */
public class RepayPlanListView extends PullableListView {
    private Context mContext;
    private RepayPlanAdapter adapter;

    public RepayPlanListView(Context context) {
        super(context);
        mContext = context;
    }

    public RepayPlanListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        setDividerHeight(0);
    }

    public RepayPlanListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
    }


    /**
     * 设置数据
     * @param list
     */
    public void setData(List<RepayPlanBean> list) {
    	if(adapter == null){
            adapter = new RepayPlanAdapter(mContext);
            setAdapter(adapter);
        }
        getTransactionAdapter().setData(list);
        adapter.notifyDataSetChanged();
    }

    private RepayPlanAdapter getTransactionAdapter() {
        return adapter;
    }

}
