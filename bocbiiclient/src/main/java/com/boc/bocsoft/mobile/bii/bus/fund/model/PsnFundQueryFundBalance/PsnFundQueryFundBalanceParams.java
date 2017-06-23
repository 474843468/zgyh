package com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundQueryFundBalance;

/**
 * Created by huixiaobo on 2016/11/18.
 * 015查询基金持仓信息—上送参数
 */
public class PsnFundQueryFundBalanceParams {
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
        return "PsnFundQueryFundBalanceParams{" +
                "fundCode='" + fundCode + '\'' +
                '}';
    }
}
