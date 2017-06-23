package com.boc.bocsoft.mobile.bii.bus.account.model;

/**
 * @author wangyang
 *         16/6/17 14:23
 *         生成会话与Token
 */
public class PublicParams {

    /** 会话Id */
    private String conversationId;
    /** 防重机制 PSNGetTokenId获取 */
    private String token;
    /**
     * 安全因子组合ID
     */
    private String _combinId;

    public String get_combinId() {
        return _combinId;
    }

    public void set_combinId(String _combinId) {
        this._combinId = _combinId;
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
