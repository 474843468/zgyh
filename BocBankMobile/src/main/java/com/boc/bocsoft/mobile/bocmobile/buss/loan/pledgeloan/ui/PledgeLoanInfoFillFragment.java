package com.boc.bocsoft.mobile.bocmobile.buss.loan.pledgeloan.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import com.boc.bocsoft.mobile.bii.bus.global.model.PsnGetSecurityFactor.CombinListBean;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.MvpBussFragment;
import com.boc.bocsoft.mobile.bocmobile.base.model.AccountBean;
import com.boc.bocsoft.mobile.bocmobile.base.model.VerifyBean;
import com.boc.bocsoft.mobile.bocmobile.base.utils.MoneyUtils;
import com.boc.bocsoft.mobile.bocmobile.base.widget.assignment.SelectAgreementView;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.listselectorview.SelectListDialog;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.listselectorview.SelectStringListDialog;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.securityverify.SecurityFactorModel;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.securityverify.SecurityVerity;
import com.boc.bocsoft.mobile.bocmobile.base.widget.edittext.EditChoiceWidget;
import com.boc.bocsoft.mobile.bocmobile.base.widget.edittext.EditMoneyInputWidget;
import com.boc.bocsoft.mobile.bocmobile.base.widget.selectaccoutview.SelectAccoutFragment;
import com.boc.bocsoft.mobile.bocmobile.base.widget.webView.ContractFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.pledgeloan.PledgeLoanConst;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.pledgeloan.model.IPledgeLoanInfoFillViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.pledgeloan.model.IPledgeParamsData;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.pledgeloan.model.deposit.LoanRateQueryParams;
import com.boc.bocsoft.mobile.common.utils.NumberUtils;
import com.boc.bocsoft.mobile.common.utils.StringUtils;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 注意，填写信息的金额都转换成人民币为单位，外币转成人民币后，下一步提交交易时币种修改为人民币。
 */
public abstract class PledgeLoanInfoFillFragment<T extends IPledgeLoanInfoFillViewModel>
        extends MvpBussFragment<PledgeLoanInfoFillContract.Presenter<T>>
        implements View.OnClickListener, PledgeLoanInfoFillContract.View,
        SelectAgreementView.OnClickContractListener {

    protected EditMoneyInputWidget etCurrentUseAmount;
    protected EditChoiceWidget etPeriod;
    protected EditChoiceWidget viewRate;
    protected EditChoiceWidget viewRepayType;
    protected EditChoiceWidget etPayeeAccount;
    protected EditChoiceWidget etPayerAccount;
    protected Button btnNext;
    protected SelectAgreementView viewAgreement;
    private View rootView;

    protected SelectStringListDialog mPeriodDialog;
    protected PledgeAccountSelectFragment mSelectAccoutFragment;

    protected final int REQUEST_CODE_SELECT_ACCOUNT_PAYEE = 1;
    protected final int REQUEST_CODE_SELECT_ACCOUNT_PAYER = 2;
    protected T mPledgeLoanInfoFillViewModel;
    protected IPledgeParamsData mPledgeParamsData;
    protected VerifyBean mVerifyBean;

    /**
     * 初始化布局
     */
    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        rootView = mInflater.inflate(R.layout.boc_fragment_pledge_loan_deposit_info_fill, null);
        return rootView;
    }

    @Override
    public void initView() {
        etCurrentUseAmount =
                (EditMoneyInputWidget) rootView.findViewById(R.id.et_current_use_amount);
        etPeriod = (EditChoiceWidget) rootView.findViewById(R.id.et_period);
        etPeriod.setOnClickListener(PledgeLoanInfoFillFragment.this);
        viewRate = (EditChoiceWidget) rootView.findViewById(R.id.view_rate);
        viewRepayType = (EditChoiceWidget) rootView.findViewById(R.id.view_repay_type);
        etPayeeAccount = (EditChoiceWidget) rootView.findViewById(R.id.et_payee_account);
        etPayeeAccount.setOnClickListener(PledgeLoanInfoFillFragment.this);
        etPayerAccount = (EditChoiceWidget) rootView.findViewById(R.id.et_payer_account);
        etPayerAccount.setOnClickListener(PledgeLoanInfoFillFragment.this);
        btnNext = (Button) rootView.findViewById(R.id.btn_next);
        viewRate.setBottomLineVisibility(true);
        viewRate.setArrowImageGone(false);
        viewRepayType.setBottomLineVisibility(true);
        viewRepayType.setArrowImageGone(false);
        etPayeeAccount.setBottomLineVisibility(true);
        etCurrentUseAmount.setMaxLeftNumber(12);
        etCurrentUseAmount.getContentMoneyEditText()
                          .setTextColor(getResources().getColor(R.color.boc_text_color_red));
        viewAgreement = (SelectAgreementView) rootView.findViewById(R.id.view_agreement);
    }

    /**
     * 先调用子类的initData,再调用该initData
     */
    @Override
    public void initData() {
        mPledgeLoanInfoFillViewModel = getPledgeLoanInfoFillViewModel();
        mPledgeParamsData = getPledgeParamsData();
        getPresenter().setConversationId(mPledgeParamsData.getConversationId());
        mPledgeLoanInfoFillViewModel.setConversationId(mPledgeParamsData.getConversationId());
        etCurrentUseAmount.setContentHint(
                String.format(getString(R.string.boc_pledge_info_available_amount_high_limit),
                        MoneyUtils.transMoneyFormatNoLossAccuracy(
                                mPledgeLoanInfoFillViewModel.getAvailableAmount(),
                                mPledgeLoanInfoFillViewModel.getCurrencyCode())));
        viewAgreement.setContractName(getContractName());
    }

    protected abstract String getContractName();

    protected abstract T getPledgeLoanInfoFillViewModel();

    @Override
    public void setListener() {
        etPeriod.setOnClickListener(this);
        etPayeeAccount.setOnClickListener(this);
        etPayerAccount.setOnClickListener(this);
        viewAgreement.setOnClickContractListener(this);
        btnNext.setOnClickListener(this);
    }

    @Override
    protected String getTitleValue() {
        return getString(R.string.boc_pledge_info_fill);
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
    public void onClick(View view) {
        if (view.getId() == R.id.et_period) {
            selectPeriod();
        } else if (view.getId() == R.id.et_payee_account) {
            selectPayeeAccount();
        } else if (view.getId() == R.id.et_payer_account) {
            selectPayerAccount();
        } else if (view.getId() == R.id.btn_next) {
            if (checkFillInfo()) {
                showLoadingDialog(false);
                getPresenter().qrySecurityFactor(getServiceId());
            }
        }
    }

    /**
     * 设置serviceId
     *
     * @return serviceId
     */
    protected abstract String getServiceId();

    /**
     * 选择还款期限
     * 用户选择1月－6月，还款方式默认为到期一次性还本付息，用户选择7-12月还款方式为按期还息到期还本。
     */
    private void selectPeriod() {
        if (mPeriodDialog == null) {
            mPeriodDialog = new SelectStringListDialog(mContext);
            final int periodMin, periodMax;
            periodMin = StringUtils.isEmpty(mPledgeParamsData.getLoanPeriodMin()) ? 1
                    : Integer.valueOf(mPledgeParamsData.getLoanPeriodMin());
            periodMax = StringUtils.isEmpty(mPledgeParamsData.getLoanPeriodMax()) ? 12
                    : Integer.valueOf(mPledgeParamsData.getLoanPeriodMax());
            List<String> periodList = new ArrayList<>();
            for (int i = periodMin; i <= periodMax; i++) {
                periodList.add(String.format(getString(R.string.boc_pledge_info_period_month), i));
            }
            mPeriodDialog.setListData(periodList);
            mPeriodDialog.setOnSelectListener(new SelectListDialog.OnSelectListener<String>() {
                @Override
                public void onSelect(int position, String model) {
                    etPeriod.setChoiceTextContent(model);
                    int period = position + periodMin;
                    mPledgeLoanInfoFillViewModel.setLoanPeriod(String.valueOf(period));
                    onSelectPeriod(period);
                    viewRepayType.setChoiceTextContent(
                            mPledgeLoanInfoFillViewModel.getPayTypeString());
                    mPeriodDialog.dismiss();
                    etPeriod.setBottomLineVisibility(true);
                    viewRate.setVisibility(View.VISIBLE);
                    viewRepayType.setVisibility(View.VISIBLE);
                    showLoadingDialog();
                    LoanRateQueryParams loanRateQueryParams = new LoanRateQueryParams();
                    loanRateQueryParams.setLoanPeriod(mPledgeLoanInfoFillViewModel.getLoanPeriod());
                    loanRateQueryParams.setFloatingRate(mPledgeParamsData.getFloatingRate());
                    loanRateQueryParams.setFloatingValue(mPledgeParamsData.getFloatingValue());
                    loanRateQueryParams.setRateIncrOpt(
                            StringUtils.isEmpty(loanRateQueryParams.getFloatingRate()) ? "1" : "2");
                    getPresenter().qryRate(loanRateQueryParams);
                }
            });
        }
        mPeriodDialog.show();
    }

    protected abstract void onSelectPeriod(int period);

    /**
     * 选择收款账户
     */
    private void selectPayeeAccount() {
        if (mSelectAccoutFragment == null) {
            mSelectAccoutFragment =
                    PledgeAccountSelectFragment.newInstance(PledgeLoanConst.typeList,
                            mPledgeLoanInfoFillViewModel.getConversationId(),
                            mPledgeLoanInfoFillViewModel.getCurrencyCode());
        }
        mSelectAccoutFragment.setIsPayee(true);
        startForResult(mSelectAccoutFragment, REQUEST_CODE_SELECT_ACCOUNT_PAYEE);
    }

    /**
     * 选择还款账户
     */
    private void selectPayerAccount() {
        if (mSelectAccoutFragment == null) {
            mSelectAccoutFragment =
                    PledgeAccountSelectFragment.newInstance(PledgeLoanConst.typeList,
                            mPledgeLoanInfoFillViewModel.getConversationId(),
                            mPledgeLoanInfoFillViewModel.getCurrencyCode());
        }
        mSelectAccoutFragment.setIsPayee(false);
        startForResult(mSelectAccoutFragment, REQUEST_CODE_SELECT_ACCOUNT_PAYER);
    }

    /**
     * 校验填写的信息
     *
     * @return 校验是否通过
     */
    private boolean checkFillInfo() {
        String currentUseAmount = etCurrentUseAmount.getContentMoney();
        if (StringUtils.isEmpty(currentUseAmount)) {
            showErrorDialog(getString(R.string.boc_pledge_info_use_money_empty));
            return false;
        }
        BigDecimal singleQuotaMin = new BigDecimal(mPledgeParamsData.getSingleQuotaMin());
        BigDecimal currentUseAmountBD = new BigDecimal(currentUseAmount);
        if (currentUseAmountBD.compareTo(singleQuotaMin) < 0) {
            showErrorDialog(String.format(getString(R.string.boc_pledge_info_use_money_too_little),
                    MoneyUtils.transMoneyFormatNoLossAccuracy(singleQuotaMin,
                            mPledgeLoanInfoFillViewModel.getCurrencyCode())));
            return false;
        }

        //用款金额<=可用额度，若大于可用额度，则提示：用款金额不能大于用款可用额度。
        if (mPledgeLoanInfoFillViewModel.getAvailableAmount() != null &&
                currentUseAmountBD.compareTo(
                        new BigDecimal(mPledgeLoanInfoFillViewModel.getAvailableAmount())) > 0) {
            showErrorDialog(getString(R.string.boc_pledge_info_use_money_too_much));
            return false;
        }
        mPledgeLoanInfoFillViewModel.setAmount(currentUseAmount);
        if (StringUtils.isEmpty(mPledgeLoanInfoFillViewModel.getLoanPeriod())) {
            showErrorDialog(getString(R.string.boc_pledge_info_peroid_empty));
            return false;
        }
        if (StringUtils.isEmpty(mPledgeLoanInfoFillViewModel.getToAccount())) {
            showErrorDialog(getString(R.string.boc_pledge_info_payee_account_empty));
            return false;
        }
        if (StringUtils.isEmpty(mPledgeLoanInfoFillViewModel.getPayAccount())) {
            showErrorDialog(getString(R.string.boc_pledge_info_payer_account_empty));
            return false;
        }
        if (!viewAgreement.isSelected()) {
            showErrorDialog(getString(R.string.boc_agreement_not_select));
            return false;
        }
        return true;
    }

    /**
     * 查看合同签检查信息是否填写
     *
     * @return 校验是否通过
     */
    private boolean checkFillInfoBeforeClickContract() {
        String currentUseAmount = etCurrentUseAmount.getContentMoney();
        if (StringUtils.isEmpty(currentUseAmount)) {
            showErrorDialog(getString(R.string.boc_pledge_info_use_money_empty_before));
            return false;
        }
        BigDecimal singleQuotaMin = new BigDecimal(mPledgeParamsData.getSingleQuotaMin());
        BigDecimal currentUseAmountBD = new BigDecimal(currentUseAmount);
        if (currentUseAmountBD.compareTo(singleQuotaMin) < 0) {
            showErrorDialog(String.format(getString(R.string.boc_pledge_info_use_money_too_little),
                    MoneyUtils.transMoneyFormatNoLossAccuracy(singleQuotaMin,
                            mPledgeLoanInfoFillViewModel.getCurrencyCode())));
            return false;
        }

        //用款金额<=可用额度，若大于可用额度，则提示：本次用款金额不能大于用款可用额度。
        if (mPledgeLoanInfoFillViewModel.getAvailableAmount() != null &&
                currentUseAmountBD.compareTo(
                        new BigDecimal(mPledgeLoanInfoFillViewModel.getAvailableAmount())) > 0) {
            showErrorDialog(getString(R.string.boc_pledge_info_use_money_too_much));
            return false;
        }
        mPledgeLoanInfoFillViewModel.setAmount(currentUseAmount);
        if (StringUtils.isEmpty(mPledgeLoanInfoFillViewModel.getLoanPeriod())) {
            showErrorDialog(getString(R.string.boc_pledge_info_peroid_empty_before));
            return false;
        }
        if (StringUtils.isEmpty(mPledgeLoanInfoFillViewModel.getToAccount())) {
            showErrorDialog(getString(R.string.boc_pledge_info_payee_account_empty_before));
            return false;
        }
        if (StringUtils.isEmpty(mPledgeLoanInfoFillViewModel.getPayAccount())) {
            showErrorDialog(getString(R.string.boc_pledge_info_payer_account_empty_before));
            return false;
        }
        return true;
    }

    @Override
    public void onLoadEmpty() {
        closeProgressDialog();
        showErrorDialog(getString(R.string.boc_load_failed));
    }

    public abstract IPledgeParamsData getPledgeParamsData();

    @Override
    public void onQryRateSuccess(String loanRate) {
        closeProgressDialog();
        mPledgeLoanInfoFillViewModel.setLoanRate(loanRate);
        viewRate.setChoiceTextContent(MoneyUtils.transRatePercentTypeFormatTotal(loanRate));
    }

    @Override
    public void onQrySecurityFactorSuccess(SecurityFactorModel securityFactorModel) {
        CombinListBean combinBean = SecurityVerity.getInstance(getActivity()).
                getDefaultSecurityFactorId(securityFactorModel);
        mPledgeLoanInfoFillViewModel.set_combinId(combinBean.getId());
        mPledgeLoanInfoFillViewModel.setCombinName(combinBean.getName());
        mPledgeLoanInfoFillViewModel.setAvailableAmount(
                new BigDecimal(mPledgeLoanInfoFillViewModel.getAvailableAmount())
                                              .setScale(2, BigDecimal.ROUND_HALF_UP)
                                              .toPlainString());
        getPresenter().verify(mPledgeLoanInfoFillViewModel);
    }

    @Override
    public void onVerifySuccess(VerifyBean verifyBean) {
        closeProgressDialog();
        mVerifyBean = verifyBean;
        startNext();
    }

    protected abstract void startNext();

    @Override
    protected void onFragmentResult(int requestCode, int resultCode, Bundle data) {
        super.onFragmentResult(requestCode, resultCode, data);
        if (resultCode != SelectAccoutFragment.RESULT_CODE_SELECT_ACCOUNT || data == null) {
            return;
        }
        AccountBean accountBean = data.getParcelable(SelectAccoutFragment.ACCOUNT_SELECT);
        String accountId = accountBean != null ? accountBean.getAccountId() : null;
        String accountNumber = accountBean != null ? accountBean.getAccountNumber() : null;
        if (requestCode == REQUEST_CODE_SELECT_ACCOUNT_PAYEE) {
            mPledgeLoanInfoFillViewModel.setToAccount(accountId);
            mPledgeLoanInfoFillViewModel.setToActNum(accountNumber);
            etPayeeAccount.setChoiceTextContent(NumberUtils.formatCardNumber(accountNumber));
        } else if (requestCode == REQUEST_CODE_SELECT_ACCOUNT_PAYER) {
            mPledgeLoanInfoFillViewModel.setPayAccount(accountId);
            mPledgeLoanInfoFillViewModel.setPayActNum(accountNumber);
            etPayerAccount.setChoiceTextContent(NumberUtils.formatCardNumber(accountNumber));
        }
    }

    protected abstract String getContractUrl();

    protected abstract Object getContractModel();

    @Override
    public void onClickContract(int index) {
        if (checkFillInfoBeforeClickContract()) {
            start(ContractFragment.newInstance(getContractUrl(), getContractModel()));
        }
    }
}