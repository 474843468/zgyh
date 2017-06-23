package com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundAttentionAdd;

/**
 * 增加基金关注参数
 * Created by liuzc on 2017/1/2.
 */
public class PsnFundAttentionAddParams {
    private String fundCode; //基金代码
    private String token; //防重机制token
    private String conversationId; //会话ID

    public String getFundCode() {
        return fundCode;
    }

    public void setFundCode(String fundCode) {
        this.fundCode = fundCode;
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
