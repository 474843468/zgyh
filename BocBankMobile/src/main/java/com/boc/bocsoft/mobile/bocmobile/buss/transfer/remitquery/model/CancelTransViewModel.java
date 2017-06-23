package com.boc.bocsoft.mobile.bocmobile.buss.transfer.remitquery.model;

/**
 * Created by wangf on 2016/7/13.
 */
public class CancelTransViewModel {

    /**
     * 撤销交易上送数据
     */

    //汇款编号
    private String remitNo;
    private String token;

    public String getRemitNo() {
        return remitNo;
    }

    public void setRemitNo(String remitNo) {
        this.remitNo = remitNo;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

}
