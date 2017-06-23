package com.boc.bocsoft.mobile.bii.bus.account.model.PsnSsmAccountChangePre;

/**
 * Created by wangtong on 2016/8/11.
 */
public class PsnSsmAccountChangePreParam {

    private String conversationId;
    private String accountId;
    private String accountNumber;
    private String _combinId;

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

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String get_combinId() {
        return _combinId;
    }

    public void set_combinId(String _combinId) {
        this._combinId = _combinId;
    }
}
