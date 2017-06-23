package com.boc.bocsoft.mobile.bii.bus.longshortforex.model.PsnVFGBailListQuery;

/**
 * Result：查询保证金账户列表
 * Created by zhx on 2016/11/21
 */
public class PsnVFGBailListQueryResult {

    /**
     * marginAccountNo : 33333333333333333
     * settleCurrency : 33
     * accountNumber : 3333333333333333333
     * signDate : Sat Feb 02 02:02:00 CST 2013
     * subAcctType : 0
     */
    // 借记卡卡号
    private String accountNumber;
    // 账户别名
    private String nickName;
    // 保证金账号
    private String marginAccountNo;
    // 结算货币
    private String settleCurrency;
    // 子账户类别
    private String subAcctType;
    // 签约时间
    private String signDate;

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getMarginAccountNo() {
        return marginAccountNo;
    }

    public void setMarginAccountNo(String marginAccountNo) {
        this.marginAccountNo = marginAccountNo;
    }

    public String getSettleCurrency() {
        return settleCurrency;
    }

    public void setSettleCurrency(String settleCurrency) {
        this.settleCurrency = settleCurrency;
    }

    public String getSubAcctType() {
        return subAcctType;
    }

    public void setSubAcctType(String subAcctType) {
        this.subAcctType = subAcctType;
    }

    public String getSignDate() {
        return signDate;
    }

    public void setSignDate(String signDate) {
        this.signDate = signDate;
    }
}
