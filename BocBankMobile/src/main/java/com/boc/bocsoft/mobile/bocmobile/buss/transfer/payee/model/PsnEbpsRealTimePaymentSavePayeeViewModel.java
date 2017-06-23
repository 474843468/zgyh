package com.boc.bocsoft.mobile.bocmobile.buss.transfer.payee.model;

/**
 * 实时付款保存收款人
 * Created by zhx on 2016/7/27
 */
public class PsnEbpsRealTimePaymentSavePayeeViewModel {
    /**
     * 收款人账号
     */
    private String payeeActno;
    /**
     * 收款人名称
     */
    private String payeeName;
    /**
     * 收款人账户所属银行
     */
    private String payeeBankName;
    /**
     * 收款人账户开户行
     */
    private String payeeOrgName;
    /**
     * 收款人账户开户行系统行号
     */
    private String payeeCnaps;
    /**
     * 收款人手机号
     */
    private String payeeMobile;

    public String getPayeeActno() {
        return payeeActno;
    }

    public void setPayeeActno(String payeeActno) {
        this.payeeActno = payeeActno;
    }

    public String getPayeeName() {
        return payeeName;
    }

    public void setPayeeName(String payeeName) {
        this.payeeName = payeeName;
    }

    public String getPayeeBankName() {
        return payeeBankName;
    }

    public void setPayeeBankName(String payeeBankName) {
        this.payeeBankName = payeeBankName;
    }

    public String getPayeeOrgName() {
        return payeeOrgName;
    }

    public void setPayeeOrgName(String payeeOrgName) {
        this.payeeOrgName = payeeOrgName;
    }

    public String getPayeeCnaps() {
        return payeeCnaps;
    }

    public void setPayeeCnaps(String payeeCnaps) {
        this.payeeCnaps = payeeCnaps;
    }

    public String getPayeeMobile() {
        return payeeMobile;
    }

    public void setPayeeMobile(String payeeMobile) {
        this.payeeMobile = payeeMobile;
    }
}