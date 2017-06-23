package com.boc.bocsoft.mobile.bocmobile.buss.account.finance.ui;

import android.annotation.SuppressLint;

import com.boc.bocsoft.mobile.bocmobile.ApplicationConst;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.securityverify.DeviceInfoModel;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.securityverify.SecurityVerity;
import com.boc.bocsoft.mobile.bocmobile.buss.account.base.BaseTransactionFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.account.finance.model.FinanceModel;
import com.boc.bocsoft.mobile.bocmobile.buss.account.finance.presenter.FinanceContract;
import com.boc.bocsoft.mobile.bocmobile.buss.account.finance.presenter.FinancePresenter;
import com.boc.bocsoft.mobile.common.utils.NumberUtils;

import java.util.LinkedHashMap;

/**
 * @author wangyang
 *         16/6/23 15:49
 *         新建签约关系确认界面
 */
@SuppressLint("ValidFragment")
public class FinanceSignConfirmFragment extends BaseTransactionFragment<FinancePresenter> implements FinanceContract.FinanceAccountSignView{

    /**
     * 新建签约关系Model
     */
    private FinanceModel financeModel;

    public FinanceSignConfirmFragment(FinanceModel financeModel) {
        this.financeModel = financeModel;
    }


    @Override
    public void initData() {
        LinkedHashMap<String,String> map = new LinkedHashMap<>();

        //设置内容
        map.put(getString(R.string.boc_finance_account_sign_finance_account), NumberUtils.formatCardNumber(financeModel.getFinanICNumber()));
        map.put(getString(R.string.boc_finance_account_sign_account), NumberUtils.formatCardNumber(financeModel.getBankAccountNumber()));
        confirmInfoView.addData(map);

        //获取安全因子
        getSecurityCombin(ApplicationConst.SERVICE_ID_FINANCE_SIGN);
    }

    @Override
    protected FinancePresenter initPresenter() {
        return new FinancePresenter(this);
    }

    @Override
    public void onClickConfirm() {
        showLoadingDialog(false);
        getPresenter().psnFinanceICSignCreate(financeModel,SecurityVerity.getInstance().getCurrentSecurityVerifyTypeId());
    }

    /**
     * 新建签约关系回调
     */
    @Override
    public void psnFinanceICSignCreateResSuccess() {
        closeProgressDialog();

        //跳转结果页
        start(new FinanceSignResultFragment(financeModel));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //防止退回上个页面,签约关系变化
        financeModel.setBankAccountId(null);
        financeModel.setBankAccountNumber(null);
    }

    @Override
    public void submitTransactionWithSecurity(DeviceInfoModel deviceInfoModel, String factorId, String[] randomNums, String[] encryptPasswords) {
        getPresenter().psnFinanceICSignCreateRes(financeModel,deviceInfoModel,factorId,randomNums,encryptPasswords);
    }
}