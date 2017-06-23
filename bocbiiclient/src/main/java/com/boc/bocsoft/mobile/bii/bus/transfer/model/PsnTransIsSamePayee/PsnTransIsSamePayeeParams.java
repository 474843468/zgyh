package com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransIsSamePayee;

/**
 * Params:判断是否存在相同收款人
 * Created by zhx on 2016/7/19
 */
public class PsnTransIsSamePayeeParams {
    /**
     * 收款人账号
     */
    private String payeeActno;
    /**
     * Cnaps号
     */
    private String cnapsCode;
    /**
     * 收款账号标识
     */
    private String bocFlag;

    public String getPayeeActno() {
        return payeeActno;
    }

    public void setPayeeActno(String payeeActno) {
        this.payeeActno = payeeActno;
    }

    public String getBocFlag() {
        return bocFlag;
    }

    public void setBocFlag(String bocFlag) {
        this.bocFlag = bocFlag;
    }

    public String getCnapsCode() {
        return cnapsCode;
    }

    public void setCnapsCode(String cnapsCode) {
        this.cnapsCode = cnapsCode;
    }
}
