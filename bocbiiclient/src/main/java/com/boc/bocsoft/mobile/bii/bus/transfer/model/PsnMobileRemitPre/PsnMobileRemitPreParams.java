package com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnMobileRemitPre;

/**
 * 汇往取款人预交易请求
 *
 * Created by liuweidong on 2016/7/12.
 */
public class PsnMobileRemitPreParams {

    /**
     * 会话ID
     */
    private String conversationId;

    /**
     * 汇出账户标识
     */
    private String accountId;
    /**
     * 收款人手机号
     */
    private String payeeMobile;
    /**
     * 收款人姓名
     */
    private String payeeName;
    /**
     * 汇款币种
     */
    private String remitCurrencyCode;
    /**
     * 汇款金额
     */
    private String remitAmount;
    /**
     * 附言
     */
    private String remark;
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

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getPayeeMobile() {
        return payeeMobile;
    }

    public void setPayeeMobile(String payeeMobile) {
        this.payeeMobile = payeeMobile;
    }

    public String getPayeeName() {
        return payeeName;
    }

    public void setPayeeName(String payeeName) {
        this.payeeName = payeeName;
    }

    public String getRemitCurrencyCode() {
        return remitCurrencyCode;
    }

    public void setRemitCurrencyCode(String remitCurrencyCode) {
        this.remitCurrencyCode = remitCurrencyCode;
    }

    public String getRemitAmount() {
        return remitAmount;
    }

    public void setRemitAmount(String remitAmount) {
        this.remitAmount = remitAmount;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String get_combinId() {
        return _combinId;
    }

    public void set_combinId(String _combinId) {
        this._combinId = _combinId;
    }
}
