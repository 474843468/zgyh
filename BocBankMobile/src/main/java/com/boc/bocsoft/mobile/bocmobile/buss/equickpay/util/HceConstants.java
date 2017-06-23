package com.boc.bocsoft.mobile.bocmobile.buss.equickpay.util;

/**
 * Created by gengjunying on 2016/12/5.
 */
public class HceConstants {

    /**
     * TSM控件应用类型：01-MasterCard；02-PBOC贷记；03-VISA；04-PBOC借记
     */
    public enum CardOrg {
        MasterCard, PBOC_Credit, VISA, PBOC_Debit
    }


    public enum NFC_SUPPORT {
        ROOT,
        ANDROID_VERTION_NOT_SUPPORT,
        NOT_SUPPORT_NFC,
        SUPPORT
    }

    public static final String HCE_SUCCEED = "9000";// HCE控件返回成功值

    public static final String MAC_STR = "0000000000000000"; // TSM要求上送的mac值
    public static final String UP_CHANNEL = "04";// TSM要求上送渠道： 04-中银易商，05-缤纷生活
    public static final String WRITE_CARD_SUCCESS = "00"; // 写卡成功（本地个人化成功）

    /**
     * ICCD卡列表返回卡组织：
     * 01-MasterCard贷记；
     * 11-MasterCard借记；
     * 02-PBOC贷记；
     * 12-PBOC借记；
     * 03-VISA贷记；
     * 13-VISA借记
     */
    public static final String MasterTypeStr = "01";// Master HCE值
    public static final String PbocCreditTypeTypeStr = "02";// 银联贷记 HCE值
    public static final String VisaTypeStr = "03";// Visa HCE值
    public static final String PbocDebitTypeTypeStr = "12";// 银联贷记 HCE值

    /**
     * ICCD卡列表返回卡状态：
     * 3-待激活；
     * 4-正常；
     * 5-待回收；
     * 6-已挂失
     */
    public static final String ACTIVATING = "3";
    public static final String NORMAL = "4";
    public static final String RETRIEVE = "5";
    public static final String LOCKED = "6";

    // 单笔限额
    public static final int MAX_SINGLE_QUOTA = 1000;

    // 初始密钥数
    public static final int INI_LUK_NUM = 10;
    // 判断密钥数，小于该数字，则重新去申请
    public static final int JUDGE_LUK_NUM = 3;

    public static final String HCE_CARD_FLAG = "5A";// 查询卡号标识

    public static final String HCE_CARD_SERIAL_FLAG = "5F34";// 查询卡序号标识

    public static final String PPSE_AID = "325041592E5359532E4444463031";// PPSE
    
    public static final String MasterCard_AID = "A0000000041010";// MasterCard
    public static final String VISA_AID = "A0000000031010";// VISA
    public static final String PBOC_Credit_AID = "A000000333010102";// PBOC 贷记
    public static final String PBOC_Debit_AID = "A000000333010101";// PBOC 借记

    /**
     * HCE控件默认设置查询结果
     */
    public static final String NO_APP = "00";// 无应用
    public static final String NO_DEFAULT = "0";// 无默认
    public static final String Master_APP = "1";// Master且默认Master
    public static final String PbocCredit_APP = "2";// 银联贷记且默认银联贷记
    public static final String Visa_APP = "4";// Visa贷记且默认Visa贷记
    public static final String PbocDebit_APP = "8";// 银联借记且默认银联借记

    public static final String REQUIRE_SUPPORT_UNLOCK = "require_support_unlock";// sp中保存是否支持锁屏支付的key

}
