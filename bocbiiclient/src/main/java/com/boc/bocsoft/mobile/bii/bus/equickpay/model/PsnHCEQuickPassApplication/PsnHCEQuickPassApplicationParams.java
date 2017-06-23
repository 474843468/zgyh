package com.boc.bocsoft.mobile.bii.bus.equickpay.model.PsnHCEQuickPassApplication;

/**
 * Created by yangle on 2016/12/15.
 * 描述: HCE闪付卡申请params
 */
public class PsnHCEQuickPassApplicationParams {

    private String deviceNo;//	设备号
    private String accountId;//	账户标识
    private String masterCardNo;//主卡卡号
    /**
     * 01:MASTERCARD贷记
     * 11:MASTERCARD借记
     * 02:PBOC贷记
     * 12:PBOC借记
     * 03:VISA贷记
     * 13:VISA借记
     */
    private String cardBrand;// 卡品牌所申请的品牌编号：
    private String singleQuota;//	单笔限额
    private String keyNum;//	密钥数 01-99
    private String conversationId;

    public String getDeviceNo() {
        return deviceNo;
    }

    public void setDeviceNo(String deviceNo) {
        this.deviceNo = deviceNo;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getMasterCardNo() {
        return masterCardNo;
    }

    public void setMasterCardNo(String masterCardNo) {
        this.masterCardNo = masterCardNo;
    }

    public String getCardBrand() {
        return cardBrand;
    }

    public void setCardBrand(String cardBrand) {
        this.cardBrand = cardBrand;
    }

    public String getSingleQuota() {
        return singleQuota;
    }

    public void setSingleQuota(String singleQuota) {
        this.singleQuota = singleQuota;
    }

    public String getKeyNum() {
        return keyNum;
    }

    public void setKeyNum(String keyNum) {
        this.keyNum = keyNum;
    }

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }
}
