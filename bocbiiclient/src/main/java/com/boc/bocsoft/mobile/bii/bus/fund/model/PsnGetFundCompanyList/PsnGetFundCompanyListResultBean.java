package com.boc.bocsoft.mobile.bii.bus.fund.model.PsnGetFundCompanyList;

/**
 * 基金公司查询 返回结果 bean
 * Created by lzc4524 on 2016/11/23.
 */
public class PsnGetFundCompanyListResultBean {
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
