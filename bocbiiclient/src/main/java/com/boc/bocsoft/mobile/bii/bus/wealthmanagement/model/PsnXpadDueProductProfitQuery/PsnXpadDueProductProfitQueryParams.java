package com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadDueProductProfitQuery;

/**
 * 到期产品查询
 * Created by zhx on 2016/9/5
 */
public class PsnXpadDueProductProfitQueryParams {

    /**
     * currentIndex : 0
     * startDate : 2019/01/26
     * accountKey :
     * conversationId : d22b34a3-58cd-427b-a02c-aa5892e0dbd3
     * pageSize : 15
     * endDate : 2019/02/01
     * currency : 000
     * _refresh : true
     */

    // 账户缓存标识（36位长度）
    private String accountKey;
    // 开始日期（yyyyMMDD）
    private String startDate;
    // 结束日期
    private String endDate;
    // 页面大小（每页显示15条）
    private String pageSize;
    // 当前页索引
    private String currentIndex;
    // 刷新标识（true：重新查询结果(在交易改变查询条件时是需要重新查询的,currentIndex需上送0)false:不需要重新查询，使用缓存中的结果）
    private String _refresh;
    // 币种（000：全部、001：人民币元 014：美元 012：英镑 013：港币 028: 加拿大元 029：澳元 038：欧元 027：日元）
    private String currency;
    private String conversationId;

    public void setCurrentIndex(String currentIndex) {
        this.currentIndex = currentIndex;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
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

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public void set_refresh(String _refresh) {
        this._refresh = _refresh;
    }

    public String getCurrentIndex() {
        return currentIndex;
    }

    public String getStartDate() {
        return startDate;
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

    public String getEndDate() {
        return endDate;
    }

    public String getCurrency() {
        return currency;
    }

    public String get_refresh() {
        return _refresh;
    }
}
