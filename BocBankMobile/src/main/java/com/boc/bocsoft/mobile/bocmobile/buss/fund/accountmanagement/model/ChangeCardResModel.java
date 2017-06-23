package com.boc.bocsoft.mobile.bocmobile.buss.fund.accountmanagement.model;

/**
 * 由049接口 PsnFundChangeCard 变更资金账户
 * 上送参数：newAccountId token
 * 返回参数：fundAccount，newAccountNo
 * Created by lyf7084 on 2016/12/13.
 */
public class ChangeCardResModel {
    /**
     * 新资金账号
     */
    private String newAccountNo;

    /**
     * 基金账号
     */
    private String fundAccount;

    public String getFundAccount() {
        return fundAccount;
    }

    public void setFundAccount(String fundAccount) {
        this.fundAccount = fundAccount;
    }

    public String getNewAccountNo() {
        return newAccountNo;
    }

    public void setNewAccountNo(String newAccountNo) {
        this.newAccountNo = newAccountNo;
    }

}
