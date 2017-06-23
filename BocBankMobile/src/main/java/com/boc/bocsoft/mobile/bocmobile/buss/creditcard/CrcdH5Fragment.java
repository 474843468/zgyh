package com.boc.bocsoft.mobile.bocmobile.buss.creditcard;

import com.boc.bocsoft.mobile.bocmobile.base.activity.web.model.WebInfoProxy;
import com.boc.bocsoft.mobile.bocmobile.base.activity.web.ui.WebFragment;
import com.boc.bocsoft.mobile.framework.ui.BasePresenter;

/**
 * 信用卡H5相关跳转页面
 * Created by liuweidong on 2016/12/19.
 */

public class CrcdH5Fragment extends WebFragment{
    public static final String CRCD_ACTIVATION = "https://22.188.135.115/bocphone/Framework/index.html?entrance=SouvenirCoin_cardActivate";

    @Override
    protected boolean isHaveTitleBarView() {
        return false;
    }

    @Override
    public void initData() {
        super.initData();
        mWebView.loadUrl(CRCD_ACTIVATION);
//        List<Cookie> coolies = BIIClient.instance.getCookies().getCookies();
//        for(Cookie cookie:coolies){
//            LogUtils.d("boc",cookie.domain()+"   name:"+cookie.name()+"--value:"+cookie.value());
//            CookieManager.getInstance().setCookie(cookie.path(),cookie.value());
//        }

    }

    @Override
    protected WebInfoProxy createWebInfoProxy() {
        return new WebInfoProxy();
    }

    @Override
    protected BasePresenter initPresenter() {
        return null;
    }

    @Override
    public void onClosed() {
        pop();
    }

}
