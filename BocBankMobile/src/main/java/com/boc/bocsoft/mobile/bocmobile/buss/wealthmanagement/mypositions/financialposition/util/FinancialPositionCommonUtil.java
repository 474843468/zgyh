package com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.mypositions.financialposition.util;

import com.boc.bocsoft.mobile.common.utils.StringUtils;

/**
 * Created by yx on 2016/11/10.
 * 中银理财-持仓公共方法处理
 */
public class FinancialPositionCommonUtil {
    /**
     * 预计年收益率（最大值）	String	不带%号，如果不为0，与yearlyRR字段组成区间
     *
     * @param mYearlyRRMax
     * @return true 不等于0 false等于0 或者格式转换异常
     */
    public static boolean isShowMax(String mYearlyRRMax) {
        if (StringUtils.isEmptyOrNull(mYearlyRRMax)) {
            return false;
        }
        try {
            float yearlyRRMax = Float.parseFloat(mYearlyRRMax);
            if (yearlyRRMax != 0) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            if (!"0".equalsIgnoreCase(mYearlyRRMax) && !"0.00".equalsIgnoreCase(mYearlyRRMax)
                    && !"0.".equalsIgnoreCase(mYearlyRRMax) && !"0.0".equalsIgnoreCase(mYearlyRRMax)
                    && !"0.000".equalsIgnoreCase(mYearlyRRMax) && !"0.0000".equalsIgnoreCase(mYearlyRRMax)
                    && !"0.00000".equalsIgnoreCase(mYearlyRRMax) && !"0.000000".equalsIgnoreCase(mYearlyRRMax)) {
                return true;
            } else {
                return false;
            }
        }
    }

    /**
     * 是否不等于零  true 不等于0，false  等于0
     *
     * @param mExpProfit
     * @return
     */
    public static boolean isUnequalToZero(String mExpProfit) {
        if (StringUtils.isEmptyOrNull(mExpProfit)) {
            return false;
        }
        try {
            float yearlyRRMax = Float.parseFloat(mExpProfit);
            if (yearlyRRMax != 0) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            if (!"0".equalsIgnoreCase(mExpProfit) && !"0.00".equalsIgnoreCase(mExpProfit)
                    && !"0.".equalsIgnoreCase(mExpProfit) && !"0.0".equalsIgnoreCase(mExpProfit)
                    && !"0.000".equalsIgnoreCase(mExpProfit) && !"0.0000".equalsIgnoreCase(mExpProfit)
                    && !"0.00000".equalsIgnoreCase(mExpProfit) && !"0.000000".equalsIgnoreCase(mExpProfit)) {
                return true;
            } else {
                return false;
            }
        }
    }
}
