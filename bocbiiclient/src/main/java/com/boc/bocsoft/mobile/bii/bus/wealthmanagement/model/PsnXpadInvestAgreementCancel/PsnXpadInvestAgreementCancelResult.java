package com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadInvestAgreementCancel;

/**
 * 投资协议终止
 * Created by guokai on 2016/9/6.
 */
public class PsnXpadInvestAgreementCancelResult {
    /**
     * transactionId : 1556066024
     * accNo : 4563***********5670
     * agrCode : 1012485
     * agrName : 余额理财投资
     * custAgrCode : 1012485
     * trsDate : 2019/03/21
     * execDate : 2019/03/23
     * extFiled : null
     */

    private String transactionId;
    private String accNo;
    private String agrCode;
    private String agrName;
    private String custAgrCode;
    private String trsDate;
    private String execDate;
    private Object extFiled;

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getAccNo() {
        return accNo;
    }

    public void setAccNo(String accNo) {
        this.accNo = accNo;
    }

    public String getAgrCode() {
        return agrCode;
    }

    public void setAgrCode(String agrCode) {
        this.agrCode = agrCode;
    }

    public String getAgrName() {
        return agrName;
    }

    public void setAgrName(String agrName) {
        this.agrName = agrName;
    }

    public String getCustAgrCode() {
        return custAgrCode;
    }

    public void setCustAgrCode(String custAgrCode) {
        this.custAgrCode = custAgrCode;
    }

    public String getTrsDate() {
        return trsDate;
    }

    public void setTrsDate(String trsDate) {
        this.trsDate = trsDate;
    }

    public String getExecDate() {
        return execDate;
    }

    public void setExecDate(String execDate) {
        this.execDate = execDate;
    }

    public Object getExtFiled() {
        return extFiled;
    }

    public void setExtFiled(Object extFiled) {
        this.extFiled = extFiled;
    }

}
