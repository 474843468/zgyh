package com.boc.bocsoft.mobile.bocmobile.buss.account.finance.model;

import org.threeten.bp.LocalDate;

import java.math.BigDecimal;

/**
 * @author wangyang
 *         16/6/29 09:58
 *         交易详情Model
 */
public class TransDetailModel {

    /**
     * 币种
     */
    private String currency;
    /**
     * 交易类型
     */
    private String transferType;
    /**
     * 收入/支出总额
     */
    private BigDecimal amount;
    /**
     * 交易日期
     */
    private LocalDate returnDate;
    /**
     * 转入/转出标识  true-转出 0,false-转入 1
     */
    private boolean amountFlag;

    public boolean isAmountFlag() {
        return amountFlag;
    }

    public void setAmountFlag(boolean amountFlag) {
        this.amountFlag = amountFlag;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getTransferType() {
        return transferType;
    }

    public void setTransferType(String transferType) {
        this.transferType = transferType;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }
}
