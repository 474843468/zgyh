package com.boc.bocsoft.mobile.bocmobile.buss.creditcard.billquery.havebills.model;

/**
 * 作者：xwg on 16/12/30 15:05
 * 信用卡设置类信息查询
 */
public class CrcdSetingsInfoModel {
    private String phoneStatmentStaus; // 1- 开通 0-未开通
    private String emailStatmentStaus;
    private String phoneStatment;
    private String emailStatment;

    public String getEmailStatment() {
        return emailStatment;
    }

    public void setEmailStatment(String emailStatment) {
        this.emailStatment = emailStatment;
    }

    public String getEmailStatmentStaus() {
        return emailStatmentStaus;
    }

    public void setEmailStatmentStaus(String emailStatmentStaus) {
        this.emailStatmentStaus = emailStatmentStaus;
    }

    public String getPhoneStatment() {
        return phoneStatment;
    }

    public void setPhoneStatment(String phoneStatment) {
        this.phoneStatment = phoneStatment;
    }

    public String getPhoneStatmentStaus() {
        return phoneStatmentStaus;
    }

    public void setPhoneStatmentStaus(String phoneStatmentStaus) {
        this.phoneStatmentStaus = phoneStatmentStaus;
    }
}
