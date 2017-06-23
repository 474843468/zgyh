package com.boc.bocsoft.mobile.bocmobile.buss.creditcard;


import com.boc.bocsoft.mobile.bocmobile.ApplicationConfig;
import com.boc.bocsoft.mobile.bocmobile.ApplicationContext;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.activity.web.model.WebInfoProxy;
import com.boc.bocsoft.mobile.bocmobile.base.activity.web.presenter.WebPresenter;
import com.boc.bocsoft.mobile.bocmobile.base.activity.web.ui.WebFragment;
import com.boc.bocsoft.mobile.framework.ui.ActivityManager;

/**
 * Created by taoyongzhen on 2016/11/7.
 */

public class EApplyCreditCardFragment extends WebFragment<Object,WebPresenter>{
    @Override
    public void initData() {
        super.initData();
//        mWebView.getWebView().clearCache(true);
        showLoadingDialog();
        mWebView.loadUrl(ApplicationConfig.EAPPLY_CREDI_CARD_PATH);

    }

    @Override
    protected WebInfoProxy createWebInfoProxy() {
        return new WebInfoProxy();
    }

    @Override
    protected boolean showWebViewProgress() {
        return true;
    }

    @Override
    protected boolean openHeartBeatWhenInitData() {
        if (ApplicationContext.getInstance().isLogin()) {
            return true;
        } else{
            return false;
        }
    }

    @Override
    public void onClosed() {
        if (mActivity.getSupportFragmentManager().getBackStackEntryCount() > 1) {
            mFragmentation.back(mActivity.getSupportFragmentManager());
        } else {
            ActivityManager.getAppManager().finishActivity();
        }
    }


    @Override
    protected WebPresenter initPresenter() {
        return new WebPresenter(this);
    }

    @Override
    protected boolean isHaveTitleBarView() {
        return true;
    }

    @Override
    protected boolean getTitleBarRed() {
        return false;
    }

    @Override
    protected String getTitleValue() {
        return getString(R.string.boc_eapply_creditcard);
    }

    @Override
    protected boolean isDisplayRightIcon() {
        return false;
    }
}
