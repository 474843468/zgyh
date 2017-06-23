package com.boc.bocsoft.mobile.bocmobile.buss.fund.common.fundstatement.ui;

import android.view.LayoutInflater;
import android.view.View;

import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.BussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.activity.MvpBussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.widget.details.DetailContentView;
import com.boc.bocsoft.mobile.bocmobile.base.widget.details.DetailTableHead;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.common.fundstatement.model.StatementModel;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.common.fundstatement.presenter.StatementPresenter;

/**
 * Created by huixiaobo on 2016/11/22.
 * 基金对账单持仓信息详情
 */
public class StatementdetailFragment extends BussFragment {

    protected View rootView;
    /**详情头布局*/
    protected DetailTableHead repayDetailHead;
    /**详情每条内容*/
    protected DetailContentView repayDetailRow;
    /**持仓信息对象*/
    private StatementModel.ListBean  listBean;

    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        rootView = mInflater.inflate(R.layout.boc_fund_statedetail, null);
        return rootView;
    }

    @Override
    protected boolean isHaveTitleBarView() {
        return true;
    }

    @Override
    protected boolean getTitleBarRed() {
        return false;
    }

    @Override
    protected boolean isDisplayRightIcon() {
        return true;
    }

    @Override
    protected String getTitleValue() {
        return getString(R.string.boc_fundstate_detail);
    }

    @Override
    public void initView() {
        super.initView();
        repayDetailHead = (DetailTableHead) rootView.findViewById(R.id.repayDetailHead);
        repayDetailRow = (DetailContentView) rootView.findViewById(R.id.repayDetailRow);
    }

    @Override
    public void initData() {
        super.initData();
        //上页面传递持仓对象
        listBean = (StatementModel.ListBean) getArguments().getSerializable(StatementConst.FUND_STATEMENT);

        if (listBean != null) {
            repayDetailHead.updateData(getString(R.string.boc_fundstate_floatLoss), listBean.getFloatLoss().toString());
            repayDetailHead.setTableRow(getString(R.string.boc_fundstate_marketValue), listBean.getMarketValue().toString());

            repayDetailRow.addDetail(getString(R.string.boc_fundstate_name), listBean.getFundName());
            repayDetailRow.addDetail(getString(R.string.boc_fundstate_applyCount), listBean.getTotalBalance());
            repayDetailRow.addDetail(getString(R.string.boc_fundstate_avilCount), listBean.getAvailableBalance());
            repayDetailRow.addDetail(getString(R.string.boc_fundstate_bonusType), listBean.getBonusType());
            repayDetailRow.addDetail(getString(R.string.boc_fundstate_dPrice), listBean.getNetPrice());
            repayDetailRow.addDetail(getString(R.string.boc_fundstate_pricedate), listBean.getNetPriceDate());
        }
    }

}
