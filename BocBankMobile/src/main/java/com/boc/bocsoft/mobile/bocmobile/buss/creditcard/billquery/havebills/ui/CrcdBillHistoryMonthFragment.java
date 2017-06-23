package com.boc.bocsoft.mobile.bocmobile.buss.creditcard.billquery.havebills.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.MvpBussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.widget.refreshliseview.PullToRefreshLayout;
import com.boc.bocsoft.mobile.bocmobile.base.widget.refreshliseview.PullableListView;
import com.boc.bocsoft.mobile.bocmobile.buss.creditcard.billquery.widget.CrcdBillInfoItemView;
import com.boc.bocsoft.mobile.framework.ui.BasePresenter;

/**
 * 历史账单-具体月份
 * Created by liuweidong on 2016/12/27.
 */

public class CrcdBillHistoryMonthFragment extends MvpBussFragment {
    private View rootView;
    private PullToRefreshLayout pullToRefreshLayout;
    private PullableListView listView;
    private TextView txtBillDate;// 账单日
    private CrcdBillInfoItemView billMoney1;// 账单金额
    private TextView txtBillSend;// 发送账单
    private CrcdBillInfoItemView billMoney2;// 支出&收入

    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        rootView = View.inflate(mContext, R.layout.boc_fragment_crcd_bill_history_month, null);
        return rootView;
    }

    @Override
    public void initView() {
        super.initView();
        pullToRefreshLayout = (PullToRefreshLayout) rootView.findViewById(R.id.pull_refresh);
        listView = (PullableListView) rootView.findViewById(R.id.listview);

        View headView = View.inflate(mContext, R.layout.boc_fragment_crcd_bill_history_month_head, null);
        listView.addHeaderView(headView, null, false);
        txtBillDate = (TextView) headView.findViewById(R.id.txt_bill_date);
        billMoney1 = (CrcdBillInfoItemView) headView.findViewById(R.id.bill_money);
        txtBillSend = (TextView) headView.findViewById(R.id.txt_bill_send);
        billMoney2 = (CrcdBillInfoItemView) headView.findViewById(R.id.bill_money_type);
    }

    @Override
    public void initData() {
        super.initData();
    }

    @Override
    public void setListener() {
        super.setListener();
        txtBillSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: 2016/12/28 发送账单
            }
        });
    }

    @Override
    protected BasePresenter initPresenter() {
        return null;
    }

    @Override
    protected String getTitleValue() {
        return "月账单";
    }

    @Override
    protected boolean getTitleBarRed() {
        return false;
    }

    @Override
    protected boolean isDisplayRightIcon() {
        return false;
    }
}
