package com.boc.bocsoft.mobile.bii.bus.equickpay.model.PsnHCEQuickPassCancel;

/**
 * Created by gengjunying on 2016/12/20.
 * Hce卡注销
 */
public class PsnHCEQuickPassCancelParams {
    //主卡卡号
    private String masterCardNo;
    //slaveCardNo
    private String slaveCardNo;
    //会话id
    private String conversationId;



    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
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



}
