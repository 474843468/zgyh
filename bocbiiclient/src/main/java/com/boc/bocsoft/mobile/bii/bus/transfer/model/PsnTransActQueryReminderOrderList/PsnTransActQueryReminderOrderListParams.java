package com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransActQueryReminderOrderList;

/**
 * 催款指令查询
 * Created by zhx on 2016/7/5
 */
public class PsnTransActQueryReminderOrderListParams {
    /**
     * 开始日期
     */
    private String startDate;
    /**
     * 结束日期
     */
    private String endDate;
    /**
     * 当前页
     */
    private int currentIndex;
    /**
     * 每页显示条数
     */
    private int pageSize;

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
