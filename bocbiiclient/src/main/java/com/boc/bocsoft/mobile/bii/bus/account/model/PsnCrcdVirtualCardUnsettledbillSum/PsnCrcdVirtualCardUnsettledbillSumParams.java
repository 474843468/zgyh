package com.boc.bocsoft.mobile.bii.bus.account.model.PsnCrcdVirtualCardUnsettledbillSum;

/**
 * @author wangyang
 *         16/7/26 17:50
 *         虚拟银行卡未出账单合计查询
 */
public class PsnCrcdVirtualCardUnsettledbillSumParams {

    /** 实体卡账户户名 */
    private String accountName;
    /** 虚拟卡卡号 */
    private String virtualCardNo;

    public PsnCrcdVirtualCardUnsettledbillSumParams(String accountName, String virtualCardNo) {
        this.accountName = accountName;
        this.virtualCardNo = virtualCardNo;
    }

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
