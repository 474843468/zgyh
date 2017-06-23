package com.boc.bocsoft.mobile.bii.bus.fess.model.PsnFessQueryAccountBalance;

/**
 * I49 4.2 002 PsnFessQueryAccountBalance查询结售汇帐户余额
 * Created by gwluo on 2016/11/16.
 */

public class PsnFessQueryAccountBalanceParams {
    private String accountId;//	账户Id

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }
}
