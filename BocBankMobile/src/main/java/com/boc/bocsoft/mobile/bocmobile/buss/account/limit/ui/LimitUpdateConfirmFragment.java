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
 *         2016/10/18 15:47
 *         关闭小额/凭签名免密/境外磁条交易
 */
@SuppressLint("ValidFragment")
public class LimitUpdateConfirmFragment extends BaseTransactionFragment<LimitTransactionPresenter> implements LimitContract.LimitUpdateView{

    private LimitModel limitModel;

    public LimitUpdateConfirmFragment(LimitModel limitModel) {
        this.limitModel = limitModel;
    }

    @Override
    protected LimitTransactionPresenter initPresenter() {
        return new LimitTransactionPresenter(this);
    }

    @Override
    public void initData() {
        LinkedHashMap<String,String> data = new LinkedHashMap<>();
        data.put(getString(R.string.boc_account_limit_account), NumberUtils.formatCardNumber(limitModel.getAccountNumber()));
        data.put(limitModel.getLimitType().getLimitTitle(mContext), MoneyUtils.transMoneyFormat(limitModel.getQuota(), ApplicationConst.CURRENCY_CNY));
        confirmInfoView.addData(data);

        getSecurityCombin(ApplicationConst.SERVICE_ID_ACCOUNT_LIMIT);
    }

    @Override
    public void onClickConfirm() {
        showLoadingDialog(false);
        getPresenter().updateLimitPre(limitModel,getCurrentFactorId());
    }


    @Override
    public void submitTransactionWithSecurity(DeviceInfoModel deviceInfoModel, String factorId, String[] randomNums, String[] encryptPasswords) {
        getPresenter().updateLimit(limitModel,deviceInfoModel,factorId,randomNums,encryptPasswords);
    }

    @Override
    public void updateLimit(LimitModel limitModel) {
        closeProgressDialog();
        popTo(LimitFragment.class,false);
        findFragment(LimitFragment.class).reload(limitModel,null);
    }
}
