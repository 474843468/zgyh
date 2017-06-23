package com.boc.bocsoft.mobile.bii.bus.account.model.PsnMedicalInsurAcctDetailQuery;

/**
 * 医保账户详情查询请求报文
 * Created by niuguobin on 2016/6/23.
 */
public class PsnMedicalInsurAcctDetailQueryParams {
    private String accountId;

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public PsnMedicalInsurAcctDetailQueryParams(String accountId) {
        this.accountId = accountId;
    }
}
