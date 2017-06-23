package com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnCrcdDividedPayAdvanceConfirm;

/**
 * Created by yangle on 2016/11/22.
 */
public class PsnCrcdDividedPayAdvanceConfirmParams {

    /**
     * _combinId : 8
     * accountId : 36165294
     * instalmentPlan : 1
     */
    // 账户标识
    private String accountId;
    // 分期计划
    private String instalmentPlan;
    // 安全因子组合id
    private String _combinId;
    /**
     * 分期付款标识
     *  EP01--消费分期
     * BI01--账单分期
     * XJ01--现金分期
     */
    private String instmtFlag;

    private String conversationId;

    public String get_combinId() {
        return _combinId;
    }

    public void set_combinId(String _combinId) {
        this._combinId = _combinId;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getInstalmentPlan() {
        return instalmentPlan;
    }

    public void setInstalmentPlan(String instalmentPlan) {
        this.instalmentPlan = instalmentPlan;
    }

    public String getInstmtFlag() {
        return instmtFlag;
    }

    public void setInstmtFlag(String instmtFlag) {
        this.instmtFlag = instmtFlag;
    }

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }
}
