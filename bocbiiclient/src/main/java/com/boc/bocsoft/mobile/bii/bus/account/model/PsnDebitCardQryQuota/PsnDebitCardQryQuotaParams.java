package com.boc.bocsoft.mobile.bii.bus.account.model.PsnDebitCardQryQuota;

/**
 * @author wangyang
 *         2016/10/10 20:31
 *         查询借记卡已设置限额
 */
public class PsnDebitCardQryQuotaParams {

    /** 借记卡账户标识 */
    private String accountId;

    public PsnDebitCardQryQuotaParams(String accountId) {
        this.accountId = accountId;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }
}
