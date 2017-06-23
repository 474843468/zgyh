package com.boc.bocsoft.mobile.bocmobile.buss.account.virtual.ui.limit;

import android.annotation.SuppressLint;
import com.boc.bocsoft.mobile.bocmobile.ApplicationConst;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.utils.MoneyUtils;
import com.boc.bocsoft.mobile.bocmobile.base.utils.PublicCodeUtils;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.securityverify.DeviceInfoModel;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.securityverify.SecurityVerity;
import com.boc.bocsoft.mobile.bocmobile.buss.account.base.BaseTransactionFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.account.virtual.model.VirtualCardModel;
import com.boc.bocsoft.mobile.bocmobile.buss.account.virtual.presenter.VirCardTransactionPresenter;
import com.boc.bocsoft.mobile.bocmobile.buss.account.virtual.presenter.VirtualCardContract;
import com.boc.bocsoft.mobile.common.utils.NumberUtils;
import com.boc.bocsoft.mobile.common.utils.date.DateFormatters;
import java.util.LinkedHashMap;

/**
 * @author wangyang
 *         16/9/1 23:06
 *         修改交易限额
 */
@SuppressLint("ValidFragment")
public class VirtualUpdateLimitConfirmFragment extends BaseTransactionFragment<VirCardTransactionPresenter> implements VirtualCardContract.VirCardUpdateTransactionView {

    private VirtualCardModel model;

    public VirtualUpdateLimitConfirmFragment(VirtualCardModel model) {
        this.model = model;
    }

    @Override
    protected VirCardTransactionPresenter initPresenter() {
        return new VirCardTransactionPresenter(this);
    }

    @Override
    public void initData() {
        LinkedHashMap<String,String> map = new LinkedHashMap<>();

        map.put(getString(R.string.boc_virtual_account_apply_result_limit_single),PublicCodeUtils.getCurrency(mContext, model.getCurrencyCode()) + " " + MoneyUtils.transMoneyFormat(model.getSignleLimit(), model.getCurrencyCode()));
        map.put(getString(R.string.boc_virtual_account_apply_result_limit_total),PublicCodeUtils.getCurrency(mContext, model.getCurrencyCode()) + " " + MoneyUtils.transMoneyFormat(model.getTotalLimit(), model.getCurrencyCode()));
        confirmInfoView.addData(map);

        map = new LinkedHashMap<>();
        map.put(getString(R.string.boc_virtual_account_detail_update_number),NumberUtils.formatCardNumber(model.getAccountNumber()));
        map.put(getString(R.string.boc_virtual_account_detail_update_account),NumberUtils.formatCardNumber2(model.getAccountIbkNum()));
        map.put(getString(R.string.boc_virtual_account_detail_update_account_name),model.getAccountName());
        map.put(getString(R.string.boc_virtual_account_detail_update_account_valid),model.getStartDate().format(
                DateFormatters.dateFormatter1) + "~" + model.getEndDate().format(DateFormatters.dateFormatter1));
        map.put(getString(R.string.boc_virtual_account_detail_update_account_atotal),PublicCodeUtils.getCurrency(mContext, model.getCurrencyCode()) + " " + MoneyUtils.transMoneyFormat(model.getAtotalLimit(), model.getCurrencyCode()));
        confirmInfoView.addData(map,true);

        getSecurityCombin(ApplicationConst.SERVICE_ID_VIRTUAL_CARD);
    }

    @Override
    public void onClickConfirm() {
        showLoadingDialog(false);
        getPresenter().psnCrcdVirtualCardFunctionSetConfirm(model, SecurityVerity.getInstance().getCurrentSecurityVerifyTypeId());
    }

    @Override
    public void submitTransactionWithSecurity(DeviceInfoModel deviceInfoModel, String factorId, String[] randomNums, String[] encryptPasswords) {
        getPresenter().psnCrcdVirtualCardFunctionSetSubmit(model,deviceInfoModel,factorId,randomNums,encryptPasswords);
    }

    @Override
    public void psnCrcdVirtualCardFunctionSetSubmit(VirtualCardModel model) {
        closeProgressDialog();
        this.model = model;
        start(new VirtualUpdateLimitResultFragment(model));
    }
}
