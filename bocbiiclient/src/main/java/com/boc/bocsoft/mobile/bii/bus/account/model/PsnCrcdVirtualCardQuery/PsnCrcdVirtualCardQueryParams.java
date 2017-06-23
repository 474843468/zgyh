package com.boc.bocsoft.mobile.bii.bus.account.model.PsnCrcdVirtualCardQuery;

import com.boc.bocsoft.mobile.bii.bus.account.model.PageParams;


/**
 * @author wangyang
 *         16/7/26 15:59
 *         虚拟银行卡查询
 */
public class PsnCrcdVirtualCardQueryParams extends PageParams{

    /** 账户标识 */
    private String accountId;

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }
}
