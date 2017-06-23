package com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.purchase.ui;

import android.annotation.SuppressLint;

import com.boc.bocsoft.mobile.bocmobile.ApplicationConst;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.utils.AccountUtils;
import com.boc.bocsoft.mobile.bocmobile.base.utils.MoneyUtils;
import com.boc.bocsoft.mobile.bocmobile.base.utils.PublicCodeUtils;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.securityverify.DeviceInfoModel;
import com.boc.bocsoft.mobile.bocmobile.buss.account.base.BaseTransactionFragment;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.purchase.model.PurchaseModel;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.purchase.presenter.PurchasePresenter;
import com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.wealthproduct.model.WealthListBean;
import com.boc.bocsoft.mobile.common.utils.date.DateFormatters;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by wangtong on 2016/9/19.
 */
@SuppressLint("ValidFragment")
public class PurchaseConfirmFragment extends BaseTransactionFragment<PurchasePresenter> implements PurchaseContact.PurchaseTransactionView {

    private PurchaseModel model;

    public PurchaseConfirmFragment(PurchaseModel model) {
        this.model = model;
    }

    @Override
    public void initView() {
        confirmInfoView.isShowSecurity(false);
    }

    @Override
    protected PurchasePresenter initPresenter() {
        return new PurchasePresenter(this);
    }

    @Override
    public void initData() {
        String buyAmount = MoneyUtils.transMoneyFormat(model.getBuyAmount() + "", model.getCurCode());
        String currency = PublicCodeUtils.getCurrency(mContext, model.getCurCode());
        String cashRemit = AccountUtils.getCashRemit(model.getCashRemitCode());
        if (ApplicationConst.CURRENCY_CNY.equals(model.getCurCode()))
            confirmInfoView.setHeadValue(getString(R.string.boc_purchase_confirm_head_two, currency), buyAmount);
        else
            confirmInfoView.setHeadValue(getString(R.string.boc_purchase_confirm_head_two, currency + "/" + cashRemit), buyAmount);

        LinkedHashMap<String, String> datas = new LinkedHashMap<>();
        datas.put(getString(R.string.boc_purchase_confirm_product), getString(R.string.boc_purchase_product_head, currency, model.getProdName(), model.getProdCode()));
        if (model.isPeriodProduct() && model.getPeriodNumber() > 0)
            datas.put(getString(R.string.boc_purchase_confirm_period), model.getPeriodNumber() + "");

        if (model.isFundProKind())
            datas.put(getString(R.string.boc_purchase_confirm_fee), MoneyUtils.transMoneyFormat(model.getPurchFee(),model.getCurCode()));

        if (model.isCanRedeem() && model.getRedeemDate() != null)
            datas.put(getString(R.string.boc_purchase_confirm_redeem), getString(R.string.boc_purchase_confirm_redeem_date, model.getRedeemDate().format(DateFormatters.dateFormatter1)));

        datas.put(getString(R.string.boc_purchase_confirm_product_risk), model.getProductRisk());
        datas.put(getString(R.string.boc_purchase_confirm_customer_risk), model.getCustomerRisk());

        confirmInfoView.setHint(model.getRiskMessage());
        confirmInfoView.addData(datas, false, false);
    }

    @Override
    public void onClickConfirm() {
        showLoadingDialog(false);
        getPresenter().productBuySubmit(model);
    }

    @Override
    public void productBuySubmit(PurchaseModel purchaseModel,List<WealthListBean> likeBeans) {
        this.model = purchaseModel;
        closeProgressDialog();
        startWithPop(new PurchaseResultFragment(model, likeBeans));
    }

    @Override
    public void submitTransactionWithSecurity(DeviceInfoModel deviceInfoModel, String factorId, String[] randomNums, String[] encryptPasswords) {
        //此方法无用,只是实现子类方法
    }
}
