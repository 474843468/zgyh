package com.boc.bocsoft.mobile.bocmobile.buss.transfer.withdrawalquery.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import com.boc.bocsoft.mobile.bocmobile.ApplicationConst;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.BussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.utils.MoneyUtils;
import com.boc.bocsoft.mobile.bocmobile.base.utils.PublicCodeUtils;
import com.boc.bocsoft.mobile.bocmobile.base.widget.details.DetailContentView;
import com.boc.bocsoft.mobile.bocmobile.base.widget.details.DetailTableHead;
import com.boc.bocsoft.mobile.bocmobile.base.widget.details.DetailTableRowGroup;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.utils.ResultStatusUtils;
import com.boc.bocsoft.mobile.bocmobile.buss.transfer.withdrawalquery.model.WithdrawalQueryViewModel;
import com.boc.bocsoft.mobile.common.utils.NumberUtils;
import com.boc.bocsoft.mobile.common.utils.date.DateFormatters;

/**
 * 取款查询 -- 详情页面
 * Created by wangf on 2016/6/28
 */
public class WithdrawalQueryDetailInfoFragment extends BussFragment {

    private View rootView;
    private DetailTableHead detailTableHead;
    private DetailContentView detailContentView;
    private DetailTableRowGroup detailTableRowGroup;

    /**
     * 详情数据
     */
    private WithdrawalQueryViewModel.ListBean queryViewModelBean;
    //代理点收款账户
    private String agentAcctNumber;


    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        rootView = mInflater.inflate(R.layout.boc_fragment_account_transdetail_info, null);
        Bundle bundle = getArguments();
        queryViewModelBean = bundle.getParcelable("DetailsInfo");
        agentAcctNumber = bundle.getString("AgentAcctNumber");
        return rootView;
    }

    @Override
    public void initView() {
        detailTableHead = (DetailTableHead) rootView.findViewById(R.id.head_view);
        detailContentView = (DetailContentView) rootView.findViewById(R.id.body_view);
        detailTableRowGroup = (DetailTableRowGroup) rootView.findViewById(R.id.bottom_view);
        detailTableRowGroup.setVisibility(View.GONE);
    }

    @Override
    public void initData() {
        detailTableHead.updateData(getResources().getString(R.string.boc_transfer_withdrawal_query_details_amount), MoneyUtils.transMoneyFormat(queryViewModelBean.getRemitAmount(), ApplicationConst.CURRENCY_CNY));
        detailTableHead.addDetail(getResources().getString(R.string.boc_transfer_withdrawal_query_details_account), NumberUtils.formatCardNumber(agentAcctNumber));

        detailContentView.addDetailRow(getResources().getString(R.string.boc_transfer_withdrawal_query_details_no), queryViewModelBean.getRemitNo());
        detailContentView.addDetailRow(getResources().getString(R.string.boc_transfer_withdrawal_query_details_name), queryViewModelBean.getPayeeName());
        detailContentView.addDetailRow(getResources().getString(R.string.boc_transfer_withdrawal_query_details_mobile), NumberUtils.formatMobileNumber(queryViewModelBean.getPayeeMobile()));
        detailContentView.addDetailRow(getResources().getString(R.string.boc_transfer_withdrawal_query_details_currency), PublicCodeUtils.getCurrency(mContext, queryViewModelBean.getCurrencyCode()));
        detailContentView.addDetailRow(getResources().getString(R.string.boc_transfer_withdrawal_query_details_date), queryViewModelBean.getTranDate().format(
                DateFormatters.dateFormatter1));
        detailContentView.addDetailRow(getResources().getString(R.string.boc_transfer_withdrawal_query_details_status), ResultStatusUtils.getWithdrawalStatus(mContext, queryViewModelBean.getRemitStatus()));
        detailContentView.addDetailRow(getResources().getString(R.string.boc_transfer_withdrawal_query_details_agentname), queryViewModelBean.getAgentName());
        detailContentView.addDetailRow(getResources().getString(R.string.boc_transfer_withdrawal_query_details_agentnum), queryViewModelBean.getAgentNum());
        detailContentView.addDetailRowNotLine(getResources().getString(R.string.boc_transfer_withdrawal_query_details_channel), ResultStatusUtils.getTransChannel(mContext, queryViewModelBean.getChannel()));
    }

    @Override
    protected boolean isDisplayRightIcon() {
        return false;
    }

    @Override
    protected boolean getTitleBarRed() {
        return false;
    }

    @Override
    protected String getTitleValue() {
        return getResources().getString(R.string.boc_transfer_details_title);
    }
}
