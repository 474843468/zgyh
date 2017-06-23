package com.boc.bocsoft.mobile.bocmobile.buss.loan.utils;

import android.content.Context;

import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.utils.MoneyUtils;
import com.boc.bocsoft.mobile.bocmobile.base.utils.PublicCodeUtils;
import com.boc.bocsoft.mobile.common.utils.StringUtils;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**贷款数据
 * Created by liuzc on 2016/8/29.
 */
public class DataUtils {
    /**
    * 还款方式
    * B：只还利息
    */

    public static final String LONETYPE_B = "B";
    /**
     * *F：等额本息
     */
    public static final String LONETYPE_F = "F";
    /**
     * G：等额本金
     */
    public static final String LONETYPE_G = "G";
    /**
     * *N：协议还款
     */
    public static final String LONETYPE_N = "N";

    /**
     * 获取还款方式描述
     * @param loanTypeCode 还款方式代码
     * @return
     */
    public static String getLoanTypeDesc(Context context, String loanTypeCode){
        if(StringUtils.isEmpty(loanTypeCode)){
            return "";
        }
        loanTypeCode = loanTypeCode.trim();
        if(loanTypeCode.equals(LONETYPE_B)){
            return context.getResources().getString(R.string.boc_loan_repay_type_only_interest);
        }
        else if(loanTypeCode.equals(LONETYPE_F)){
            return context.getResources().getString(R.string.boc_loan_repay_type_equal_int);
        }
        else if(loanTypeCode.equals(LONETYPE_G)){
            return context.getResources().getString(R.string.boc_loan_repay_equal_capint);
        }
        else if(loanTypeCode.equals(LONETYPE_N)){
            return context.getResources().getString(R.string.boc_loan_repay_type_deal);
        }
        return "";
    }

    /**
     * 获取还款方式描述
     * @param context
     * @param interestType
     * @return
     */
    public static String getRepayTypeDesc(Context context, String interestType, String period){

        if(!StringUtils.isEmpty(interestType) && !StringUtils.isEmpty(period)){
            //中银E贷的还款方式描述
            if(interestType.equals(LONETYPE_B) && (period.equals("01") || period.equals("1"))){
                return context.getResources().getString(R.string.boc_details_repaytypeOne);
            }
            else if(interestType.equals(LONETYPE_B) && period.equals("98")){
                return context.getResources().getString(R.string.boc_details_repaytypeTwo);
            }
        }

        if(!StringUtils.isEmptyOrNull(interestType)){
            //非中银E贷的情况
            if(interestType.equals(LONETYPE_F)){
                return context.getResources().getString(R.string.boc_loan_repay_type_equal_int);
            }
            else if(interestType.equals(LONETYPE_G)){
                return context.getResources().getString(R.string.boc_loan_repay_equal_capint);
            }
            else if(interestType.equals(LONETYPE_B)){
                return context.getResources().getString(R.string.boc_loan_repay_type_only_interest);
            }
            else if(interestType.equals(LONETYPE_N)){
                return context.getResources().getString(R.string.boc_loan_repay_type_deal);
            }
        }
        return "";
    }

    /**
     * 获取币种描述
     * @param context
     * @param letter CNY等
     * @return
     */
    public static String getCurrencyDescByLetter(Context context, String letter){
        //CNY 返回 人民币元
        if(letter != null && letter.equals("CNY")){
            return context.getString(R.string.boc_finance_account_transfer_recharge_max_money);
        }

        String result = PublicCodeUtils.getCurrencyWithLetter(context, letter);
        if(StringUtils.isEmptyOrNull(result) || result.equals("-")){
            return PublicCodeUtils.getCurrency(context, letter);
        }
        return result;
    }

    /**
     * 还款周期描述
     * @param context
     * @param code
     * @return
     */
    public static String getLoanRepayPeriodDesc(Context context, String code){
        if(StringUtils.isEmpty(code)){
            return "";
        }

        if(code.equals("01")){
            return context.getResources().getString(R.string.boc_month);
        }
        else if(code.equals("02")){
            return context.getResources().getString(R.string.boc_month_two);
        }
        else if(code.equals("03")){
            return context.getResources().getString(R.string.boc_month_three);
        }
        else if(code.equals("04")){
            return context.getResources().getString(R.string.boc_month_four);
        }
        else if(code.equals("06")){
            return context.getResources().getString(R.string.boc_month_six);
        }
        else if(code.equals("12")){
            return context.getResources().getString(R.string.boc_year);
        }
        else if(code.equals("14")){
            return context.getResources().getString(R.string.boc_week_two);
        }
        else if(code.equals("98")){
            return context.getResources().getString(R.string.boc_end_date);
        }

        return "";
    }

    /**
     * 以指定格式显示手续费，当手续费<=0时，显示为-
     * @param charges
     * @return
     */
    public static String getFormatCharges(String charges){
        try{
            if(StringUtils.isEmptyOrNull(charges)){
                return " - ";
            }

            if(Double.parseDouble(MoneyUtils.getNormalMoneyFormat(charges)) <= 0){
                return  " - ";
            }

            return charges;
        }catch (Exception e){
            return " - ";
        }
    }

    /**
     * 对origValue进行四舍五入，保留两位小数
     * @param origValue
     * @return
     */
    public static String getFormatMoney(String origValue){
        if(StringUtils.isEmptyOrNull(origValue)){
            return "0.00";
        }

        origValue = MoneyUtils.getNormalMoneyFormat(origValue);
        try{
            BigDecimal bdValue = new BigDecimal(origValue);
            return bdValue.setScale(2, BigDecimal.ROUND_HALF_UP).toString();
        }catch (Exception e){
            e.printStackTrace();
            return "0.00";
        }
    }

    /**
     * 还款期限单位: 年、月、日
     */
    public static final String LOAN_PERIOD_UNIT_YEAR = "Y";
    public static final String LOAN_PERIOD_UNIT_MONTH = "M";
    public static final String LOAN_PERIOD_UNIT_DAY = "D";

    /**
     * 获取还款周期描述
     * @param code
     * @return
     */
    public static String getLoanUnitPeriodUnitDesc(Context context, String code){
        if(StringUtils.isEmpty(code)){
            return "";
        }
        code = code.trim();
        if(code.equals(LOAN_PERIOD_UNIT_YEAR)){
            return context.getResources().getString(R.string.boc_year);
        }
        else if(code.equals(LOAN_PERIOD_UNIT_MONTH)){
            return context.getResources().getString(R.string.boc_month);
        }
        else if(code.equals(LOAN_PERIOD_UNIT_DAY)){
            return context.getResources().getString(R.string.boc_day);
        }

        return "";
    }

}
