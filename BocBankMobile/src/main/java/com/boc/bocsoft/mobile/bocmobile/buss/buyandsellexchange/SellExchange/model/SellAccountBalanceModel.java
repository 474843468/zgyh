package com.boc.bocsoft.mobile.bocmobile.buss.buyandsellexchange.SellExchange.model;

import java.io.Serializable;

/**
 * 结汇，账户和余额
 * Created by gwluo on 2016/12/21.
 */

public class SellAccountBalanceModel implements Serializable {
    private boolean isBalanceFail;//余额接口结果，true 余额失败，false 余额成功
    private String accountNum;
    private String accountId;
    private String accountType;//账户类型
    private String currency;//	币种
    private String cashRemit;//	钞汇
    private String availableBalance;//	可用余额
    private boolean isShowTitle;//	是否显示标题

    public boolean isShowTitle() {
        return isShowTitle;
    }

    public void setShowTitle(boolean showTitle) {
        isShowTitle = showTitle;
    }

    public boolean isBalanceFail() {
        return isBalanceFail;
    }

    public void setBalanceFail(boolean balanceFail) {
        isBalanceFail = balanceFail;
    }

    public String getAccountNum() {
        return accountNum;
    }

    public void setAccountNum(String accountNum) {
        this.accountNum = accountNum;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getCashRemit() {
        return cashRemit;
    }

    public void setCashRemit(String cashRemit) {
        this.cashRemit = cashRemit;
    }

    public String getAvailableBalance() {
        return availableBalance;
    }

    public void setAvailableBalance(String availableBalance) {
        this.availableBalance = availableBalance;
    }
}
