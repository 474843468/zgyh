package com.boc.bocsoft.mobile.bocmobile.buss.account.limit.ui.limit;

import android.annotation.SuppressLint;

import com.boc.bocsoft.mobile.bocmobile.ApplicationConst;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.utils.MoneyUtils;
import com.boc.bocsoft.mobile.bocmobile.base.utils.PublicCodeUtils;
import com.boc.bocsoft.mobile.bocmobile.base.utils.ToastUtils;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.securityverify.DeviceInfoModel;
import com.boc.bocsoft.mobile.bocmobile.buss.account.base.BaseTransactionFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.account.limit.model.QuotaModel;
import com.boc.bocsoft.mobile.bocmobile.buss.account.limit.presenter.LimitContract;
import com.boc.bocsoft.mobile.bocmobile.buss.account.limit.presenter.LimitTransactionPresenter;

import java.util.LinkedHashMap;

/**
 * @author wangyang
 *         2016/10/17 14:49
 *         修改限额确认界面
 */
@SuppressLint("ValidFragment")
public class LimitSetConfirmFragment extends BaseTransactionFragment<LimitTransactionPresenter> implements LimitContract.QuotaTransactionView {

    private QuotaModel quotaModel;

    public LimitSetConfirmFragment(QuotaModel quotaModel) {
        this.quotaModel = quotaModel;
    }

    @Override
    protected LimitTransactionPresenter initPresenter() {
        return new LimitTransactionPresenter(this);
    }

    @Override
    public void initData() {
        LinkedHashMap<String,String> data = new LinkedHashMap<>();

        if(quotaModel.isUpdateAtm())
            data.put(getString(R.string.boc_account_limit_update_atm), PublicCodeUtils.getCurrency(mContext,quotaModel.getCurrency())+" "+ MoneyUtils.transMoneyFormat(quotaModel.getTransDay(),quotaModel.getCurrency()));

        if(quotaModel.isUpdatePos())
            data.put(getString(R.string.boc_account_limit_update_pos), PublicCodeUtils.getCurrency(mContext,quotaModel.getCurrency())+" "+ MoneyUtils.transMoneyFormat(quotaModel.getAllDayPOS(),quotaModel.getCurrency()));

        if(quotaModel.isUpdateAtmCash())
            data.put(getString(R.string.boc_account_limit_update_atm_cash), PublicCodeUtils.getCurrency(mContext,quotaModel.getCurrency())+" "+ MoneyUtils.transMoneyFormat(quotaModel.getCashDayATM(),quotaModel.getCurrency()));

        if(quotaModel.isUpdateBorderPos())
            data.put(getString(R.string.boc_account_limit_update_border_pos), PublicCodeUtils.getCurrency(mContext,quotaModel.getCurrency())+" "+ MoneyUtils.transMoneyFormat(quotaModel.getConsumeForeignPOS(),quotaModel.getCurrency()));

        if(quotaModel.isUpdateBorderAtm())
            data.put(getString(R.string.boc_account_limit_update_border_atm), PublicCodeUtils.getCurrency(mContext,quotaModel.getCurrency())+" "+ MoneyUtils.transMoneyFormat(quotaModel.getCashDayForeignATM(),quotaModel.getCurrency()));

        confirmInfoView.addData(data);

        getSecurityCombin(ApplicationConst.SERVICE_ID_ACCOUNT_LIMIT_SET);
    }

    @Override
    public void onClickConfirm() {
        showLoadingDialog(false);
        getPresenter().setQuotaPre(quotaModel,getCurrentFactorId());
    }

    @Override
    public void submitTransactionWithSecurity(DeviceInfoModel deviceInfoModel, String factorId, String[] randomNums, String[] encryptPasswords) {
        getPresenter().setQuota(quotaModel,deviceInfoModel,factorId,randomNums,encryptPasswords);
    }

    @Override
    public void setQuota(boolean isSuccess) {
        closeProgressDialog();

        ToastUtils.showLong(getString(R.string.boc_account_limit_update_success));
        popTo(LimitSetFragment.class,false);
        findFragment(LimitSetFragment.class).queryAllQuota(quotaModel);
    }
}
