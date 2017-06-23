package com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnOFAIncreaseBind;

/**
 * 重新绑定理财账户_结果
 * Created by Wan mengxin on 2016/9/20.
 */
public class PsnOFAIncreaseBindResult {
    // 账户类型
    private String accountType;
    //账户别名
    private String nickname;
    //账号（DisplayNumber）
    private String accountNumber;

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }
}
