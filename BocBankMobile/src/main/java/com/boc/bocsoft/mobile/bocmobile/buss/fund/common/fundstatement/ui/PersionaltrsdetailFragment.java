package com.boc.bocsoft.mobile.bocmobile.buss.fund.common.fundstatement.ui;

import android.view.LayoutInflater;
import android.view.View;

import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.MvpBussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.utils.MoneyUtils;
import com.boc.bocsoft.mobile.bocmobile.base.widget.details.DetailContentView;
import com.boc.bocsoft.mobile.bocmobile.base.widget.details.DetailTableHead;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.common.fundstatement.model.PersionaltrsModel;
import com.boc.bocsoft.mobile.bocmobile.buss.fund.common.fundstatement.presenter.StatementPresenter;

/**
 * Created by huixiaobo on 2016/11/24.
 * 交易流水详情
 */
public class PersionaltrsdetailFragment extends MvpBussFragment<StatementPresenter> {


    protected View rootView;
    /**详情头部布局*/
    protected DetailTableHead repayDetailHead;
    /**详情每条布局*/
    protected DetailContentView repayDetailRow;
    /**交易流水对象*/
    private PersionaltrsModel mPersionaltrs;

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
    protected boolean isDisplayRightIcon() {
        return false;
    }

    @Override
    protected String getTitleValue() {
        return getString(R.string.boc_fundstate_detail);
    }

    @Override
    protected boolean getTitleBarRed() {
        return false;
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
        mPersionaltrs = (PersionaltrsModel) getArguments().getSerializable(StatementConst.FUND_PERSIONLTRS);

        if (mPersionaltrs != null) {
            repayDetailHead.updateData(getString(R.string.boc_fundstate_applycunt), MoneyUtils.transMoneyFormat(mPersionaltrs.getFundConfirmCount(), mPersionaltrs.getCurrency()));
            repayDetailHead.setTableRow(getString(R.string.boc_fundstate_cimtDate), mPersionaltrs.getPaymentDate());

            repayDetailRow.addDetail(getString(R.string.boc_fundstate_name), mPersionaltrs.getFundName());
            repayDetailRow.addDetail(getString(R.string.boc_fundstate_applyType), mPersionaltrs.getFundTranType());
            repayDetailRow.addDetail(getString(R.string.boc_fundstate_palydate), mPersionaltrs.getApplyDate());
            repayDetailRow.addDetail(getString(R.string.boc_fundstate_applyamount), mPersionaltrs.getFundConfirmAmount());
            repayDetailRow.addDetail(getString(R.string.boc_fundstate_applystutas), mPersionaltrs.getTrsStatus());
            repayDetailRow.addDetail(getString(R.string.boc_fundstate_applyaAccount), mPersionaltrs.getFundAccountNo());
        }

    }

    @Override
    protected StatementPresenter initPresenter() {
        return null;
    }
}
