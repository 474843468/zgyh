package com.boc.bocsoft.mobile.bii.bus.account.model.PsnMedicalInsurAcctDetailQuery;

import org.threeten.bp.LocalDate;

import java.math.BigDecimal;

/**
 * 账户详情列表
 * Created by niubuobin on 2016/6/23.
 */
public class AccountDetai {
    /**
     * 币种
     */
    private String currencyCode;
    /**
     * 钞汇标识
     * 00=无钞汇标识
     * 01=现钞
     * 02=现汇
     */
    private String cashRemit;
    /**
     * 当前余额
     */
    private BigDecimal bookBalance;
    /**
     * 可用余额
     */
    private BigDecimal availableBalance;
    /**
     * 开户日期
     * YYYY/MM/DD
     */
    private LocalDate openDate;
    /**
     * 子账户状态
     */
    private String status;
    /**
     * 结清日期
     * YYYY/MM/DD
     */
    private LocalDate settlementDate;
    /**
     * 冻结金额
     */
    private BigDecimal holdAmount;

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public String getCashRemit() {
        return cashRemit;
    }

    public void setCashRemit(String cashRemit) {
        this.cashRemit = cashRemit;
    }

    public BigDecimal getBookBalance() {
        return bookBalance;
    }

    public void setBookBalance(BigDecimal bookBalance) {
        this.bookBalance = bookBalance;
    }

    public BigDecimal getAvailableBalance() {
        return availableBalance;
    }

    public void setAvailableBalance(BigDecimal availableBalance) {
        this.availableBalance = availableBalance;
    }

    public LocalDate getOpenDate() {
        return openDate;
    }

    public void setOpenDate(LocalDate openDate) {
        this.openDate = openDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDate getSettlementDate() {
        return settlementDate;
    }

    public void setSettlementDate(LocalDate settlementDate) {
        this.settlementDate = settlementDate;
    }

    public BigDecimal getHoldAmount() {
        return holdAmount;
    }

    public void setHoldAmount(BigDecimal holdAmount) {
        this.holdAmount = holdAmount;
    }
}
