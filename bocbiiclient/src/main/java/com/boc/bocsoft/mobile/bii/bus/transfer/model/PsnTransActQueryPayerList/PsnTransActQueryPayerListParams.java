package com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransActQueryPayerList;

/**
 * Params:查询付款人列表
 * Created by zhx on 2016/6/29.
 */
public class PsnTransActQueryPayerListParams {
    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    /**
     * 会话ID
     */
    private String conversationId;

}
