package com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnQueryActTypeByActNum;

/**
 * 根据账号查询账户类型
 * Created by zhx on 2016/7/27
 */
public class PsnQueryActTypeByActNumResult {
    /**
     * 账户类型
     */
    private String accountType;
    /**
     * 账户归属机构
     */
    private String ibkNumber;

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getIbkNumber() {
        return ibkNumber;
    }

    public void setIbkNumber(String ibkNumber) {
        this.ibkNumber = ibkNumber;
    }
}
