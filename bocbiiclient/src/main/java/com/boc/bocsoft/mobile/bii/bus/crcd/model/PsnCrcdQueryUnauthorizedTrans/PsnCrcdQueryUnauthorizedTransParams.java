package com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdQueryUnauthorizedTrans;

/**
 * 4.112 112查询信用卡未入账待授权交易PsnCrcdQueryUnauthorizedTrans
 * Created by liuweidong on 2016/12/14.
 */

public class PsnCrcdQueryUnauthorizedTransParams {
    private String accountId;
    private String currentIndex;
    private String _refresh;
    private String pageSize;
    private String conversationId;

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getCurrentIndex() {
        return currentIndex;
    }

    public void setCurrentIndex(String currentIndex) {
        this.currentIndex = currentIndex;
    }

    public String get_refresh() {
        return _refresh;
    }

    public void set_refresh(String _refresh) {
        this._refresh = _refresh;
    }

    public String getPageSize() {
        return pageSize;
    }

    public void setPageSize(String pageSize) {
        this.pageSize = pageSize;
    }

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }
}
