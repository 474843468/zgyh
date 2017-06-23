package com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransQueryTransferRecordsNew;

/**
 * 转账记录查询 - 新接口
 * Created by wangf on 2016/8/9.
 */
public class PsnTransQueryTransferRecordsNewParams {
    private String transType;
    private String startDate;
    private String endDate;
    private String startAmount;
    private String endAmount;
    private String payeeAccountName;
    private String status;
    private int currentIndex;
    private int pageSize;

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

    public String getStartAmount() {
        return startAmount;
    }

    public void setStartAmount(String startAmount) {
        this.startAmount = startAmount;
    }

    public String getEndAmount() {
        return endAmount;
    }

    public void setEndAmount(String endAmount) {
        this.endAmount = endAmount;
    }

    public String getPayeeAccountName() {
        return payeeAccountName;
    }

    public void setPayeeAccountName(String payeeAccountName) {
        this.payeeAccountName = payeeAccountName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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
