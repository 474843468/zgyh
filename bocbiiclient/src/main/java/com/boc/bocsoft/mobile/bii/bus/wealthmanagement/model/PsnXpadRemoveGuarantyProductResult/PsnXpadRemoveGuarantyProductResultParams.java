package com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadRemoveGuarantyProductResult;

/**
 * 组合购买解除
 * Created by zhx on 2016/9/5
 */
public class PsnXpadRemoveGuarantyProductResultParams {

    /**
     * xpadCode : 9456
     * cashRemit : 1
     * accountKey : 97483dc7-885f-4f45-a2ad-e60f38e87573
     * token : ccarucjw
     * status : 1
     * xpadName : 吃货01
     * conversationId : d22b34a3-58cd-427b-a02c-aa5892e0dbd3
     * ibknum : 40740
     * channel : 2
     * buyAmt : 900
     * tranSeq : 4354354311115
     * typeOfAccount : 119
     * currency : 014
     */

    // 组合交易流水号
    private String tranSeq;
    // currency
    private String currency;
    // 购买金额
    private String buyAmt;
    // 产品代码
    private String xpadCode;
    // 产品名称
    private String xpadName;
    // 钞汇标识
    private String cashRemit;
    // 状态
    private String status;
    // 渠道
    private String channel;
    // 账号缓存标识（36位长度）
    private String accountKey;
    // 省行联行号
    private String ibknum;
    // 账户类型（SY-活一本交易 CD-借记卡交易 MW-网上专属理财 GW-长城信用卡 必输）
    private String typeOfAccount;
    private String token;
    private String conversationId;

    public void setXpadCode(String xpadCode) {
        this.xpadCode = xpadCode;
    }

    public void setCashRemit(String cashRemit) {
        this.cashRemit = cashRemit;
    }

    public void setAccountKey(String accountKey) {
        this.accountKey = accountKey;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setXpadName(String xpadName) {
        this.xpadName = xpadName;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    public void setIbknum(String ibknum) {
        this.ibknum = ibknum;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public void setBuyAmt(String buyAmt) {
        this.buyAmt = buyAmt;
    }

    public void setTranSeq(String tranSeq) {
        this.tranSeq = tranSeq;
    }

    public void setTypeOfAccount(String typeOfAccount) {
        this.typeOfAccount = typeOfAccount;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getXpadCode() {
        return xpadCode;
    }

    public String getCashRemit() {
        return cashRemit;
    }

    public String getAccountKey() {
        return accountKey;
    }

    public String getToken() {
        return token;
    }

    public String getStatus() {
        return status;
    }

    public String getXpadName() {
        return xpadName;
    }

    public String getConversationId() {
        return conversationId;
    }

    public String getIbknum() {
        return ibknum;
    }

    public String getChannel() {
        return channel;
    }

    public String getBuyAmt() {
        return buyAmt;
    }

    public String getTranSeq() {
        return tranSeq;
    }

    public String getTypeOfAccount() {
        return typeOfAccount;
    }

    public String getCurrency() {
        return currency;
    }
}
