package com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransNationalAddPayee;

/**
 * 国内跨行汇款：新增收款人
 * Created by zhx on 2016/7/19
 */
public class PsnTransNationalAddPayeeParams {

    /**
     * payeeName : jack
     * token : v8w2gstn
     * toAccountId : 100318108
     * cnapsCode : 40740
     * bankName : 中国农业银行
     * payeeMobile : 12345678989
     * toOrgName : 中国农业银行北京分行
     */
    /**
     * 收款人姓名
     */
    private String payeeName;
    /**
     * 防重机制，通过PSNGetTokenId接口获取
     */
    private String token;
    /**
     * 收款人姓名
     */
    private String toAccountId;
    /**
     * 账户开户行行号
     */
    private String cnapsCode;
    /**
     * 账户所属银行名称
     */
    private String bankName;
    /**
     * 收款人姓名
     */
    private String payeeMobile;
    /**
     * 账户开户行名称
     */
    private String toOrgName;

    private String conversationId;

    public void setPayeeName(String payeeName) {
        this.payeeName = payeeName;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setToAccountId(String toAccountId) {
        this.toAccountId = toAccountId;
    }

    public void setCnapsCode(String cnapsCode) {
        this.cnapsCode = cnapsCode;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public void setPayeeMobile(String payeeMobile) {
        this.payeeMobile = payeeMobile;
    }

    public void setToOrgName(String toOrgName) {
        this.toOrgName = toOrgName;
    }

    public String getPayeeName() {
        return payeeName;
    }

    public String getToken() {
        return token;
    }

    public String getToAccountId() {
        return toAccountId;
    }

    public String getCnapsCode() {
        return cnapsCode;
    }

    public String getBankName() {
        return bankName;
    }

    public String getPayeeMobile() {
        return payeeMobile;
    }

    public String getToOrgName() {
        return toOrgName;
    }

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }
}
