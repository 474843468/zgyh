package com.boc.bocsoft.mobile.bocmobile.buss.account.apply.ui;

import android.annotation.SuppressLint;

import com.boc.bocsoft.mobile.bocmobile.ApplicationConst;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.securityverify.DeviceInfoModel;
import com.boc.bocsoft.mobile.bocmobile.buss.account.apply.model.ApplyAccountModel;
import com.boc.bocsoft.mobile.bocmobile.buss.account.apply.presenter.ApplyContract;
import com.boc.bocsoft.mobile.bocmobile.buss.account.apply.presenter.ApplyPresenter;
import com.boc.bocsoft.mobile.bocmobile.buss.account.base.BaseTransactionFragment;
import com.boc.bocsoft.mobile.common.utils.NumberUtils;

import java.util.LinkedHashMap;

/**
 * @author wangyang
 *         16/8/2 18:16
 *         申请定期/活期确认页面
 */
@SuppressLint("ValidFragment")
public class AccountApplyConfirmFragment extends BaseTransactionFragment<ApplyPresenter> implements ApplyContract.ApplyAccountView{

    /**
     * 申请定期/活期Model
     */
    private ApplyAccountModel applyAccountModel;

    public AccountApplyConfirmFragment(ApplyAccountModel applyAccountModel) {
        this.applyAccountModel = applyAccountModel;
    }

    @Override
    protected String getTitleValue() {
        return getString(R.string.boc_fragment_confirm_title);
    }

    @Override
    public void initData() {
        LinkedHashMap<String,String> map = new LinkedHashMap<>();

        //设置内容
        map.put(getString(R.string.boc_account), NumberUtils.formatCardNumber(applyAccountModel.getAccountNumber()));
        map.put(getString(R.string.boc_accountTypeSMS), applyAccountModel.getAccountTypeSMS()+getString(R.string.boc_account_name));
        String purpose = applyAccountModel.getAccountPurposeString().replace(",","，");
        map.put(getString(R.string.boc_accountPurpose), purpose);

        confirmInfoView.addData(map);

        //获取安全因子(借记卡)
        getSecurityCombin(ApplicationConst.SERVICE_ID_APPLY_ACCOUNT);
    }

    @Override
    protected ApplyPresenter initPresenter() {
        return new ApplyPresenter(this);
    }

    @Override
    public void onClickConfirm() {
        showLoadingDialog(false);
        getPresenter().psnApplyTermDepositeConfirm(applyAccountModel,getCurrentFactorId());
    }

    @Override
    public void depositeResultSuccess(ApplyAccountModel applyAccountModel) {
        closeProgressDialog();
        AccountApplyResultFragment fragment = new AccountApplyResultFragment(applyAccountModel);
        fragment.setCallBack(callBack);
        startWithPop(fragment);
    }

    @Override
    public void submitTransactionWithSecurity(DeviceInfoModel deviceInfoModel, String factorId, String[] randomNums, String[] encryptPasswords) {
        getPresenter().psnApplyTermDepositeResult(applyAccountModel,deviceInfoModel,factorId,randomNums,encryptPasswords);
    }
}
