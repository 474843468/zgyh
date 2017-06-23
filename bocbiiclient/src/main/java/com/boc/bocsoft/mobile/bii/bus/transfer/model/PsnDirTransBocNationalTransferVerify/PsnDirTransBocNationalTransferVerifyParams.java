package com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnDirTransBocNationalTransferVerify;

/**
 * Created by WM on 2016/6/16.
 */
public class PsnDirTransBocNationalTransferVerifyParams {

    /**
     * fromAccountId : 100032106
     * payeeActno : 99522222107
     * executeType : 0
     * remark : nocomments
     * payeeId : 8328826
     * payeeName : 1
     * bankName : boc
     * toOrgName : bj
     * cnapsCode : 42465
     * amount : 300.55
     * currency : 001
     * remittanceInfo  : tution
     * payeeMobile : 13454566622
     * executeDate : 2012/12/27
     * _combinId : 4
     */

    private String fromAccountId;
    private String payeeActno;
    private String executeType;
    private String remark;
    private String payeeId;
    private String payeeName;
    private String bankName;
    private String toOrgName;
    private String cnapsCode;
    private String amount;
    private String currency;
    private String remittanceInfo;
    private String payeeMobile;
    private String executeDate;
    private String _combinId;
    private String conversationId;

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    public String getFromAccountId() {
        return fromAccountId;
    }

    public void setFromAccountId(String fromAccountId) {
        this.fromAccountId = fromAccountId;
    }

    public String getPayeeActno() {
        return payeeActno;
    }

    public void setPayeeActno(String payeeActno) {
        this.payeeActno = payeeActno;
    }

    public String getExecuteType() {
        return executeType;
    }

    public void setExecuteType(String executeType) {
        this.executeType = executeType;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getPayeeId() {
        return payeeId;
    }

    public void setPayeeId(String payeeId) {
        this.payeeId = payeeId;
    }

    public String getPayeeName() {
        return payeeName;
    }

    public void setPayeeName(String payeeName) {
        this.payeeName = payeeName;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getToOrgName() {
        return toOrgName;
    }

    public void setToOrgName(String toOrgName) {
        this.toOrgName = toOrgName;
    }

    public String getCnapsCode() {
        return cnapsCode;
    }

    public void setCnapsCode(String cnapsCode) {
        this.cnapsCode = cnapsCode;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getRemittanceInfo() {
        return remittanceInfo;
    }

    public void setRemittanceInfo(String remittanceInfo) {
        this.remittanceInfo = remittanceInfo;
    }

    public String getPayeeMobile() {
        return payeeMobile;
    }

    public void setPayeeMobile(String payeeMobile) {
        this.payeeMobile = payeeMobile;
    }

    public String getExecuteDate() {
        return executeDate;
    }

    public void setExecuteDate(String executeDate) {
        this.executeDate = executeDate;
    }

    public String get_combinId() {
        return _combinId;
    }
    public void set_combinId(String _combinId) {
        this._combinId = _combinId;
    }
}
