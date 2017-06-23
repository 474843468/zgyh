package com.boc.bocsoft.mobile.bii.bus.equickpay.model.PsnHCEQuickPassQuotaSettingVerify;

/**
 * Created by yangle on 2016/12/15.
 * 描述: HCE闪付卡限额设置预交易params
 */
public class PsnHCEQuickPassQuotaSettingVerifyParams {

    private String masterCardNo;// 主卡卡号
    private String slaveCardNo;// 从卡卡号
    private String singleQuota;//	单笔限额
    private String _combinId;// 安全工具组合
    private String conversationId;

    public String getMasterCardNo() {
        return masterCardNo;
    }

    public void setMasterCardNo(String masterCardNo) {
        this.masterCardNo = masterCardNo;
    }

    public String getSlaveCardNo() {
        return slaveCardNo;
    }

    public void setSlaveCardNo(String slaveCardNo) {
        this.slaveCardNo = slaveCardNo;
    }

    public String getSingleQuota() {
        return singleQuota;
    }

    public void setSingleQuota(String singleQuota) {
        this.singleQuota = singleQuota;
    }

    public String get_combinId() {
        return _combinId;
    }

    public void set_combinId(String _combinId) {
        this._combinId = _combinId;
    }

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }
}
