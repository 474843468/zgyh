package com.boc.bocsoft.mobile.bii.bus.account.model.PsnCrcdVirtualCardUnsettledbillQuery;

import com.boc.bocsoft.mobile.bii.bus.account.model.PageParams;

/**
 * @author wangyang
 *         16/7/26 17:34
 *         虚拟银行卡未出账单查询
 */
public class PsnCrcdVirtualCardUnsettledbillQueryParams extends PageParams{

    /** 账户户名 */
    private String accountName;
    /** 虚拟卡卡号 */
    private String virtualCardNo;

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getVirtualCardNo() {
        return virtualCardNo;
    }

    public void setVirtualCardNo(String virtualCardNo) {
        this.virtualCardNo = virtualCardNo;
    }
}
