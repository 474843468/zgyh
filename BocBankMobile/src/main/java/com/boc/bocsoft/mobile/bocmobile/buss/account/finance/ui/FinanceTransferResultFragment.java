package com.boc.bocsoft.mobile.bocmobile.buss.account.finance.ui;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;

import com.boc.bocsoft.mobile.bocmobile.ApplicationConst;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.utils.MoneyUtils;
import com.boc.bocsoft.mobile.bocmobile.base.widget.operation.BaseOperationResultView;
import com.boc.bocsoft.mobile.bocmobile.base.widget.operation.OperationResultBottom;
import com.boc.bocsoft.mobile.bocmobile.base.widget.operation.OperationResultHead;
import com.boc.bocsoft.mobile.bocmobile.buss.account.base.BaseAccountFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.account.finance.model.TransferResultModel;
import com.boc.bocsoft.mobile.bocmobile.buss.account.overview.ui.OverviewFragment;
import com.boc.bocsoft.mobile.common.utils.NumberUtils;

/**
 * @author wangyang
 *         16/6/27 20:26
 *         充值结果页面
 */
@SuppressLint("ValidFragment")
public class FinanceTransferResultFragment extends BaseAccountFragment implements BaseOperationResultView.BtnCallback, OperationResultBottom.HomeBtnCallback {

    /**
     * 充值结果Model
     */
    private TransferResultModel resultModel;
    /**
     * 充值结果View
     */
    private BaseOperationResultView borvResult;

    public FinanceTransferResultFragment(TransferResultModel resultModel) {
        this.resultModel = resultModel;
    }

    @Override
    protected View onCreateView(LayoutInflater mInflater) {
        return mInflater.inflate(R.layout.boc_fragment_finance_result, null);
    }

    @Override
    protected String getTitleValue() {
        return getString(R.string.boc_account_loss_success_title);
    }

    @Override
    public void initView() {
        borvResult = (BaseOperationResultView) mContentView.findViewById(R.id.rv_result);
        borvResult.updateButtonStyle();
    }

    @Override
    public void setListener() {
        borvResult.setgoHomeOnclick(this);
    }

    @Override
    public void initData() {
        borvResult.setTopDividerVisible(true);
        //设置头部信息
        if (resultModel.isSuccess())
            borvResult.updateHead(OperationResultHead.Status.SUCCESS, getString(R.string.boc_finance_account_recharge_success));
        else
            borvResult.updateHead(OperationResultHead.Status.FAIL, getString(R.string.boc_finance_account_recharge_fail));

        //设置详情
        borvResult.addDetailRow(getString(R.string.boc_finance_account_transfer_transaction), resultModel.getTransactionId());
        if (resultModel.isSelf()) {
            borvResult.addDetailRow(getString(R.string.boc_finance_account_transfer_recharge_expend), NumberUtils.formatCardNumber(resultModel.getIcCardNum()));
        } else {
            borvResult.addDetailRow(getString(R.string.boc_finance_account_transfer_recharge_income), NumberUtils.formatCardNumber2(resultModel.getIcCardNum()));
        }
        borvResult.addDetailRow(getString(R.string.boc_finance_account_transfer_recharge_amount), getString(R.string.boc_finance_account_balance_currency) + " " + MoneyUtils.transMoneyFormat(resultModel.getAmount(), ApplicationConst.CURRENCY_CNY));

        borvResult.addDetailRow(getString(R.string.boc_finance_account_transfer_recharge_account), NumberUtils.formatCardNumber(resultModel.getBankAccountNum()));


        //如果为给自己充值,设置我的电子账户按钮l
//        if (resultModel.isSelf()) {
        borvResult.addContentItem(getString(R.string.boc_finance_account_recharge_account), 0);
        borvResult.setOnclick(this);
//        }
    }

    @Override
    public void onClickListener(View v) {
        //跳转至我的电子账户概览页面
        start(new FinanceAccountViewFragment());
    }

    @Override
    public boolean onBackPress() {
        popToAndReInit(OverviewFragment.class);
        return true;
    }

    @Override
    public void onHomeBack() {
        mActivity.finish();
    }

    @Override
    public boolean onBack() {
        popToAndReInit(OverviewFragment.class);
        return false;
    }
}
