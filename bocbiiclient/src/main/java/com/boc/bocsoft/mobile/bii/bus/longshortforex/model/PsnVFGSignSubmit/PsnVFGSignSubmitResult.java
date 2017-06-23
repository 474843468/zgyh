package com.boc.bocsoft.mobile.bii.bus.longshortforex.model.PsnVFGSignSubmit;

/**
 * Result：双向宝签约提交交易
 * Created by zhx on 2016/11/21
 */
public class PsnVFGSignSubmitResult {

    /**
     * marginAccountNo : 33333333333333333
     * accountNumber : 3333333333333333333
     * bailEName : 3333333333333333
     * bailCName : 3333333333333333333333333333333333333333
     */
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
