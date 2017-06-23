package com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransQueryTransferRecord;

/**
 * 转账记录查询
 * Created by wangf on 2016/6/23.
 */
public class PsnTransQueryTransferRecordParams {


    /**
     * transType : PB999
     * startDate : 2019/01/26
     * endDate : 2019/02/01
     * pageSize : 15
     * currentIndex : 0
     */

    /**
     * 会话ID
     */
    private String conversationId;


    /**
     * 转账类型
     */
    private String transType;
    /**
     * 起始日期
     */
    private String startDate;
    /**
     * 结束日期
     */
    private String endDate;
    /**
     * 当前页
     */
    private int pageSize;
    /**
     * 每页显示条数
     */
    private int currentIndex;

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    public String getTransType() {
        return transType;
    }

    public void setTransType(String transType) {
        this.transType = transType;
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
}
