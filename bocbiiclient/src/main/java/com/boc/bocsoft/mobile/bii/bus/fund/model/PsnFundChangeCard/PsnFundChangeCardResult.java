package com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundChangeCard;

/**
 * Created by lyf7084 on 2016/11/24.
 */
public class PsnFundChangeCardResult {

    /**
     * newAccountNo : 4111287577431100
     * fundAccount : 1234567890123456
     */
    private String newAccountNo;
    private String fundAccount;

    public void setNewAccountNo(String newAccountNo) {
        this.newAccountNo = newAccountNo;
    }

    public void setFundAccount(String fundAccount) {
        this.fundAccount = fundAccount;
    }

    public String getNewAccountNo() {
        return newAccountNo;
    }

    public String getFundAccount() {
        return fundAccount;
    }
}
