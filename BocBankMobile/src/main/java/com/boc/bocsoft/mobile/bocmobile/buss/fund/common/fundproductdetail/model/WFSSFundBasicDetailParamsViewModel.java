package com.boc.bocsoft.mobile.bocmobile.buss.fund.common.fundproductdetail.model;

/**
 * 登陆前基金详情参数-四方数据
 * Created by lzc4524 on 2016/11/25.
 */
public class WFSSFundBasicDetailParamsViewModel {
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
