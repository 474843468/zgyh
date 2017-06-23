package com.boc.bocsoft.mobile.bocmobile.buss.loan.common.otherloanapply.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.boc.bocsoft.mobile.bocmobile.ApplicationConst;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.BussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.utils.MoneyUtils;
import com.boc.bocsoft.mobile.bocmobile.base.widget.operation.BaseOperationResultView;
import com.boc.bocsoft.mobile.bocmobile.base.widget.operation.OperationResultBottom;
import com.boc.bocsoft.mobile.bocmobile.base.widget.operation.OperationResultDetail;
import com.boc.bocsoft.mobile.bocmobile.base.widget.operation.OperationResultHead;
import com.boc.bocsoft.mobile.bocmobile.buss.common.menu.ModuleActivityDispatcher;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.common.loanmanagement.LoanManagerFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.common.otherloanapply.LoanApplyOtherConst;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.common.otherloanapply.model.OnLineLoanSubmitModel;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.common.otherloanapply.model.OnLineLoanSubmitResult;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.query.ui.EloanMoreOptionFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.query.ui.EloanRepayDetailFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.system.home.ui.HomeFragment;
import com.boc.bocsoft.mobile.common.utils.StringUtils;

/**
 * 使用{@link LoanApplyOtherResultFragment#newInstance}静态方法来创建该fragment的一个实例
 */
public class LoanApplyOtherResultFragment extends BussFragment implements BaseOperationResultView.BtnCallback, OperationResultBottom.HomeBtnCallback{

    private static final String PARAM = "param";
    private static final String PARAM2 = "param2";

    private View rootView;
    //结果页
    private BaseOperationResultView borvResult;

    private OnLineLoanSubmitResult mSubmitResult;
    private OnLineLoanSubmitModel mSubmitModel;

    /**
     * 使用该静态方法快速创建该fragment的一个实例，它接收了指定的参数
     *
     * @param submitResult 提交申请的结果
     * @return LoanApplyOtherResultFragment的一个实例
     */
    public static LoanApplyOtherResultFragment newInstance(OnLineLoanSubmitResult submitResult,
                                                           OnLineLoanSubmitModel mSubmitModel) {
        LoanApplyOtherResultFragment fragment = new LoanApplyOtherResultFragment();
        Bundle args = new Bundle();
        args.putParcelable(PARAM, submitResult);
        args.putParcelable(PARAM2, mSubmitModel);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * 初始化布局
     */
    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        rootView = mInflater.inflate(R.layout.boc_fragment_loan_apply_other_result, null);
        return rootView;
    }

    @Override
    public void initView() {
        borvResult = (BaseOperationResultView) mContentView.findViewById(R.id.rv_result);
        borvResult.findViewById(R.id.txt_title).setVisibility(View.GONE);
    }

    @Override
    public void initData() {
        if (getArguments() != null) {
            mSubmitResult = getArguments().getParcelable(PARAM);
            if (mSubmitResult != null) {
                if ("1".equals(mSubmitResult.getApplyResult())) {
                    borvResult.updateHead(OperationResultHead.Status.SUCCESS,
                            getString(R.string.boc_loan_other_apply_success));
                    mSubmitModel =  getArguments().getParcelable(PARAM2);
                    addDetailInfo();
                } else {
                    borvResult.updateHead(OperationResultHead.Status.FAIL,
                            getString(R.string.boc_submit_failed));
                }
            }
        }
    }

    private void addDetailInfo(){
        boolean isCustomer = false;
        if (!StringUtils.isEmpty(mSubmitModel.getAppName())) {
            isCustomer = true;
            StringBuilder show = new StringBuilder(mSubmitModel.getAppName());
            if (!StringUtils.isEmpty(mSubmitModel.getAppSex())) {
                show.append("(")
                        .append("1".equals(mSubmitModel.getAppSex()) ? "男" : "女")
                        .append(")");
            }
            borvResult.addDetailRow(getResources().getString(R.string.boc_loan_customer_name),
                    show.toString());
        }

        if (!StringUtils.isEmpty(mSubmitModel.getAppAge())) {
            isCustomer = true;
            borvResult.addDetailRow(getResources().getString(R.string.boc_loan_customer_age),
                    mSubmitModel.getAppAge());
        }

        if (!isCustomer) {
            if (!StringUtils.isEmpty(mSubmitModel.getEntName())) {
                borvResult.addDetailRow(getResources().getString(R.string.boc_loan_enterprise_name),
                        mSubmitModel.getEntName());
            }

            if (!StringUtils.isEmpty(mSubmitModel.getOfficeAddress())) {
                borvResult.addDetailRow(getResources().getString(R.string.boc_loan_enterprise_office_address),
                        mSubmitModel.getOfficeAddress());
            }
            if (!StringUtils.isEmpty(mSubmitModel.getMainBusiness())) {
                borvResult.addDetailRow(getResources().getString(R.string.boc_loan_enterprise_main_business),
                        mSubmitModel.getMainBusiness());
            }
            if (!StringUtils.isEmpty(mSubmitModel.getPrincipalName())) {
                borvResult.addDetailRow(getResources().getString(R.string.boc_loan_enterprise_principal_name),
                        mSubmitModel.getPrincipalName());
            }
        }

        if (!StringUtils.isEmpty(mSubmitModel.getAppPhone())) {
            borvResult.addDetailRow(getResources().getString(R.string.boc_loan_phone),
                    mSubmitModel.getAppPhone());
        }
        if (!StringUtils.isEmpty(mSubmitModel.getAppEmail())) {
            borvResult.addDetailRow(getResources().getString(R.string.boc_loan_email),
                    mSubmitModel.getAppEmail());
        }

        String currencyCode = ApplicationConst.CURRENCY_CNY;
        try{
            currencyCode = LoanApplyOtherConst.currencyCodeList.get(Integer.parseInt(mSubmitModel.getCurrency()) - 1);
        }catch (Exception e){
            e.printStackTrace();
        }
        if (!StringUtils.isEmpty(mSubmitModel.getTuitionTradePrice())) {
            borvResult.addDetailRow(getResources().getString(R.string.boc_loan_tuition_trade_price),
                    MoneyUtils.transMoneyFormat(mSubmitModel.getTuitionTradePrice(), currencyCode));
        }
        if (!StringUtils.isEmpty(mSubmitModel.getHouseTradePrice())) {
            borvResult.addDetailRow(getResources().getString(R.string.boc_loan_house_trade_price),
                    MoneyUtils.transMoneyFormat(mSubmitModel.getHouseTradePrice(), currencyCode));
        }
        if (!StringUtils.isEmpty(mSubmitModel.getHouseAge())) {
            borvResult.addDetailRow(getResources().getString(R.string.boc_loan_house_age2),
                    mSubmitModel.getHouseAge() + getString(R.string.boc_invest_treaty_period_year));
        }
        if (!StringUtils.isEmpty(mSubmitModel.getCarTradePrice())) {
            borvResult.addDetailRow(getResources().getString(R.string.boc_loan_car_price),
                    MoneyUtils.transMoneyFormat(mSubmitModel.getCarTradePrice(), currencyCode));
        }

        if (!StringUtils.isEmpty(mSubmitModel.getCurrency())) {
            borvResult.addDetailRow(getResources().getString(R.string.boc_loan_currency),
                    LoanApplyOtherConst.currencyList.get(
                            Integer.valueOf(mSubmitModel.getCurrency()) - 1));
        }

        if (!StringUtils.isEmpty(mSubmitModel.getLoanAmount())) {
            borvResult.addDetailRow(getResources().getString(R.string.boc_loan_amount),
                    MoneyUtils.transMoneyFormat(mSubmitModel.getLoanAmount(), currencyCode));
        }
        if (!StringUtils.isEmpty(mSubmitModel.getLoanTerm())) {
            borvResult.addDetailRow(getResources().getString(R.string.boc_loan_term),
                    mSubmitModel.getLoanTerm() + "月");
        }
        //提供抵押担保
        if (!StringUtils.isEmpty(mSubmitModel.getGuaTypeFlag())) {
            if ("1".equals(mSubmitModel.getGuaTypeFlag())) {
                borvResult.addDetailRow(getResources().getString(R.string.boc_loan_gua_type_flag),
                        "是");
                borvResult.addDetailRow(getResources().getString(R.string.boc_loan_gua_type),
                        LoanApplyOtherConst.guaTypeList.get(
                                Integer.valueOf(mSubmitModel.getGuaType()) - 1));
            } else {
                borvResult.addDetailRow(getResources().getString(R.string.boc_loan_gua_type_flag),
                        "否");
            }
        }
        if (!StringUtils.isEmpty(mSubmitModel.getGuaWay())) {
            borvResult.addDetailRow(getResources().getString(R.string.boc_loan_gua_way),
                    LoanApplyOtherConst.guawayList.get(
                            Integer.valueOf(mSubmitModel.getGuaWay()) - 1));
        }

        borvResult.addDetailRow(getResources().getString(R.string.boc_loan_branch),
                mSubmitModel.getDeptName());
        borvResult.addDetailRow(getResources().getString(R.string.boc_loan_branch_address),
                mSubmitModel.getDeptAddr());
    }

    @Override
    public void setListener() {
        borvResult.setOnclick(this);
        borvResult.setgoHomeOnclick(this);
    }

    @Override
    protected String getTitleValue() {
        return getResources().getString(R.string.boc_sure_result);
    }

    @Override
    protected boolean getTitleBarRed() {
        return false;
    }

    @Override
    public void onHomeBack() {
        ModuleActivityDispatcher.popToHomePage();
    }

    @Override
    protected boolean isDisplayRightIcon() {
        return false;
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
    public void onClickListener(View v) {

    }
}