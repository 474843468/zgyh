package com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransActQueryPayerList;

/**
 * 查询付款人列表
 * Created by zhx on 2016/6/29.
 */
public class PsnTransActQueryPayerListResult {
    /**
     * 付款人姓名
     */
    private String payerName;
    /**
     * 付款人客户号
     */
    private String payerCustomerId;
    /**
     * 付款人手机
     */
    private String payerMobile;
    /**
     * 付款人类型
     */
    private String identifyType;
    /**
     * 付款人ID
     */
    private String payerId;

    public String getPayerName() {
        return payerName;
    }

    public void setPayerName(String payerName) {
        this.payerName = payerName;
    }

    public String getPayerCustomerId() {
        return payerCustomerId;
    }

    public void setPayerCustomerId(String payerCustomerId) {
        this.payerCustomerId = payerCustomerId;
    }

    public String getPayerMobile() {
        return payerMobile;
    }

    public void setPayerMobile(String payerMobile) {
        this.payerMobile = payerMobile;
    }

    public String getIdentifyType() {
        return identifyType;
    }

    public void setIdentifyType(String identifyType) {
        this.identifyType = identifyType;
    }

    public String getPayerId() {
        return payerId;
    }

    public void setPayerId(String payerId) {
        this.payerId = payerId;
    }
}
