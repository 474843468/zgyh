package com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundCompanyQueryOutlay;

/**
 * 登录前基金公司列表查询返回
 * Created by liuzc on 2016/12/23.
 */
public class PsnFundCompanyQueryOutlayResult {
    private String fundCompanyName; //基金公司名称
    private String fundCompanyCode; //基金公司代码

    public String getFundCompanyName() {
        return fundCompanyName;
    }

    public void setFundCompanyName(String fundCompanyName) {
        this.fundCompanyName = fundCompanyName;
    }

    public String getFundCompanyCode() {
        return fundCompanyCode;
    }

    public void setFundCompanyCode(String fundCompanyCode) {
        this.fundCompanyCode = fundCompanyCode;
    }
}
