package com.boc.bocsoft.mobile.bocmobile.buss.common.util;

import android.content.Context;

import com.boc.bocsoft.mobile.bocmobile.base.model.FundBean;
import com.boc.bocsoft.mobile.bocmobile.base.utils.PublicCodeUtils;
import com.boc.bocsoft.mobile.common.utils.StringUtils;

import java.math.BigDecimal;

/**
 * 投资理财工具类
 * Created by lxw on 2016/9/14 0014.
 */
public class InvestUtils {

    /**
     * 获取货币对
     * @param source
     * @param target
     * @return
     */
    public static String getCurrencyPair(String source, String target){

        if (StringUtils.isEmptyOrNull(source) || StringUtils.isEmptyOrNull(target)){
            return "";
        }
        return source + "-" + target;
    }

    /**
     * 获取货币对名称
     * @param source
     * @param target
     * @return
     */
    public static String getCurrencyPairName(Context context, String source, String target){
        return PublicCodeUtils.getGoldCurrencyCode(context, source)
                + "/" + PublicCodeUtils.getGoldCurrencyCode(context, target);
    }

    /**
     * 通过货币对获取货币码
     * @param pair
     * @return
     */
    public static String[] getCurrencyCodeByPair(String pair){
        String[] result = new String[2];
        if (!StringUtils.isEmptyOrNull(pair)) {
            result = pair.split("-");
        }
        return result;
    }

    /**
     * 计算基金的起购金额
     * @param fundBean
     * @return
     */
    public static String fundBoyLimitMoney(FundBean fundBean) {

        if (fundBean != null) {
            String money = "--";

            if ("Y".equals(fundBean.getIsBuy())) {
                money = fundBean.getOrderLowLimit().toString();
            } else if ("Y".equals(fundBean.getIsInvt())) {
                money = fundBean.getApplyLowLimit().toString();
            }
            if (StringUtils.isEmptyOrNull(money)) {
                return money;
            }


            try {
                BigDecimal tmp = new BigDecimal(money);
               return getLimitMoney(tmp);
            } catch (Exception ex) {
                return money;
            }
        }
        return "--";
    }

    public static String getLimitMoney(BigDecimal money){
        if(money == null){
            return "--";
        }
        try {
            BigDecimal tmp = money;
            if (tmp.compareTo(new BigDecimal("10000")) >= 0) {
                String value = tmp.divide(new BigDecimal("10000")).toString();
                String[] arr = value.split("\\.");

                if (arr.length == 1) {
                    return  value + "万元起购";
                }
                else if (arr.length == 2 && arr[1].length() <= 2) {
                    return  value + "万元起购";
                } else {
                    return money + "元起购";
                }
                //System.out.println(value);
            } else {
                return money + "元起购";
            }

        } catch (Exception ex) {
            return money.toString();
        }
    }
}
