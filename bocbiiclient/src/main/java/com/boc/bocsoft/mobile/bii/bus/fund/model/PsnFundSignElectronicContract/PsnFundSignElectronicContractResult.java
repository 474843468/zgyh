package com.boc.bocsoft.mobile.bii.bus.fund.model.PsnFundSignElectronicContract;

/**
 * Created by zcy7065 on 2016/12/2.
 */
public class PsnFundSignElectronicContractResult {

    private String investAccount;//投资交易账号
    private String accountId;//账户ID
    private String SignDate;//签约日期
    private String fincCode;//基金代码

    public void setInvestAccount(String investAccount) {
        this.investAccount = investAccount;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public void setSignDate(String SignDate) {
        this.SignDate = SignDate;
    }

    public void setFincCode(String fincCode) {
        this.fincCode = fincCode;
    }

    public String getInvestAccount() {
        return investAccount;
    }

    public String getAccountId() {
        return accountId;
    }

    public String getSignDate() {
        return SignDate;
    }

    public String getFincCode() {
        return fincCode;
    }

}
