package com.boc.bocsoft.mobile.bii.bus.longshortforex.model.PsnVFGQueryMaxAmount;

/**
 * Params：保证金账户可转出最大金额查询
 * Created by zhx on 2016/11/28
 */
public class PsnVFGQueryMaxAmountParams {

    /**
     * cashRemit : 1
     * currencyCode : 014
     * token :
     * conversationId :
     */
    private String cashRemit; // 0 ：人民币 1：钞 2：汇
    private String currencyCode; // 签约货币(014美元 001人民币 038欧元 013港币 027日元 029澳元)
    private String token; // 填""即可
    private String conversationId; // 填""即可

    public void setCashRemit(String cashRemit) {
        this.cashRemit = cashRemit;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    public String getCashRemit() {
        return cashRemit;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public String getToken() {
        return token;
    }

    public String getConversationId() {
        return conversationId;
    }
}
