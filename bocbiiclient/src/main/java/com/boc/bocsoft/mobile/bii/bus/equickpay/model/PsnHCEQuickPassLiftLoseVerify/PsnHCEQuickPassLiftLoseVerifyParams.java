package com.boc.bocsoft.mobile.bii.bus.equickpay.model.PsnHCEQuickPassLiftLoseVerify;

/**
 * Created by gengjunying on 2016/12/20.
 */
public class PsnHCEQuickPassLiftLoseVerifyParams {
    //主卡卡号
    private String masterCardNo;
    //从卡卡号
    private String slaveCardNo;
    //安全工具组合
    private String _combinId;
    //会话id
    private String conversationId;

    public String get_combinId() {
        return _combinId;
    }

    public void set_combinId(String _combinId) {
        this._combinId = _combinId;
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


    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }
}
