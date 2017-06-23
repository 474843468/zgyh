package com.boc.bocsoft.mobile.bocmobile.buss.account.limit.ui;

import android.annotation.SuppressLint;

import com.boc.bocsoft.mobile.bocmobile.ApplicationConst;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.utils.MoneyUtils;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.securityverify.DeviceInfoModel;
import com.boc.bocsoft.mobile.bocmobile.buss.account.base.BaseTransactionFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.account.limit.model.LimitModel;
import com.boc.bocsoft.mobile.bocmobile.buss.account.limit.presenter.LimitContract;
import com.boc.bocsoft.mobile.bocmobile.buss.account.limit.presenter.LimitTransactionPresenter;
import com.boc.bocsoft.mobile.common.utils.NumberUtils;

import java.util.LinkedHashMap;

/**
 * @author wangyang
 *         2016/10/22 14:40
 *         小额凭签名免密/境外磁条交易
 */
@SuppressLint("ValidFragment")
public class LimitOpenConfirmFragment extends BaseTransactionFragment<LimitTransactionPresenter> implements LimitContract.LimitOpenView {

    private LimitModel mainLimit,otherLimit;

    public LimitOpenConfirmFragment(LimitModel mainLimit,LimitModel otherLimit) {
        this.mainLimit = mainLimit;
        this.otherLimit = otherLimit;
    }

    @Override
    protected LimitTransactionPresenter initPresenter() {
        return new LimitTransactionPresenter(this);
    }

    @Override
    public void initData() {
        LinkedHashMap<String,String> data = new LinkedHashMap<>();
        data.put(getString(R.string.boc_account_limit_account), NumberUtils.formatCardNumber(mainLimit.getAccountNumber()));
        data.put(mainLimit.getLimitType().getLimitTitle(mContext), MoneyUtils.transMoneyFormat(mainLimit.getQuota(), ApplicationConst.CURRENCY_CNY));
        confirmInfoView.addData(data);

        getSecurityCombin(ApplicationConst.SERVICE_ID_ACCOUNT_LIMIT);
    }

    @Override
    public void submitTransactionWithSecurity(DeviceInfoModel deviceInfoModel, String factorId, String[] randomNums, String[] encryptPasswords) {
        getPresenter().openLimit(mainLimit,deviceInfoModel,factorId,randomNums,encryptPasswords);
    }

    @Override
    public void onClickConfirm() {
        showLoadingDialog(false);
        getPresenter().openLimitPre(mainLimit,getCurrentFactorId());
    }

    @Override
    public void openLimit(LimitModel limitModel) {
        closeProgressDialog();

        //跳转结果页
        start(new LimitResultFragment(limitModel,otherLimit));
    }
}
