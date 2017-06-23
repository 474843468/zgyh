package com.boc.bocsoft.mobile.bii.bus.account.model.PsnCrcdReportLossConfirm;

/**
 * 信用卡挂失及补卡确认请求
 *
 * Created by liuweidong on 2016/6/13.
 */
public class PsnCrcdReportLossConfirmParams {

    /**
     * 会话ID
     */
    private String conversationId;
    /**
     * 账户ID
     */
    private int accountId;
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

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public String get_combinId() {
        return _combinId;
    }

    public void set_combinId(String _combinId) {
        this._combinId = _combinId;
    }
}
