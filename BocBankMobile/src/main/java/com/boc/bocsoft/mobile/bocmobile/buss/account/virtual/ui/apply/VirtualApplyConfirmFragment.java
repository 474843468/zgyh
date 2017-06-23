package com.boc.bocsoft.mobile.bocmobile.buss.account.virtual.ui.apply;

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
 *         申请虚拟卡确认页
 */
@SuppressLint("ValidFragment")
public class VirtualApplyConfirmFragment extends BaseTransactionFragment<VirCardTransactionPresenter> implements VirtualCardContract.VirCardApplyTransactionView {

    private VirtualCardModel model;

    public VirtualApplyConfirmFragment(VirtualCardModel model) {
        this.model = model;
    }

    @Override
    protected VirCardTransactionPresenter initPresenter() {
        return new VirCardTransactionPresenter(this);
    }

    @Override
    protected String getTitleValue() {
        return getString(R.string.boc_fragment_confirm_title);
    }

    @Override
    public void initData() {
        LinkedHashMap<String,String> map = new LinkedHashMap<>();
        map.put(getString(R.string.boc_virtual_account_apply_info_account),NumberUtils.formatCardNumber(model.getAccountNumber()));
        map.put(getString(R.string.boc_virtual_account_apply_info_name),model.getAccountName());
        confirmInfoView.addData(map);

        map = new LinkedHashMap<>();
        map.put(getString(R.string.boc_virtual_account_apply_info_date_valid),model.getStartDate().format(
                DateFormatters.dateFormatter1) + " ~ " + model.getEndDate().format(DateFormatters.dateFormatter1));
        map.put(getString(R.string.boc_virtual_account_detail_info_single_limit),PublicCodeUtils.getCurrency(mContext, model.getCurrencyCode()) + " " + MoneyUtils.transMoneyFormat(model.getSignleLimit(), model.getCurrencyCode()));
        map.put(getString(R.string.boc_virtual_account_detail_info_total_limit),PublicCodeUtils.getCurrency(mContext, model.getCurrencyCode()) + " " + MoneyUtils.transMoneyFormat(model.getTotalLimit(), model.getCurrencyCode()));

        confirmInfoView.addData(map,true);

        getSecurityCombin(ApplicationConst.SERVICE_ID_VIRTUAL_CARD);
    }

    @Override
    public void onClickConfirm() {
        showLoadingDialog(false);
        getPresenter().psnCrcdVirtualCardApplyConfirm(model, SecurityVerity.getInstance().getCurrentSecurityVerifyTypeId());
    }

    @Override
    public void psnCrcdVirtualCardApplySubmit(VirtualCardModel model) {
        closeProgressDialog();
        this.model = model;
        start(new VirtualCardApplyResultFragment(model));
    }

    @Override
    public void submitTransactionWithSecurity(DeviceInfoModel deviceInfoModel, String factorId, String[] randomNums, String[] encryptPasswords) {
        getPresenter().psnCrcdVirtualCardApplySubmit(model, deviceInfoModel, factorId, randomNums, encryptPasswords);
    }
}
