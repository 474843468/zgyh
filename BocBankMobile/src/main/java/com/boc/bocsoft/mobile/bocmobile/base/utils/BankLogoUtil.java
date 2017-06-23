package com.boc.bocsoft.mobile.bocmobile.base.utils;

import com.boc.bocsoft.mobile.bocmobile.R;

import java.util.HashMap;
import java.util.Map;

/**
 * BankLogoUtil：银行logo工具类
 * Created by zhx on 2016/7/20
 */
public class BankLogoUtil {
    private static Map<String, Integer> mBankLogoMap = new HashMap<String, Integer>();
    private static Map<String, Integer> mBankLogoMap2 = new HashMap<String, Integer>();

    static {
        mBankLogoMap.put("中国银行", R.drawable.ic_bank_logo_18);
        mBankLogoMap.put("中国工商银行", R.drawable.ic_bank_logo_02);
        mBankLogoMap.put("中国农业银行", R.drawable.ic_bank_logo_11);
        mBankLogoMap.put("中国建设银行", R.drawable.ic_bank_logo_07);
        mBankLogoMap.put("交通银行", R.drawable.ic_bank_logo_08);
        mBankLogoMap.put("招商银行", R.drawable.ic_bank_logo_17);
        mBankLogoMap.put("中国邮政储蓄银行", R.drawable.ic_bank_logo_16);
        mBankLogoMap.put("中国民生银行", R.drawable.ic_bank_logo_10);
        mBankLogoMap.put("兴业银行", R.drawable.ic_bank_logo_15);
        mBankLogoMap.put("中信银行", R.drawable.ic_bank_logo_19);
        mBankLogoMap.put("上海浦东发展银行", R.drawable.ic_bank_logo_14);
        mBankLogoMap.put("中国光大银行", R.drawable.ic_bank_logo_03);
        mBankLogoMap.put("平安银行（原深圳发展银行）", R.drawable.ic_bank_logo_13);
        mBankLogoMap.put("华夏银行", R.drawable.ic_bank_logo_06);
        mBankLogoMap.put("广发银行股份有限公司", R.drawable.ic_bank_logo_04);
        mBankLogoMap.put("国家开发银行", R.drawable.ic_bank_logo_05);
        mBankLogoMap.put("中国进出口银行", R.drawable.ic_bank_logo_09);
        mBankLogoMap.put("中国农业发展银行", R.drawable.ic_bank_logo_12);
        mBankLogoMap.put("江苏银行", R.drawable.ic_bank_logo_20);
        mBankLogoMap.put("其他银行", R.drawable.other_bank_logo);

        mBankLogoMap2.put("中国银行", R.drawable.ic_bank_logo_18);
        mBankLogoMap2.put("中国工商银行", R.drawable.ic_bank_logo_02);
        mBankLogoMap2.put("中国农业银行股份有限公司", R.drawable.ic_bank_logo_11);
        mBankLogoMap2.put("中国建设银行股份有限公司总行", R.drawable.ic_bank_logo_07);
        mBankLogoMap2.put("交通银行", R.drawable.ic_bank_logo_08);
        mBankLogoMap2.put("招商银行股份有限公司", R.drawable.ic_bank_logo_17);
        mBankLogoMap2.put("中国邮政储蓄银行有限责任公司", R.drawable.ic_bank_logo_16);
        mBankLogoMap2.put("中国民生银行", R.drawable.ic_bank_logo_10);
        mBankLogoMap2.put("兴业银行总行", R.drawable.ic_bank_logo_15);
        mBankLogoMap2.put("中信银行股份有限公司", R.drawable.ic_bank_logo_19);
        mBankLogoMap2.put("上海浦东发展银行", R.drawable.ic_bank_logo_14);
        mBankLogoMap2.put("中国光大银行", R.drawable.ic_bank_logo_03);
        mBankLogoMap2.put("平安银行（原深圳发展银行）", R.drawable.ic_bank_logo_13);
        mBankLogoMap2.put("华夏银行股份有限公司总行", R.drawable.ic_bank_logo_06);
        mBankLogoMap2.put("广发银行股份有限公司", R.drawable.ic_bank_logo_04);
        mBankLogoMap2.put("国家开发银行", R.drawable.ic_bank_logo_05);
        mBankLogoMap2.put("中国进出口银行", R.drawable.ic_bank_logo_09);
        mBankLogoMap2.put("中国农业发展银行", R.drawable.ic_bank_logo_12);
        mBankLogoMap2.put("江苏银行股份有限公司", R.drawable.ic_bank_logo_20);
        mBankLogoMap2.put("其他银行", R.drawable.other_bank_logo);
    }

    /**
     * 根据银行别名获取银行logo的资源Id
     *
     * @param bankAlias
     * @return
     */
    public static Integer getLogoResId(String bankAlias) {
        return mBankLogoMap.get(bankAlias);
    }

    /**
     * 根据银行名称获取银行logo的资源Id（推荐使用getLogoResByBankName1）
     *
     * @param bankName
     * @return
     */
    @Deprecated
    public static Integer getLogoResByBankName(String bankName) {
        return mBankLogoMap2.get(bankName);
    }

    /**
     * 根据银行名称获取银行logo的资源Id
     *
     * @param bankName
     * @return
     */
    public static Integer getLogoResByBankName1(String bankName) {
        Integer resId = null;
        if(bankName != null) {
            if(bankName.contains("中国银行")) {
                resId = R.drawable.ic_bank_logo_18;
            } else if(bankName.contains("中国工商银行")) {
                resId = R.drawable.ic_bank_logo_02;
            } else if(bankName.contains("中国农业银行")) {
                resId = R.drawable.ic_bank_logo_11;
            } else if(bankName.contains("中国建设银行")) {
                resId = R.drawable.ic_bank_logo_07;
            } else if(bankName.contains("交通银行")) {
                resId = R.drawable.ic_bank_logo_08;
            } else if(bankName.contains("招商银行")) {
                resId = R.drawable.ic_bank_logo_17;
            } else if(bankName.contains("中国邮政储蓄银行")) {
                resId = R.drawable.ic_bank_logo_16;
            } else if(bankName.contains("中国民生银行")) {
                resId = R.drawable.ic_bank_logo_10;
            } else if(bankName.contains("兴业银行")) {
                resId = R.drawable.ic_bank_logo_15;
            } else if(bankName.contains("中信银行")) {
                resId = R.drawable.ic_bank_logo_19;
            } else if(bankName.contains("上海浦东发展银行")) {
                resId = R.drawable.ic_bank_logo_14;
            } else if(bankName.contains("中国光大银行")) {
                resId = R.drawable.ic_bank_logo_03;
            } else if(bankName.contains("平安银行（原深圳发展银行）")) {
                resId = R.drawable.ic_bank_logo_13;
            } else if(bankName.contains("华夏银行")) {
                resId = R.drawable.ic_bank_logo_06;
            } else if(bankName.contains("广发银行股份有限公司")) {
                resId = R.drawable.ic_bank_logo_04;
            } else if(bankName.contains("国家开发银行")) {
                resId = R.drawable.ic_bank_logo_05;
            } else if(bankName.contains("中国进出口银行")) {
                resId = R.drawable.ic_bank_logo_09;
            } else if(bankName.contains("中国农业发展银行")) {
                resId = R.drawable.ic_bank_logo_12;
            } else if(bankName.contains("江苏银行")) {
                resId = R.drawable.ic_bank_logo_20;
            } else if(bankName.contains("其他银行")) {
                resId = R.drawable.other_bank_logo;
            }
        }
        return resId;
    }

}
