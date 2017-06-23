package com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadSetBonusMode;

/**
 * I42-4.14 014修改分红方式交易 PsnXpadSetBonusMode 请求model
 * Created by yx on 2016/11/14.
 */
public class PsnXpadSetBonusModeParams {

    /**
     * mode : 0
     * prodCode : RJYL-606-LJF082501
     * cashRemit : 01
     * conversationId : bee5f5a5-9386-4bc9-aacb-c961a23c6b8e
     * prodName : RJYL-606-LJF082501
     * currencyCode : 014
     * accountKey : b7ebcfbf-67fa-4117-98e7-526fcb9670fc
     * token : ggnirv3k
     */
    /**
     * 分红方式	String		1=现金分红 ,0=再投资分红
     */
    private String mode;
    /**
     * 产品代码
     */
    private String prodCode;
    /**
     * 钞汇标识
     */
    private String cashRemit;
    /**
     * 会话id
     */
    private String conversationId;
    /**
     * 产品名称
     */
    private String prodName;
    /**
     * 币种
     */
    private String currencyCode;
    /**
     * 账号缓存标识
     */
    private String accountKey;
    /**
     * 防重标识
     */
    private String token;

    public void setMode(String mode) {
        this.mode = mode;
    }

    public void setProdCode(String prodCode) {
        this.prodCode = prodCode;
    }

    public void setCashRemit(String cashRemit) {
        this.cashRemit = cashRemit;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    public void setProdName(String prodName) {
        this.prodName = prodName;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public void setAccountKey(String accountKey) {
        this.accountKey = accountKey;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getMode() {
        return mode;
    }

    public String getProdCode() {
        return prodCode;
    }

    public String getCashRemit() {
        return cashRemit;
    }

    public String getConversationId() {
        return conversationId;
    }

    public String getProdName() {
        return prodName;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public String getAccountKey() {
        return accountKey;
    }

    public String getToken() {
        return token;
    }
}
