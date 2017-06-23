package com.boc.bocsoft.mobile.wfss.buss.funds.model.newslist;

/**
 * 3.8 基金详情接口8（基金新闻列表，分页)
 * Created by gwluo on 2016/10/26.
 */

public class WFSSNewsListParams {
    private String pageNo;//	页数		上送页数，默认为1
    private String pageSize;//	每页最大条数		上送每页最大条数，默认为10
    private String fundBakCode;//基金公共代码

    public String getPageNo() {
        return pageNo;
    }

    public String getPageSize() {
        return pageSize;
    }

    public String getFundBakCode() {
        return fundBakCode;
    }

    public void setFundBakCode(String fundBakCode) {
        this.fundBakCode = fundBakCode;
    }


    public void setPageNo(String pageNo) {
        this.pageNo = pageNo;
    }

    public void setPageSize(String pageSize) {
        this.pageSize = pageSize;
    }
}
