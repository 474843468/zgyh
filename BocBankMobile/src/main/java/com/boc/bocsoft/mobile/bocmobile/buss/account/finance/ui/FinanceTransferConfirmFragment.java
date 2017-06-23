package com.boc.bocsoft.mobile.bocmobile.buss.account.finance.ui;

import android.annotation.SuppressLint;

import com.boc.bocsoft.mobile.bocmobile.ApplicationConst;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.securityverify.DeviceInfoModel;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.securityverify.SecurityVerity;
import com.boc.bocsoft.mobile.bocmobile.buss.account.base.BaseTransactionFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.account.finance.model.TransferModel;
import com.boc.bocsoft.mobile.bocmobile.buss.account.finance.model.TransferResultModel;
import com.boc.bocsoft.mobile.bocmobile.buss.account.finance.presenter.FinanceContract;
import com.boc.bocsoft.mobile.bocmobile.buss.account.finance.presenter.FinanceTransferPresenter;
import com.boc.bocsoft.mobile.common.utils.NumberUtils;

import java.util.LinkedHashMap;

/**
 * @author wangyang
 *         16/6/23 15:49
 *         电子账户详情
 */
@SuppressLint("ValidFragment")
public class FinanceTransferConfirmFragment extends BaseTransactionFragment<FinanceTransferPresenter> implements FinanceContract.FinanceAccountRechargeView {

    /**
     * 充值信息
     */
    private TransferModel transferModel;

    /**
     * 初始化传入充值信息
     *
     * @param transferModel
     */
    public FinanceTransferConfirmFragment(TransferModel transferModel) {
        this.transferModel = transferModel;
    }

    @Override
    protected String getTitleValue() {
        return getString(R.string.boc_finance_account_transfer_confirm_title);
    }

    @Override
    public void setListener() {
        super.setListener();
        confirmInfoView.setListener(this);
    }

    @Override
    public void initData() {
        LinkedHashMap<String, String> map = new LinkedHashMap<>();


        //设置充值账户
        if (transferModel.isSelf()) {
            confirmInfoView.isShowSecurity(false);
            map.put(getString(R.string.boc_finance_account_transfer_recharge_expend), NumberUtils.formatCardNumber(transferModel.getFinanceICNumber()));
        } else {
            //设置收款人
            map.put(getString(R.string.boc_finance_account_transfer_recharge_income_name), transferModel.getPayeeName());
            map.put(getString(R.string.boc_finance_account_transfer_recharge_income), NumberUtils.formatCardNumber2(transferModel.getFinanceICNumber()));
        }
        //设置金额
        map.put(getString(R.string.boc_finance_account_transfer_recharge_amount), getString(R.string.boc_finance_account_balance_currency) + transferModel.getAmount());
        //设置充值账户
        map.put(getString(R.string.boc_finance_account_transfer_recharge_account), NumberUtils.formatCardNumber(transferModel.getBankAccountNumber()));

        confirmInfoView.addData(map);

        //给他人充值调用安全因子
        if (!transferModel.isSelf())
            getSecurityCombin(ApplicationConst.SERVICE_ID_FINANCE_TRANSFER);
    }

    @Override
    protected FinanceTransferPresenter initPresenter() {
        return new FinanceTransferPresenter(this);
    }

    @Override
    public void onClickConfirm() {
        showLoadingDialog(false);
        if (transferModel.isSelf())
            //给自己充值
            getPresenter().psnFinanceTransferSelf(transferModel);
        else
            //给他人充值
            getPresenter().psnFinanceTransferOtherPre(transferModel, SecurityVerity.getInstance().getCurrentSecurityVerifyTypeId());
    }

    /**
     * 充值成功回调
     *
     * @param model
     */
    @Override
    public void psnFinanceTransferSuccess(TransferResultModel model) {
        closeProgressDialog();
        //跳转结果页面
        start(new FinanceTransferResultFragment(model));
    }

    @Override
    public void submitTransactionWithSecurity(DeviceInfoModel deviceInfoModel, String factorId, String[] randomNums, String[] encryptPasswords) {
        getPresenter().psnFinanceTransferOther(transferModel, deviceInfoModel, factorId, randomNums, encryptPasswords);
    }
}
