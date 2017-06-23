package com.boc.bocsoft.mobile.bocmobile.buss.transfer.makecollection.model;

/**
 * 删除付款人
 * Created by zhx on 2016/7/5
 */
public class DeletePayerViewModel {
    /**
     * 付款人ID
     */
    private String payerId;
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

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    /////////////////////////////////////////////////////////
    // 响应
    ////////////////////////////////////////////////////////

}
