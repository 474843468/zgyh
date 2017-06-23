package com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.mypositions.shareconversion.model.PsnXpadShareTransitionCommit;

import java.io.Serializable;

/**
 * Created by zn on 2016/9/10.
 * 4.71 071 份额转换  确认提交  PsnXpadShareTransitionCommit  请求Model
 */
public class PsnXpadShareTransitionCommitReqModel implements Serializable {
    /**
     * 帐号缓存标识	String
     */
    private String accountKey;
    /**
     * 防重机制，通过PSNGetTokenId接口获取	String
     */
    private String token;

    //会话ID
    private String conversationId;


    public String getAccountKey() {
        return accountKey;
    }

    public void setAccountKey(String accountKey) {
        this.accountKey = accountKey;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }
}
