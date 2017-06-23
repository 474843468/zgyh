package com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnDirTransCrossBankAddPayee;

import com.boc.bocsoft.mobile.bii.common.model.BIIResponse;

/**
 * Created by WYme on 2016/12/10.
 */
public class PsnDirTransCrossBankAddPayeeParams  {

    /**
     * payeeActno : 54522222104
     * payeeName : pseudo
     * cnapsCode : 42465
     * bankName : 中国农业银行
     * toOrgName : 中国农业银行北京分行
     * payeeMobile : 18801283218
     * token : v8w2gstn
     * payeeId : 8328526
     */

    private String payeeActno;
    private String payeeName;
    private String cnapsCode;
    private String bankName;
    private String toOrgName;
    private String payeeMobile;
    private String token;
    private String payeeId;

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

    public String getPayeeName() {
        return payeeName;
    }

    public void setPayeeName(String payeeName) {
        this.payeeName = payeeName;
    }

    public String getCnapsCode() {
        return cnapsCode;
    }

    public void setCnapsCode(String cnapsCode) {
        this.cnapsCode = cnapsCode;
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

    public String getPayeeMobile() {
        return payeeMobile;
    }

    public void setPayeeMobile(String payeeMobile) {
        this.payeeMobile = payeeMobile;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getPayeeId() {
        return payeeId;
    }

    public void setPayeeId(String payeeId) {
        this.payeeId = payeeId;
    }
}
