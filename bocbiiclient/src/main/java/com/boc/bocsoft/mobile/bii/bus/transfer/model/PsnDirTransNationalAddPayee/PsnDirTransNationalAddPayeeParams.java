package com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnDirTransNationalAddPayee;

/**
 * Created by WYme on 2016/9/8.
 */
public class PsnDirTransNationalAddPayeeParams {

    /**
     * payeeActno : 6214830297711556
     * payeeId : 123167842
     * payeeName : 李三
     * payeeMobile :
     * bankName : 招商银行股份有限公司
     * toOrgName : 招商银行股份有限公司广州越秀支行
     * cnapsCode : 308581002136
     * token : inrhugzr
     */

    private String payeeActno;
    private String payeeId;
    private String payeeName;
    private String payeeMobile;
    private String bankName;
    private String toOrgName;
    private String cnapsCode;
    private String token;

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    private String conversationId;

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

    public String getPayeeName() {
        return payeeName;
    }

    public void setPayeeName(String payeeName) {
        this.payeeName = payeeName;
    }

    public String getPayeeMobile() {
        return payeeMobile;
    }

    public void setPayeeMobile(String payeeMobile) {
        this.payeeMobile = payeeMobile;
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

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
