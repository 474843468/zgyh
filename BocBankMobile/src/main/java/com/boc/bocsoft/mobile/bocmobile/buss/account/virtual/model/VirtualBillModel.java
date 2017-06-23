package com.boc.bocsoft.mobile.bocmobile.buss.account.virtual.model;

import org.threeten.bp.LocalDate;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author wangyang
 *         16/9/3 19:37
 *         虚拟银行卡账单
 */
public class VirtualBillModel {

    /** 存入总计 */
    private BigDecimal totalIn;
    /** 支出总计 */
    private BigDecimal totalOut;
    /** 币种 -- 001=人民币元,014=美元,027=日元,038=欧元 */
    private String currency;
    /** 到期还款日 */
    private LocalDate dueDate;
    /** 本期账面余额 */
    private BigDecimal currentBalance;
    /** 账单日期 */
    private LocalDate billDate;
    /** 上期账面余额 */
    private BigDecimal lastBalance;
    /** 最小还款额 */
    private BigDecimal minPaymentAmount;
    /** 交易明细列表 */
    private List<VirtualBillTransModel> transModels;

    public BigDecimal getTotalIn() {
        return totalIn;
    }

    public void setTotalIn(BigDecimal totalIn) {
        this.totalIn = totalIn;
    }

    public BigDecimal getTotalOut() {
        return totalOut;
    }

    public void setTotalOut(BigDecimal totalOut) {
        this.totalOut = totalOut;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public BigDecimal getCurrentBalance() {
        return currentBalance;
    }

    public void setCurrentBalance(BigDecimal currentBalance) {
        this.currentBalance = currentBalance;
    }

    public LocalDate getBillDate() {
        return billDate;
    }

    public void setBillDate(LocalDate billDate) {
        this.billDate = billDate;
    }

    public BigDecimal getLastBalance() {
        return lastBalance;
    }

    public void setLastBalance(BigDecimal lastBalance) {
        this.lastBalance = lastBalance;
    }

    public BigDecimal getMinPaymentAmount() {
        return minPaymentAmount;
    }

    public void setMinPaymentAmount(BigDecimal minPaymentAmount) {
        this.minPaymentAmount = minPaymentAmount;
    }

    public List<VirtualBillTransModel> getTransModels() {
        return transModels;
    }

    public void setTransModels(List<VirtualBillTransModel> transModels) {
        this.transModels = transModels;
    }

    @Override
    public String toString() {
        return "VirtualBillModel{" +
                "totalIn=" + totalIn +
                ", totalOut=" + totalOut +
                ", currency='" + currency + '\'' +
                ", dueDate=" + dueDate +
                ", currentBalance=" + currentBalance +
                ", billDate=" + billDate +
                ", lastBalance=" + lastBalance +
                ", minPaymentAmount=" + minPaymentAmount +
                ", transModels=" + transModels +
                '}';
    }
}
