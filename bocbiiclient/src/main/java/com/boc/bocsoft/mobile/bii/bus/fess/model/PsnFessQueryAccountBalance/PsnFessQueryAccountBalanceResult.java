package com.boc.bocsoft.mobile.bii.bus.fess.model.PsnFessQueryAccountBalance;

import java.io.Serializable;

/**
 * 4.2 002 PsnFessQueryAccountBalance查询结售汇帐户余额
 * Created by gwluo on 2016/11/16.
 */

public class PsnFessQueryAccountBalanceResult implements Serializable{
    private String currency;//	币种
    private String cashRemit;//	钞汇
    private String availableBalance;//	可用余额

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
