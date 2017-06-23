package com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnMobileRemitDetailsQuery;

/**
 * 手机取款 -- 汇出详情查询
 * Created by wangf on 2016/7/5.
 */
public class PsnMobileRemitDetailsQueryParams {

    /**
     * transactionId : 100000000001
     */

    //网银交易序号
    private String transactionId;

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }
}
