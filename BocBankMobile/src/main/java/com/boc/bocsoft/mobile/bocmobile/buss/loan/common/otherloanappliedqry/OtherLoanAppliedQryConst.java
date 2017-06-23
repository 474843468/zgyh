package com.boc.bocsoft.mobile.bocmobile.buss.loan.common.otherloanappliedqry;

import android.content.Context;

import com.boc.bocsoft.mobile.bocmobile.ApplicationConst;
import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.utils.PublicCodeUtils;
import com.boc.bocsoft.mobile.bocmobile.buss.loan.utils.DataUtils;

/**
 * Created by liuzc on 2016/8/15.
 */
public class OtherLoanAppliedQryConst {
    public final static String KEY_QRY_NAME = "keyName"; //字段名：姓名
    public final static String KEY_QRY_PHONE = "keyPhone"; //字段名：手机或邮箱
    public final static String KEY_LOAN_NUMBER = "keyLoanNumber"; //贷款编号
    public final static String KEY_CONVERSATIONID = "conversationID"; //会话ID

    public final static String LOAN_STATUS_SUCCESS = "4"; //贷款状态：已成功处理
    public final static String LOAN_STATUS_FAIL = "3"; //贷款状态：未成功处理


//    public final static int MAX_LENGTH_NAME = 60; //姓名的最大字符个数
//    public final static int MAX_LENGTH_PHONE = 30; //手机号的最大字符个数

    /**
     * 通过货币代码获取货币描述
     * @param currencyCode 货币代码:1、2..
     * @return 货币：人民币、美元..
     */
    public static String getCurrentcyDesc(Context context, String currencyCode){
        String localCurrencyCode= getCurrencyCode(currencyCode);
        return PublicCodeUtils.getCurrency(context, localCurrencyCode);
    }

    /**
     * 获取真实币种代码
     * @param returnCode
     * @return
     */
    public static String getCurrencyCode(String returnCode){
        String result = ApplicationConst.CURRENCY_CNY;
        if(returnCode == null){
            return result;
        }
        returnCode = returnCode.trim();
        if(returnCode.equals("1")){
            result = ApplicationConst.CURRENCY_USD;
        }
        else if(returnCode.equals("2")){
            result = ApplicationConst.CURRENCY_JPY;
        }
        else if(returnCode.equals("3")){
            result = ApplicationConst.CURRENCY_EUR;
        }
        else if(returnCode.equals("4")){
            result = ApplicationConst.CURRENCY_HKD;
        }
        else if(returnCode.equals("5")){
            result = ApplicationConst.CURRENCY_GBP;
        }
        else if(returnCode.equals("6")){
            result = ApplicationConst.CURRENCY_MOP;
        }
        else if(returnCode.equals("7")){
            result = ApplicationConst.CURRENCY_CAD;
        }
        else{
            result = ApplicationConst.CURRENCY_CNY;
        }
        return result;
    }

    /**
     * 获取状态描述
     * @param statusCode 状态码
     * @return 状态描述
     */
    public static String getStatusDesc(Context context, String statusCode){
        String result = context.getResources().getString(R.string.boc_loan_applied_status_wait);
        if(statusCode == null){
            return result;
        }
        statusCode = statusCode.trim();
        if(statusCode.equals(LOAN_STATUS_SUCCESS)){
            result = context.getResources().getString(R.string.boc_loan_applied_status_success);
        }
        else if(statusCode.equals(LOAN_STATUS_FAIL)){
            result = context.getResources().getString(R.string.boc_loan_applied_status_fail);
        }
        else{
            result = context.getResources().getString(R.string.boc_loan_applied_status_wait);
        }
        return result;
    }

    /**
     * 判断是否是邮箱地址格式
     * @param param
     * @return
     */
    public static boolean isEmailAddress(String param){
        return (param != null && param.indexOf("@") > -1);
    }

    /**
     * 性别：男、女
     * @param sexCode
     * @return
     */
    public static String getSexDesc(Context context, String sexCode){
        if(sexCode != null && sexCode.trim().equals("2")){
            return context.getResources().getString(R.string.boc_woman);
        }

        return context.getResources().getString(R.string.boc_man);
    }

    /**
     * 获取担保方式
     * @param context
     * @param code
     * @return
     */
    public static String getGuaWay(Context context, String code){
        if(code == null){
            return context.getResources().getString(R.string.boc_loan_guaway_others);
        }
        code = code.trim();
        if(code.equals("1")){
            return context.getResources().getString(R.string.boc_loan_guaway_house);
        }
        else if(code.equals("2")){
            return context.getResources().getString(R.string.boc_loan_guaway_right);
        }
        else{
            return context.getResources().getString(R.string.boc_loan_guaway_others);
        }
    }

    //是否能提供抵押担保
    public static String getGuaTypeFlag(Context context, String guaType){
        if(guaType != null && guaType.trim().equals("1")){
            return context.getResources().getString(R.string.boc_yes);
        }

        return context.getResources().getString(R.string.boc_no);
    }

    /**
     * 获取担保类别
     * @param guaTypeCode
     * @return
     */
    public static String getGuaTypeDesc(Context context, String guaTypeCode){
        if(guaTypeCode == null){
            return context.getResources().getString(R.string.boc_loan_guatype_other_fixed_property);
        }

        guaTypeCode = guaTypeCode.trim();
        if(guaTypeCode.equals("1")){
            return context.getResources().getString(R.string.boc_loan_guatype_house);
        }
        else if(guaTypeCode.equals("2")){
            return context.getResources().getString(R.string.boc_loan_guatype_shop);
        }
        else if(guaTypeCode.equals("3")){
            return context.getResources().getString(R.string.boc_loan_guatype_land);
        }
        else {
            return context.getResources().getString(R.string.boc_loan_guatype_other_fixed_property);
        }
    }

    //获取拒绝原因
    public static String getRefuseReasonDesc(Context context, String code){
        if(code == null){
            return "";
        }
        code = code.trim();
        if(code.equals("1")){
            return context.getResources().getString(R.string.boc_loan_refuse_reason1);
        }
        else if(code.equals("2")){
            return context.getResources().getString(R.string.boc_loan_refuse_reason2);
        }
        else if(code.equals("3")){
            return context.getResources().getString(R.string.boc_loan_refuse_reason3);
        }
        else {
            return "";
        }
    }
}
