package com.boc.bocsoft.mobile.bii.bus.longshortforex.model.PsnVFGCancelContract;

/**
 * Params：双向宝解约
 * Created by zhx on 2016/11/21
 */
public class PsnVFGCancelContractParams {

    /**
     * marginAccountNo : 123456789
     * settleCurrency : 001
     * accountNumber : 4563510800034881051
     * token : rx0fv45t
     */
    // 借记卡卡号
    private String accountNumber;
    // 保证金账号
    private String marginAccountNo;
    // 结算货币(014美元 001人民币 038欧元 013港币 027日元 029澳元)
    private String settleCurrency;
    private String conversationId;
    // 防重机制，通过PSNGetTokenId接口获取
    private String token;

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getMarginAccountNo() {
        return marginAccountNo;
    }

    public void setMarginAccountNo(String marginAccountNo) {
        this.marginAccountNo = marginAccountNo;
    }

    public String getSettleCurrency() {
        return settleCurrency;
    }

    public void setSettleCurrency(String settleCurrency) {
        this.settleCurrency = settleCurrency;
    }

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
