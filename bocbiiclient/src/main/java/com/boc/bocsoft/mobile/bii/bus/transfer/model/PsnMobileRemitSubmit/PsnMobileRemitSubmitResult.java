package com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnMobileRemitSubmit;

/**
 * Created by liuweidong on 2016/7/12.
 */
public class PsnMobileRemitSubmitResult {

    /**
     * 汇出账户
     */
    private String fromActNumber;
    /**
     * 别名
     */
    private String nickName;
    /**
     * 汇款人姓名
     */
    private String fromAcctName;
    /**
     * 收款人手机号
     */
    private String payeeMobile;
    /**
     * 收款人姓名
     */
    private String payeeName;
    /**
     * 汇款币种
     */
    private String currencyCode;
    /**
     * 汇款金额
     */
    private int remitAmount;
    /**
     * 附言
     */
    private String remark;
    /**
     * 到期日期
     */
    private String dueDate;

    public String getFromActNumber() {
        return fromActNumber;
    }

    public void setFromActNumber(String fromActNumber) {
        this.fromActNumber = fromActNumber;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getFromAcctName() {
        return fromAcctName;
    }

    public void setFromAcctName(String fromAcctName) {
        this.fromAcctName = fromAcctName;
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

    public int getRemitAmount() {
        return remitAmount;
    }

    public void setRemitAmount(int remitAmount) {
        this.remitAmount = remitAmount;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }
}

