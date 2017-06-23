package com.boc.bocsoft.mobile.wfss.buss.funds.model.fundnotice;

/**
 * 3.7 基金详情接口7（基金公告列表，分页)
 * Created by gwluo on 2016/10/26.
 */

public class WFSSFundnoticeParams {
    private String fundId;//基金Id	20	Y
    private String pageNo;//页数，上送页数，默认为1
    private String pageSize; //每页最大条数

    public String getFundId() {
        return fundId;
    }

    public String getPageNo() {
        return pageNo;
    }

    public String getPageSize() {
        return pageSize;
    }

    public void setPageSize(String pageSize) {
        this.pageSize = pageSize;
    }

    public void setFundId(String fundId) {
        this.fundId = fundId;
    }

    public void setPageNo(String pageNo) {
        this.pageNo = pageNo;
    }
}
