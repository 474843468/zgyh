package com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundCompanyInfoQuery;

/**
 * Created by huixiaobo on 2016/11/18.
 * 061基金公司信息查询—上送参数
 */
public class PsnFundCompanyInfoQueryParams {
    /**基金代码*/
    private String fundCode;

    public String getFundCode() {
        return fundCode;
    }

    public void setFundCode(String fundCode) {
        this.fundCode = fundCode;
    }

    @Override
    public String toString() {
        return "PsnFundCompanyInfoQueryParams{" +
                "fundCode='" + fundCode + '\'' +
                '}';
    }
}
