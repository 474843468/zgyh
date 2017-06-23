package com.boc.bocsoft.mobile.bocmobile.buss.loan.common.otherloanapply.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnELOANBatchRepaySubmit.PsnELOANBatchRepaySubmitParams;
import com.boc.bocsoft.mobile.bocmobile.ApplicationConst;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.BussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.utils.MoneyUtils;
import com.boc.bocsoft.mobile.bocmobile.base.widget.confirmview.ConfirmInfoView;
import com.boc.bocsoft.mobile.bocmobile.base.widget.details.DetailTableRow;
import com.boc.bocsoft.mobile.bocmobile.base.widget.divider.HorizonalDivider;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.common.otherloanapply.LoanApplyOtherConst;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.common.otherloanapply.model.OnLineLoanSubmitModel;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.common.otherloanapply.model.OnLineLoanSubmitResult;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.common.otherloanapply.presenter.LoanApplyOtherConfirmationPresenter;
import com.boc.bocsoft.mobile.common.utils.StringUtils;

import java.util.LinkedHashMap;

/**
 * 使用{@link LoanApplyOtherConfirmationFragment#newInstance}静态方法来创建该fragment的一个实例
 */
public class LoanApplyOtherConfirmationFragment extends BussFragment
        implements LoanApplyOtherConfirmationContract.View{

    private static final String PARAM = "param";
//    protected LinearLayout lytInfoCustomer;
//    protected LinearLayout lytInfoEnterprise;
//    protected LinearLayout lytInfoLoan;
//    protected Button btnSubmit;
//    protected ScrollView viewApplyInfo;
//    private View rootView;

    //确认页面
    protected ConfirmInfoView confirmInfoView;

    private LoanApplyOtherConfirmationContract.Presenter mLoanApplyOtherConfirmationPresenter;
    private OnLineLoanSubmitModel mSubmitModel;
    private boolean isCustomer;

    /**
     * 使用该静态方法快速创建该fragment的一个实例，它接收了指定的参数
     *
     * @param submitModel 提交信息
     * @return LoanApplyOtherConfirmationFragment的一个实例
     */
    public static LoanApplyOtherConfirmationFragment newInstance(
            OnLineLoanSubmitModel submitModel) {
        LoanApplyOtherConfirmationFragment fragment = new LoanApplyOtherConfirmationFragment();
        Bundle args = new Bundle();
        args.putParcelable(PARAM, submitModel);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * 初始化布局
     */
    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        confirmInfoView = new ConfirmInfoView(mContext);
        return confirmInfoView;
    }

    @Override
    public void initView() {
        //初始化组件
        confirmInfoView.isShowSecurity(false); //隐藏安全组件
    }

    @Override
    public void initData() {
        if (getArguments() != null) {
            mSubmitModel = getArguments().getParcelable(PARAM);
            setupView();
        }
        mLoanApplyOtherConfirmationPresenter = new LoanApplyOtherConfirmationPresenter(this);
    }

    @Override
    public void setListener() {
        confirmInfoView.setListener(new ConfirmInfoView.OnClickListener(){
            @Override
            public void onClickConfirm() {
                showLoadingDialog(false);
                mLoanApplyOtherConfirmationPresenter.submit(mSubmitModel);
            }

            @Override
            public void onClickChange() {

            }
        });
    }

    private void setupView() {

        LinkedHashMap<String, String> datasCustomer = new LinkedHashMap<String, String>();
        LinkedHashMap<String, String> datasEnterprise = new LinkedHashMap<String, String>();
        LinkedHashMap<String, String> datasInfoLoan = new LinkedHashMap<String, String>();

        closeProgressDialog();
        if (!StringUtils.isEmpty(mSubmitModel.getAppName())) {
            isCustomer = true;
            StringBuilder show = new StringBuilder(mSubmitModel.getAppName());
            if (!StringUtils.isEmpty(mSubmitModel.getAppSex())) {
                show.append("(")
                    .append("1".equals(mSubmitModel.getAppSex()) ? "男" : "女")
                    .append(")");
            }
            datasCustomer.put(getString(R.string.boc_loan_customer_name), show.toString());
        }
        if (!StringUtils.isEmpty(mSubmitModel.getAppAge())) {
            isCustomer = true;
            datasCustomer.put(getString(R.string.boc_loan_customer_age), mSubmitModel.getAppAge());
        }


        if (!isCustomer) {
            if (!StringUtils.isEmpty(mSubmitModel.getEntName())) {
                datasEnterprise.put(getString(R.string.boc_loan_enterprise_name), mSubmitModel.getEntName());
            }

            if (!StringUtils.isEmpty(mSubmitModel.getOfficeAddress())) {
                datasEnterprise.put(getString(R.string.boc_loan_enterprise_office_address),
                        mSubmitModel.getOfficeAddress());
            }
            if (!StringUtils.isEmpty(mSubmitModel.getMainBusiness())) {
                datasEnterprise.put(getString(R.string.boc_loan_enterprise_main_business),
                        mSubmitModel.getMainBusiness());
            }
            if (!StringUtils.isEmpty(mSubmitModel.getPrincipalName())) {
                datasEnterprise.put(getString(R.string.boc_loan_enterprise_principal_name),
                        mSubmitModel.getPrincipalName());
            }
        }


        if (!StringUtils.isEmpty(mSubmitModel.getAppPhone())) {
            if(isCustomer){
                datasCustomer.put(getString(R.string.boc_loan_phone), mSubmitModel.getAppPhone());
            }
            else{
                datasEnterprise.put(getString(R.string.boc_loan_phone), mSubmitModel.getAppPhone());
            }
        }
        if (!StringUtils.isEmpty(mSubmitModel.getAppEmail())) {
            if(isCustomer){
                datasCustomer.put(getString(R.string.boc_loan_email), mSubmitModel.getAppEmail());
            }
            else{
                datasEnterprise.put(getString(R.string.boc_loan_email), mSubmitModel.getAppEmail());
            }
        }

        String currencyCode = ApplicationConst.CURRENCY_CNY;
        try{
            currencyCode = LoanApplyOtherConst.currencyCodeList.get(Integer.parseInt(mSubmitModel.getCurrency()) - 1);
        }catch (Exception e){
            e.printStackTrace();
        }

        if (!StringUtils.isEmpty(mSubmitModel.getCurrency())) {
            datasInfoLoan.put(getString(R.string.boc_loan_currency), LoanApplyOtherConst.currencyList.get(
                    Integer.valueOf(mSubmitModel.getCurrency()) - 1));
        }

        if (!StringUtils.isEmpty(mSubmitModel.getTuitionTradePrice())) {
            datasInfoLoan.put(getString(R.string.boc_loan_tuition_trade_price),
                    MoneyUtils.transMoneyFormat(mSubmitModel.getTuitionTradePrice(), currencyCode));
        }
        if (!StringUtils.isEmpty(mSubmitModel.getHouseTradePrice())) {
            datasInfoLoan.put(getString(R.string.boc_loan_house_trade_price),
                    MoneyUtils.transMoneyFormat(mSubmitModel.getHouseTradePrice(),currencyCode));
        }
        if (!StringUtils.isEmpty(mSubmitModel.getHouseAge())) {
            datasInfoLoan.put(getString(R.string.boc_loan_house_age2), mSubmitModel.getHouseAge() + getString(R.string.boc_invest_treaty_period_year));
        }
        if (!StringUtils.isEmpty(mSubmitModel.getCarTradePrice())) {
            datasInfoLoan.put(getString(R.string.boc_loan_car_price), MoneyUtils.transMoneyFormat(mSubmitModel.getCarTradePrice(), currencyCode));
        }

        if (!StringUtils.isEmpty(mSubmitModel.getLoanAmount())) {
            datasInfoLoan.put(getString(R.string.boc_loan_amount), MoneyUtils.transMoneyFormat(mSubmitModel.getLoanAmount(), currencyCode));
        }
        if (!StringUtils.isEmpty(mSubmitModel.getLoanTerm())) {
            datasInfoLoan.put(getString(R.string.boc_loan_term), mSubmitModel.getLoanTerm() + "月");
        }
        //提供抵押担保
        if (!StringUtils.isEmpty(mSubmitModel.getGuaTypeFlag())) {
            if ("1".equals(mSubmitModel.getGuaTypeFlag())) {
                datasInfoLoan.put(getString(R.string.boc_loan_gua_type_flag), "是");
                datasInfoLoan.put(getString(R.string.boc_loan_gua_type),
                        LoanApplyOtherConst.guaTypeList.get(
                                Integer.valueOf(mSubmitModel.getGuaType()) - 1));
            } else {
                datasInfoLoan.put(getString(R.string.boc_loan_gua_type_flag), "否");
            }
        }
        if (!StringUtils.isEmpty(mSubmitModel.getGuaWay())) {
            datasInfoLoan.put(getString(R.string.boc_loan_gua_way), LoanApplyOtherConst.guawayList.get(
                    Integer.valueOf(mSubmitModel.getGuaWay()) - 1));
        }

        datasInfoLoan.put(getString(R.string.boc_loan_branch), mSubmitModel.getDeptName());
        datasInfoLoan.put(getString(R.string.boc_loan_branch_address), mSubmitModel.getDeptAddr());


        if(isCustomer){
            confirmInfoView.addData(datasCustomer);
        }
        else{
            confirmInfoView.addData(datasEnterprise);
        }
        confirmInfoView.addData(datasInfoLoan);
    }

    private void addRowView(String title, String value, LinearLayout lyt) {
        DetailTableRow tableRow = new DetailTableRow(mContext);
        tableRow.updateData(title, value);
        tableRow.setRowLineVisable(false);
        lyt.addView(tableRow);
        lyt.addView(new HorizonalDivider(mContext));
    }

    @Override
    protected String getTitleValue() {
        return getString(R.string.boc_fragment_confirm_title);
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
    public void onDestroy() {
        mLoanApplyOtherConfirmationPresenter.unsubscribe();
        super.onDestroy();
    }

    @Override
    public void onSubmitSuccess(OnLineLoanSubmitResult submitResult) {
        closeProgressDialog();
        start(LoanApplyOtherResultFragment.newInstance(submitResult, mSubmitModel));
    }

}