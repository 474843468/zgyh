package com.boc.bocsoft.mobile.bii.bus.account.model.PsnCrcdVirtualCardSettledbillQuery;

import org.threeten.bp.LocalDate;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author wangyang
 *         16/7/26 17:59
 *         虚拟银行卡已出账单查询
 */
public class PsnCrcdVirtualCardSettledbillQueryResult {

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
    /** 信用卡已出账单交易信息 */
    private List<CrcdTransaction> crcdTransInfo;

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

    public List<CrcdTransaction> getCrcdTransInfo() {
        return crcdTransInfo;
    }

    public void setCrcdTransInfo(List<CrcdTransaction> crcdTransInfo) {
        this.crcdTransInfo = crcdTransInfo;
    }
}
