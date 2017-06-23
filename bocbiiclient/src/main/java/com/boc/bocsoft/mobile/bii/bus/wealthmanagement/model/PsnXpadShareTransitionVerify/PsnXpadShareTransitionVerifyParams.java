package com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadShareTransitionVerify;

import java.io.Serializable;

/**
 * Created by zn on 2016/9/10.
 * 4.70 070  PsnXpadShareTransitionVerify    锁定期份额转换预交易    请求Model
 */
public class PsnXpadShareTransitionVerifyParams{
    /**
     * 帐号缓存标识	String
     */
    private String accountKey;
    /**
     * 产品代码	String
     */
    private String proId;
    /**
     * 转换份额	String
     */
    private String tranUnit;

    /***
     * 防重标识	String
     */
    private String token;
    /**
     * 钞汇类型	String
     */
    private String charCode;
    /**
     * 持仓流水号	String
     */
    private String serialNo;

    ;    //会话ID
    private String conversationId;

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    public String getAccountKey() {
        return accountKey;
    }

    public void setAccountKey(String accountKey) {
        this.accountKey = accountKey;
    }

    public String getProId() {
        return proId;
    }

    public void setProId(String proId) {
        this.proId = proId;
    }

    public String getTranUnit() {
        return tranUnit;
    }

    public void setTranUnit(String tranUnit) {
        this.tranUnit = tranUnit;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getCharCode() {
        return charCode;
    }

    public void setCharCode(String charCode) {
        this.charCode = charCode;
    }

    public String getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo;
    }
}
