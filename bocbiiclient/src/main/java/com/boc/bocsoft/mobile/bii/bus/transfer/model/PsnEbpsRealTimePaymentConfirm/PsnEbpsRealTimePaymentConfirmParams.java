package com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnEbpsRealTimePaymentConfirm;

/**
 * Created by WM on 2016/6/24.
 */
public class PsnEbpsRealTimePaymentConfirmParams {

    /**
     * fromAccountId : 183159755
     * transoutaccparent : 活期一本通1020******4370活期一本通 陕西
     * payeeActno : 123213434343434
     * payeeActno2 : 123213434343434
     * payeeName : 王小吉
     * payeeCnaps : 102100099996
     * payeeBankName : 中国工商银行
     * payeeOrgName : 中国工商银行
     * currency : 001
     * amount : 34.00
     * memo : 刚到
     * sendMsgFlag : true
     * executeType : 0
     * _combinId : 32
     * payeeMobile : 15656567878
     * remittanceInfo : 刚到
     */

    private String fromAccountId;
    private String transoutaccparent;
    private String payeeActno;
    private String payeeActno2;
    private String payeeName;
    private String payeeCnaps;
    private String payeeBankName;
    private String payeeOrgName;
    private String currency;
    private String amount;
    private String memo;

//    private boolean saveAsPayeeYn;
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
    private String _combinId;
    private String payeeMobile;
    private String remittanceInfo;

    public String isSendMsgFlag() {
        return sendMsgFlag;
    }

    public void setSendMsgFlag(String sendMsgFlag) {
        this.sendMsgFlag = sendMsgFlag;
    }

    private String sendMsgFlag;

//    public boolean isSaveAsPayeeYn() {
//        return saveAsPayeeYn;
//    }
//
//    public void setSaveAsPayeeYn(boolean saveAsPayeeYn) {
//        this.saveAsPayeeYn = saveAsPayeeYn;
//    }

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

    public String getTransoutaccparent() {
        return transoutaccparent;
    }

    public void setTransoutaccparent(String transoutaccparent) {
        this.transoutaccparent = transoutaccparent;
    }

    public String getPayeeActno() {
        return payeeActno;
    }

    public void setPayeeActno(String payeeActno) {
        this.payeeActno = payeeActno;
    }

    public String getPayeeActno2() {
        return payeeActno2;
    }

    public void setPayeeActno2(String payeeActno2) {
        this.payeeActno2 = payeeActno2;
    }

    public String getPayeeName() {
        return payeeName;
    }

    public void setPayeeName(String payeeName) {
        this.payeeName = payeeName;
    }

    public String getPayeeCnaps() {
        return payeeCnaps;
    }

    public void setPayeeCnaps(String payeeCnaps) {
        this.payeeCnaps = payeeCnaps;
    }

    public String getPayeeBankName() {
        return payeeBankName;
    }

    public void setPayeeBankName(String payeeBankName) {
        this.payeeBankName = payeeBankName;
    }

    public String getPayeeOrgName() {
        return payeeOrgName;
    }

    public void setPayeeOrgName(String payeeOrgName) {
        this.payeeOrgName = payeeOrgName;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getExecuteType() {
        return executeType;
    }

    public void setExecuteType(String executeType) {
        this.executeType = executeType;
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
