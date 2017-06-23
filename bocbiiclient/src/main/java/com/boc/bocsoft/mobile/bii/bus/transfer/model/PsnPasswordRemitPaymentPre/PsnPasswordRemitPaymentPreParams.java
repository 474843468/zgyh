package com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnPasswordRemitPaymentPre;

/**
 * ATM无卡取款预交易请求
 *
 * Created by liuweidong on 2016/7/19.
 */
public class PsnPasswordRemitPaymentPreParams {
    /**
     * 会话ID
     */
    private String conversationId;
    /**
     * 汇款类型：1：ATM无卡取现；0：密码汇款
     */
    private String freeRemitType;
    /**
     * 账户ID
     */
    private String fromAccountId;
    /**
     * 收款人手机号
     */
    private String payeeMobile;
    /**
     * 收款人姓名
     */
    private String payeeName;
    /**
     * 转账金额
     */
    private String amount;
    /**
     * 币种
     */
    private String currencyCode;
    private String furInf;
    /**
     * 安全因子
     */
    private String _combinId;

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    public String getFreeRemitType() {
        return freeRemitType;
    }

    public void setFreeRemitType(String freeRemitType) {
        this.freeRemitType = freeRemitType;
    }

    public String getFromAccountId() {
        return fromAccountId;
    }

    public void setFromAccountId(String fromAccountId) {
        this.fromAccountId = fromAccountId;
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

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public String getFurInf() {
        return furInf;
    }

    public void setFurInf(String furInf) {
        this.furInf = furInf;
    }

    public String get_combinId() {
        return _combinId;
    }

    public void set_combinId(String _combinId) {
        this._combinId = _combinId;
    }
}
