package com.boc.bocsoft.mobile.bii.bus.fess.model.PsnFessQueryExchangeRebateRate;

/**
 * I49 4.21 024PsnFessQueryExchangeRebateRate查询结购汇优惠后牌价信息
 * Created by wzn7074 on 2016/11/16.
 */
public class PsnFessQueryExchangeRebateRateResult {

    /**
     * referenceRate : 641.8
     */
    private String referenceRate;//基准汇率
    private String exchangeRate;//优惠后汇率

    public String getExchangeRate() {
        return exchangeRate;
    }

    public void setExchangeRate(String exchangeRate) {
        this.exchangeRate = exchangeRate;
    }

    public void setReferenceRate(String referenceRate) {
        this.referenceRate = referenceRate;
    }

    public String getReferenceRate() {
        return referenceRate;
    }
}
