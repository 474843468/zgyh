package com.boc.bocsoft.mobile.bocmobile.buss.account.apply.ui;


import android.annotation.SuppressLint;
import android.view.View;

import com.boc.bocsoft.mobile.bocmobile.base.model.AccountBean;
import com.boc.bocsoft.mobile.bocmobile.buss.account.apply.model.ApplyAccountModel;
import com.boc.bocsoft.mobile.bocmobile.buss.account.base.BaseServiceBureauFragment;

/**
 * 服务须知Fragment
 * Created by liuyang on 2016/6/30
 */
@SuppressLint("ValidFragment")
public class ServiceBureauFragment extends BaseServiceBureauFragment {

    private int applyType;

    private AccountBean accountBean;

    public ServiceBureauFragment() {
        super(false);
        this.applyType = ApplyAccountModel.APPLY_TYPE_REGULAR;
    }

    public ServiceBureauFragment(boolean isOnlyClosed, int applyType) {
        super(isOnlyClosed);
        this.applyType = applyType;
    }

    public ServiceBureauFragment(AccountBean accountBean, boolean isOnlyClosed, int applyType) {
        this(isOnlyClosed,applyType);
        this.accountBean = accountBean;
    }

    @Override
    protected String getUrl() {
        return "file:///android_asset/webviewcontent/bureau/TimeOrDemandDepositNotice.html";
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (applyType) {
            case ApplyAccountModel.APPLY_TYPE_REGULAR:
                AccountApplyFragment fragment = new AccountApplyFragment(accountBean,ApplyAccountModel.APPLY_TYPE_REGULAR);
                fragment.setCallBack(callBack);
                //定期一本通
                start(fragment);
                break;
            case ApplyAccountModel.APPLY_TYPE_CURRENT:
                //活期一本通
                fragment = new AccountApplyFragment(accountBean,ApplyAccountModel.APPLY_TYPE_CURRENT);
                fragment.setCallBack(callBack);
                //定期一本通
                start(fragment);
                break;
        }
    }
}
