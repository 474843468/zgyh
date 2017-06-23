package com.boc.bocsoft.mobile.bii.bus.activitymanagementpaltform.model.PsnQueryTransActivityStatus;

/**
 * 3.37 037 PsnQueryTransActivityStatus 查询交易可参与的活动并取票
 * <p>
 * Created by yx on 2016/12/19.
 */
public class PsnQueryTransActivityStatusParams {
    private String transactionId;//交易id

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }
}
