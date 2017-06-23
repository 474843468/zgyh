package com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdQueryForeignPayOff;

import com.boc.bocsoft.mobile.bii.bus.global.model.PsnCommonQueryAllChinaBankAccount.PsnCommonQueryAllChinaBankAccountResult;

import java.math.BigDecimal;

/**
 * 作者：xwg on 16/11/21 18:20
 * 信用卡查询购汇还款信息
 */
public class PsnCrcdQueryForeignPayOffResult {
    /**
     *  转入账户信息
     */
    private PsnCommonQueryAllChinaBankAccountResult accountInfo;
    /**
     *  汇率
     */
    private BigDecimal exchangeRate;
    /**
     *  最小还款金额（人民币元）
     */
    private BigDecimal minPayAmt;
    /**
     *  未还款金额（人民币元）	全额还款金额使用该字段
     */
    private BigDecimal payedAmt;
    /**
     *  到期还款日	格式：yyyy/MM/dd
     */
    private String dueDate;
    /**
     *上期结欠金额（人民币元）		该余额为非负数，由oweFlag 该字段判断是存款还是欠款或者余额0。
     */
    private BigDecimal priorOweAmt;
    /**
     *  当前账户余额（人民币元）该余额为非负数，由balanceFlag该字段判断是存款还是欠款或者余额0。
     */
    private BigDecimal currentBalance;
    /**
     *最小还款金额（账户币种）
     */
    private BigDecimal minPayAmtFore;
    /**
     *未还款金额（账户币种）
     */
    private BigDecimal payedAmtFore;
    /**
     *上期结欠金额（账户币种）		该余额为非负数，由oweFlagFore该字段判断是存款还是欠款或者余额0。
     */
    private BigDecimal priorOweAmtFore;
    /**
     *  当前账户余额（账户币种）		该余额为非负数，由balanceFlagFore该字段判断是存款还是欠款或者余额0。
     */
    private BigDecimal currentBalanceFore;
    /**
     *上期结欠金额标识位（人民币元）		“0”-透支
     “1”-结余
     “2”-余额0
     */
    private String oweFlag;
    /**
     *当前账户余额标识位（人民币元）		“0”-透支
     “1”-结余
     “2”-余额0
     */
    private String balanceFlag;
    /**
     *上期结欠金额标识位（账户币种）
     */
    private String oweFlagFore;
    /**
     *当前账户余额标识位（账户币种）
     */
    private String balanceFlagFore;


    public PsnCommonQueryAllChinaBankAccountResult getAccountInfo() {
        return accountInfo;
    }

    public void setAccountInfo(PsnCommonQueryAllChinaBankAccountResult accountInfo) {
        this.accountInfo = accountInfo;
    }

    public String getBalanceFlag() {
        return balanceFlag;
    }

    public void setBalanceFlag(String balanceFlag) {
        this.balanceFlag = balanceFlag;
    }

    public String getBalanceFlagFore() {
        return balanceFlagFore;
    }

    public void setBalanceFlagFore(String balanceFlagFore) {
        this.balanceFlagFore = balanceFlagFore;
    }

    public BigDecimal getCurrentBalance() {
        return currentBalance;
    }

    public void setCurrentBalance(BigDecimal currentBalance) {
        this.currentBalance = currentBalance;
    }

    public BigDecimal getCurrentBalanceFore() {
        return currentBalanceFore;
    }

    public void setCurrentBalanceFore(BigDecimal currentBalanceFore) {
        this.currentBalanceFore = currentBalanceFore;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public BigDecimal getExchangeRate() {
        return exchangeRate;
    }

    public void setExchangeRate(BigDecimal exchangeRate) {
        this.exchangeRate = exchangeRate;
    }

    public BigDecimal getMinPayAmt() {
        return minPayAmt;
    }

    public void setMinPayAmt(BigDecimal minPayAmt) {
        this.minPayAmt = minPayAmt;
    }

    public BigDecimal getMinPayAmtFore() {
        return minPayAmtFore;
    }

    public void setMinPayAmtFore(BigDecimal minPayAmtFore) {
        this.minPayAmtFore = minPayAmtFore;
    }

    public String getOweFlag() {
        return oweFlag;
    }

    public void setOweFlag(String oweFlag) {
        this.oweFlag = oweFlag;
    }

    public String getOweFlagFore() {
        return oweFlagFore;
    }

    public void setOweFlagFore(String oweFlagFore) {
        this.oweFlagFore = oweFlagFore;
    }

    public BigDecimal getPayedAmt() {
        return payedAmt;
    }

    public void setPayedAmt(BigDecimal payedAmt) {
        this.payedAmt = payedAmt;
    }

    public BigDecimal getPayedAmtFore() {
        return payedAmtFore;
    }

    public void setPayedAmtFore(BigDecimal payedAmtFore) {
        this.payedAmtFore = payedAmtFore;
    }

    public BigDecimal getPriorOweAmt() {
        return priorOweAmt;
    }

    public void setPriorOweAmt(BigDecimal priorOweAmt) {
        this.priorOweAmt = priorOweAmt;
    }

    public BigDecimal getPriorOweAmtFore() {
        return priorOweAmtFore;
    }

    public void setPriorOweAmtFore(BigDecimal priorOweAmtFore) {
        this.priorOweAmtFore = priorOweAmtFore;
    }


}
