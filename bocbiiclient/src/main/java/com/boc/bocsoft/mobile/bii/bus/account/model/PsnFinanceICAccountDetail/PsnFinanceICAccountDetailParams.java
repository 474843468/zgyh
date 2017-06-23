package com.boc.bocsoft.mobile.bii.bus.account.model.PsnFinanceICAccountDetail;

/**
 * @author wangyang
 *         16/6/17 09:25
 *         电子现金账户概览参数
 */
public class PsnFinanceICAccountDetailParams {

    /** 账号Id */
    private String accountId;

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public PsnFinanceICAccountDetailParams(String accountId) {
        this.accountId = accountId;
    }
}
