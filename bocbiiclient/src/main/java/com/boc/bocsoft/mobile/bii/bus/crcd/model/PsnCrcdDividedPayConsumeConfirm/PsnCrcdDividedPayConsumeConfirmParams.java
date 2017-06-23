package com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdDividedPayConsumeConfirm;

import java.math.BigDecimal;

/**
 * Created by lq7090 on 2016/11/17.
 */
public class PsnCrcdDividedPayConsumeConfirmParams {

    /**
     * accountId : 37905296
     * currencyCode : 001
     * amount : 100
     * divPeriod : 3
     * chargeMode : 0
     * transId : 2
     * mainAcctId : 001A03F700491C03
     * sequence : 2
     * crcdFinalFour : 1234
     * _combinId : 40
     */

    private int accountId;
    private String currencyCode;
    private BigDecimal amount;
    private int divPeriod;
    private String chargeMode;
    private int transId;
    private String mainAcctId;
    private int sequence;
    private String crcdFinalFour;
    private String _combinId;
    private String conversationId;

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

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

    public int getTransId() {
        return transId;
    }

    public void setTransId(int transId) {
        this.transId = transId;
    }

    public String getMainAcctId() {
        return mainAcctId;
    }

    public void setMainAcctId(String mainAcctId) {
        this.mainAcctId = mainAcctId;
    }

    public int getSequence() {
        return sequence;
    }

    public void setSequence(int sequence) {
        this.sequence = sequence;
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
}
