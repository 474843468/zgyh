package com.boc.bocsoft.mobile.wfss.buss.funds.model.queryfundbasicdetail;

/**
 * 3.2 基金详情接口（基本信息）
 * Created by gwluo on 2016/10/25.
 */

public class WFSSQueryFundBasicDetailParams {
    //基金Id上送fundId为内部唯一标识，不是给客户展示的基金code
    private String fundId;

    private String fundBakCode;

    public String getFundBakCode() {
        return fundBakCode;
    }

    public void setFundBakCode(String fundBakCode) {
        this.fundBakCode = fundBakCode;
    }

    public String getFundId() {
        return fundId;
    }

    public void setFundId(String fundId) {
        this.fundId = fundId;
    }
}
