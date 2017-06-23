package com.boc.bocsoft.mobile.bocmobile.buss.creditcard.utils;

import com.boc.bocsoft.mobile.bocmobile.ApplicationConst;

import java.util.ArrayList;

/**
 * 信用卡
 *
 * Created by liuweidong on 2017/1/3.
 */
public class CrcdUtil {

    /**
     * 过滤信用卡账户类型
     */
    public static ArrayList<String> filterAccountType() {
        /*103 104 107*/
        ArrayList<String> accountTypeList = new ArrayList<String>();
        accountTypeList.add(ApplicationConst.ACC_TYPE_ZHONGYIN);
        accountTypeList.add(ApplicationConst.ACC_TYPE_GRE);
        accountTypeList.add(ApplicationConst.ACC_TYPE_SINGLEWAIBI);
        return accountTypeList;
    }
}
