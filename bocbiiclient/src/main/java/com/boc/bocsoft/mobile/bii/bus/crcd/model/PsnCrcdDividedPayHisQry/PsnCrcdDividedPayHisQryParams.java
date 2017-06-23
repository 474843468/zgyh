package com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdDividedPayHisQry;

/**
 * Created by yangle on 2016/11/22.
 */
public class PsnCrcdDividedPayHisQryParams {


    private String accountId;
    private String currentIndex;
    private String pageSize;
    private String _refresh; // 刷新标识:true 重新查询 false：从缓存中查询
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

    public String getPageSize() {
        return pageSize;
    }

    public void setPageSize(String pageSize) {
        this.pageSize = pageSize;
    }

    public String get_refresh() {
        return _refresh;
    }

    public void set_refresh(String _refresh) {
        this._refresh = _refresh;
    }

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }
}
