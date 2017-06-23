package com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.batchprepay.ui.submit;

import java.io.Serializable;

/**
 * 批量提前还款-汇总信息
 * Created by liuzc on 2016/9/8.
 */
public class BatchPrepaySubmitTotalBean implements Serializable{
    private String totalCapital; //本金
    private String totalInterest; //利息
    private String totalCharges; //手续费
    private String totalAmount; //本息合计，不包含手续费
    private String totalAmountWithCharges; //总金额，包含手续费
    private String payAccount; //还款账户
    private String payAccountID; //还款账户ID

    public String getTotalCapital() {
        return totalCapital;
    }

    public void setTotalCapital(String totalCapital) {
        this.totalCapital = totalCapital;
    }

    public String getTotalInterest() {
        return totalInterest;
    }

    public void setTotalInterest(String totalInterest) {
        this.totalInterest = totalInterest;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getPayAccount() {
        return payAccount;
    }

    public void setPayAccount(String payAccount) {
        this.payAccount = payAccount;
    }

    public String getPayAccountID() {
        return payAccountID;
    }

    public void setPayAccountID(String payAccountID) {
        this.payAccountID = payAccountID;
    }

    public String getTotalCharges() {
        return totalCharges;
    }

    public void setTotalCharges(String totalCharges) {
        this.totalCharges = totalCharges;
    }

    public String getTotalAmountWithCharges() {
        return totalAmountWithCharges;
    }

    public void setTotalAmountWithCharges(String totalAmountWithCharges) {
        this.totalAmountWithCharges = totalAmountWithCharges;
    }
}
