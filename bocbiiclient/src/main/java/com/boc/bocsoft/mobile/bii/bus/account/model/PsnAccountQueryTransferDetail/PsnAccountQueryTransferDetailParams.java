package com.boc.bocsoft.mobile.bii.bus.account.model.PsnAccountQueryTransferDetail;

/**
 * Created by wangf on 2016/6/4.
 */
public class PsnAccountQueryTransferDetailParams {


    /**
     * accountId : 101020305
     * currency :
     * cashRemit :
     * startDate : 2019/01/26
     * endDate : 2019/02/01
     * currentIndex : 0
     * _refresh : true
     * pageSize : 10
     */

    /**
     * 会话ID
     */
    private String conversationId;

    /**
     * 账户标识
     */
    private String accountId;
    /**
     * 币种
     * 001 = 人民币
     * 014 = 美元
     * 027 = 日元
     * 038 = 欧元
     */
    private String currency;
    /**
     * 钞汇
     * 01 = 现钞
     * 02 = 现汇
     */
    private String cashRemit;
    /**
     * 起始日期
     */
    private String startDate;
    /**
     * 结束日期
     */
    private String endDate;
    /**
     * 当前页索引
     * _refresh = true 时必须送0
     */
    private int currentIndex;
    /**
     * 是否重新查询结果
     * true：重新查询
     * false：不重新查询
     * 当前查询条件改变时需要重新查询
     */
    private String _refresh;
    /**
     * 页面大小
     */
    private int pageSize;

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

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getCashRemit() {
        return cashRemit;
    }

    public void setCashRemit(String cashRemit) {
        this.cashRemit = cashRemit;
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

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
}
