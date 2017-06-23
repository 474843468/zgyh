package com.boc.bocsoft.mobile.bii.bus.longshortforex.model.PsnVFGTradeInfoQuery;

/**
 * Created by zc7067 on 2016/11/17.
 *
 * @des 双向宝-交易查询 012
 */
public class PsnVFGTradeInfoQueryParams {

    /**
     * "queryType": "1",
     * "currencyCode": "001",
     * "pageSize": 15,
     * "_refresh": "true",
     * "currentIndex": 0
     */
    //查询类型，当前有效委托 "1";历史挂单"2";历史成交 "3";斩仓交易查询"4";未平仓交易"5";对账单"6";
    private String queryType;
    //结算币种 人民币、美元、欧元、港币、日元、澳元
    private String currencyCode;
    //页面大小
    private String pageSize;
    //是否刷新
    private String _refresh;
    //页面索引
    private String currentIndex;
    //会话
    private String conversationId;
    //开始时间
    private String startDate;
    //结束时间
    private String endDate;
    public void setQueryType(String queryType) {
        this.queryType = queryType;
    }

    public String getQueryType() {
        return queryType;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setPageSize(String pageSize) {
        this.pageSize = pageSize;
    }

    public String getPageSize() {
        return pageSize;
    }

    public void set_refresh(String _refresh) {
        this._refresh = _refresh;
    }

    public String get_refresh() {
        return _refresh;
    }

    public void setCurrentIndex(String currentIndex) {
        this.currentIndex = currentIndex;
    }

    public String getCurrentIndex() {
        return currentIndex;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    public String getConversationId() {
        return conversationId;
    }
    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getEndDate() {
        return endDate;
    }
}
