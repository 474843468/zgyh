package com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLOANUseRecordsQuery;

/**
 * Created by Administrator on 2016/6/16.
 * 用款查询上送参数
 */
public class PsnLOANUseRecordsQueryParams {

    /**查询开始日期 yyyy/MM/dd*/
    private String startDate;
    /**查询结束日期 yyyy/MM/dd*/
    private String endDate;
    /**贷款账号*/
    private String loanActNum;
    /**每页显示条数 该字段最大值为10（后台系统CTIS最大支持每页返回10条记录）*/
    private String pageSize;
    /**当前页 第一页送1，第二页送2，第三页送3，以此类推*/
    private String currentIndex;

    public String getStartDate() {
        return startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public String getLoanActNum() {
        return loanActNum;
    }

    public String getPageSize() {
        return pageSize;
    }

    public String getCurrentIndex() {
        return currentIndex;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public void setLoanActNum(String loanActNum) {
        this.loanActNum = loanActNum;
    }

    public void setPageSize(String pageSize) {
        this.pageSize = pageSize;
    }

    public void setCurrentIndex(String currentIndex) {
        this.currentIndex = currentIndex;
    }

    @Override
    public String toString() {
        return "PsnLOANUseRecordsQueryParams{" +
                "startDate='" + startDate + '\'' +
                ", endDate='" + endDate + '\'' +
                ", loanActNum='" + loanActNum + '\'' +
                ", pageSize='" + pageSize + '\'' +
                ", currentIndex='" + currentIndex + '\'' +
                '}';
    }
}
