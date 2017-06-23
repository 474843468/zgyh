package com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransManagePayeeModifyAlias;

/**
 * 修改收款人别名
 * Created by zhx on 2016/7/26
 */
public class PsnTransManagePayeeModifyAliasResult {

    /**
     * payeeAlias : testName
     * regionCode : null
     * bankNum : null
     * payBankCode : null
     * countryCode : null
     * bankName : null
     * type : null
     * postcode : null
     * bankCode : null
     * accountName : testaccount
     * accountNumber : 666666666666
     * address : null
     * payeeCNName : null
     * accountIbkNum : null
     * payBankName : null
     * payeetId : 66
     * swift : null
     * mobile : null
     */
    /**
     * 地址
     */
    private String address;
    /**
     * 帐户类型
     */
    private String type;
    /**
     * 帐户
     */
    private String accountNumber;
    /**
     * 收款人中文名称
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
     * 手机号
     */
    private String mobile;
    /**
     * 收款行所属银行
     */
    private String bankCode;
    /**
     * 收款地区(跨境汇款)
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
     * 收款人常驻国家
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
    private int payeetId;

    public void setPayeeAlias(String payeeAlias) {
        this.payeeAlias = payeeAlias;
    }

    public void setRegionCode(String regionCode) {
        this.regionCode = regionCode;
    }

    public void setBankNum(String bankNum) {
        this.bankNum = bankNum;
    }

    public void setPayBankCode(String payBankCode) {
        this.payBankCode = payBankCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setPayeeCNName(String payeeCNName) {
        this.payeeCNName = payeeCNName;
    }

    public void setAccountIbkNum(String accountIbkNum) {
        this.accountIbkNum = accountIbkNum;
    }

    public void setPayBankName(String payBankName) {
        this.payBankName = payBankName;
    }

    public void setPayeetId(int payeetId) {
        this.payeetId = payeetId;
    }

    public void setSwift(String swift) {
        this.swift = swift;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPayeeAlias() {
        return payeeAlias;
    }

    public String getRegionCode() {
        return regionCode;
    }

    public String getBankNum() {
        return bankNum;
    }

    public String getPayBankCode() {
        return payBankCode;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public String getBankName() {
        return bankName;
    }

    public String getType() {
        return type;
    }

    public String getPostcode() {
        return postcode;
    }

    public String getBankCode() {
        return bankCode;
    }

    public String getAccountName() {
        return accountName;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public String getAddress() {
        return address;
    }

    public String getPayeeCNName() {
        return payeeCNName;
    }

    public String getAccountIbkNum() {
        return accountIbkNum;
    }

    public String getPayBankName() {
        return payBankName;
    }

    public int getPayeetId() {
        return payeetId;
    }

    public String getSwift() {
        return swift;
    }

    public String getMobile() {
        return mobile;
    }
}
