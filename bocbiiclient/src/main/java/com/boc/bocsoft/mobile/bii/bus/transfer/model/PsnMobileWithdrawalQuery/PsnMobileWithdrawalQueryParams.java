package com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnMobileWithdrawalQuery;

/**
 * 手机取款 -- 取款查询
 * Created by wangf on 2016/6/23.
 */
public class PsnMobileWithdrawalQueryParams {


    /**
     * startDate : 2019/01/26
     * endDate : 2019/02/01
     * currentIndex : 0
     * pageSize : 10
     */

    /**
     * 会话ID
     */
    private String conversationId;

    /**
     * 起始日期
     */
    private String startDate;
    /**
     * 结束日期
     */
    private String endDate;
    /**
     * 当前页号
     */
    private int currentIndex;
    /**
     * 每页显示条数
     */
    private int pageSize;

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
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

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
}
