package com.boc.bocsoft.mobile.bocmobile.buss.common.product;

import android.app.Activity;

import com.boc.bocsoft.mobile.bocmobile.ApplicationContext;
import com.boc.bocsoft.mobile.bocmobile.base.utils.LogUtils;
import com.boc.bocsoft.mobile.bocmobile.buss.common.LoginCallback;
import com.boc.bocsoft.mobile.bocmobile.buss.common.lianlong.ModuleDispatcher;
import com.boc.bocsoft.mobile.bocmobile.buss.common.menu.ModuleActivityDispatcher;
import com.boc.bocsoft.mobile.bocmobile.buss.login.ui.LoginContext;
import com.chinamworld.boc.commonlib.BaseCommonTools;

import java.util.Map;

/**
 * 产品页面控制类
 * Created by lxw on 2016/9/27 0027.
 */
public class ProductDispatcher {

    private static final String LOG_TAG = ProductDispatcher.class.getSimpleName();
    /**
     * 分发到基金基金页面
     * @param activity
     * @param productId
     */
    public static void dispatchToFund(Activity activity, String productId){
        LogUtils.i(LOG_TAG, "dispatchToFund start...");
        try{
            LogUtils.i(LOG_TAG, "跳转联龙产品");
            LogUtils.i(LOG_TAG, "产品标识为:" + productId);
            ModuleDispatcher.gotoFincMudule(activity, productId);
        } catch (Exception ex){
            LogUtils.i(LOG_TAG, "跳转联龙产品时发生异常:" + ex.getMessage());
        }

    }

    /**
     * 分发到理财页面
     * @param activity
     * @param productId
     */
    public static void dispatchToInvest(Activity activity, String productId){
        ModuleDispatcher.gotoInvestMudule(activity, productId);
    }

    /**
     * 分发到理财页面
     * @param activity
     * @param buyRate
     * @param sellRate
     * @param sourceCurrencyCode
     * @param targetCurrencyCode
     */
    public static void dispatchToGold(Activity activity, String buyRate, String sellRate, String sourceCurrencyCode, String targetCurrencyCode){
        ModuleDispatcher.gotoGoldMudule(activity, buyRate, sellRate, sourceCurrencyCode, targetCurrencyCode);
    }


    /**
     * 分发到联龙结购汇页面
     * @param activity
     * @param params
     */
    public static void dispatchSBRemitMudule(Activity activity, Map<String, Object> params){

        if (ApplicationContext.getInstance().isLogin()) {
            LoginContext.instance.saveCookiesToLianLong();
            ModuleDispatcher.gotoSBRemitMudule(activity, params);
        } else {
            BaseCommonTools.getInstance().SetCookie(null, null);
            ModuleActivityDispatcher.startToLogin(activity, new LoginCallbackImpl(activity, params));
        }
    }

    /***
     * 登录成功后回调
     */
    static class LoginCallbackImpl implements LoginCallback {
        private Activity activity;
        private Map<String, Object> params;
        public LoginCallbackImpl(Activity activity, Map<String, Object> params){
            this.activity = activity;
            this.params = params;
        }
        @Override
        public void success() {
            ModuleDispatcher.gotoSBRemitMudule(activity, params);
        }
    }

}
