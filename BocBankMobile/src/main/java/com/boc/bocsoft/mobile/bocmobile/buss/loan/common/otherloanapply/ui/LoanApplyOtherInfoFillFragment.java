package com.boc.bocsoft.mobile.bocmobile.buss.loan.common.otherloanapply.ui;

import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Selection;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.boc.bocsoft.mobile.bocmobile.ApplicationConst;
import com.boc.bocsoft.mobile.bocmobile.ApplicationContext;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.BussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.widget.details.DetailTableRowCheckBox;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.listselectorview.SelectListDialog;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.listselectorview.SelectStringListDialog;
import com.boc.bocsoft.mobile.bocmobile.base.widget.divider.HorizonalDivider;
import com.boc.bocsoft.mobile.bocmobile.base.widget.edittext.EditChoiceWidget;
import com.boc.bocsoft.mobile.bocmobile.base.widget.edittext.EditClearWidget;
import com.boc.bocsoft.mobile.bocmobile.base.widget.edittext.EditMoneyInputWidget;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.common.otherloanapply.LoanApplyOtherConst;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.common.otherloanapply.model.OnLineLoanBranchBean;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.common.otherloanapply.model.OnLineLoanFieldBean;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.common.otherloanapply.model.OnLineLoanProductBean;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.common.otherloanapply.model.OnLineLoanSubmitModel;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.common.otherloanapply.presenter.LoanApplyOtherInfoFillPresenter;
import com.boc.bocsoft.mobile.common.utils.StringUtils;
import com.boc.bocsoft.mobile.framework.utils.PublicConst;
import com.boc.bocsoft.mobile.framework.utils.regex.RegexResult;
import com.boc.bocsoft.mobile.framework.utils.regex.RegexUtils;

import java.math.BigDecimal;

public class LoanApplyOtherInfoFillFragment extends BussFragment
        implements LoanApplyOtherInfoFillContract.View, View.OnClickListener,
        CompoundButton.OnCheckedChangeListener {

    protected LinearLayout lytInfoCustomer;
    protected LinearLayout lytInfoEnterprise;
    protected LinearLayout lytInfoLoan;
    protected Button btnNext;
    protected ScrollView viewApplyInfo;
    private View rootView;
    private EditChoiceWidget customerNameView; //姓名
    private EditClearWidget customerAgeView; //年龄
    private EditClearWidget enterpriseNameView; //企业名称
    private EditClearWidget officeAddressView; //办公地址
    private EditClearWidget mainBusinessView; //主营业务
    private EditClearWidget principalNameView; //负责人姓名
    private EditClearWidget phoneView; //联系电话
    private EditClearWidget emailView; //email
    private EditMoneyInputWidget tuitionView; //学费生活费总额
    private EditMoneyInputWidget housePriceView; //房屋交易价
    private EditClearWidget houseAgeView; //房龄
    private EditMoneyInputWidget carPriceView; //净车价
    private EditChoiceWidget currencyView; //贷款币种
    private EditMoneyInputWidget loanAmountView; //贷款金额
    private EditClearWidget loanTermView;  //贷款期限
    private DetailTableRowCheckBox guaTypeFlagView; //担保方式
    private EditChoiceWidget guaWayView, guaTypeView; //担保方式, 担保类别
    private HorizonalDivider guaTypeViewDivider;
    private EditChoiceWidget bankBranchView; //业务办理网点
    private EditChoiceWidget bankBranchAddressView; //网点地址
    private SelectStringListDialog mCurrencyDialog, mGuaWayDialog, mGuaTypeDialog;

    private LoanApplyOtherInfoFillPresenter mLoanApplyOtherInfoFillPresenter;
    private String mCityCode;
    private OnLineLoanProductBean mProductBean;
    private boolean isCustomer;
    private OnLineLoanFieldBean mOnLineLoanFieldBean;
    private OnLineLoanSubmitModel mOnLineLoanSubmitModel;
    private String mCurrency, mGuaway, mGuaType, mDeptId;

    private static final String PARAM_CITY = "param_city";
    private static final String PARAM_PRODUCT = "param_product";
    private static final int REQUEST_BRANCH = 1;

    public static LoanApplyOtherInfoFillFragment newInstance(String cityCode,
            OnLineLoanProductBean productBean) {
        LoanApplyOtherInfoFillFragment fragment = new LoanApplyOtherInfoFillFragment();
        Bundle args = new Bundle();
        args.putString(PARAM_CITY, cityCode);
        args.putParcelable(PARAM_PRODUCT, productBean);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * 初始化布局
     */
    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        rootView = mInflater.inflate(R.layout.boc_fragment_loan_apply_other_info_fill, null);
        return rootView;
    }

    @Override
    public void initView() {
        lytInfoCustomer = (LinearLayout) rootView.findViewById(R.id.lyt_info_customer);
        lytInfoEnterprise = (LinearLayout) rootView.findViewById(R.id.lyt_info_enterprise);
        lytInfoLoan = (LinearLayout) rootView.findViewById(R.id.lyt_info_loan);
        btnNext = (Button) rootView.findViewById(R.id.btn_next);
        btnNext.setOnClickListener(LoanApplyOtherInfoFillFragment.this);
        viewApplyInfo = (ScrollView) rootView.findViewById(R.id.view_apply_info);
        viewApplyInfo.setVisibility(View.INVISIBLE);
    }

    @Override
    public void initData() {
        mLoanApplyOtherInfoFillPresenter = new LoanApplyOtherInfoFillPresenter(this);
        if (getArguments() != null) {
            mCityCode = getArguments().getString(PARAM_CITY);
            mProductBean = getArguments().getParcelable(PARAM_PRODUCT);
            mOnLineLoanSubmitModel = new OnLineLoanSubmitModel();
            mOnLineLoanSubmitModel.setCityCode(mCityCode);
            mOnLineLoanSubmitModel.setProductCode(mProductBean.getProductCode());
            mOnLineLoanSubmitModel.setProductName(mProductBean.getProductName());
            showLoadingDialog();
            mLoanApplyOtherInfoFillPresenter.getOnLineLoanFieldQry(mProductBean.getProductCode());
        }
    }

    @Override
    public void setListener() {
        btnNext.setOnClickListener(LoanApplyOtherInfoFillFragment.this);
    }

    @Override
    protected String getTitleValue() {
        return getString(R.string.boc_eloan_apply_pagename2);
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
        mLoanApplyOtherInfoFillPresenter.unsubscribe();
        super.onDestroy();
    }

    /**
     *
     * @param bean
     */
    @Override
    public void onLoadOnLineLoanFieldSuccess(OnLineLoanFieldBean bean) {
        mOnLineLoanFieldBean = bean;
        viewApplyInfo.setVisibility(View.VISIBLE);
        closeProgressDialog();
        if (isNeeded(bean.getAppNameNeed())) {
            isCustomer = true;
            StringBuilder show =
                    new StringBuilder(ApplicationContext.getInstance().getUser().getCustomerName());
            if (isNeeded(bean.getAppSexNeed())) {
                show.append("(")
                    .append("1".equals(ApplicationContext.getInstance().getUser().getGender()) ? "男"
                            : "女")
                    .append(")");
            }
            customerNameView = new EditChoiceWidget(mContext);
            customerNameView.setChoiceTitleBold(true);
            customerNameView.setChoiceTextName(getString(R.string.boc_loan_customer_name));
            customerNameView.setChoiceTextContent(show.toString());
            customerNameView.setArrowImageGone(false);
            lytInfoCustomer.addView(customerNameView);
            lytInfoCustomer.addView(new HorizonalDivider(mContext));
        }
        if (isNeeded(bean.getAppAgeNeed())) {
            isCustomer = true;
            customerAgeView = new EditClearWidget(mContext);
            customerAgeView.getContentEditText().setInputType(InputType.TYPE_CLASS_NUMBER);
            customerAgeView.setChoiceTitleBold(true);
            customerAgeView.getContentEditText()
                           .setFilters(new InputFilter[] { new InputFilter.LengthFilter(2) });
            customerAgeView.setEditWidgetTitle(getString(R.string.boc_loan_customer_age));
            lytInfoCustomer.addView(customerAgeView);
            lytInfoCustomer.addView(new HorizonalDivider(mContext));
        }

        (isCustomer ? lytInfoCustomer : lytInfoEnterprise).setVisibility(View.VISIBLE);
        (isCustomer ? lytInfoEnterprise : lytInfoCustomer).setVisibility(View.GONE);

        if (!isCustomer) {
            if (isNeeded(bean.getEntNameNeed())) {
                enterpriseNameView = new EditClearWidget(mContext);
                enterpriseNameView.setEditWidgetTitle(getString(R.string.boc_loan_enterprise_name));
                enterpriseNameView.setChoiceTitleBold(true);
                lytInfoEnterprise.addView(enterpriseNameView);
                lytInfoEnterprise.addView(new HorizonalDivider(mContext));
            }

            if (isNeeded(bean.getOfficeAddressNeed())) {
                officeAddressView = new EditClearWidget(mContext);
                officeAddressView.setEditWidgetTitle(
                        getString(R.string.boc_loan_enterprise_office_address));
                officeAddressView.setChoiceTitleBold(true);
                lytInfoEnterprise.addView(officeAddressView);
                lytInfoEnterprise.addView(new HorizonalDivider(mContext));
            }
            if (isNeeded(bean.getMainBusinessNeed())) {
                mainBusinessView = new EditClearWidget(mContext);
                mainBusinessView.setChoiceTitleBold(true);
                mainBusinessView.setEditWidgetTitle(
                        getString(R.string.boc_loan_enterprise_main_business));
                lytInfoEnterprise.addView(mainBusinessView);
                lytInfoEnterprise.addView(new HorizonalDivider(mContext));
            }
            if (isNeeded(bean.getPrincipalNameNeed())) {
                principalNameView = new EditClearWidget(mContext);
                principalNameView.setChoiceTitleBold(true);
                principalNameView.setEditWidgetTitle(
                        getString(R.string.boc_loan_enterprise_principal_name));
                lytInfoEnterprise.addView(principalNameView);
                lytInfoEnterprise.addView(new HorizonalDivider(mContext));
            }
        }

        if (isNeeded(bean.getAppPhoneNeed())) {
            phoneView = new EditClearWidget(mContext);
            phoneView.setChoiceTitleBold(true);
            phoneView.setEditWidgetTitle(getString(R.string.boc_loan_phone));
            phoneView.getContentEditText()
                     .setInputType(InputType.TYPE_CLASS_NUMBER);
            phoneView.getContentEditText()
                     .setFilters(new InputFilter[] { new InputFilter.LengthFilter(15) });
            (isCustomer ? lytInfoCustomer : lytInfoEnterprise).addView(phoneView);
            (isCustomer ? lytInfoCustomer : lytInfoEnterprise).addView(
                    new HorizonalDivider(mContext));
        }
        if (isNeeded(bean.getAppEmailNeed())) {
            emailView = new EditClearWidget(mContext);
            emailView.setChoiceTitleBold(true);
            emailView.setEditWidgetTitle(getString(R.string.boc_loan_email));
            emailView.getContentEditText()
                     .setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
            (isCustomer ? lytInfoCustomer : lytInfoEnterprise).addView(emailView);
            (isCustomer ? lytInfoCustomer : lytInfoEnterprise).addView(
                    new HorizonalDivider(mContext));
        }
        if (lytInfoCustomer.getChildCount() > 1) {
            lytInfoCustomer.removeViewAt(lytInfoCustomer.getChildCount() - 1);
        }
        if (lytInfoEnterprise.getChildCount() > 1) {
            lytInfoEnterprise.removeViewAt(lytInfoEnterprise.getChildCount() - 1);
        }

        if (isNeeded(bean.getCurrencyNeed())) {
            currencyView = new EditChoiceWidget(mContext);
            currencyView.setChoiceTitleBold(true);
            currencyView.setChoiceTextName(getString(R.string.boc_loan_currency));
            currencyView.setChoiceTextContent(getString(R.string.boc_common_select));
            currencyView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectCurrency();
                }
            });
            lytInfoLoan.addView(currencyView);
            lytInfoLoan.addView(new HorizonalDivider(mContext));
        }

        if (isNeeded(bean.getTuitionTradePriceNeed())) {
            tuitionView = new EditMoneyInputWidget(mContext);
            tuitionView.setMaxLeftNumber(8);
            tuitionView.setScrollView(rootView);
            tuitionView.getTitleTextView().getPaint().setFakeBoldText(true);
            tuitionView.setEditWidgetTitle(getString(R.string.boc_loan_tuition_trade_price));
            lytInfoLoan.addView(tuitionView);
            lytInfoLoan.addView(new HorizonalDivider(mContext));
        }
        if (isNeeded(bean.getHouseTradePriceNeed())) {
            housePriceView = new EditMoneyInputWidget(mContext);
            housePriceView.setScrollView(rootView);
            housePriceView.setMaxLeftNumber(8);
            housePriceView.getTitleTextView().getPaint().setFakeBoldText(true);
            housePriceView.setEditWidgetTitle(getString(R.string.boc_loan_house_trade_price));
            lytInfoLoan.addView(housePriceView);
            lytInfoLoan.addView(new HorizonalDivider(mContext));
        }
        if (isNeeded(bean.getHouseAgeNeed())) {
            houseAgeView = new EditClearWidget(mContext);
            houseAgeView.setChoiceTitleBold(true);
            houseAgeView.setEditWidgetTitle(getString(R.string.boc_loan_house_age2));
            houseAgeView.getContentEditText().setClearIconVisible(false);
            houseAgeView.getContentEditText().setInputType(InputType.TYPE_CLASS_NUMBER);
            houseAgeView.setClearEditRightTextView("年");
            lytInfoLoan.addView(houseAgeView);
            lytInfoLoan.addView(new HorizonalDivider(mContext));
        }
        if (isNeeded(bean.getCarTradePriceNeed())) {
            carPriceView = new EditMoneyInputWidget(mContext);
            carPriceView.setScrollView(rootView);
            carPriceView.setMaxLeftNumber(8);
            carPriceView.getTitleTextView().getPaint().setFakeBoldText(true);
            carPriceView.setEditWidgetTitle(getString(R.string.boc_loan_car_price));
            lytInfoLoan.addView(carPriceView);
            lytInfoLoan.addView(new HorizonalDivider(mContext));
        }

        if (isNeeded(bean.getLoanAmountNeed())) {
            loanAmountView = new EditMoneyInputWidget(mContext);
            loanAmountView.setScrollView(rootView);
            loanAmountView.setMaxLeftNumber(8);
            loanAmountView.getTitleTextView().getPaint().setFakeBoldText(true);
            loanAmountView.getContentMoneyEditText()
                          .setTextColor(getResources().getColor(R.color.boc_text_color_red));
            loanAmountView.setEditWidgetTitle(getString(R.string.boc_loan_amount));
            lytInfoLoan.addView(loanAmountView);
            lytInfoLoan.addView(new HorizonalDivider(mContext));
        }

        if (isNeeded(bean.getLoanTermNeed())) {
            loanTermView = new EditClearWidget(mContext);
            loanTermView.setChoiceTitleBold(true);
            loanTermView.setEditWidgetTitle(getString(R.string.boc_loan_term));
            loanTermView.getContentEditText().setClearIconVisible(false);
            loanTermView.getContentEditText().setInputType(InputType.TYPE_CLASS_NUMBER);
            loanTermView.setClearEditRightTextView("月");
            lytInfoLoan.addView(loanTermView);
            lytInfoLoan.addView(new HorizonalDivider(mContext));
        }
        //提供抵押担保
        if (isNeeded(bean.getGuaTypeFlagNeed())) {
            guaTypeFlagView = new DetailTableRowCheckBox(mContext);
            guaTypeFlagView.setTitleBold(true);
            guaTypeFlagView.setTitle(getString(R.string.boc_loan_gua_type_flag));
            lytInfoLoan.addView(guaTypeFlagView);
            lytInfoLoan.addView(new HorizonalDivider(mContext));
            guaTypeView = new EditChoiceWidget(mContext);
            guaTypeView.setChoiceTitleBold(true);
            guaTypeView.setChoiceTextName(getString(R.string.boc_loan_gua_type));
            guaTypeView.setChoiceTextContent(getString(R.string.boc_common_select));
            guaTypeView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectGuaType();
                }
            });
            lytInfoLoan.addView(guaTypeView);
            guaTypeViewDivider = new HorizonalDivider(mContext);
            lytInfoLoan.addView(guaTypeViewDivider);
            guaTypeFlagView.setOnCheckedChangeListener(this);
            guaTypeView.setVisibility(View.GONE);
            guaTypeViewDivider.setVisibility(View.GONE);
        }

        if (isNeeded(bean.getGuaWayNeed())) {
            guaWayView = new EditChoiceWidget(mContext);
            guaWayView.setChoiceTitleBold(true);
            guaWayView.setChoiceTextName(getString(R.string.boc_loan_gua_way));
            guaWayView.setChoiceTextContent(getString(R.string.boc_common_select));
            guaWayView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectGuaWay();
                }
            });
            lytInfoLoan.addView(guaWayView);
            lytInfoLoan.addView(new HorizonalDivider(mContext));
        }

        bankBranchView = new EditChoiceWidget(mContext);
        bankBranchView.setChoiceTitleBold(true);
        bankBranchView.setChoiceTextContent(getString(R.string.boc_common_select));
        bankBranchView.setChoiceTextName(getString(R.string.boc_loan_branch));
        bankBranchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectBankBranch();
            }
        });
        lytInfoLoan.addView(bankBranchView);
        lytInfoLoan.addView(new HorizonalDivider(mContext));

        bankBranchAddressView = new EditChoiceWidget(mContext);
        bankBranchAddressView.setChoiceTitleBold(true);
        bankBranchAddressView.setChoiceTextName(getString(R.string.boc_loan_branch_address));
        bankBranchAddressView.setArrowImageGone(false);
        bankBranchAddressView.setVisibility(View.GONE);
        lytInfoLoan.addView(bankBranchAddressView);
    }

    private void selectCurrency() {
        //弹出对话框选择币种
        if (mCurrencyDialog == null) {
            mCurrencyDialog = new SelectStringListDialog(mContext);
            mCurrencyDialog.setListData(LoanApplyOtherConst.currencyList);
            mCurrencyDialog.setOnSelectListener(new SelectListDialog.OnSelectListener<String>() {
                @Override
                public void onSelect(int position, String model) {
                    currencyView.setChoiceTextContent(model);
                    //币种更换，则清空金额
                    if(StringUtils.isEmptyOrNull(mCurrency) ||
                            !mCurrency.equals(LoanApplyOtherConst.currencyCodeList.get(position))){
                        loanAmountView.getContentMoneyEditText().clearText();
                        tuitionView.getContentMoneyEditText().clearText();
                    }
                    mCurrency = LoanApplyOtherConst.currencyCodeList.get(position);
                    mOnLineLoanSubmitModel.setCurrency(String.valueOf(position + 1));
                    loanAmountView.setCurrency(mCurrency);
                    tuitionView.setCurrency(mCurrency);
                    mCurrencyDialog.dismiss();
                }
            });
        }
        mCurrencyDialog.show();
    }

    /**
     * 担保方式
     */
    private void selectGuaWay() {
        if (mGuaWayDialog == null) {
            mGuaWayDialog = new SelectStringListDialog(mContext);
            mGuaWayDialog.setListData(LoanApplyOtherConst.guawayList);
            mGuaWayDialog.setOnSelectListener(new SelectListDialog.OnSelectListener<String>() {
                @Override
                public void onSelect(int position, String model) {
                    guaWayView.setChoiceTextContent(model);
                    mGuaway = String.valueOf(position + 1);
                    mOnLineLoanSubmitModel.setGuaWay(mGuaway);
                    mGuaWayDialog.dismiss();
                }
            });
        }
        mGuaWayDialog.show();
    }

    private void selectGuaType() {
        if (mGuaTypeDialog == null) {
            mGuaTypeDialog = new SelectStringListDialog(mContext);
            mGuaTypeDialog.setListData(LoanApplyOtherConst.guaTypeList);
            mGuaTypeDialog.setOnSelectListener(new SelectListDialog.OnSelectListener<String>() {
                @Override
                public void onSelect(int position, String model) {
                    guaTypeView.setChoiceTextContent(model);
                    mGuaType = String.valueOf(position + 1);
                    mOnLineLoanSubmitModel.setGuaType(mGuaType);
                    mGuaTypeDialog.dismiss();
                }
            });
        }
        mGuaTypeDialog.show();
    }

    /**
     * 选择网点
     */

    private void selectBankBranch() {
        String refresh = StringUtils.isEmpty(mOnLineLoanSubmitModel.getDeptID()) ? "true" : "false";
        //跳转到网点列表
        startForResult(LoanApplyOtherBranchSelectFragment.newInstance(mCityCode,
                mProductBean.getProductCode(), refresh), REQUEST_BRANCH);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_next) {
            goToLoanApplyFragment();
        }
    }


    //TODO 跳转到贷款申请页面
    //TODO 校验规则
    private void goToLoanApplyFragment() {
        if (isCustomer) {
            if (isNeeded(mOnLineLoanFieldBean.getAppNameNeed())) {

                if(!isValidValue("mbdi_lm_eloan_name", customerNameView.getChoiceTextContent())){
                    return;
                }
                mOnLineLoanSubmitModel.setAppName(
                        ApplicationContext.getInstance().getUser().getCustomerName());
            }
            if (isNeeded(mOnLineLoanFieldBean.getAppSexNeed())) {
                mOnLineLoanSubmitModel.setAppSex(
                        ApplicationContext.getInstance().getUser().getGender());
            }
            if (isNeeded(mOnLineLoanFieldBean.getAppAgeNeed())) {
                if (StringUtils.isEmpty(customerAgeView.getEditWidgetContent())){
                    showErrorDialog(getString(R.string.boc_loan_input_name));
                    return;
                }
                else if(Integer.parseInt(customerAgeView.getEditWidgetContent()) < 18
                        || Integer.parseInt(customerAgeView.getEditWidgetContent()) >= 65) {
                    showErrorDialog(getString(R.string.boc_loan_customer_age_empty));
                    return;
                }

                mOnLineLoanSubmitModel.setAppAge(
                        customerAgeView.getContentEditText().getText().toString());
            }
        }
        if (!isCustomer) {
            if (isNeeded(mOnLineLoanFieldBean.getEntNameNeed())) {
                if(!isValidValue(enterpriseNameView.getEditWidgetContent(),
                        getString(R.string.boc_loan_enterprise_name_empty),
                        60,
                        getString(R.string.boc_loan_ent_name_max))){
                    return;
                }

                mOnLineLoanSubmitModel.setEntName(enterpriseNameView.getEditWidgetContent());
            }

            if (isNeeded(mOnLineLoanFieldBean.getOfficeAddressNeed())) {
                if(!isValidValue(officeAddressView.getEditWidgetContent(),
                        getString(R.string.boc_loan_enterprise_office_address_empty),
                        100,
                        getString(R.string.boc_loan_office_name_max))){
                    return;
                }
                mOnLineLoanSubmitModel.setOfficeAddress(officeAddressView.getEditWidgetContent());
            }

            if (isNeeded(mOnLineLoanFieldBean.getMainBusinessNeed())) {
                if(!isValidValue(mainBusinessView.getEditWidgetContent(),
                        getString(R.string.boc_loan_enterprise_main_business_empty),
                        500,
                        getString(R.string.boc_loan_main_job_name_max))){
                    return;
                }

                mOnLineLoanSubmitModel.setMainBusiness(mainBusinessView.getEditWidgetContent());
            }

            if (isNeeded(mOnLineLoanFieldBean.getPrincipalNameNeed())) {
                if(!isValidValue(principalNameView.getEditWidgetContent(),
                        getString(R.string.boc_loan_enterprise_principal_name_empty),
                        30,
                        getString(R.string.boc_loan_prin_name_max))){
                    return;
                }
                mOnLineLoanSubmitModel.setPrincipalName(principalNameView.getEditWidgetContent());
            }
        }

        if (isNeeded(mOnLineLoanFieldBean.getAppPhoneNeed())) {
            if (StringUtils.isEmptyOrNull(phoneView.getEditWidgetContent())) {
                showErrorDialog(getString(R.string.boc_loan_phone_empty));
                return;
            }

            int length = phoneView.getEditWidgetContent().length();
            if(length < 11 || length > 15){
                showErrorDialog(getString(R.string.boc_loan_phone_error));
                return;
            }
            mOnLineLoanSubmitModel.setAppPhone(phoneView.getEditWidgetContent());
        }

        if (isNeeded(mOnLineLoanFieldBean.getAppEmailNeed())) {
            if (!isValidValue("mbdi_loan_otherqry_email", emailView.getEditWidgetContent())) {
                return;
            }
            mOnLineLoanSubmitModel.setAppEmail(emailView.getEditWidgetContent());
        }

        if (isNeeded(mOnLineLoanFieldBean.getTuitionTradePriceNeed())) {
            if(!StringUtils.isEmptyOrNull(mCurrency) && mCurrency.equals(ApplicationConst.CURRENCY_JPY)){
                if(!isValidNumber(tuitionView.getContentMoney(),
                        getString(R.string.boc_loan_tuition_trade_price_empty),
                        100000000.00f,
                        getString(R.string.boc_loan_amount_jpy))){
                    return;
                }
            }
            else{
                if(!isValidNumber(tuitionView.getContentMoney(),
                        getString(R.string.boc_loan_tuition_trade_price_empty),
                        100000000.00f,
                        getString(R.string.boc_loan_amount_common))){
                    return;
                }
            }
            mOnLineLoanSubmitModel.setTuitionTradePrice(tuitionView.getContentMoney());
        }

        if (isNeeded(mOnLineLoanFieldBean.getHouseTradePriceNeed())) {
            if(!isValidNumber(housePriceView.getContentMoney(),
                    getString(R.string.boc_loan_house_trade_price_empty),
                    100000000.00f,
                    getString(R.string.boc_loan_amount_common))){
                return;
            }

            mOnLineLoanSubmitModel.setHouseTradePrice(housePriceView.getContentMoney());
        }
        if (isNeeded(mOnLineLoanFieldBean.getHouseAgeNeed())) {
            if (StringUtils.isEmpty(houseAgeView.getEditWidgetContent())){
                showErrorDialog(getString(R.string.boc_loan_house_age_empty));
                return;
            }
            else if(Integer.parseInt(houseAgeView.getEditWidgetContent()) <= 0
                    || Integer.parseInt(houseAgeView.getEditWidgetContent())>=100) {
                showErrorDialog(getString(R.string.boc_loan_house_age_max));
                return;
            }

            mOnLineLoanSubmitModel.setHouseAge(houseAgeView.getEditWidgetContent());
        }
        if (isNeeded(mOnLineLoanFieldBean.getCarTradePriceNeed())) {
            if(!isValidNumber(carPriceView.getContentMoney(),
                    getString(R.string.boc_loan_car_price_empty),
                    100000000.00f,
                    getString(R.string.boc_loan_amount_common))){
                return;
            }

            mOnLineLoanSubmitModel.setCarTradePrice(carPriceView.getContentMoney());
        }

        if(isNeeded(mOnLineLoanFieldBean.getCurrencyNeed())){
            if(currencyView.getChoiceTextContent() == null ||
                    currencyView.getChoiceTextContent().equals(getString(R.string.boc_common_select))){
                getString(R.string.boc_loan_select_currency);
                return;
            }
        }

        if (isNeeded(mOnLineLoanFieldBean.getLoanAmountNeed())) {

            if(!StringUtils.isEmptyOrNull(mCurrency) && mCurrency.equals(ApplicationConst.CURRENCY_JPY)){
                if(!isValidNumber(loanAmountView.getContentMoney(),
                        getString(R.string.boc_loan_amount_empty),
                        100000000.00f,
                        getString(R.string.boc_loan_amount_jpy))){
                    return;
                }
            }
            else{
                if(!isValidNumber(loanAmountView.getContentMoney(),
                        getString(R.string.boc_loan_amount_empty),
                        100000000.00f,
                        getString(R.string.boc_loan_amount_common))){
                    return;
                }
            }

            mOnLineLoanSubmitModel.setLoanAmount(loanAmountView.getContentMoney());
        }

        if (isNeeded(mOnLineLoanFieldBean.getLoanTermNeed())) {
            if(!isValidNumber(loanTermView.getEditWidgetContent(),
                    getString(R.string.boc_loan_term_empty),
                    1000.00f,
                    getString(R.string.boc_loan_term_max))){
                return;
            }

            mOnLineLoanSubmitModel.setLoanTerm(loanTermView.getEditWidgetContent());
        }

        if (isNeeded(mOnLineLoanFieldBean.getGuaTypeFlagNeed())) {
            if (guaTypeFlagView.isChecked()) {
                mOnLineLoanSubmitModel.setGuaTypeFlag("1");
                mOnLineLoanSubmitModel.setGuaType(mGuaType);

                if(StringUtils.isEmpty(guaTypeView.getChoiceTextContent()) ||
                        guaTypeView.getChoiceTextContent().equals(getString(R.string.boc_common_select))){
                    showErrorDialog(getString(R.string.boc_loan_gua_type_empty));
                    return;
                }

            } else {
                mOnLineLoanSubmitModel.setGuaTypeFlag("2");
            }
        }

        if (isNeeded(mOnLineLoanFieldBean.getGuaWayNeed())) {
            if(StringUtils.isEmpty(guaWayView.getChoiceTextContent()) ||
                    guaWayView.getChoiceTextContent().equals(getString(R.string.boc_common_select))){
                showErrorDialog(getString(R.string.boc_loan_gua_way_empty));
                return;
            }
            mOnLineLoanSubmitModel.setGuaWay(mGuaway);
        }

        if(StringUtils.isEmpty(bankBranchView.getChoiceTextContent()) ||
                bankBranchView.getChoiceTextContent().equals(getString(R.string.boc_common_select))){
            showErrorDialog(getString(R.string.boc_loan_bank_branch_empty));
            return;
        }

        if(!isValidAgeAndHouseAge()){
            showErrorDialog(getString(R.string.boc_loan_age_loanperiod_limit));
            return;
        }

        if(!isValideHouseOrCarAmount()){
            showErrorDialog(getString(R.string.boc_loan_amount_housecar_limit));
            return;
        }

        if(!isValideForeignTuition()){
            showErrorDialog(getString(R.string.boc_loan_amount_foreigntuition_limit));
            return;
        }

        start(LoanApplyOtherConfirmationFragment.newInstance(mOnLineLoanSubmitModel));
    }

    /**
     * 年龄 + 贷款年限 < 65
     * @return
     */
    private boolean isValidAgeAndHouseAge(){
        if (isNeeded(mOnLineLoanFieldBean.getAppAgeNeed()) && isNeeded(mOnLineLoanFieldBean.getLoanTermNeed())) {
            try{
                int age = Integer.parseInt(customerAgeView.getEditWidgetContent());
                int loanTermMonth = Integer.parseInt(loanTermView.getEditWidgetContent());
                double loanTerYear = loanTermMonth / 12.0;
                return (age + loanTerYear <= 65.0);
            }catch (Exception e){
                e.printStackTrace();
                return true;
            }

        }
        return true;
    }

    /**
     * 住房贷款和汽车贷款时，贷款金额不能超过交易价格的70%；
     * @return
     */
    private boolean isValideHouseOrCarAmount(){
        try{
            if(StringUtils.isEmptyOrNull(loanAmountView.getContentMoney())){
                return true;
            }
            BigDecimal bdLoanAmount = new BigDecimal(loanAmountView.getContentMoney());
            if (isNeeded(mOnLineLoanFieldBean.getHouseTradePriceNeed())) {
                return bdLoanAmount.compareTo((new BigDecimal(housePriceView.getContentMoney()).multiply(new BigDecimal(0.7)))) <= 0;
            }

            if(isNeeded(mOnLineLoanFieldBean.getCarTradePriceNeed())){
                return bdLoanAmount.compareTo((new BigDecimal(carPriceView.getContentMoney()).multiply(new BigDecimal(0.7)))) <= 0;
            }
        }catch(Exception e){
            e.printStackTrace();
            return true;
        }
        return true;
    }

    /**
     * 外汇留学贷款：贷款金额不能超过学费生活费总额的90%
     * @return
     */
    private boolean isValideForeignTuition(){
        try{
            if(StringUtils.isEmptyOrNull(loanAmountView.getContentMoney())){
                return true;
            }
            BigDecimal bdLoanAmount = new BigDecimal(loanAmountView.getContentMoney());
            if (isNeeded(mOnLineLoanFieldBean.getTuitionTradePriceNeed()) && isNeeded(mOnLineLoanFieldBean.getCurrencyNeed())) {
                return bdLoanAmount.compareTo((new BigDecimal(tuitionView.getContentMoney()).multiply(new BigDecimal(0.9)))) <= 0;
            }

        }catch(Exception e){
            e.printStackTrace();
            return true;
        }
        return true;
    }

    /**
     * 数字是否合法
     * @param value
     * @param emptyTips
     * @param maxValue
     * @param maxLengthTips
     * @return
     */
    private boolean isValidNumber(String value, String emptyTips, float maxValue, String maxLengthTips){
        if(StringUtils.isEmpty(value)){
            showErrorDialog(emptyTips);
            return false;
        }

        if(Float.parseFloat(value) >= maxValue || Float.parseFloat(value) <= 0){
            showErrorDialog(maxLengthTips);
            return false;
        }

        return true;
    }

    /**
     * 获取字符串长度,一个中文2个字符
     * @param value
     * @return
     */
    private int getLength(String value){
        if(StringUtils.isEmptyOrNull(value)){
            return 0;
        }
        int result = 0;
        for(int i = 0; i < value.length(); i ++){
            char charAt = value.charAt(i);
            //32-122包含了空格、大小写字母、数字和一些常用的符号
            if(charAt >= 32 && charAt <= 122){
                result ++;
            }
            else{
                result += 2;
            }
        }

        return result;
    }

    /**
     * 校验value值是否合法，并提示
     * @param value
     * @param emptyTips
     * @param maxLength
     * @param maxLengthTips
     * @return
     */
    private boolean isValidValue(String value, String emptyTips, int maxLength, String maxLengthTips){
        if(StringUtils.isEmpty(value)){
            showErrorDialog(emptyTips);
            return false;
        }

        if(getLength(value) > maxLength){
            showErrorDialog(maxLengthTips);
            return false;
        }

        return true;
    }

    /**
     * 正则表达校验value是否合法
     * @param regexName
     * @param value
     * @return
     */
    private boolean isValidValue(String regexName, String value){
        if(regexName == null || value == null){
            return false;
        }
        // 收款人姓名
        RegexResult regexResult = RegexUtils.check(mContext, regexName,
                value.replaceAll("\\s*", ""), true);
        if (!regexResult.isAvailable) {
            showErrorDialog(regexResult.errorTips);
            return false;
        }
        return true;
    }

    /**
     * 将字符串是否必需转成boolean
     * 1 是；2 否
     *
     * @param needed 是否必需
     * @return 是否必需的boolean值
     */
    private boolean isNeeded(String needed) {
        return "1".equals(needed);
    }

    @Override
    protected void onFragmentResult(int requestCode, int resultCode, Bundle data) {
        if (requestCode == REQUEST_BRANCH && resultCode == RESULT_OK && data != null) {
            OnLineLoanBranchBean branchBean = data.getParcelable(PublicConst.RESULT_DATA);
            if (branchBean != null) {
                mOnLineLoanSubmitModel.setDeptID(branchBean.getDeptID());
                mOnLineLoanSubmitModel.setDeptName(branchBean.getDeptName());
                mOnLineLoanSubmitModel.setDeptAddr(branchBean.getDeptAddr());
                bankBranchView.setChoiceTextContent(branchBean.getDeptName());
                bankBranchAddressView.setChoiceTextContent(branchBean.getDeptAddr());
                bankBranchAddressView.setVisibility(View.VISIBLE);
            }
        }
    }

    //选择是否提供抵押担保
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        int visible = isChecked ? View.VISIBLE : View.GONE;
        guaTypeView.setVisibility(visible);
        guaTypeViewDivider.setVisibility(visible);
    }
}