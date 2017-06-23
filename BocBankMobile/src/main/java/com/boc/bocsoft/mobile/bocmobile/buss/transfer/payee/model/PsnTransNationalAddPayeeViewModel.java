package com.boc.bocsoft.mobile.bocmobile.buss.transfer.payee.model;

/**
 * Contact:国内跨行汇款：新增收款人
 * Created by zhx on 2016/7/22
 */
public class PsnTransNationalAddPayeeViewModel {
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
    /**
     * 账户开户行行号
     */
    private String cnapsCode;

    public String getPayeeName() {
        return payeeName;
    }

    public void setPayeeName(String payeeName) {
        this.payeeName = payeeName;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getToAccountId() {
        return toAccountId;
    }

    public void setToAccountId(String toAccountId) {
        this.toAccountId = toAccountId;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getPayeeMobile() {
        return payeeMobile;
    }

    public void setPayeeMobile(String payeeMobile) {
        this.payeeMobile = payeeMobile;
    }

    public String getToOrgName() {
        return toOrgName;
    }

    public void setToOrgName(String toOrgName) {
        this.toOrgName = toOrgName;
    }

    public String getCnapsCode() {
        return cnapsCode;
    }

    public void setCnapsCode(String cnapsCode) {
        this.cnapsCode = cnapsCode;
    }

}
