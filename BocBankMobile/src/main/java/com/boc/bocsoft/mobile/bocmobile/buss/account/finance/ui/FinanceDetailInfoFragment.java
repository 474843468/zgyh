package com.boc.bocsoft.mobile.bocmobile.buss.account.finance.ui;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;

import com.boc.bocsoft.mobile.bocmobile.ApplicationConst;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.utils.MoneyUtils;
import com.boc.bocsoft.mobile.bocmobile.base.widget.details.DetailTableRow;
import com.boc.bocsoft.mobile.bocmobile.buss.account.base.BaseAccountFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.account.finance.model.FinanceModel;
import com.boc.bocsoft.mobile.common.utils.NumberUtils;

/**
 * @author wangyang
 *         15/1/1 16:28
 *         账户详情界面
 */
@SuppressLint("ValidFragment")
public class FinanceDetailInfoFragment extends BaseAccountFragment {

    private DetailTableRow dtrName, dtrAccount, dtrTotalBalance, dtrSupplyBalance;

    private DetailTableRow dtrCashUpper, dtrSingleLimit, dtrStatus;

    private FinanceModel financeModel;

    /**
     * 初始化传入充值信息
     *
     * @param financeModel
     */
    public FinanceDetailInfoFragment(FinanceModel financeModel) {
        this.financeModel = financeModel;
    }

    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        return mInflater.inflate(R.layout.boc_fragment_finance_detail_info, null);
    }

    @Override
    protected String getTitleValue() {
        return getString(R.string.boc_account_detail);
    }

    @Override
    public void initView() {
        dtrName = (DetailTableRow) mContentView.findViewById(R.id.dtr_name);
        dtrAccount = (DetailTableRow) mContentView.findViewById(R.id.dtr_account);
        dtrTotalBalance = (DetailTableRow) mContentView.findViewById(R.id.dtr_total_balance);
        dtrSupplyBalance = (DetailTableRow) mContentView.findViewById(R.id.dtr_supply_balance);
        dtrCashUpper = (DetailTableRow) mContentView.findViewById(R.id.dtr_cash_upper);
        dtrSingleLimit = (DetailTableRow) mContentView.findViewById(R.id.dtr_single_limit);
        dtrStatus = (DetailTableRow) mContentView.findViewById(R.id.dtr_account_state);
        dtrName.setBodyHeight(getResources().getDimensionPixelSize(R.dimen.boc_space_between_96px));
        dtrAccount.setBodyHeight(getResources().getDimensionPixelSize(R.dimen.boc_space_between_96px));
        dtrTotalBalance.setBodyHeight(getResources().getDimensionPixelSize(R.dimen.boc_space_between_96px));
        dtrSupplyBalance.setBodyHeight(getResources().getDimensionPixelSize(R.dimen.boc_space_between_96px));
        dtrCashUpper.setBodyHeight(getResources().getDimensionPixelSize(R.dimen.boc_space_between_96px));
        dtrSingleLimit.setBodyHeight(getResources().getDimensionPixelSize(R.dimen.boc_space_between_96px));
        dtrStatus.setBodyHeight(getResources().getDimensionPixelSize(R.dimen.boc_space_between_96px));
    }

    @Override
    public void initData() {
        dtrName.updateValue(financeModel.getNickName());
        dtrAccount.updateValue(NumberUtils.formatCardNumber(financeModel.getFinanICNumber()));
        if (financeModel.getTotalBalance() != null)
            dtrTotalBalance.updateValue(getString(R.string.boc_finance_account_balance_currency) + MoneyUtils.transMoneyFormat(financeModel.getTotalBalance(), ApplicationConst.CURRENCY_CNY),true);
        else
            dtrTotalBalance.updateValue(null,true);

        if (financeModel.getSupplyBalance() != null)
            dtrSupplyBalance.updateValue(getString(R.string.boc_finance_account_balance_currency) + MoneyUtils.transMoneyFormat(financeModel.getSupplyBalance(), ApplicationConst.CURRENCY_CNY),true);
        else
            dtrSupplyBalance.updateValue(null,true);

        if (financeModel.geteCashUpperLimit() != null)
            dtrCashUpper.updateValue(getString(R.string.boc_finance_account_balance_currency) + MoneyUtils.transMoneyFormat(financeModel.geteCashUpperLimit(), ApplicationConst.CURRENCY_CNY),true);
        else
            dtrCashUpper.updateValue(null,true);

        if (financeModel.getSingleLimit() != null)
            dtrSingleLimit.updateValue(getString(R.string.boc_finance_account_balance_currency) + MoneyUtils.transMoneyFormat(financeModel.getSingleLimit(), ApplicationConst.CURRENCY_CNY),true);
        else
            dtrSingleLimit.updateValue(null,true);

        dtrStatus.updateValue(financeModel.getStatus());
    }
}
