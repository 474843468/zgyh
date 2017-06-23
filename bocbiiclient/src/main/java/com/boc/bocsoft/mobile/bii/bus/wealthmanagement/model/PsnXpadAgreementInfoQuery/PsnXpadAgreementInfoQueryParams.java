package com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadAgreementInfoQuery;

/**
 * 客户投资协议详情
 * Created by guokai on 2016/9/6.
 */
public class PsnXpadAgreementInfoQueryParams {
    /**
     * 客户协议编号
     */
    private String custAgrCode;
    /**
     * 协议类型
     * 1：智能投资
     * 2：定时定额投资
     * 3：周期滚续投资
     * 4：余额理财投资
     * 5：业绩基准周期滚续
     */
    private String agrType;

    public String getCustAgrCode() {
        return custAgrCode;
    }

    public void setCustAgrCode(String custAgrCode) {
        this.custAgrCode = custAgrCode;
    }

    public String getAgrType() {
        return agrType;
    }

    public void setAgrType(String agrType) {
        this.agrType = agrType;
    }
}
