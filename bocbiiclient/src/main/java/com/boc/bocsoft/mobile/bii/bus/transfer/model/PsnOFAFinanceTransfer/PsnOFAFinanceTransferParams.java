package com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnOFAFinanceTransfer;

/**
 * Created by WYme on 2016/11/1.
 */
public class PsnOFAFinanceTransferParams {

    /**
     * financialAccountId : 40352294
     * bankAccountId : 39123385
     * currency : 001
     * cashRemit : 01
     * amount : 100
     * token : 52a1901y
     */

    private String financialAccountId;
    private String bankAccountId;
    private String currency;
    private String cashRemit;
    private String amount;
    private String token;


    public String getConversationId() {
        return conversationId;
    }
    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }
    private String conversationId;
    public String getFinancialAccountId() {
        return financialAccountId;
    }
    public void setFinancialAccountId(String financialAccountId) {
        this.financialAccountId = financialAccountId;
    }

    public String getBankAccountId() {
        return bankAccountId;
    }

    public void setBankAccountId(String bankAccountId) {
        this.bankAccountId = bankAccountId;
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

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
