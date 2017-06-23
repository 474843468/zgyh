package com.boc.bocsoft.mobile.bii.bus.account.model.PsnFinanceICAccountDetail;

import java.math.BigDecimal;

/**
 * @author wangyang
 *         16/6/17 09:25
 *         电子现金账户概览结果
 */
public class PsnFinanceICAccountDetailResult {

    /** 电子现金账户状态 */
    private String status;
    /** 电子现金卡片余额上限 */
    private BigDecimal eCashUpperLimit;
    /** 电子现金单笔交易限额 */
    private BigDecimal singleLimit;
    /** 币种 */
    private String currency;
    /** 钞/汇 */
    private String cashRemit;
    /** 电子现金账户余额 */
    private BigDecimal totalBalance;
    /** 补登账户余额 */
    private BigDecimal supplyBalance;
    /** 最大可充值金额 */
    private BigDecimal maxLoadingAmount;
    /** 电子现金账户号 */
    private String cardNumber;

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public BigDecimal geteCashUpperLimit() {
        return eCashUpperLimit;
    }

    public void seteCashUpperLimit(BigDecimal eCashUpperLimit) {
        this.eCashUpperLimit = eCashUpperLimit;
    }

    public BigDecimal getSingleLimit() {
        return singleLimit;
    }

    public void setSingleLimit(BigDecimal singleLimit) {
        this.singleLimit = singleLimit;
    }

    public BigDecimal getTotalBalance() {
        return totalBalance;
    }

    public void setTotalBalance(BigDecimal totalBalance) {
        this.totalBalance = totalBalance;
    }

    public BigDecimal getSupplyBalance() {
        return supplyBalance;
    }

    public void setSupplyBalance(BigDecimal supplyBalance) {
        this.supplyBalance = supplyBalance;
    }

    public BigDecimal getMaxLoadingAmount() {
        return maxLoadingAmount;
    }

    public void setMaxLoadingAmount(BigDecimal maxLoadingAmount) {
        this.maxLoadingAmount = maxLoadingAmount;
    }
}
