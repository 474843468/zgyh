package com.boc.bocsoft.mobile.bocmobile.buss.system.home.presenter;

import com.boc.bocsoft.mobile.bocmobile.R;

import java.util.LinkedHashMap;

/**
 * 主页默认设置
 * Created by lxw on 2016/7/22 0022.
 */
public final class HomeDefaultConfig {

    // 默认的广告图片
    public final static int DEFAULT_HOME_ADS = R.drawable.boc_home_default_ad;
    // 默认的广告图片
    public final static int LOGIN_HOME_ADS = R.drawable.boc_home_login_ad;

    // 默认广告的地址
    //public final static String DEFAULT_HOME_ADS_PATH = "file:///android_asset/advertisement/homeDefaultAds.html";
    public final static String DEFAULT_HOME_ADS_PATH = "";
    
    // 默认广告的地址
    public final static String LOGIN_HOME_ADS_PATH = "action://login";

    public final static String[] DEFAULT_MAIN_MENU = new String[]{"account_0000",
            "transfer_0000",
            "bocinvt_0000",
            "creditCard_0000",
            "crossBorderFinance_0000",
            "assetManager_0000",
            "consumeFinance_0000"
    };

    public final static String[] HOME_MODULE_LIST = new String[]{
            "account_0000",
            "transfer_0000",
            "trans_0000",
            "deposit_0000",
            "payee_0000",
            "loan_0000",
            "creditCard_0000",
            "assetManager_0000",
            "atmWithdrawal_0000",
            "phonePayment_0000"
    };

    /**
     * 默认的基金列表
     */
    public final static LinkedHashMap<String, String> HOME_DEFAULT_FUND_MAP = new LinkedHashMap<>();

    static{
        HOME_DEFAULT_FUND_MAP.put("000539", "中银活期宝");
        HOME_DEFAULT_FUND_MAP.put("001677", "中银战略新兴");
    }

    /**
     * 默认的账户贵金属列表
     */
    public final static String[] HOME_DEFAULT_GOLD_LIST = new String[]{"035-001"};

    /**
     * 默认的结购汇列表
     */
    public final static String[] HOME_DEFAULT_FESS_LIST = new String[]{"USD"};
}
