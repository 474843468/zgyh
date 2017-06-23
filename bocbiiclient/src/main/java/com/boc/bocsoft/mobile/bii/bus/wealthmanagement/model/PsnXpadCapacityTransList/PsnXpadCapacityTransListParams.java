package com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadCapacityTransList;

/**
 * 客户投资协议交易明细查询
 * Created by guokai on 2016/9/6.
 */
public class PsnXpadCapacityTransListParams {

    /**
     * 协议类型
     * agrType : 5
     * 客户协议编号
     * custAgrCode : 4000000001
     */

    private String agrType;
    private String custAgrCode;

    public String getAgrType() {
        return agrType;
    }

    public void setAgrType(String agrType) {
        this.agrType = agrType;
    }

    public String getCustAgrCode() {
        return custAgrCode;
    }

    public void setCustAgrCode(String custAgrCode) {
        this.custAgrCode = custAgrCode;
    }
}
