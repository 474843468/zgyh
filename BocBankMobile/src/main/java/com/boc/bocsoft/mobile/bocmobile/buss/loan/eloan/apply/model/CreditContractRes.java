package com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.apply.model;

/**
 * 查询征信授权协议模板,接口返回参数实体类
 * Created by xintong on 2016/6/13.
 */
public class CreditContractRes {
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
