package com.boc.bocsoft.mobile.bii.bus.loan.model.PsnCreditContractQuery;

/**
 * 征信授权协议接口返回参数
 * Created by xintong on 2016/6/13.
 */
public class PsnCreditContractQueryResult {
    //协议ID
    private String contractNo;
    //征信合同内容
    private String creditContrac;

    public String getContractNo() {
        return contractNo;
    }

    public void setContractNo(String contractNo) {
        this.contractNo = contractNo;
    }

    public String getCreditContrac() {
        return creditContrac;
    }

    public void setCreditContrac(String creditContrac) {
        this.creditContrac = creditContrac;
    }
}
