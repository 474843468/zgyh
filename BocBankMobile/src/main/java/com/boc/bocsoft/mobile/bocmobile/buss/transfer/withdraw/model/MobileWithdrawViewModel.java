package com.boc.bocsoft.mobile.bocmobile.buss.transfer.withdraw.model;

import java.io.Serializable;

/**
 * Created by liuweidong on 2016/8/4.
 */
public class MobileWithdrawViewModel implements Serializable {
    /**
     * 取款金额
     */
    private double remitAmount;
    /**
     * 取款币种
     */
    private String currencyCode;
    /**
     * 附言
     */
    private String remark;
    /**
     * 汇款编号
     */
    private String remitNo;
    /**
     * 收款人姓名
     */
    private String payeeName;
    /**
     * 收款人手机号
     */
    private String payeeMobile;
    /**
     * 取款密码
     */
    private String withDrawPwd;
    private String withDrawPwd_RC;

    public double getRemitAmount() {
        return remitAmount;
    }

    public void setRemitAmount(double remitAmount) {
        this.remitAmount = remitAmount;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getRemitNo() {
        return remitNo;
    }

    public void setRemitNo(String remitNo) {
        this.remitNo = remitNo;
    }

    public String getPayeeName() {
        return payeeName;
    }

    public void setPayeeName(String payeeName) {
        this.payeeName = payeeName;
    }

    public String getPayeeMobile() {
        return payeeMobile;
    }

    public void setPayeeMobile(String payeeMobile) {
        this.payeeMobile = payeeMobile;
    }

    public String getWithDrawPwd() {
        return withDrawPwd;
    }

    public void setWithDrawPwd(String withDrawPwd) {
        this.withDrawPwd = withDrawPwd;
    }

    public String getWithDrawPwd_RC() {
        return withDrawPwd_RC;
    }

    public void setWithDrawPwd_RC(String withDrawPwd_RC) {
        this.withDrawPwd_RC = withDrawPwd_RC;
    }
}
