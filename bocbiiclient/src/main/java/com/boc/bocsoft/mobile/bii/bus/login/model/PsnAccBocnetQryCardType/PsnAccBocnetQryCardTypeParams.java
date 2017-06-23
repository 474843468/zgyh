package com.boc.bocsoft.mobile.bii.bus.login.model.PsnAccBocnetQryCardType;

/**
 * Created by feib on 16/8/2.
 * 通过卡号查询卡类型,判断是否可以进行卡号登录
 */
public class PsnAccBocnetQryCardTypeParams {
    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    private String accountNumber;

}
