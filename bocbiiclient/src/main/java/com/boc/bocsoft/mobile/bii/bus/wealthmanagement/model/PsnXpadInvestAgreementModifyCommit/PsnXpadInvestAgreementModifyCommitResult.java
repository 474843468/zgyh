package com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadInvestAgreementModifyCommit;

/**
 * 投资协议修改提交
 * Created by guokai on 2016/9/6.
 */
public class PsnXpadInvestAgreementModifyCommitResult {
    private String transactionId;//	网银交易流水号
    private String accNo;//	银行帐号
    private String agrCode;//	产品协议
    private String agrName;//	协议名称
    private String ustAgrCode;//	客户协议代码
    private String operDate;//	操作日期

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getAccNo() {
        return accNo;
    }

    public void setAccNo(String accNo) {
        this.accNo = accNo;
    }

    public String getAgrCode() {
        return agrCode;
    }

    public void setAgrCode(String agrCode) {
        this.agrCode = agrCode;
    }

    public String getAgrName() {
        return agrName;
    }

    public void setAgrName(String agrName) {
        this.agrName = agrName;
    }

    public String getUstAgrCode() {
        return ustAgrCode;
    }

    public void setUstAgrCode(String ustAgrCode) {
        this.ustAgrCode = ustAgrCode;
    }

    public String getOperDate() {
        return operDate;
    }

    public void setOperDate(String operDate) {
        this.operDate = operDate;
    }
}
