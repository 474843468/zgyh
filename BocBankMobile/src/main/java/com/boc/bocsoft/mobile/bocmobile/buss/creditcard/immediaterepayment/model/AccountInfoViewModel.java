package com.boc.bocsoft.mobile.bocmobile.buss.creditcard.immediaterepayment.model;

import java.math.BigDecimal;

/**
 * 作者：xwg on 16/11/25 08:43
 *  普通借记卡还款账户详情
 */
public class AccountInfoViewModel {

    // 可用余额
    private BigDecimal availableBalance;
    // 币种
    private String currencyCode;

    /*交易响应数据*/
    // 到期日期
    private String dueDate;
    // 交易状态
    private String status;

    private String cashRemit;

    public String getCashRemit() {
        return cashRemit;
    }

    public void setCashRemit(String cashRemit) {
        this.cashRemit = cashRemit;
    }


    public BigDecimal getAvailableBalance() {
        return availableBalance;
    }

    public void setAvailableBalance(BigDecimal availableBalance) {
        this.availableBalance = availableBalance;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
