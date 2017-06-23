package com.boc.bocsoft.mobile.bocmobile.base.widget.AccountOverview;

/**
 * 借记卡币种余额
 * Created by niuguobin on 2016/6/17.
 */
public class BalanceModel {
    /**
     * 币种
     */
    private String currency;
    /**
     * 是否显示“钞”图标
     * 如果为空时不显示
     * 01显示“钞”
     * 02显示“汇”
     */
    private String moneyIcon;
    /**
     * 余额
     */
    private String balance;
    /**
     * 可用余额
     */
    private String usableBlance;

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getMoneyIcon() {
        return moneyIcon;
    }

    public void setMoneyIcon(String moneyIcon) {
        this.moneyIcon = moneyIcon;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getUsableBlance() {
        return usableBlance;
    }

    public void setUsableBlance(String usableBlance) {
        this.usableBlance = usableBlance;
    }

}
