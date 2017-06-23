package com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnDirTransBocTransferVerify;

/**
 * Created by WM on 2016/6/12.
 */
public class PsnDirTransBocTransferVerifyParams {

    /**
     * fromAccountId : 100032106
     * payeeActno : 99522222104
     * executeType : 0
     * payeeMobile : 12312434222
     * remark : nocomments
     * currency : 001
     * payeeName : 孙克克
     * amount : 355.12
     * remittanceInfo : tution
     * executeDate : 2010/01/20
     * startDate : 2011/01/20
     * endDate : 2011/04/10
     * cycleSelect : M
     * payeeId : 8328817
     * “_combinId” : ”4”
     */

    private String fromAccountId;
    private String payeeActno;
    private String executeType;
    private String payeeMobile;
    private String remark;
    private String currency;
    private String payeeName;
    private String amount;
    private String remittanceInfo;
    private String executeDate;
    private String startDate;
    private String endDate;
    private String cycleSelect;
    private String payeeId;

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

    /**
     * 安全因子组合Id
     */
    private String _combinId;

    private String conversationId;


    public String getFromAccountId() {
        return fromAccountId;
    }

    public void setFromAccountId(String fromAccountId) {
        this.fromAccountId = fromAccountId;
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

    public String getCycleSelect() {
        return cycleSelect;
    }

    public void setCycleSelect(String cycleSelect) {
        this.cycleSelect = cycleSelect;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getExecuteDate() {
        return executeDate;
    }

    public void setExecuteDate(String executeDate) {
        this.executeDate = executeDate;
    }

    public String getExecuteType() {
        return executeType;
    }

    public void setExecuteType(String executeType) {
        this.executeType = executeType;
    }

    public String getPayeeActno() {
        return payeeActno;
    }

    public void setPayeeActno(String payeeActno) {
        this.payeeActno = payeeActno;
    }

    public String getPayeeId() {
        return payeeId;
    }

    public void setPayeeId(String payeeId) {
        this.payeeId = payeeId;
    }

    public String getPayeeMobile() {
        return payeeMobile;
    }

    public void setPayeeMobile(String payeeMobile) {
        this.payeeMobile = payeeMobile;
    }

    public String getPayeeName() {
        return payeeName;
    }

    public void setPayeeName(String payeeName) {
        this.payeeName = payeeName;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getRemittanceInfo() {
        return remittanceInfo;
    }

    public void setRemittanceInfo(String remittanceInfo) {
        this.remittanceInfo = remittanceInfo;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }
}
