package com.boc.bocsoft.mobile.bii.bus.account.model.PsnDebitcardLossReportConfirm;

/**
 * 借记卡预交易请求
 *
 * Created by liuweidong on 2016/6/6.
 */
public class PsnDebitcardLossReportConfirmParams {

    /**
     * 会话ID
     */
    private String conversationId;
    /**
     * 借记卡账号
     */
    private String accountNumber;
    /**
     * 挂失期限 1：5天；2：长期
     */
    private String lossDays;
    /**
     * 是否同时冻结借记卡主账户 Y：是；N：否
     */
    private String accFlozenFlag;
    /**
     * 安全因子ID
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

    public String getLossDays() {
        return lossDays;
    }

    public void setLossDays(String lossDays) {
        this.lossDays = lossDays;
    }

    public String getAccFlozenFlag() {
        return accFlozenFlag;
    }

    public void setAccFlozenFlag(String accFlozenFlag) {
        this.accFlozenFlag = accFlozenFlag;
    }

    public String get_combinId() {
        return _combinId;
    }

    public void set_combinId(String _combinId) {
        this._combinId = _combinId;
    }
}
