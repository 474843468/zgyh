package com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadAutTradStatus;

/**
 * 委托常规交易状况查询
 * Created by zhx on 2016/9/5
 */
public class PsnXpadAutTradStatusParams {

    /**
     * currentIndex : 0
     * accountKey : 97483dc7-885f-4f45-a2ad-e60f38e87573
     * conversationId : d22b34a3-58cd-427b-a02c-aa5892e0dbd3
     * pageSize : 15
     */

    private String currentIndex;
    // 账号缓存标识
    private String accountKey;
    private String conversationId;
    private String pageSize;

    public void setCurrentIndex(String currentIndex) {
        this.currentIndex = currentIndex;
    }

    public void setAccountKey(String accountKey) {
        this.accountKey = accountKey;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    public void setPageSize(String pageSize) {
        this.pageSize = pageSize;
    }

    public String getCurrentIndex() {
        return currentIndex;
    }

    public String getAccountKey() {
        return accountKey;
    }

    public String getConversationId() {
        return conversationId;
    }

    public String getPageSize() {
        return pageSize;
    }
}
