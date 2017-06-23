package com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnMobileWithdrawal;

/**
 * 汇款解付响应
 *
 * Created by liuweidong on 2016/7/12.
 */
public class PsnMobileWithdrawalResult {

    /**
     * 汇款编号
     */
    private String remitNo;
    /**
     * 收款人手机号
     */
    private String payeeMobile;
    /**
     * 收款人姓名
     */
    private String payeeName;
    /**
     * 取款币种
     */
    private String currencyCode;
    /**
     * 取款金额
     */
    private double remitAmount;
    /**
     * 附言
     */
    private String remark;

    public String getRemitNo() {
        return remitNo;
    }

    public void setRemitNo(String remitNo) {
        this.remitNo = remitNo;
    }

    public String getPayeeMobile() {
        return payeeMobile;
    }

    public void setPayeeMobile(String payeeMobile) {
        this.payeeMobile = payeeMobile;
    }

    public String getPayeeName() {
        return payeeName;
    }

    public void setPayeeName(String payeeName) {
        this.payeeName = payeeName;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public double getRemitAmount() {
        return remitAmount;
    }

    public void setRemitAmount(double remitAmount) {
        this.remitAmount = remitAmount;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
