package com.boc.bocsoft.mobile.bii.bus.account.model.PsnCrcdVirtualCardApplyInit;

/**
 * @author wangyang
 *         16/7/25 14:32
 *         申请虚拟银行卡初始化
 */
public class PsnCrcdVirtualCardApplyInitParams {

    /** 附属卡卡号 */
    private String accountId;

    public PsnCrcdVirtualCardApplyInitParams(String accountId) {
        this.accountId = accountId;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }
}
