package com.boc.bocsoft.mobile.bii.bus.account.model.PsnDebitCardQryQuota;

import java.math.BigDecimal;

/**
 * @author wangyang
 *         2016/10/10 20:33
 *         查询借记卡已设置限额Result
 */
public class PsnDebitCardQryQuotaResult {

    /** 币种 */
    private String currency;
    /** 每日转账交易限额 */
    private BigDecimal transDay;
    /** 每日POS交易限额 */
    private BigDecimal allDayPOS;
    /** 境内ATM取现日限额 */
    private BigDecimal cashDayATM;
    /** 境外POS消费限额 */
    private BigDecimal consumeForeignPOS;
    /** 境外ATM取款日限额 */
    private BigDecimal cashDayForeignATM;

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public BigDecimal getTransDay() {
        return transDay;
    }

    public void setTransDay(BigDecimal transDay) {
        this.transDay = transDay;
    }

    public BigDecimal getAllDayPOS() {
        return allDayPOS;
    }

    public void setAllDayPOS(BigDecimal allDayPOS) {
        this.allDayPOS = allDayPOS;
    }

    public BigDecimal getCashDayATM() {
        return cashDayATM;
    }

    public void setCashDayATM(BigDecimal cashDayATM) {
        this.cashDayATM = cashDayATM;
    }

    public BigDecimal getConsumeForeignPOS() {
        return consumeForeignPOS;
    }

    public void setConsumeForeignPOS(BigDecimal consumeForeignPOS) {
        this.consumeForeignPOS = consumeForeignPOS;
    }

    public BigDecimal getCashDayForeignATM() {
        return cashDayForeignATM;
    }

    public void setCashDayForeignATM(BigDecimal cashDayForeignATM) {
        this.cashDayForeignATM = cashDayForeignATM;
    }
}
