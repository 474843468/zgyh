package com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdQueryFutureBill;

/**
 * 4.4 004查询信用卡未出账单PsnCrcdQueryFutureBill
 * Created by liuweidong on 2016/12/14.
 */

public class PsnCrcdQueryFutureBillParams {
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
