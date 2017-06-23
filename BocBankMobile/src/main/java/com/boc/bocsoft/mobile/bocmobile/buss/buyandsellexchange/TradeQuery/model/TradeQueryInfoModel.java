package com.boc.bocsoft.mobile.bocmobile.buss.buyandsellexchange.TradeQuery.model;

import java.util.List;

/**
 *
 * Created by wzn7074 on 2016/12/16.
 */
public class TradeQueryInfoModel {
    //页面大小
    private String pageSize;
    //是否刷新
    private String _refresh;
    //页面索引
    private String currentIndex;
    //会话Id
    private String conversationId;
    //开始时间
    private String startDate;
    //结束时间
    private String endDate;

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
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


    //响应字段
    //记录总数
    private int recordNumber;
    private List<TradeQueryInfoResultEntity> List;

    public int getRecordNumber() {
        return recordNumber;
    }

    public void setRecordNumber(int recordNumber) {
        this.recordNumber = recordNumber;
    }

    public java.util.List<TradeQueryInfoResultEntity> getList() {
        return List;
    }

    public void setList(java.util.List<TradeQueryInfoResultEntity> list) {
        List = list;
    }


    public static class TradeQueryInfoResultEntity {

    }


}
