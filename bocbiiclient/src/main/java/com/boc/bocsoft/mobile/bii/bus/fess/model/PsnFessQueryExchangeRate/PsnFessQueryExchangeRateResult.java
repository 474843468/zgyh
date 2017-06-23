package com.boc.bocsoft.mobile.bii.bus.fess.model.PsnFessQueryExchangeRate;

import java.math.BigDecimal;

/**
 * I49 4.6 006PsnFessQueryExchangeRate查询结购汇当前参考牌价
 * Created by gwluo on 2016/11/18.
 */

public class PsnFessQueryExchangeRateResult {
    private BigDecimal referenceRate;// 参考牌价	BigDecimal

    public BigDecimal getReferenceRate() {
        return referenceRate;
    }

    public void setReferenceRate(BigDecimal referenceRate) {
        this.referenceRate = referenceRate;
    }
}
