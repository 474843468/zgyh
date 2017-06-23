package com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundQueryHistoryDetail;

/**
 * 047 查询历史交易信息-上送参数
 * Created by wy7105 on 2016/11/24.
 */
public class PsnFundQueryHistoryDetailParams {

    /**
     * transType : 0
     * endDate : 2019/02/01
     * pageSize : 10
     * currentIndex : 0
     * startDate : 2019/01/26
     * _refresh : true
     */
    private String transType; //交易类型
    private String endDate; //截止日期
    private String pageSize; //每页显示条数
    private String currentIndex; //当前页
    private String startDate; //开始日期
    private String _refresh; //刷新标志
    private String conversationId; //会话ID

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    public void setTransType(String transType) {
        this.transType = transType;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public void setPageSize(String pageSize) {
        this.pageSize = pageSize;
    }

    public void setCurrentIndex(String currentIndex) {
        this.currentIndex = currentIndex;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public void set_refresh(String _refresh) {
        this._refresh = _refresh;
    }

    public String getTransType() {
        return transType;
    }

    public String getEndDate() {
        return endDate;
    }

    public String getPageSize() {
        return pageSize;
    }

    public String getCurrentIndex() {
        return currentIndex;
    }

    public String getStartDate() {
        return startDate;
    }

    public String get_refresh() {
        return _refresh;
    }
}
