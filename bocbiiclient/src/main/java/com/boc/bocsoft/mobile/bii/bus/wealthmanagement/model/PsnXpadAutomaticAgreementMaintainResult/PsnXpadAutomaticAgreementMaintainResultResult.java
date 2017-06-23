package com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadAutomaticAgreementMaintainResult;

/**
 * 协议维护
 * Created by guokai on 2016/9/6.
 */
public class PsnXpadAutomaticAgreementMaintainResultResult {
    private String transactionId;//	网银交易序号
    private String status;//	维护状态

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }
}
