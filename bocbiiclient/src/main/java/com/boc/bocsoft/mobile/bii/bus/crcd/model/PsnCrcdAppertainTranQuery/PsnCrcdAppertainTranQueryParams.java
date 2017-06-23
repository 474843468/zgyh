package com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdAppertainTranQuery;

/**
 * Name: liukai
 * Time：2016/12/2 15:42.
 * Created by lk7066 on 2016/12/2.
 * It's used to
 */

public class PsnCrcdAppertainTranQueryParams {


    /**
     * accountId : 6259061103573454
     * startDate : 2020/12/27
     * endDate : 2021/01/27
     * pageSize : 10
     * currentIndex : 1
     * _refresh : true
     * conversationId : d7c88f80-65f2-4e53-a6c6-3104d6dc51fd
     */

    private String accountId;
    private String startDate;
    private String endDate;
    private String pageSize;
    private String currentIndex;
    private String _refresh;
    private String conversationId;

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getPageSize() {
        return pageSize;
    }

    public void setPageSize(String pageSize) {
        this.pageSize = pageSize;
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

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }
}
