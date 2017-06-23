package com.boc.bocsoft.mobile.bocmobile.buss.buyandsellexchange.BuyExchange.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 首页model
 * Created by gwluo on 2016/11/16.
 */

public class BuyAndSellExcHomeModel implements Serializable {
    private String conversationId;

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }


    //4.13 013PsnFessQueryForLimit查询个人结售汇额度
//    private String annAmtUSD;//本年额度内已结/购汇金额折美元	String
//    private String annRmeAmtUSD;//本年额度内剩余可/售结汇金额折美元	String
//    private String typeStatus;//个人主体分类状态代码	String	01正常  02预关注  03关注名单
//    private String signStatus;//	确认书签署状态	String	0未告知  1已告知
//    private String custName1;//	交易主体姓名	String	用于风险提示函与关注名单告知书提示使用

//    public String getCustName1() {
//        return custName1;
//    }
//
//    public void setCustName1(String custName1) {
//        this.custName1 = custName1;
//    }
//
//    public String getAnnAmtUSD() {
//        return annAmtUSD;
//    }
//
//    public void setAnnAmtUSD(String annAmtUSD) {
//        this.annAmtUSD = annAmtUSD;
//    }
//
//    public String getAnnRmeAmtUSD() {
//        return annRmeAmtUSD;
//    }
//
//    public void setAnnRmeAmtUSD(String annRmeAmtUSD) {
//        this.annRmeAmtUSD = annRmeAmtUSD;
//    }
//
//    public String getTypeStatus() {
//        return typeStatus;
//    }
//
//    public void setTypeStatus(String typeStatus) {
//        this.typeStatus = typeStatus;
//    }
//
//    public String getSignStatus() {
//        return signStatus;
//    }
//
//    public void setSignStatus(String signStatus) {
//        this.signStatus = signStatus;
//    }

//    public List<AccountBalance> getAccountBalanceList() {
//        return accountBalanceList;
//    }
//
//    public void setAccountBalanceList(List<AccountBalance> accountBalanceList) {
//        this.accountBalanceList = accountBalanceList;
//    }
//
//    //4.2 002 PsnFessQueryAccountBalance查询结售汇帐户余额
//    private List<AccountBalance> accountBalanceList = new ArrayList<>();


//    public class AccountBalance {
//        private String currency;//	币种
//        private String cashRemit;//	钞汇
//        private String availableBalance;//	可用余额
//
//        public String getCurrency() {
//            return currency;
//        }
//
//        public void setCurrency(String currency) {
//            this.currency = currency;
//        }
//
//        public String getCashRemit() {
//            return cashRemit;
//        }
//
//        public void setCashRemit(String cashRemit) {
//            this.cashRemit = cashRemit;
//        }
//
//        public String getAvailableBalance() {
//            return availableBalance;
//        }
//
//        public void setAvailableBalance(String availableBalance) {
//            this.availableBalance = availableBalance;
//        }
//    }

//    I49 4.1 001 PsnFessQueryAccount查询结售汇账户列表
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
    private String custName;// 姓名
    // 国家/地区 从核心60460-A选项查回，为两位字母的国家代码，
    // 例如中国为“CN”
    private String countryCode;
    private List<Account> accountList = new ArrayList<>();//账户列表

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

    public List<Account> getAccountList() {
        return accountList;
    }

    public void setAccountList(List<Account> accountList) {
        this.accountList = accountList;
    }

    public class Account {
        private String accountNumber;//	账号	String
        private String accountId;//	账户id	String
        private String accountName;//	账户名称	String
        private String accountType;//	账户类型	String	119:长城电子借记卡188:活期一本通
        private String accountIbkNum;//	账户所属地区
        private String nickName;//	账户别名

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
