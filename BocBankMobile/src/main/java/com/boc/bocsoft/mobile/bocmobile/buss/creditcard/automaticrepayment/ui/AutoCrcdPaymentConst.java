package com.boc.bocsoft.mobile.bocmobile.buss.creditcard.automaticrepayment.ui;

import java.util.ArrayList;
import java.util.List;

/**
 * Name: liukai
 * Time：2016/11/25 16:22.
 * Created by lk7066 on 2016/11/25.
 * It's used to
 */

public class AutoCrcdPaymentConst {

    public static List<String> autoPayMoney = new ArrayList<String>();

    static {
        autoPayMoney.add("全额还款");
        autoPayMoney.add("最低额还款");
    }

    public static List<String> autoPayWay = new ArrayList<String>();

    static {
        autoPayWay.add("均以人民币元还款");
        autoPayWay.add("人民币元和外币结欠均以相应账户还款");
        autoPayWay.add("外币结欠以相应账户还款");
    }

}
