package com.boc.bocsoft.mobile.bii.bus.equickpay.model.PsnHCEQuickPassActivationVerify;

/**
 * Created by yangle on 2016/12/15.
 * 描述:HCE闪付卡激活预交易params
 */
public class PsnHCEQuickPassActivationVerifyParams {

    private String deviceNo;//	设备号
    private String masterCardNo;// 主卡卡号
    private String slaveCardNo;// 从卡卡号
    private String _combinId;//	安全工具组合
    private String conversationId;

    public String getDeviceNo() {
        return deviceNo;
    }

    public void setDeviceNo(String deviceNo) {
        this.deviceNo = deviceNo;
    }

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
