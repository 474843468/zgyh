package com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransActQueryPaymentOrderList;

/**
 * Created by Administrator on 2016/6/28.
 */
public class PsnPaymentOrderListParam {

    private String currentIndex;
    private String endDate;
    private String pageSize;
    private String startDate;

    public String getCurrentIndex() {
        return currentIndex;
    }

    public void setCurrentIndex(String currentIndex) {
        this.currentIndex = currentIndex;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getPageSize() {
        return pageSize;
    }

    public void setPageSize(String pageSize) {
        this.pageSize = pageSize;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }
}
