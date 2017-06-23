package com.boc.bocsoft.mobile.bocmobile.buss.loan.nonreloan.query.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLOANListEQuery.PsnLOANListEQueryResult;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.BussFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.common.loanmanagement.LoanCosnt;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.reloan.query.ReloanQryConst;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.reloan.query.ui.ReloanUseRecordsFragment;


/**
 * Created by liuzc on 2016/10/8.
 * 非循环贷款-功能页面
 */
public class NonReloanMoreOptionFragment extends BussFragment implements View.OnClickListener {
    /**
     * view
     */
    private View rootView;

    protected LinearLayout llyUseRecords;  //用款记录
    protected LinearLayout llyPreRepay;    //提前还款
    protected LinearLayout llyRepayPlan;   //还款计划
    protected LinearLayout llyPaySetting;   //支付功能设置

    private PsnLOANListEQueryResult.PsnLOANListEQueryBean mQryResultBean = null;
    private String currentDate = null;

    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        rootView = mInflater.inflate(R.layout.boc_fragment_loan_reloan_moreopt, null);
        return rootView;
    }

    @Override
    public void initView() {
        super.initView();
        llyUseRecords = (LinearLayout) rootView.findViewById(R.id.llyUseRecords);
        llyPreRepay = (LinearLayout) rootView.findViewById(R.id.llyPreRepay);
        llyRepayPlan = (LinearLayout) rootView.findViewById(R.id.llyRepayPlan);
        llyPaySetting = (LinearLayout) rootView.findViewById(R.id.llyPaySetting);

        llyUseRecords.setOnClickListener(this);
        llyPreRepay.setVisibility(View.GONE);
        llyRepayPlan.setVisibility(View.GONE);
        llyPaySetting.setVisibility(View.GONE);
    }


    @Override
    public void initData() {
        super.initData();

        Bundle bundle  = getArguments();
        mQryResultBean = (PsnLOANListEQueryResult.PsnLOANListEQueryBean)bundle.getSerializable(
                ReloanQryConst.KEY_STATUS_QRY_RESULT_BEAN);
        currentDate = getArguments().getString(LoanCosnt.LOAN_ENDDATE);
    }

    @Override
    protected String getTitleValue() {
        return getResources().getString(R.string.boc_loan_mored);
    }

    @Override
    protected boolean getTitleBarRed() {
        return false;
    }

    @Override
    protected boolean isDisplayRightIcon() {
        return false;
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.llyUseRecords) { //用款记录
            Bundle bundle = new Bundle();
            bundle.putString(ReloanQryConst.KEY_ACCOUNT_NUMBER, mQryResultBean.getAccountNumber());
            bundle.putString(ReloanQryConst.KEY_CURRENCY, mQryResultBean.getCurrencyCode());
            ReloanUseRecordsFragment fragment = new ReloanUseRecordsFragment();
            fragment.setArguments(bundle);
            start(fragment);
        }
    }

}
