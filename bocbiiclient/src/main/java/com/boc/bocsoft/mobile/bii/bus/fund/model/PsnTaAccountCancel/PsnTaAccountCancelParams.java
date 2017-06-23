package com.boc.bocsoft.mobile.bii.bus.fund.model.PsnTaAccountCancel;

/**
 * 045接口 Ta账户取消关联/销户
 * Created by lyf7084 on 2016/11/24.
 */
public class PsnTaAccountCancelParams {

    /**
     * taAccountNo : 622078115671
     * transType : 0
     * fundRegCode : 11
     * token : ccarucjw
     */
    private String taAccountNo;
    private String transType;
    private String fundRegCode;
    private String token;
    private String conversationId; //会话ID

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    public void setTaAccountNo(String taAccountNo) {
        this.taAccountNo = taAccountNo;
    }

    public void setTransType(String transType) {
        this.transType = transType;
    }

    public void setFundRegCode(String fundRegCode) {
        this.fundRegCode = fundRegCode;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getTaAccountNo() {
        return taAccountNo;
    }

    public String getTransType() {
        return transType;
    }

    public String getFundRegCode() {
        return fundRegCode;
    }

    public String getToken() {
        return token;
    }
}
