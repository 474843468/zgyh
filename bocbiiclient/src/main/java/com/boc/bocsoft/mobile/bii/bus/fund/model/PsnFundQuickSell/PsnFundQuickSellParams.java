package com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundQuickSell;

import java.math.BigDecimal;

/**
 * Created by zcy7065 on 2016/12/13.
 */
public class PsnFundQuickSellParams {

    private BigDecimal fincSellAmount;//赎回份额
    private String fundCode;//基金代码
    private String token;

    public void setFincSellAmount(BigDecimal fincSellAmount) {
        this.fincSellAmount = fincSellAmount;
    }

    public void setFundCode(String fundCode) {
        this.fundCode = fundCode;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public BigDecimal getFincSellAmount() {
        return fincSellAmount;
    }

    public String getFundCode() {
        return fundCode;
    }

    public String getToken() {
        return token;
    }
}
