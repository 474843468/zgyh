package com.boc.bocsoft.mobile.bii.bus.fess.model.PsnFessQueryAccount;

import java.util.List;

/**
 * I49 4.1 001 PsnFessQueryAccount查询结售汇账户列表
 * Created by gwluo on 2016/11/16.
 */

public class PsnFessQueryAccountResult {
    /**
     * 证件类型	String	个人客户证件类型：
     * 01－居民身份证
     * 02－临时身份证
     * 03－护照
     * 04－户口簿
     * 05－军人身份证
     * 06－武装警察身份证
     * 08－外交人员身份证
     * 09－外国人居留许可证
     * 10－边民出入境通行证
     * 11－其它
     * 47－港澳居民来往内地通行证（香港）
     * 48－港澳居民来往内地通行证（澳门）
     * 49－台湾居民来往大陆通行证”
     */
    private String identityType;
    private String identityNumber;// 证件号码
    //    private String custName;// 姓名
    // 国家/地区 从核心60460-A选项查回，为两位字母的国家代码，
    // 例如中国为“CN”
//    private String countryCode;
    private List<Account> list;//账户列表

    public List<Account> getList() {
        return list;
    }

    public void setList(List<Account> list) {
        this.list = list;
    }

    public String getIdentityType() {
        return identityType;
    }

    public void setIdentityType(String identityType) {
        this.identityType = identityType;
    }

    public String getIdentityNumber() {
        return identityNumber;
    }

    public void setIdentityNumber(String identityNumber) {
        this.identityNumber = identityNumber;
    }

//    public String getCustName() {
//        return custName;
//    }

//    public void setCustName(String custName) {
//        this.custName = custName;
//    }

//    public String getCountryCode() {
//        return countryCode;
//    }

//    public void setCountryCode(String countryCode) {
//        this.countryCode = countryCode;
//    }

    public class Account {
        private String accountNumber;//	账号	String
        private String accountId;//	账户id	String
        private String accountName;//	账户名称	String
        private String accountType;//	账户类型	String	119:长城电子借记卡188:活期一本通
        private String accountIbkNum;//	账户所属地区
        private String nickName;//	账户别名
        private String custName;// 姓名
        private String countryCode;// 国家代码

        public String getCustName() {
            return custName;
        }

        public void setCustName(String custName) {
            this.custName = custName;
        }

        public String getCountryCode() {
            return countryCode;
        }

        public void setCountryCode(String countryCode) {
            this.countryCode = countryCode;
        }

        public String getAccountNumber() {
            return accountNumber;
        }

        public void setAccountNumber(String accountNumber) {
            this.accountNumber = accountNumber;
        }

        public String getAccountId() {
            return accountId;
        }

        public void setAccountId(String accountId) {
            this.accountId = accountId;
        }

        public String getAccountName() {
            return accountName;
        }

        public void setAccountName(String accountName) {
            this.accountName = accountName;
        }

        public String getAccountType() {
            return accountType;
        }

        public void setAccountType(String accountType) {
            this.accountType = accountType;
        }

        public String getAccountIbkNum() {
            return accountIbkNum;
        }

        public void setAccountIbkNum(String accountIbkNum) {
            this.accountIbkNum = accountIbkNum;
        }

        public String getNickName() {
            return nickName;
        }

        public void setNickName(String nickName) {
            this.nickName = nickName;
        }
    }
}
