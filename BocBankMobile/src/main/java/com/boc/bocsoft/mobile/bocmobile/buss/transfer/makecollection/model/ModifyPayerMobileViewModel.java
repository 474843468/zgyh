package com.boc.bocsoft.mobile.bocmobile.buss.transfer.makecollection.model;

/**
 * 修改付款人手机
 * Created by zhx on 2016/6/30
 */
public class ModifyPayerMobileViewModel {

    /**
     * payerId : 13426314777
     * payerMobile : 13426314777
     * token : 123456
     */

    /**
     * 付款人ID
     */
    private String payerId;
    /**
     * 付款人更改后手机号
     */
    private String payerMobile;
    /**
     * 防重机制，通过PSNGetTokenId接口获取
     */
    private String token;

    public String getPayerId() {
        return payerId;
    }

    public void setPayerId(String payerId) {
        this.payerId = payerId;
    }

    public String getPayerMobile() {
        return payerMobile;
    }

    public void setPayerMobile(String payerMobile) {
        this.payerMobile = payerMobile;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    //======================================//
    // 下面大致对应接口响应的字段
    //======================================//
    /**
     * 付款人客户号
     */
    private int payerCustomerId;
    /**
     * 付款人姓名
     */
    private String payerName;
    /**
     * 付款人类型
     */
    private String identifyType;

    public int getPayerCustomerId() {
        return payerCustomerId;
    }

    public void setPayerCustomerId(int payerCustomerId) {
        this.payerCustomerId = payerCustomerId;
    }

    public String getPayerName() {
        return payerName;
    }

    public void setPayerName(String payerName) {
        this.payerName = payerName;
    }

    public String getIdentifyType() {
        return identifyType;
    }

    public void setIdentifyType(String identifyType) {
        this.identifyType = identifyType;
    }
}
