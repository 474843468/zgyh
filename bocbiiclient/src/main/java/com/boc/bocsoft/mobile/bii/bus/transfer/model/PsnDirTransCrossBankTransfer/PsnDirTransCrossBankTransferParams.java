package com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnDirTransCrossBankTransfer;

/**
 * Created by WM on 2016/6/24.
 */
public class PsnDirTransCrossBankTransferParams {


    /**
     * fromAccountId : 183159755
     * payeeActno : 6214830297711551
     * executeType : 0
     * bankName : 中国工商银行
     * toOrgName : 中国工商银行
     * cnapsCode : 102100099996
     * remark : 刚到
     * amount : 34.00
     * payeeName : 张一
     * currency : 001
     * sendMsgFlag : true
     * payeeId : 287384693
     * _combinId : 32
     * payeeMobile : 15668899887
     * remittanceInfo : 刚到
     */

    private String fromAccountId;
    private String payeeActno;
    private String executeType;

    public String getExecuteDate() {
        return executeDate;
    }

    public void setExecuteDate(String executeDate) {
        this.executeDate = executeDate;
    }

    public String getSendMsgFlag() {
        return sendMsgFlag;
    }

    private String executeDate;
    private String bankName;
    private String toOrgName;
    private String cnapsCode;
    private String remark;
    private String amount;
    private String payeeName;
    private String currency;

    public String isSendMsgFlag() {
        return sendMsgFlag;
    }

    public void setSendMsgFlag(String sendMsgFlag) {
        this.sendMsgFlag = sendMsgFlag;
    }

    private String sendMsgFlag;
    private String payeeId;
    private String _combinId;
    private String payeeMobile;
    private String remittanceInfo;

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    private String conversationId;

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

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getPayeeName() {
        return payeeName;
    }

    public void setPayeeName(String payeeName) {
        this.payeeName = payeeName;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }



    public String getPayeeId() {
        return payeeId;
    }

    public void setPayeeId(String payeeId) {
        this.payeeId = payeeId;
    }

    public String get_combinId() {
        return _combinId;
    }

    public void set_combinId(String _combinId) {
        this._combinId = _combinId;
    }

    public String getPayeeMobile() {
        return payeeMobile;
    }

    public void setPayeeMobile(String payeeMobile) {
        this.payeeMobile = payeeMobile;
    }

    public String getRemittanceInfo() {
        return remittanceInfo;
    }

    public void setRemittanceInfo(String remittanceInfo) {
        this.remittanceInfo = remittanceInfo;
    }
}
