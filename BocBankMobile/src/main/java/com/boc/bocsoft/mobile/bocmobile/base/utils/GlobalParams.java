package com.boc.bocsoft.mobile.bocmobile.base.utils;

import com.boc.bocsoft.mobile.common.utils.StringUtils;

/**
 * 账户公共类
 * Created by niuguboin on 2016/6/22.
 */
public class GlobalParams {

    public static String CONFIG_NAME = "config";

    public static double MAX_QUOTA = 300000;
    /**
     * 修改账户别名正则表达式
     */
    public static String AVAIABLE_ALISA = "^\\w{0,20}$";

    /**
     * 调用规则验证账户别名是否合法
     * @param s
     * @return
     */
    public static boolean isValidName(String s) {
        if(StringUtils.isEmptyOrNull(s))
            return false;

        if(s.matches(AVAIABLE_ALISA))
            return true;
        else
            return false;
    }
}
