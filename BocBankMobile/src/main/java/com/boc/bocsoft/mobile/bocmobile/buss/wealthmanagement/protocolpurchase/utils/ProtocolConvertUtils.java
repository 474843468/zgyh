package com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.protocolpurchase.utils;

import android.content.Context;

import com.boc.bocsoft.mobile.bocmobile.R;
import com.boc.bocsoft.mobile.common.utils.StringUtils;

/**
 * Created by liuweidong on 2016/11/5.
 */
public class ProtocolConvertUtils {
    /**
     * 购买频率
     *
     * @param isNeedPur
     * @return
     */
    public static String convertIsNeedPur(String isNeedPur) {
        String convertStr = "";
        if (StringUtils.isEmptyOrNull(isNeedPur)) {
            return "";
        }
        if ("0".equals(isNeedPur)) {
            convertStr = "期初申购";
        } else if ("1".equals(isNeedPur)) {
            convertStr = "不申购";
        }
        return convertStr;
    }

    /**
     * 赎回频率
     *
     * @param isNeedRed
     * @return
     */
    public static String convertIsNeedRed(String isNeedRed) {
        String convertStr = "";
        if (StringUtils.isEmptyOrNull(isNeedRed)) {
            return "";
        }
        if ("0".equals(isNeedRed)) {
            convertStr = "期末赎回";
        } else if ("1".equals(isNeedRed)) {
            convertStr = "不赎回";
        }
        return convertStr;
    }


    /**
     * 客户风险等级	String	1：保守型投资者
     * 2：稳健型投资者
     * 3：平衡型投资者
     * 4：成长型投资者
     * 5：进取型投资者
     * 未做过风险评估 /不存在此客户返回空格
     */
    public static String getCustRisk(String custRisk) {
        String custRiskStr = "";
        if (StringUtils.isEmptyOrNull(custRisk)) {
            return "";
        }
        if ("1".equals(custRisk)) {
            custRiskStr = "保守型投资者";
        } else if ("2".equals(custRisk)) {
            custRiskStr = "稳健型投资者";
        } else if ("3".equals(custRisk)) {
            custRiskStr = "平衡型投资者";
        } else if ("4".equals(custRisk)) {
            custRiskStr = "成长型投资者";
        } else if ("5".equals(custRisk)) {
            custRiskStr = "进取型投资者";
        }
        return custRiskStr;
    }

    /**
     * 产品风险级别	String	0：低风险产品
     * 1：中低风险产品
     * 2：中等风险产品
     * 3：中高风险产品
     * 4：高风险产品
     */
    public static String getProRisk(String proRisk) {
        String proRiskStr = "";
        if (StringUtils.isEmptyOrNull(proRisk)) {
            return "";
        }
        if ("0".equals(proRisk)) {
            proRiskStr = "低风险产品";
        } else if ("1".equals(proRisk)) {
            proRiskStr = "中低风险产品";
        } else if ("2".equals(proRisk)) {
            proRiskStr = "中等风险产品";
        } else if ("3".equals(proRisk)) {
            proRiskStr = "中高风险产品";
        } else if ("4".equals(proRisk)) {
            proRiskStr = "高风险产品";
        }
        return proRiskStr;
    }

    /**
     * 投资周期
     *
     * @param periodAgr
     * @return
     */
    public static String convertPeriodAgr(String periodAgr) {
        String convertStr = "";
        if (StringUtils.isEmptyOrNull(periodAgr)) {
            return "";
        }
        char c = periodAgr.charAt(periodAgr.length() - 1);
        String value = periodAgr.substring(0, periodAgr.length() - 1);
        if ("d".equals(String.valueOf(c))) {
            convertStr = value + "天";
        } else if ("m".equals(String.valueOf(c))) {
            convertStr = value + "月";
        } else if ("w".equals(String.valueOf(c))) {
            convertStr = value + "周";
        } else if ("y".equals(String.valueOf(c))) {
            convertStr = value + "年";
        }
        return convertStr;
    }

    /**
     * 风险揭示信息
     *
     * @param context
     * @param riskMsg
     * @return
     */
    public static String convertRiskMsg(Context context, String riskMsg) {
        String convertStr = "";
        if (StringUtils.isEmptyOrNull(riskMsg)) {
            return "";
        }
        if ("0".equals(riskMsg.trim())) {
            convertStr = context.getString(R.string.boc_protocol_risk_message_0);
        } else if ("1".equals(riskMsg.trim())) {
            convertStr = context.getString(R.string.boc_protocol_risk_message_1);
        } else if ("2".equals(riskMsg.trim())) {
            convertStr = context.getString(R.string.boc_protocol_risk_message_2);
        }
        return convertStr;
    }

}
