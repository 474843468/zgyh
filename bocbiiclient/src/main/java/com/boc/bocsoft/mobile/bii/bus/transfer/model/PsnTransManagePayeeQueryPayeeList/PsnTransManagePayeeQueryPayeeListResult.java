package com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransManagePayeeQueryPayeeList;

/**
 * Created by Wangyuan on 2016/6/12
 * 查询收款人列表
 */
public class PsnTransManagePayeeQueryPayeeListResult {
    /**
     * 地址(收款人为国内跨行时该字段表示分行行名,收款人为跨境时该字段表示分行地址“收款人地址”)
     */
    private String address;
    /**
     * 帐户类型
     */
    private String type;
    /**
     * 账户
     */
    private String accountNumber;
    /**
     * 收款人中文名称(跨境汇款)
     */
    private String payeeCNName;
    /**
     * 收款人银行名称
     */
    private String bankName;
    /**
     * 收款人别名
     */
    private String payeeAlias;
    /**
     * 收款人别名
     */
    private String mobile;
    /**
     * 收款行所属银行
     */
    private String bankCode;
    /**
     * 收款行所属银行
     */
    private String regionCode;
    /**
     * 收款人邮政编码
     */
    private String postcode;
    /**
     * 收款行行号(跨境汇款)
     */
    private String bankNum;
    /**
     * 收款人常驻国家(跨境汇款)
     */
    private String countryCode;
    /**
     * 二代支付行行号
     */
    private String payBankCode;
    /**
     * 二代支付行行名
     */
    private String payBankName;
    /**
     * 收款人帐户名称
     */
    private String accountName;
    /**
     * 收款行联行号（所属地区）
     */
    private String accountIbkNum;
    /**
     * 收款银行SWIFT CODE
     */
    private String swift;
    /**
     * 收款人ID
     */
    private String payeetId;
    /**
     * 收款账户标志(0他行,1中行,2信用卡(对私),3二代支付,4跨境汇款收款人)
     */
    private String bocFlag;
    /**
     * CNAPS号
     */
    private String cnapsCode;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getPayeeCNName() {
        return payeeCNName;
    }

    public void setPayeeCNName(String payeeCNName) {
        this.payeeCNName = payeeCNName;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getPayeeAlias() {
        return payeeAlias;
    }

    public void setPayeeAlias(String payeeAlias) {
        this.payeeAlias = payeeAlias;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public String getRegionCode() {
        return regionCode;
    }

    public void setRegionCode(String regionCode) {
        this.regionCode = regionCode;
    }


    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public String getBankNum() {
        return bankNum;
    }

    public void setBankNum(String bankNum) {
        this.bankNum = bankNum;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getPayBankCode() {
        return payBankCode;
    }

    public void setPayBankCode(String payBankCode) {
        this.payBankCode = payBankCode;
    }

    public String getPayBankName() {
        return payBankName;
    }

    public void setPayBankName(String payBankName) {
        this.payBankName = payBankName;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getAccountIbkNum() {
        return accountIbkNum;
    }

    public void setAccountIbkNum(String accountIbkNum) {
        this.accountIbkNum = accountIbkNum;
    }

    public String getSwift() {
        return swift;
    }

    public void setSwift(String swift) {
        this.swift = swift;
    }

    public String getPayeetId() {
        return payeetId;
    }

    public void setPayeetId(String payeetId) {
        this.payeetId = payeetId;
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
