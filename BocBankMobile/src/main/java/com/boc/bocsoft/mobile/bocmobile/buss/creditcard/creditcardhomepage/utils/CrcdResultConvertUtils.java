package com.boc.bocsoft.mobile.bocmobile.buss.creditcard.creditcardhomepage.utils;

import android.content.Context;

import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.bocmobile.base.utils.MoneyUtils;
import com.boc.bocsoft.mobile.common.utils.StringUtils;

/**
 * Created by liuweidong on 2016/12/10.
 */

public class CrcdResultConvertUtils {
    /**
     * 实时余额标志位
     *
     * @param rtBalanceFlag
     * @return
     */
    public static String convertRtBalanceFlag(Context context, String rtBalanceFlag) {
        String convertStr = "";
        if (StringUtils.isEmptyOrNull(rtBalanceFlag)) {
            return convertStr;
        }
        switch (rtBalanceFlag) {
            case "0":
                convertStr = context.getString(R.string.boc_crcd_home_balance_0);
                break;
            case "1":
            case "2":
                convertStr = context.getString(R.string.boc_crcd_home_balance_1);
                break;
            default:
                break;
        }
        return convertStr;
    }

    /**
     * 卡状态表
     * @param carStatus
     * @return
     */
    public static String convertCarStatus(String carStatus){
        String convertStr = "";
        if (StringUtils.isEmptyOrNull(carStatus)) {
            return "正常";
        }
        switch (carStatus){
            case "ACCC":
                convertStr = "销户销卡";
                break;
            case "ACTP":
                convertStr = "卡未激活";
                break;
            case "CANC":
                convertStr = "卡片取消";
                break;
            case "DCFR":
                convertStr = "欺诈拒绝";
                break;
            case "FRAU":
                convertStr = "欺诈冻结";
                break;
            case "FUSA":
                convertStr = "首次使用超限";
                break;
            case "LOST":
                convertStr = "挂失";
                break;
            case "LOCK":
                convertStr = "锁卡";
                break;
            case "PIFR":
                convertStr = "没收卡";
                break;
            case "PINR":
                convertStr = "密码输入错误超次";
                break;
            case "QCPB":
                convertStr = "卡预销户";
                break;
            case "REFR":
                convertStr = "欺诈卡，需联系发卡行";
                break;
            case "STOL":
                convertStr = "偷窃卡";
                break;
            case "STPP":
                convertStr = "止付";
                break;
            case "BFRA":
                convertStr = "分行冻结";
                break;
            case "CFRA":
                convertStr = "催收冻结";
                break;
            case "SFRA":
                convertStr = "客服冻结";
                break;
            default:
                convertStr = "正常";
                break;
        }
        return convertStr;
    }


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

}
