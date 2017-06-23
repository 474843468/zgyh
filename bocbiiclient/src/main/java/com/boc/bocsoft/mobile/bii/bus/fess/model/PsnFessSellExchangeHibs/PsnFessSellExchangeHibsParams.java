package com.boc.bocsoft.mobile.bii.bus.fess.model.PsnFessSellExchangeHibs;

/**
 * 4.17 017PsnFessSellExchangeHibs	结汇（HIBS新)
 * Created by gwluo on 2016/11/18.
 */

public class PsnFessSellExchangeHibsParams {
    private String accountId;//	账户ID	String 	M
    private String fundSource;//	资金来源	String	M		见附录“结汇资金属性代码”表,,不同证件类型及交易主体对应资金属性下拉选项取值不同
    private String currency;//	币种	String	M
    private String cashRemit;//	钞汇	String	M
    private String amount;//	结汇金额	String	M
    private String token;//	防止重复提交令牌	String	M
    private String conversationId;//会话id

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getFundSource() {
        return fundSource;
    }

    public void setFundSource(String fundSource) {
        this.fundSource = fundSource;
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
