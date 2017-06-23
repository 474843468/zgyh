package com.boc.bocsoft.mobile.bocmobile.buss.creditcard.cashinstallments.ui;

/**
 * Created by cry7096 on 2016/11/25.
 */

import android.annotation.SuppressLint;
import com.boc.bocsoft.mobile.bocmobile.base.model.AccountBean;
import com.boc.bocsoft.mobile.bocmobile.buss.account.base.BaseServiceBureauFragment;

/**
 * 服务须知Fragment
 * Created by cry7096 on 2016/6/30
 */
@SuppressLint("ValidFragment")
public class CrcdCashServiceBureauFragment extends BaseServiceBureauFragment {

    private int applyType;

    private AccountBean accountBean;

    public CrcdCashServiceBureauFragment(boolean isOnlyClosed, int applyType) {
        super(isOnlyClosed);
        this.applyType = applyType;
    }

    @Override
    protected String getUrl() {
        return "file:///android_asset/webviewcontent/crcd/CashInstallmentNotice.html";
    }
}
