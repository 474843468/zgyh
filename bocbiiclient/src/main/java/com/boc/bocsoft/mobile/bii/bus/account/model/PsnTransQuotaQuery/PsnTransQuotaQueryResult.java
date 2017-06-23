package com.boc.bocsoft.mobile.bii.bus.account.model.PsnTransQuotaQuery;


import java.math.BigDecimal;

/**
 * Created by wangyang on 2016/11/1.
 */
public class PsnTransQuotaQueryResult {


    /**
     * executeDate  : 2016/10/25
     * quotaAmount  : 10000
     */
    private BigDecimal quotaAmount;

    public BigDecimal getQuotaAmount() {
        return quotaAmount;
    }

    public void setQuotaAmount(BigDecimal quotaAmount) {
        this.quotaAmount = quotaAmount;
    }

}
