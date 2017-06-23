package com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransActQueryPaymentOrderList;

import org.threeten.bp.LocalDateTime;

/**
 * 付款指令查询
 * Created by zhx on 2016/6/29.
 */
public class PsnTransActQueryPaymentOrderListParams {
    /**
     * 开始日期
     */
    private LocalDateTime startDate;
    /**
     * 结束日期
     */
    private LocalDateTime endDate;
    /**
     * 当前页
     */
    private int currentIndex;
    /**
     * 每页显示条数
     */
    private int pageSize;

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
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