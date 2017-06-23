package com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnQueryActTypeByActNum;

/**
 * 根据账号查询账户类型
 * Created by zhx on 2016/7/27
 */
public class PsnQueryActTypeByActNumParams {
    /**
     * 账号
     */
    private String accountNumber;
    /**
     * 收款人姓名
     */
    private String toName;

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getToName() {
        return toName;
    }

    public void setToName(String toName) {
        this.toName = toName;
    }
}
