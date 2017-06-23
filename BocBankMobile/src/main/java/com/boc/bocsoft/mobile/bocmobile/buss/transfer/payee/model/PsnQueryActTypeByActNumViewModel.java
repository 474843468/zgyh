package com.boc.bocsoft.mobile.bocmobile.buss.transfer.payee.model;

/**
 * 根据账号查询账户类型
 * Created by zhx on 2016/7/27
 */
public class PsnQueryActTypeByActNumViewModel {
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

    //======================================//
    // 下面大致对应接口响应的字段
    //======================================//

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