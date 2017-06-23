package com.boc.bocsoft.mobile.bocmobile.buss.loan.common.otherloanapply;

import com.boc.bocsoft.mobile.bocmobile.ApplicationConst;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by XieDu on 2016/7/29.
 */
public class LoanApplyOtherConst {
    public static List<String> currencyList = new ArrayList<String>();
    public static List<String> currencyCodeList = new ArrayList<String>();

    static {
        currencyList.add("美元");
        currencyCodeList.add(ApplicationConst.CURRENCY_USD);
        currencyList.add("日元");
        currencyCodeList.add(ApplicationConst.CURRENCY_JPY);
        currencyList.add("欧元");
        currencyCodeList.add(ApplicationConst.CURRENCY_EUR);
        currencyList.add("港币");
        currencyCodeList.add(ApplicationConst.CURRENCY_HKD);
        currencyList.add("英镑");
        currencyCodeList.add(ApplicationConst.CURRENCY_GBP);
        currencyList.add("澳元");
        currencyCodeList.add(ApplicationConst.CURRENCY_AUD);
        currencyList.add("加元");
        currencyCodeList.add(ApplicationConst.CURRENCY_CAD);
    }

    public static List<String> guawayList = new ArrayList<String>();

    static {
        guawayList.add("房产抵押");
        guawayList.add("有价权利质押");
        guawayList.add("其他");
    }

    public static List<String> guaTypeList = new ArrayList<>();

    static {
        guaTypeList.add("住房");
        guaTypeList.add("商铺");
        guaTypeList.add("土地");
        guaTypeList.add("其他固定资产");
    }
}
