package com.boc.bocsoft.mobile.bocmobile.buss.loan.pledgeloan.ui.deposit;

import android.os.Bundle;
import com.boc.bocsoft.mobile.bocmobile.ApplicationConst;
import com.boc.bocsoft.mobile.bocmobile.ApplicationContext;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.utils.MoneyUtils;
import com.boc.bocsoft.mobile.bocmobile.base.utils.PublicCodeUtils;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.pledgeloan.PledgeLoanConst;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.pledgeloan.model.IPledgeParamsData;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.pledgeloan.model.deposit.ContractDepositModel;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.pledgeloan.model.deposit.PersonalTimeAccountBean;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.pledgeloan.model.deposit.PledgeDepositInfoFillViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.pledgeloan.model.deposit.PledgeDepositReceiptViewModel;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.pledgeloan.presenter.deposit.PledgeLoanDepositInfoFillPresenter;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.pledgeloan.ui.PledgeLoanInfoFillContract;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.pledgeloan.ui.PledgeLoanInfoFillFragment;
import com.boc.bocsoft.mobile.common.utils.NumberUtils;
import com.boc.bocsoft.mobile.framework.utils.PublicConst;
import java.util.ArrayList;
import java.util.List;

/**
 * 使用{@link PledgeLoanDepositInfoFillFragment#newInstance}静态方法来创建该fragment的一个实例
 */
public class PledgeLoanDepositInfoFillFragment
        extends PledgeLoanInfoFillFragment<PledgeDepositInfoFillViewModel> {

    private PledgeDepositReceiptViewModel mPledgeDepositReceiptViewModel;

    /**
     * 使用该静态方法快速创建该fragment的一个实例，它接收了指定的参数
     *
     * @param param 参数
     * @return PledgeLoanDepositInfoFillFragment的一个实例
     */
    public static PledgeLoanDepositInfoFillFragment newInstance(
            PledgeDepositReceiptViewModel param) {
        PledgeLoanDepositInfoFillFragment fragment = new PledgeLoanDepositInfoFillFragment();
        Bundle args = new Bundle();
        args.putParcelable(PublicConst.PARAM_DATA, param);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected String getContractName() {
        return getString(R.string.boc_pledge_deposit_contract);
    }

    @Override
    protected PledgeLoanInfoFillContract.Presenter<PledgeDepositInfoFillViewModel> initPresenter() {
        return new PledgeLoanDepositInfoFillPresenter(this);
    }

    @Override
    protected PledgeDepositInfoFillViewModel getPledgeLoanInfoFillViewModel() {
        mPledgeLoanInfoFillViewModel = new PledgeDepositInfoFillViewModel();
        mPledgeLoanInfoFillViewModel.setPayCycle("01");
        mPledgeLoanInfoFillViewModel.setLoanType("PLEA");
        if (getArguments() != null && (mPledgeDepositReceiptViewModel =
                getArguments().getParcelable(PublicConst.PARAM_DATA)) != null) {
            mPledgeLoanInfoFillViewModel.setAccountNumber(
                    mPledgeDepositReceiptViewModel.getAccountNumber());
            mPledgeLoanInfoFillViewModel.setAccountId(
                    mPledgeDepositReceiptViewModel.getAccountId());
            mPledgeLoanInfoFillViewModel.setPersonalTimeAccountBeanArrayList(
                    mPledgeDepositReceiptViewModel.getPersonalTimeAccountBeanArrayList());
            mPledgeLoanInfoFillViewModel.setCurrencyCode(ApplicationConst.CURRENCY_CNY);
            mPledgeLoanInfoFillViewModel.setAvailableAmount(
                    mPledgeDepositReceiptViewModel.getAvailableAmount().toPlainString());
        }
        return mPledgeLoanInfoFillViewModel;
    }

    @Override
    public void startNext() {
        start(PledgeLoanDepositInfoConfirmFragment.newInstance(mPledgeLoanInfoFillViewModel,
                mVerifyBean));
    }

    @Override
    protected String getContractUrl() {
        return "file:///android_asset/webviewcontent/loan/pledgeloan/clientlicenseLoan.html";
    }

    @Override
    protected String getServiceId() {
        return "PB092";
    }

    @Override
    protected void onSelectPeriod(int period) {
        mPledgeLoanInfoFillViewModel.setPayType(PledgeLoanConst.LONETYPE_B);
    }

    @Override
    public IPledgeParamsData getPledgeParamsData() {
        return mPledgeDepositReceiptViewModel.getLoanDepositMultipleQueryBean();
    }

    @Override
    protected Object getContractModel() {
        ContractDepositModel contractDepositModel = new ContractDepositModel();
        contractDepositModel.setBorrower(
                ApplicationContext.getInstance().getUser().getCustomerName());
        contractDepositModel.setCdcard(NumberUtils.formatIDNumber(
                ApplicationContext.getInstance().getUser().getIdentityNumber()));
        String cdType = PublicCodeUtils.getIdentityType(mContext,
                ApplicationContext.getInstance().getUser().getIdentityType());
        contractDepositModel.setCdtype(cdType);
        contractDepositModel.setFloatingrate(mPledgeParamsData.getFloatingRate());
        contractDepositModel.setFloatingValue(mPledgeParamsData.getFloatingValue());
        contractDepositModel.setLoanamount(etCurrentUseAmount.getContentMoney());
        contractDepositModel.setLoanperiod(mPledgeLoanInfoFillViewModel.getLoanPeriod());
        contractDepositModel.setReciveraccount(mPledgeLoanInfoFillViewModel.getToActNum());
        contractDepositModel.setReciver(contractDepositModel.getBorrower());
        contractDepositModel.setRepayment(contractDepositModel.getBorrower());
        contractDepositModel.setRepaymentaccount(mPledgeLoanInfoFillViewModel.getPayActNum());
        List<ContractDepositModel.PrimaryaccountlistEntity> accountList = new ArrayList<>();
        for (PersonalTimeAccountBean personalTimeAccountBean : mPledgeLoanInfoFillViewModel.getPersonalTimeAccountBeanArrayList()) {
            ContractDepositModel.PrimaryaccountlistEntity entity =
                    new ContractDepositModel.PrimaryaccountlistEntity();
            entity.setAccountmark("R".equals(personalTimeAccountBean.getConvertType()) ? getString(
                    R.string.boc_overview_detail_regular_info_convert_type_auto)
                    : getString(R.string.boc_overview_detail_regular_info_convert_type_no_auto));
            entity.setAccounttype(PublicCodeUtils.getDepositReceiptType(mContext,
                    personalTimeAccountBean.getType()));
            entity.setCurency(PublicCodeUtils.getCurrency(mContext,
                    personalTimeAccountBean.getCurrencyCode()));
            entity.setAmount(MoneyUtils.transMoneyFormatNoLossAccuracy(
                    personalTimeAccountBean.getAvailableBalance(),
                    personalTimeAccountBean.getCurrencyCode()));
            entity.setAmountticket(personalTimeAccountBean.getCdNumber());
            entity.setRrimaryaccount(mPledgeLoanInfoFillViewModel.getAccountNumber());
            entity.setVolumenumber(personalTimeAccountBean.getVolumeNumber());
            accountList.add(entity);
        }
        contractDepositModel.setPrimaryaccountlist(accountList);
        return contractDepositModel;
    }
}