package com.boc.bocsoft.mobile.bocmobile.buss.qrcodepay.utils;

import com.boc.bocsoft.mobile.bocmobile.ApplicationConst;
import com.boc.bocsoft.mobile.bocmobile.base.utils.MoneyUtils;
import com.boc.bocsoft.mobile.common.utils.StringUtils;

import java.math.BigDecimal;

/**
 * Created by wangf on 2016/9/23.
 */
public class QrCodeUtils {

    /**
     * 比较两个数值的大小
     *
     * @return -1表示小于；0代表等于，1代表大于；-5代表传递的值为“”或者null;-10代表转换格式异常
     */
    public static boolean isCompareAmountCanNext(String str1, String str2) {
        boolean isCan = false;
        int mcompareTo = MoneyUtils.compareTo(str1, str2);

        switch (mcompareTo) {
            case -10:
                isCan = false;
                break;
            case -5:
                isCan = false;
                break;
            case -1:
                isCan = true;
                break;
            case 0:
                isCan = true;
                break;
            case 1:
                isCan = false;
                break;
        }
        return isCan;
    }
    
    
    /**
     * 取卡号的后四位
     * @param strNum
     * @return
     */
    public static String getShortCardNum(String strNum){
    	 if(StringUtils.isEmpty(strNum)){
    		 return "";
    	 }
    	return strNum.substring(strNum.length()-4, strNum.length());
    }
    
    
    /**
     * 将带有符号的字符串转为不带符号并且有小数的字符串
     * @param quota
     * @return
     */
    public static String getFormatTransQuota(String quota){
    	if(StringUtils.isEmpty(quota)){
   		 	return "0";
   	 	}
    	
    	if("0".equals(quota)){
    		return "0";
    	}
    	
    	if(quota.length() < 18){
    		return quota;
    	}
    	
    	String str1 = quota.substring(0, quota.length() - 3); 
    	String str2 = quota.substring(quota.length() - 3, quota.length()); 
    	
    	return str1 + "." + str2;
    }
    
    /**
     * 去掉字符串的符号
     * @param quota
     * @return
     */
    public static String getFormatTransQuotaNoSign(String quota){
        BigDecimal b1;
        String str = getFormatTransQuota(quota);
        try{
            b1 = new BigDecimal(str);
        }catch (Exception e){
            e.printStackTrace();
            return str;
        }

    	return b1.toString();
    }


    /**
     * 判断账户类型是否为信用卡
     * @param accountType
     * @return
     */
    public static int judgeAccountIsCredit(String accountType) {
        int flag = -1;
        if (ApplicationConst.ACC_TYPE_ZHONGYIN.equals(accountType) ||
                ApplicationConst.ACC_TYPE_GRE.equals(accountType) ||
                ApplicationConst.ACC_TYPE_SINGLEWAIBI.equals(accountType)) {//信用卡
            flag = 0;
        } else if (ApplicationConst.ACC_TYPE_BRO.equals(accountType)) {//借记卡
            flag = 1;
        }
        return flag;
    }


}
