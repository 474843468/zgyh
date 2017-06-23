package com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnEbpsRealTimePaymentSavePayee;

/**
 * 实时付款保存收款人
 * Created by zhx on 2016/7/27
 */
public class PsnEbpsRealTimePaymentSavePayeeParams {
    /**
     * 收款人账号
     */
    private String payeeActno;
    /**
     * 收款人名称
     */
    private String payeeName;
    /**
     * 收款人账户所属银行
     */
    private String payeeBankName;
    /**
     * 收款人账户开户行
     */
    private String payeeOrgName;
    /**
     * 收款人账户开户行系统行号
     */
    private String payeeCnaps;
    /**
     * 收款人手机号
     */
    private String payeeMobile;
    /**
     * 防重提交，通过PSNGetTokenId接口获取
     */
    private String token;

    private String conversationId;

    public void setPayeeName(String payeeName) {
        this.payeeName = payeeName;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setPayeeCnaps(String payeeCnaps) {
        this.payeeCnaps = payeeCnaps;
    }

    public void setPayeeBankName(String payeeBankName) {
        this.payeeBankName = payeeBankName;
    }

    public void setPayeeActno(String payeeActno) {
        this.payeeActno = payeeActno;
    }

    public void setPayeeOrgName(String payeeOrgName) {
        this.payeeOrgName = payeeOrgName;
    }

    public void setPayeeMobile(String payeeMobile) {
        this.payeeMobile = payeeMobile;
    }

    public String getPayeeName() {
        return payeeName;
    }

    public String getToken() {
        return token;
    }

    public String getPayeeCnaps() {
        return payeeCnaps;
    }

    public String getPayeeBankName() {
        return payeeBankName;
    }

    public String getPayeeActno() {
        return payeeActno;
    }

    public String getPayeeOrgName() {
        return payeeOrgName;
    }

    public String getPayeeMobile() {
        return payeeMobile;
    }

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }
}