package com.boc.bocsoft.mobile.bii.bus.account.model.PsnAccountLossReportConfirm;

/**
 * 活期一本通预交易请求
 *
 * Created by liuweidong on 2016/6/7.
 */
public class PsnAccountLossReportConfirmParams {

    /**
     * 会话ID
     */
    private String conversationId;
    /**
     * 账号
     */
    private String accountNumber;
    /**
     * 安全因子组合ID
     */
    private String _combinId;

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
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
