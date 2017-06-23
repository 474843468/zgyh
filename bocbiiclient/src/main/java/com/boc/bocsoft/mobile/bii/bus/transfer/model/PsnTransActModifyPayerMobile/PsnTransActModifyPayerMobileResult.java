package com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransActModifyPayerMobile;

/**
 * 修改付款人手机
 * Created by zhx on 2016/6/29.
 */
public class PsnTransActModifyPayerMobileResult {
    /**
     * 付款人姓名
     */
    private String payerName;
    /**
     * 付款人客户号
     */
    private int payerCustomerId;
    /**
     * 付款人手机号
     */
    private String payerMobile;
    /**
     * 付款人类型
     */
    private String identifyType;

    public String getPayerName() {
        return payerName;
    }

    public void setPayerName(String payerName) {
        this.payerName = payerName;
    }

    public int getPayerCustomerId() {
        return payerCustomerId;
    }

    public void setPayerCustomerId(int payerCustomerId) {
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
}