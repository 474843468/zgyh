package com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.accountmanagement.model;

/**
 * ViewModel：双向宝-新增保证金账户-双向宝签约提交交易
 * Created by zhx on 2016/12/12
 */
public class VFGSignSubmitViewModel {
    // 借记卡卡号
    private String accountNumber;
    // 保证金账号
    private String marginAccountNo;
    // 保证金产品英文名称
    private String bailEName;
    // 保证金产品中文名称
    private String bailCName;

    public void setMarginAccountNo(String marginAccountNo) {
        this.marginAccountNo = marginAccountNo;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public void setBailEName(String bailEName) {
        this.bailEName = bailEName;
    }

    public void setBailCName(String bailCName) {
        this.bailCName = bailCName;
    }

    public String getMarginAccountNo() {
        return marginAccountNo;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public String getBailEName() {
        return bailEName;
    }

    public String getBailCName() {
        return bailCName;
    }
}