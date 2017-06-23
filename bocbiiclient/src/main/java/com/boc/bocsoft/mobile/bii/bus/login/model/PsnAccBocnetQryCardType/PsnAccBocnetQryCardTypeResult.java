package com.boc.bocsoft.mobile.bii.bus.login.model.PsnAccBocnetQryCardType;


/**
 * Created by feib on 16/8/2.
 */
public class PsnAccBocnetQryCardTypeResult {
    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    /**
     * 119-长城电子借记卡103-中银系列信用卡104-长城信用卡107-单外币信用卡
     */
    private String accountType;
}
