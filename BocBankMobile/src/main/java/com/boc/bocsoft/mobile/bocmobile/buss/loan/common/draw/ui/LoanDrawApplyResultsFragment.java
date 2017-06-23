package com.boc.bocsoft.mobile.bocmobile.buss.loan.common.draw.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.BussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.utils.MoneyUtils;
import com.boc.bocsoft.mobile.bocmobile.base.widget.operation.BaseOperationResultView;
import com.boc.bocsoft.mobile.bocmobile.base.widget.operation.OperationResultBottom;
import com.boc.bocsoft.mobile.bocmobile.base.widget.operation.OperationResultHead;
import com.boc.bocsoft.mobile.bocmobile.buss.common.menu.ModuleActivityDispatcher;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.common.draw.model.LoanDrawApplySubmitReq;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.common.loanmanagement.LoanManagerFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.EloanConst;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.utils.DataUtils;
import com.boc.bocsoft.mobile.common.utils.NumberUtils;
import com.boc.bocsoft.mobile.common.utils.StringUtils;


public class LoanDrawApplyResultsFragment extends BussFragment implements BaseOperationResultView.BtnCallback, OperationResultBottom.HomeBtnCallback {
    protected RelativeLayout bottom;
    private View mRoot;
    private OperationResultHead.Status status;
    private LoanDrawApplySubmitReq params;

    //结果页
    private BaseOperationResultView borvResult;

    private String repayCardNumber = null; //还款账户对应卡号
    private String remainAmount = null; //可用余额

    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        mRoot = mInflater.inflate(R.layout.fragment_eloan_draw_operatingresults, null);
        return mRoot;
    }

    public void setStatus(OperationResultHead.Status status) {
        this.status = status;
    }

    @Override
    protected void titleRightIconClick() {
        super.titleRightIconClick();
    }

    public void setRepayCardNumber(String value){
        repayCardNumber = value;
    }

    public void setRemainAmout(String value){
        remainAmount = value;
    }

    @Override
    protected String getTitleValue() {
        return getString(R.string.boc_eloan_draw_operatingresults_pagename);
    }

    @Override
    public void beforeInitView() {

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
    public void initView() {
//        ll_content = (LinearLayout) mRoot.findViewById(R.id.ll_content);
////        bottom = (RelativeLayout) mRoot.findViewById(R.id.bottom);
//        bt_backresult = (OperationResultBottom) mRoot.findViewById(R.id.bt_backresult);

        borvResult = (BaseOperationResultView) mContentView.findViewById(R.id.rv_result);
        borvResult.findViewById(R.id.txt_title).setVisibility(View.GONE);

    }

    @Override
    public void initData() {

        params= (LoanDrawApplySubmitReq) getArguments().getSerializable(EloanConst.ELON_DRAW_COMMIT);
        borvResult.updateHead(status,  getResources().getString(R.string.boc_result_status_transfer_success));

        borvResult.addDetailRow(getResources().getString(R.string.boc_pledge_result_use_money_amount_title), String.format("%s %s", DataUtils.getCurrencyDescByLetter(mContext,
                params.getCurrencyCode()), MoneyUtils.transMoneyFormat(params.getAmount(), params.getCurrencyCode())));
        if(!StringUtils.isEmptyOrNull(params.getLoanCycleToActNum()) &&  !params.getLoanCycleToActNum().equals("0")){
            borvResult.addDetailRow(getResources().getString(R.string.boc_pledge_info_payee_account),
                    NumberUtils.formatCardNumber(params.getLoanCycleToActNum()));
        }

        if(!StringUtils.isEmptyOrNull(remainAmount)){
            borvResult.addDetailRow(getString(R.string.boc_overview_item_amount_title_available),
                    String.format("%s %s", DataUtils.getCurrencyDescByLetter(mContext,
                            params.getCurrencyCode()), remainAmount));
        }

        borvResult.addDetailRow(getResources().getString(R.string.boc_repaydetails_interest),
                getResources().getString(R.string.boc_loan_n_month, params.getLoanPeriod()) +
                "/"+MoneyUtils.transRateFormat(params.getLoanRate()) + "%");

        if(!StringUtils.isEmptyOrNull(repayCardNumber) && !repayCardNumber.equals("0")){
            borvResult.addDetailRow(getString(R.string.boc_details_repayaccount), NumberUtils.formatCardNumber(repayCardNumber));
        }

    }

    @Override
    public void setListener() {
        borvResult.setOnclick(this);
        borvResult.setgoHomeOnclick(this);
    }

    @Override
    protected void titleLeftIconClick() {
        onClickBack();
    }

    @Override
    public boolean onBack() {
        onClickBack();
        return false;
    }

    /**
     * 返回事件处理
     */
    private void onClickBack(){
        if(getFragmentManager().findFragmentByTag(LoanManagerFragment.class.getName()) != null){
            popToAndReInit(LoanManagerFragment.class);
        }
        else{
            getActivity().finish();
        }
    }

    @Override
    public void onHomeBack() {
        ModuleActivityDispatcher.popToHomePage();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onClickListener(View v) {
    }

}
