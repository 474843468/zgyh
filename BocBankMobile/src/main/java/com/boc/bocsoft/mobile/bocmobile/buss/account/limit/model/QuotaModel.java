package com.boc.bocsoft.mobile.bocmobile.buss.account.limit.model;

import java.math.BigDecimal;

/**
 * @author wangyang
 *         2016/10/14 10:50
 *         限额Model
 */
public class QuotaModel {

    public static final BigDecimal LIMIT_MAX = new BigDecimal(5000000);

    /**
     * 账户标识
     */
    private String accountId;
    /**
     * 账户Number
     */
    private String accountNumber;
    /**
     * 币种
     */
    private String currency;
    /**
     * 每日转账交易限额
     */
    private BigDecimal transDay;
    /**
     * 每日POS交易限额
     */
    private BigDecimal allDayPOS;
    /**
     * 境内ATM取现日限额
     */
    private BigDecimal cashDayATM;
    /**
     * 境外POS消费限额
     */
    private BigDecimal consumeForeignPOS;
    /**
     * 境外ATM取款日限额
     */
    private BigDecimal cashDayForeignATM;
    /**
     * 是否修改ATM转账限额
     */
    private boolean isUpdateAtm;
    /**
     * 是否修改POS消费日限额
     */
    private boolean isUpdatePos;
    /**
     * 是否修改ATM消费日取款限额
     */
    private boolean isUpdateAtmCash;
    /**
     * 是否修改境外POS消费日限额
     */
    private boolean isUpdateBorderPos;
    /**
     * 是否修改境外ATM取款日限额
     */
    private boolean isUpdateBorderAtm;

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

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

    public boolean isUpdateAtm() {
        return isUpdateAtm;
    }

    public void setUpdateAtm(boolean updateAtm) {
        isUpdateAtm = updateAtm;
    }

    public boolean isUpdatePos() {
        return isUpdatePos;
    }

    public void setUpdatePos(boolean updatePos) {
        isUpdatePos = updatePos;
    }

    public boolean isUpdateAtmCash() {
        return isUpdateAtmCash;
    }

    public void setUpdateAtmCash(boolean updateAtmCash) {
        isUpdateAtmCash = updateAtmCash;
    }

    public boolean isUpdateBorderPos() {
        return isUpdateBorderPos;
    }

    public void setUpdateBorderPos(boolean updateBorderPos) {
        isUpdateBorderPos = updateBorderPos;
    }

    public boolean isUpdateBorderAtm() {
        return isUpdateBorderAtm;
    }

    public void setUpdateBorderAtm(boolean updateBorderAtm) {
        isUpdateBorderAtm = updateBorderAtm;
    }
}
