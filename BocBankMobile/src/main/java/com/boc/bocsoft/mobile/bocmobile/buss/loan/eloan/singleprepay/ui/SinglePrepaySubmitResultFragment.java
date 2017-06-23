package com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.singleprepay.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import com.boc.bocsoft.mobile.bocmobile.ApplicationConst;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.BussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.utils.MoneyUtils;
import com.boc.bocsoft.mobile.bocmobile.base.widget.operation.BaseOperationResultView;
import com.boc.bocsoft.mobile.bocmobile.base.widget.operation.OperationResultBottom;
import com.boc.bocsoft.mobile.bocmobile.base.widget.operation.OperationResultHead;
import com.boc.bocsoft.mobile.bocmobile.buss.common.menu.ModuleActivityDispatcher;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.common.loanmanagement.LoanManagerFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.EloanConst;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.query.ui.EloanMoreOptionFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.query.ui.EloanRepayDetailFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.singleprepay.model.SinglePrepaySubmitRes;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.utils.DataUtils;
import com.boc.bocsoft.mobile.bocmobile.buss.system.home.ui.HomeFragment;
import com.boc.bocsoft.mobile.common.utils.NumberUtils;
import com.boc.bocsoft.mobile.common.utils.StringUtils;

import java.math.BigDecimal;
import java.util.LinkedHashMap;

/**
 * Created by qiangchen on 2016/7/25.
 */
public class SinglePrepaySubmitResultFragment extends BussFragment implements BaseOperationResultView.BtnCallback, OperationResultBottom.HomeBtnCallback {

    private View mRoot;
    private OperationResultHead.Status status;
    protected LinearLayout ll_content;
    protected OperationResultBottom bt_backresult;

    private OperationResultHead operationResultHead;
    //结果页
    private BaseOperationResultView borvResult;

    private SinglePrepaySubmitRes prepaySubmitRes;
    private String availableBalance = null; //卡内可用余额
    private String charges = null; //提前还款手续费
    private String currencyCode = null; //币种

    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        mRoot = mInflater.inflate(R.layout.fragment_eloan_prepay_results, null);
        return mRoot;
    }

    @Override
    public void initView() {
        borvResult = (BaseOperationResultView) mContentView.findViewById(R.id.rv_result);
        borvResult.findViewById(R.id.txt_title).setVisibility(View.GONE);
    }

    public void setStatus(OperationResultHead.Status status) {
        this.status = status;
    }

    @Override
    protected void titleRightIconClick() {
        super.titleRightIconClick();
    }

    @Override
    protected String getTitleValue() {
        return getString(R.string.boc_eloan_draw_operatingresults_pagename);
    }

    @Override
    public void beforeInitView() {
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
    public void initData() {
        prepaySubmitRes= (SinglePrepaySubmitRes) getArguments().getSerializable(EloanConst.ELON_PREPAY_COMMIT);
        charges = getArguments().getString(EloanConst.LOAN_PREPAY_CHARGES);
        availableBalance = getArguments().getString(EloanConst.LOAN_PREPAY_AVLAMOUNT);
        currencyCode = getArguments().getString(EloanConst.LOAN_PREPAY_CURRENCYCODE);

        if(status == OperationResultHead.Status.SUCCESS){
            borvResult.updateHead(status,  getResources().getString(R.string.boc_eloan_prepay_success));

            //总额： 本金、利息、手续费
            BigDecimal totalAmount = new BigDecimal(prepaySubmitRes.getAdvanceRepayCapital()).add(
                    new BigDecimal(prepaySubmitRes.getAdvanceRepayInterest()));
            BigDecimal bdRemainAmount = new BigDecimal(availableBalance).subtract(totalAmount);
            borvResult.addDetailRow(getResources().getString(R.string.boc_eloan_prepay_capital_interest),
                    MoneyUtils.transMoneyFormat(totalAmount, currencyCode));
            borvResult.addDetailRow(getResources().getString(R.string.boc_eloan_fee),
                    DataUtils.getFormatCharges(MoneyUtils.transMoneyFormat(charges, currencyCode)));
            borvResult.addDetailRow(getResources().getString(R.string.boc_eloan_prepay_repaymentAccount),
                    NumberUtils.formatCardNumber(prepaySubmitRes.getPayAccount()));
            //还款后剩余金额字段有歧义不显示，605中原意为本次还款之后还有多少钱要还，含利息。新接口未返回这个字段
//            borvResult.addDetailRow(getResources().getString(R.string.boc_eloan_prepayAccount2),
//                    MoneyUtils.transMoneyFormat(bdRemainAmount, currencyCode));
        }
        else{
            borvResult.updateHead(status,  getResources().getString(R.string.boc_eloan_prepay_fail));
            borvResult.setDetailsTitleIsShow(false);
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
    public void onHomeBack() {
        ModuleActivityDispatcher.popToHomePage();
    }

    @Override
    public boolean onBack() {
        onClickBack();
        return false;
    }

    /**
     * 点击返回
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
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onClickListener(View v) {

    }

}
