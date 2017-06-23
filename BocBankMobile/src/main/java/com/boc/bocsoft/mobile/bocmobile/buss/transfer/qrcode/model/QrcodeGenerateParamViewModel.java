package com.boc.bocsoft.mobile.bocmobile.buss.transfer.qrcode.model;

/**
 * Created by xdy4486 on 2016/6/28.
 */
public class QrcodeGenerateParamViewModel {
    /**
     * 客户名称
     */
    private String custName;
    /**
     * 客户账号
     */
    private String custActNum;

    public String getCustName() {
        return custName;
    }

    public void setCustName(String custName) {
        this.custName = custName;
    }

    public String getCustActNum() {
        return custActNum;
    }

    public void setCustActNum(String custActNum) {
        this.custActNum = custActNum;
    }
}
