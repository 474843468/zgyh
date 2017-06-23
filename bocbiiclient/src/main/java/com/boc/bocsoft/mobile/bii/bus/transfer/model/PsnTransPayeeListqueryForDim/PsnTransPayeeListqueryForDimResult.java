package com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransPayeeListqueryForDim;

import java.util.List;

/**
 * 查询收款人列表返回结果
 * Created by liuyang on 2016/6/23.
 */
public class PsnTransPayeeListqueryForDimResult {


    /**
     * recordCount : 0
     * list : [{"address":"中国农业银行北京分行","type":"119","mobile":"18801283218","accountNumber":"4563515005005799507","isAppointed":"1","payeeCNName":null,"bocFlag":"3","bankNum":null,"accountIbkNum":null,"bankCode":null,"cnapsCode":"42465","payeeAlias":null,"accountName":"pseudo2","bankName":"中国农业银行北京分行","payBankName":null,"payBankCode":null,"countryCode":null,"postcode":null,"regionCode":null,"swift":null,"payeetId":8334726}]
     */

    private int recordCount;
    /**
     * address : 中国农业银行北京分行     收款人为国内跨行时该字段表示分行行名；收款人为跨境时该字段表示收款人地址
     * type : 119
     * mobile : 18801283218
     * accountNumber : 4563515005005799507
     * isAppointed : 1
     * payeeCNName : null
     * bocFlag : 3                          0他行,1中行,2信用卡(对私),3二代支付,4跨境汇款收款人
     * bankNum : null
     * accountIbkNum : null
     * bankCode : null
     * cnapsCode : 42465
     * payeeAlias : null
     * accountName : pseudo2
     * bankName : 中国农业银行北京分行
     * payBankName : null
     * payBankCode : null
     * countryCode : null
     * postcode : null
     * regionCode : null
     * swift : null
     * payeetId : 8334726
     */

    private List<PayeeAccountBean> list;

    public int getRecordCount() {
        return recordCount;
    }

    public void setRecordCount(int recordCount) {
        this.recordCount = recordCount;
    }

    public List<PayeeAccountBean> getList() {
        return list;
    }

    public void setList(List<PayeeAccountBean> list) {
        this.list = list;
    }



    public static class PayeeAccountBean {
        //地址
        private String address;
        //帐户类型
        private String type;
        //手机号
        private String mobile;
        //账户
        private String accountNumber;
        //是否定向收款人
        private String isAppointed;
        //收款人中文名称(跨境汇款)
        private String payeeCNName;
        //收款账户标志
        private String bocFlag;
        //收款行行号(跨境汇款)
        private String bankNum;
        //收款行联行号（所属地区）
        private String accountIbkNum;
        //收款行所属银行
        private String bankCode;
        //CNAPS号
        private String cnapsCode;
        //收款人别名
        private String payeeAlias;
        //收款人帐户名称
        private String accountName;
        //收款人银行名称
        private String bankName;
        //二代支付行行名
        private String payBankName;
        //二代支付行行号
        private String payBankCode;
        //收款人常驻国家(跨境汇款)
        private String countryCode;
        //收款人邮政编码
        private String postcode;
        //收款地区(跨境汇款)
        private String regionCode;
        //收款银行SWIFT CODE
        private String swift;
        //收款人ID
        private Integer payeetId;

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

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getAccountNumber() {
            return accountNumber;
        }

        public void setAccountNumber(String accountNumber) {
            this.accountNumber = accountNumber;
        }

        public String getIsAppointed() {
            return isAppointed;
        }

        public void setIsAppointed(String isAppointed) {
            this.isAppointed = isAppointed;
        }

        public String getPayeeCNName() {
            return payeeCNName;
        }

        public void setPayeeCNName(String payeeCNName) {
            this.payeeCNName = payeeCNName;
        }

        public String getBocFlag() {
            return bocFlag;
        }

        public void setBocFlag(String bocFlag) {
            this.bocFlag = bocFlag;
        }

        public String getBankNum() {
            return bankNum;
        }

        public void setBankNum(String bankNum) {
            this.bankNum = bankNum;
        }

        public String getAccountIbkNum() {
            return accountIbkNum;
        }

        public void setAccountIbkNum(String accountIbkNum) {
            this.accountIbkNum = accountIbkNum;
        }

        public String getBankCode() {
            return bankCode;
        }

        public void setBankCode(String bankCode) {
            this.bankCode = bankCode;
        }

        public String getCnapsCode() {
            return cnapsCode;
        }

        public void setCnapsCode(String cnapsCode) {
            this.cnapsCode = cnapsCode;
        }

        public String getPayeeAlias() {
            return payeeAlias;
        }

        public void setPayeeAlias(String payeeAlias) {
            this.payeeAlias = payeeAlias;
        }

        public String getAccountName() {
            return accountName;
        }

        public void setAccountName(String accountName) {
            this.accountName = accountName;
        }

        public String getBankName() {
            return bankName;
        }

        public void setBankName(String bankName) {
            this.bankName = bankName;
        }

        public String getPayBankName() {
            return payBankName;
        }

        public void setPayBankName(String payBankName) {
            this.payBankName = payBankName;
        }

        public String getPayBankCode() {
            return payBankCode;
        }

        public void setPayBankCode(String payBankCode) {
            this.payBankCode = payBankCode;
        }

        public String getCountryCode() {
            return countryCode;
        }

        public void setCountryCode(String countryCode) {
            this.countryCode = countryCode;
        }

        public String getPostcode() {
            return postcode;
        }

        public void setPostcode(String postcode) {
            this.postcode = postcode;
        }

        public String getRegionCode() {
            return regionCode;
        }

        public void setRegionCode(String regionCode) {
            this.regionCode = regionCode;
        }

        public String getSwift() {
            return swift;
        }

        public void setSwift(String swift) {
            this.swift = swift;
        }

        public Integer getPayeetId() {
            return payeetId;
        }

        public void setPayeetId(Integer payeetId) {
            this.payeetId = payeetId;
        }
    }
}
