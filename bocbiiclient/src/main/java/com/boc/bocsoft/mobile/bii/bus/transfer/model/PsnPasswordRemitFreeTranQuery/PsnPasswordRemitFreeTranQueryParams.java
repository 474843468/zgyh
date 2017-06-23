package com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnPasswordRemitFreeTranQuery;

/**
 * 密码汇款查询请求（ATM无卡取款查询）
 *
 * Created by liuweidong on 2016/6/24.
 */
public class PsnPasswordRemitFreeTranQueryParams {
    /**
     * 会话ID
     */
    private String conversationId;
    /**
     * 账户ID
     */
    private String accountId;
    /**
     * 交易类型 O-汇款 I-收款
     */
    private String freeRemitTrsType;
    /**
     * 起始日期
     */
    private String startDate;
    /**
     * 结束日期
     */
    private String endDate;
    /**
     * 每页显示条数
     */
    private int pageSize;
    /**
     * 当前记录索引
     */
    private int currentIndex;
    private String _refresh;

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getFreeRemitTrsType() {
        return freeRemitTrsType;
    }

    public void setFreeRemitTrsType(String freeRemitTrsType) {
        this.freeRemitTrsType = freeRemitTrsType;
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

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getCurrentIndex() {
        return currentIndex;
    }

    public void setCurrentIndex(int currentIndex) {
        this.currentIndex = currentIndex;
    }

    public String get_refresh() {
        return _refresh;
    }

    public void set_refresh(String _refresh) {
        this._refresh = _refresh;
    }
}
