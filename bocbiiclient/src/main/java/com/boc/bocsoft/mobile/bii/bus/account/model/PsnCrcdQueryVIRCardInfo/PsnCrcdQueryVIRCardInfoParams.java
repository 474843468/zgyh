package com.boc.bocsoft.mobile.bii.bus.account.model.PsnCrcdQueryVIRCardInfo;

/**
 * @author wangyang
 *         2016/10/28 21:49
 *         查询虚拟卡详情
 */
public class PsnCrcdQueryVIRCardInfoParams {

    private String accountId;

    public PsnCrcdQueryVIRCardInfoParams(String accountId) {
        this.accountId = accountId;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }
}
