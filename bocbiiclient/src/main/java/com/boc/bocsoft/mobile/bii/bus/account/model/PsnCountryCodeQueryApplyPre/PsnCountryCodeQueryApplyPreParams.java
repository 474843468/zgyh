package com.boc.bocsoft.mobile.bii.bus.account.model.PsnCountryCodeQueryApplyPre;

/**
 * 查询个人客户国籍信息请求
 * Created by liuyang on 2016/6/12.
 */
public class PsnCountryCodeQueryApplyPreParams {


    /**
     * 申请人账户标识
     */

    private String accountId;

    public PsnCountryCodeQueryApplyPreParams(String accountId) {
        this.accountId = accountId;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }
}
