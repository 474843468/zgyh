package com.boc.bocsoft.mobile.bocmobile.buss.creditcard.creditcardhomepage.model;

/**
 * Created by wangf on 2016/12/13.
 */

public class CrcdSettingsInfoBean {

    //自动还款状态 - 默认返回人民币账户的自动还款状态 1-自动还款  0-非自动还款
    private String paymentStatus;
    //自动还款方式 - “FULL”全额还款  “MPAY”部分还款
    private String paymentMode;
    //自动还款账户 - 默认返回人民币账户的自动还款账户
    private String paymentAccount;
    //外币账单自动购汇设置状态 - “0”未设定 “1”已设定
    private String foreignPayOffStatus;
    //POS消费验证方式 - “0”签名  “1”签名+密码
    private String verifyMode;
    //POS消费验密触发金额
    private String posTriggerAmount;
    //短信触发金额
    private String smsTriggerAmount;
    //纸质账单开通状态 - 1:开通 0:未开通
    private String paperStatmentStatus;
    //电子账单开通状态 - 1:开通 0:未开通
    private String emailStatmentStatus;
    //推入式账单开通状态 - 1:开通 0:未开通
    private String phoneStatmentStatus;
    //电子对账单 - 根据账单寄送方式字段判断是否开通电子对账单，如开通，显示邮箱地址
    private String emailStatment;
    //纸质对账单 - 根据账单寄送方式字段判断是否开通电子对账单，如开通，显示地址
    private String paperStatment;
    //推入式对账单 - 根据账单寄送方式字段判断是否开通电子对账单，如开通，显示手机号
    private String phoneStatment;
    //全球交易人民币记账标识 - 外币账户才会有“ADTE”表示已设定 其他表示未设定
    private String chargeFlag;

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(String paymentMode) {
        this.paymentMode = paymentMode;
    }

    public String getPaymentAccount() {
        return paymentAccount;
    }

    public void setPaymentAccount(String paymentAccount) {
        this.paymentAccount = paymentAccount;
    }

    public String getForeignPayOffStatus() {
        return foreignPayOffStatus;
    }

    public void setForeignPayOffStatus(String foreignPayOffStatus) {
        this.foreignPayOffStatus = foreignPayOffStatus;
    }

    public String getVerifyMode() {
        return verifyMode;
    }

    public void setVerifyMode(String verifyMode) {
        this.verifyMode = verifyMode;
    }

    public String getPosTriggerAmount() {
        return posTriggerAmount;
    }

    public void setPosTriggerAmount(String posTriggerAmount) {
        this.posTriggerAmount = posTriggerAmount;
    }

    public String getSmsTriggerAmount() {
        return smsTriggerAmount;
    }

    public void setSmsTriggerAmount(String smsTriggerAmount) {
        this.smsTriggerAmount = smsTriggerAmount;
    }

    public String getPaperStatmentStatus() {
        return paperStatmentStatus;
    }

    public void setPaperStatmentStatus(String paperStatmentStatus) {
        this.paperStatmentStatus = paperStatmentStatus;
    }

    public String getEmailStatmentStatus() {
        return emailStatmentStatus;
    }

    public void setEmailStatmentStatus(String emailStatmentStatus) {
        this.emailStatmentStatus = emailStatmentStatus;
    }

    public String getPhoneStatmentStatus() {
        return phoneStatmentStatus;
    }

    public void setPhoneStatmentStatus(String phoneStatmentStatus) {
        this.phoneStatmentStatus = phoneStatmentStatus;
    }

    public String getEmailStatment() {
        return emailStatment;
    }

    public void setEmailStatment(String emailStatment) {
        this.emailStatment = emailStatment;
    }

    public String getPaperStatment() {
        return paperStatment;
    }

    public void setPaperStatment(String paperStatment) {
        this.paperStatment = paperStatment;
    }

    public String getPhoneStatment() {
        return phoneStatment;
    }

    public void setPhoneStatment(String phoneStatment) {
        this.phoneStatment = phoneStatment;
    }

    public String getChargeFlag() {
        return chargeFlag;
    }

    public void setChargeFlag(String chargeFlag) {
        this.chargeFlag = chargeFlag;
    }
}
