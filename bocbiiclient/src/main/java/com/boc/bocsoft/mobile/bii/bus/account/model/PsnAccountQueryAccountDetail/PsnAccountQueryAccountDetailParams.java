package com.boc.bocsoft.mobile.bii.bus.account.model.PsnAccountQueryAccountDetail;

/**
 * 查询账户详情
 * Created by xdy4486 on 2016/6/25.
 */
public class PsnAccountQueryAccountDetailParams {

    private String accountId;

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public PsnAccountQueryAccountDetailParams(String accountId) {
        this.accountId = accountId;
    }

    public PsnAccountQueryAccountDetailParams() {
    }
}
