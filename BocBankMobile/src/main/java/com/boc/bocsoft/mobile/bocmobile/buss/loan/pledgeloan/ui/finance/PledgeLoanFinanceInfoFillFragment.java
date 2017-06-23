package com.boc.bocsoft.mobile.bocmobile.buss.loan.pledgeloan.ui.finance;

import android.os.Bundle;
import com.boc.bocsoft.mobile.bocmobile.ApplicationContext;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.utils.PublicCodeUtils;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.pledgeloan.model.IPledgeParamsData;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.pledgeloan.model.finance.ContractFinanceModel;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.pledgeloan.model.finance.FinancePledgeParamsData;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.pledgeloan.model.finance.PledgeFinanceInfoFillViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.pledgeloan.model.finance.PledgeProductBean;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.pledgeloan.presenter.finance.PledgeLoanFinanceInfoFillPresenter;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.pledgeloan.ui.PledgeLoanInfoFillContract;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.pledgeloan.ui.PledgeLoanInfoFillFragment;
import com.boc.bocsoft.mobile.common.utils.NumberUtils;
import com.boc.bocsoft.mobile.common.utils.date.DateFormatters;
import java.math.BigDecimal;

/**
 * 作者：XieDu
 * 创建时间：2016/8/20 13:39
 * 描述：
 */
public class PledgeLoanFinanceInfoFillFragment
        extends PledgeLoanInfoFillFragment<PledgeFinanceInfoFillViewModel> {

    private PledgeProductBean mPledgeProductBean;

    public final static String PARAM_AVAILABLE_AMOUNT = "AvailableAmount";
    public final static String PARAM_PRODUCT_BEAN = "PledgeProductBean";
    public final static String PARAM_FINANCE_PARAMS_DATA = "FinancePledgeParamsData";

    /**
     * 使用该静态方法快速创建该fragment的一个实例，它接收了指定的参数
     */
    public static PledgeLoanFinanceInfoFillFragment newInstance(String availableLoanAmount,
            PledgeProductBean pledgeProductBean, FinancePledgeParamsData financePledgeParamsData) {
        PledgeLoanFinanceInfoFillFragment fragment = new PledgeLoanFinanceInfoFillFragment();
        Bundle args = new Bundle();
        args.putString(PARAM_AVAILABLE_AMOUNT, availableLoanAmount);
        args.putParcelable(PARAM_PRODUCT_BEAN, pledgeProductBean);
        args.putParcelable(PARAM_FINANCE_PARAMS_DATA, financePledgeParamsData);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected String getContractName() {
        return getString(R.string.boc_pledge_deposit_finance);
    }

    @Override
    protected PledgeFinanceInfoFillViewModel getPledgeLoanInfoFillViewModel() {
        mPledgeLoanInfoFillViewModel = new PledgeFinanceInfoFillViewModel();
        mPledgeLoanInfoFillViewModel.setLoanType("FIN-LOAN");
        if (getArguments() != null
                && (mPledgeProductBean = getArguments().getParcelable(PARAM_PRODUCT_BEAN))
                != null) {
            mPledgeLoanInfoFillViewModel.setCurrencyCode(mPledgeProductBean.getCurCode());
            mPledgeLoanInfoFillViewModel.setPledgeRate(mPledgeProductBean.getPledgeRate());
            mPledgeLoanInfoFillViewModel.setAvailableAmount(
                    getArguments().getString(PARAM_AVAILABLE_AMOUNT));
        }
        return mPledgeLoanInfoFillViewModel;
    }

    @Override
    protected PledgeLoanInfoFillContract.Presenter<PledgeFinanceInfoFillViewModel> initPresenter() {
        return new PledgeLoanFinanceInfoFillPresenter(this);
    }

    @Override
    public IPledgeParamsData getPledgeParamsData() {
        return getArguments().getParcelable(PARAM_FINANCE_PARAMS_DATA);
    }

    @Override
    protected String getServiceId() {
        return "PB097";
    }

    @Override
    protected void onSelectPeriod(int period) {
        mPledgeLoanInfoFillViewModel.setPayCycle(period <= 6 ? "1" : "2");
        mPledgeLoanInfoFillViewModel.setPayType(period <= 6 ? "1" : "2");
    }

    @Override
    protected void startNext() {
        start(PledgeLoanFinanceInfoConfirmFragment.newInstance(mPledgeLoanInfoFillViewModel,
                mVerifyBean));
    }

    @Override
    protected String getContractUrl() {
        return "file:///android_asset/webviewcontent/loan/pledgeloan/PersonalFinanceProductAgreement.html";
    }

    @Override
    protected Object getContractModel() {
        ContractFinanceModel contractFinanceModel = new ContractFinanceModel();
        contractFinanceModel.setBorrower(
                ApplicationContext.getInstance().getUser().getCustomerName());
        contractFinanceModel.setCdcard(NumberUtils.formatIDNumber(
                ApplicationContext.getInstance().getUser().getIdentityNumber()));
        String cdType = PublicCodeUtils.getIdentityType(mContext,
                ApplicationContext.getInstance().getUser().getIdentityType());
        contractFinanceModel.setCdtype(cdType);
        contractFinanceModel.setCurency(PublicCodeUtils.getCurrency(mContext,
                mPledgeLoanInfoFillViewModel.getCurrencyCode()));
        contractFinanceModel.setFloatingrate(mPledgeParamsData.getFloatingRate());
        contractFinanceModel.setFloatingValue(mPledgeParamsData.getFloatingValue());
        contractFinanceModel.setLoanamount(etCurrentUseAmount.getContentMoney());
        contractFinanceModel.setLoanperiod(mPledgeLoanInfoFillViewModel.getLoanPeriod());
        contractFinanceModel.setMaturitydate(
                mPledgeProductBean.getProdEnd().format(DateFormatters.dateFormatter1));
        contractFinanceModel.setProductcode(mPledgeProductBean.getProdCode());
        contractFinanceModel.setProductname(mPledgeProductBean.getProdName());
        contractFinanceModel.setReciveraccount(mPledgeLoanInfoFillViewModel.getToActNum());
        contractFinanceModel.setReciver(contractFinanceModel.getBorrower());
        contractFinanceModel.setRepayment(contractFinanceModel.getBorrower());
        contractFinanceModel.setRepaymentaccount(mPledgeLoanInfoFillViewModel.getPayActNum());
        contractFinanceModel.setShareholder(mPledgeProductBean.getHoldingQuantity());
        contractFinanceModel.setShareavailable(mPledgeProductBean.getAvailableLoanAmt());
        BigDecimal frozenAmount = new BigDecimal(mPledgeProductBean.getHoldingQuantity()).subtract(
                new BigDecimal(mPledgeProductBean.getAvailableLoanAmt()));
        contractFinanceModel.setSharefrozen(
                frozenAmount.setScale(2, BigDecimal.ROUND_DOWN).toPlainString());
        contractFinanceModel.setTel(NumberUtils.formatMobileNumberWithAsterrisk(ApplicationContext.getInstance().getUser().getMobile()));
        return contractFinanceModel;
    }
}
