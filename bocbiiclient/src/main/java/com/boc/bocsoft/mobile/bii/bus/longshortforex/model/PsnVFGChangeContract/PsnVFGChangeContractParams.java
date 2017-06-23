package com.boc.bocsoft.mobile.bii.bus.longshortforex.model.PsnVFGChangeContract;

/**
 * Params：变更签约账户
 * Created by zhx on 2016/11/21
 */
public class PsnVFGChangeContractParams {

    /**
     * marginAccountNo : 12346789
     * settleCurrency : 001
     * accountId : 101418297
     * token : ygwdoijm
     * oldAccountNumber : 4563510800034881051
     */
    // 原借记卡卡号
    private String oldAccountNumber;
    // 变更后借记卡账户标识
    private String accountId;
    // 结算货币(014美元 001人民币 038欧元 013港币 027日元 029澳元)
    private String settleCurrency;
    // 保证金账号
    private String marginAccountNo;
    // 防重机制，通过PSNGetTokenId接口获取
    private String token;
    private String conversationId;

    public void setMarginAccountNo(String marginAccountNo) {
        this.marginAccountNo = marginAccountNo;
    }

    public void setSettleCurrency(String settleCurrency) {
        this.settleCurrency = settleCurrency;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setOldAccountNumber(String oldAccountNumber) {
        this.oldAccountNumber = oldAccountNumber;
    }

    public String getMarginAccountNo() {
        return marginAccountNo;
    }

    public String getSettleCurrency() {
        return settleCurrency;
    }

    public String getAccountId() {
        return accountId;
    }

    public String getToken() {
        return token;
    }

    public String getOldAccountNumber() {
        return oldAccountNumber;
    }

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }
}
