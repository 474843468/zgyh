package com.boc.bocsoft.mobile.bii.bus.fess.model.PsnFessQueryHibsExchangeTrans;

/**
 * 4.18 018PsnFessQueryHibsExchangeTrans查询全渠道结购汇交易列表
 * Created by gwluo on 2016/11/18.
 */

public class PsnFessQueryHibsExchangeTransParams {

    private String conversationId; //会话
    private String fessFlag;//	结售汇业务类型 00全部 01结汇 11购汇	默认上送为“00”查询全部
    private String startDate;//	起始时间	String 	M	yyyy/MM/dd
    private String endDate;//	结束时间	String	M	yyyy/MM/dd
    private String currentIndex;//	当前页索引	String	M
    private String pageSize;//	页面大小	String	M
    private String _refresh;//	刷新标志	String	M	true：重新查询结果(在交易改变查询条件时是需要重新查询的,currentIndex需上送0)false:不需要重新查询，使用缓存中的结果

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    public String getFessFlag() {
        return fessFlag;
    }

    public void setFessFlag(String fessFlag) {
        this.fessFlag = fessFlag;
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
}
