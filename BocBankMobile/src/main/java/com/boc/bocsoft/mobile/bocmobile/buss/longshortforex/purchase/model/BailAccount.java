package com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.purchase.model;

import java.math.BigDecimal;

/**
 * @author wangyang
 *         2016/12/26 15:43
 *         保证金账户
 */
public class BailAccount {

    // 保证金帐号
    private String marginAccountNo;
    // 结算货币(014美元 001人民币 038欧元 013港币 027日元 029澳元)
    private String currency;
    // 账户净值
    private BigDecimal marginNetBalance;
    // 账户余额
    private BigDecimal marginFund;
    // 暂计盈亏标志
    private String profitLossFlag;
    // 账户暂计盈亏
    private BigDecimal currentProfitLoss;
    // 开仓头寸
    private BigDecimal openPosition;
    // 已占用保证金
    private BigDecimal marginOccupied;
    // 可用保证金
    private BigDecimal marginAvailable;
    // 最大可交易额
    private BigDecimal maxTradeAmount;
    // 最大可提取额
    private BigDecimal maxDrawAmount;
    // 保证金充足率
    private BigDecimal marginRate;
    // 是否已经进入报警区
    private String alarmFlag;
    // 资金钞余额
    private BigDecimal cashBanlance;
    // 资金汇余额
    private BigDecimal remitBanlance;

    public String getMarginAccountNo() {
        return marginAccountNo;
    }

    public void setMarginAccountNo(String marginAccountNo) {
        this.marginAccountNo = marginAccountNo;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public BigDecimal getMarginNetBalance() {
        return marginNetBalance;
    }

    public void setMarginNetBalance(BigDecimal marginNetBalance) {
        this.marginNetBalance = marginNetBalance;
    }

    public BigDecimal getMarginFund() {
        return marginFund;
    }

    public void setMarginFund(BigDecimal marginFund) {
        this.marginFund = marginFund;
    }

    public String getProfitLossFlag() {
        return profitLossFlag;
    }

    public void setProfitLossFlag(String profitLossFlag) {
        this.profitLossFlag = profitLossFlag;
    }

    public BigDecimal getCurrentProfitLoss() {
        return currentProfitLoss;
    }

    public void setCurrentProfitLoss(BigDecimal currentProfitLoss) {
        this.currentProfitLoss = currentProfitLoss;
    }

    public BigDecimal getOpenPosition() {
        return openPosition;
    }

    public void setOpenPosition(BigDecimal openPosition) {
        this.openPosition = openPosition;
    }

    public BigDecimal getMarginOccupied() {
        return marginOccupied;
    }

    public void setMarginOccupied(BigDecimal marginOccupied) {
        this.marginOccupied = marginOccupied;
    }

    public BigDecimal getMarginAvailable() {
        return marginAvailable;
    }

    public void setMarginAvailable(BigDecimal marginAvailable) {
        this.marginAvailable = marginAvailable;
    }

    public BigDecimal getMaxTradeAmount() {
        return maxTradeAmount;
    }

    public void setMaxTradeAmount(BigDecimal maxTradeAmount) {
        this.maxTradeAmount = maxTradeAmount;
    }

    public BigDecimal getMaxDrawAmount() {
        return maxDrawAmount;
    }

    public void setMaxDrawAmount(BigDecimal maxDrawAmount) {
        this.maxDrawAmount = maxDrawAmount;
    }

    public BigDecimal getMarginRate() {
        return marginRate;
    }

    public void setMarginRate(BigDecimal marginRate) {
        this.marginRate = marginRate;
    }

    public String getAlarmFlag() {
        return alarmFlag;
    }

    public void setAlarmFlag(String alarmFlag) {
        this.alarmFlag = alarmFlag;
    }

    public BigDecimal getCashBanlance() {
        return cashBanlance;
    }

    public void setCashBanlance(BigDecimal cashBanlance) {
        this.cashBanlance = cashBanlance;
    }

    public BigDecimal getRemitBanlance() {
        return remitBanlance;
    }

    public void setRemitBanlance(BigDecimal remitBanlance) {
        this.remitBanlance = remitBanlance;
    }
}
