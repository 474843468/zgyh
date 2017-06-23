package com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdDividedPayBillSetConfirm;

import java.math.BigDecimal;

/**
 * Created by lq7090 on 2016/11/17.
 * 账单分期预交易
 */
public class PsnCrcdDividedPayBillSetConfirmParams {

    /**
     * accountId : 37905296
     * currencyCode : 001
     * amount : 11111
     * divPeriod : 6
     * chargeMode : 0
     * lowAmt : 100
     * crcdFinalFour : 1234
     * _combinId : 40
     */

    private int accountId;
    private String currencyCode;
    private BigDecimal amount;
    private int divPeriod;
    private String chargeMode;
    private BigDecimal lowAmt;
    private String crcdFinalFour;
    private String _combinId;
    private String conversationId;

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public int getDivPeriod() {
        return divPeriod;
    }

    public void setDivPeriod(int divPeriod) {
        this.divPeriod = divPeriod;
    }

    public String getChargeMode() {
        return chargeMode;
    }

    public void setChargeMode(String chargeMode) {
        this.chargeMode = chargeMode;
    }

    public BigDecimal getLowAmt() {
        return lowAmt;
    }

    public void setLowAmt(BigDecimal lowAmt) {
        this.lowAmt = lowAmt;
    }

    public String getCrcdFinalFour() {
        return crcdFinalFour;
    }

    public void setCrcdFinalFour(String crcdFinalFour) {
        this.crcdFinalFour = crcdFinalFour;
    }

    public String get_combinId() {
        return _combinId;
    }

    public void set_combinId(String _combinId) {
        this._combinId = _combinId;
    }

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }
}
