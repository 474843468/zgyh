package com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.accountmanagement.model;

import com.boc.bocsoft.mobile.bii.bus.account.model.PsnAccountQueryAccountDetail.PsnAccountQueryAccountDetailResult;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadAccountQuery.PsnXpadAccountQueryResult;
import com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadReset.PsnXpadResetResult;
import com.boc.bocsoft.mobile.common.utils.BeanConvertor.BeanConvertor;

import org.threeten.bp.LocalDate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 账户管理数据模型
 * Created by Wanmengxin on 2016/9/20.
 */
public class AccountModel {

    /**
     * 普通账户数据模型
     */
    public AccountModel AccountModel;

    // 创建会话
    private String conversationId;

    // 防重机制
    private String tokenId;

    // 用户风险评级
    private String riskLevel;

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    public String getTokenId() {
        return tokenId;
    }

    public void setTokenId(String tokenId) {
        this.tokenId = tokenId;
    }

    public String getRiskLevel() {
        return riskLevel;
    }

    public void setRiskLevel(String riskLevel) {
        this.riskLevel = riskLevel;
    }

    // 理财账户对象
    public PadAccountEntity padAccountEntity;

    public class PadAccountEntity {
        // 客户理财账户
        private String xpadAccount;
        // 资金账号
        private String accountNo;
        // 账户类型
        private String accountType;
        // 账户开户行
        private String bancID;
        // 账户状态
        private String xpadAccountSatus;
        // 账户ID
        private String accountId;
        // 省行联行号
        private String ibkNumber;
        // 账户缓存标识
        private String accountKey;

        public String getXpadAccount() {
            return xpadAccount;
        }

        public void setXpadAccount(String xpadAccount) {
            this.xpadAccount = xpadAccount;
        }

        public String getAccountNo() {
            return accountNo;
        }

        public void setAccountNo(String accountNo) {
            this.accountNo = accountNo;
        }

        public String getAccountType() {
            return accountType;
        }

        public void setAccountType(String accountType) {
            this.accountType = accountType;
        }

        public String getBancID() {
            return bancID;
        }

        public void setBancID(String bancID) {
            this.bancID = bancID;
        }

        public String getXpadAccountSatus() {
            return xpadAccountSatus;
        }

        public void setXpadAccountSatus(String xpadAccountSatus) {
            this.xpadAccountSatus = xpadAccountSatus;
        }

        public String getAccountId() {
            return accountId;
        }

        public void setAccountId(String accountId) {
            this.accountId = accountId;
        }

        public String getIbkNumber() {
            return ibkNumber;
        }

        public void setIbkNumber(String ibkNumber) {
            this.ibkNumber = ibkNumber;
        }

        public String getAccountKey() {
            return accountKey;
        }

        public void setAccountKey(String accountKey) {
            this.accountKey = accountKey;
        }
    }

    // 主页点击item账户id
    private String choiceId;
    // 主页点击item账号
    private String choiceNum;
    // 主页点击item账户类型
    private String choiceType;

    public String getChoiceId() {
        return choiceId;
    }

    public void setChoiceId(String choiceId) {
        this.choiceId = choiceId;
    }

    public String getChoiceNum() {
        return choiceNum;
    }

    public void setChoiceNum(String choiceNum) {
        this.choiceNum = choiceNum;
    }

    public String getChoiceType() {
        return choiceType;
    }

    public void setChoiceType(String choiceType) {
        this.choiceType = choiceType;
    }

    /**
     * 登记页面数据模型
     * 登记结果数据
     */

    // 登记页点击item
    private String registId;

    //专属理财账户
    private String xpadAccount;

    //银行账户
    private String bankNum;

    public String getRegistId() {
        return registId;
    }

    public void setRegistId(String registId) {
        this.registId = registId;
    }

    private List<padAllAcountDetail> allAcountList;

    public List<padAllAcountDetail> getAllAcountList() {
        return allAcountList;
    }

    //所有中行卡信息集合
    public padAllAcountDetail allAcountDetail;

    public class padAllAcountDetail {
        //网银账户标识
        private String accountId;
        //账户名称
        private String accountName;
        //账号
        private String accountNumber;
        //联行号
        private String accountIbkNum;
        //账户类型(账户，卡，存折等)
        private String accountType;
        //所属银行机构标识
        private Integer branchId;
        //账户别名
        private String nickName;
        //	账户状态
        private String accountStatus;
        //	使用客户标识
        private Integer customerId;
        //	货币码
        private String currencyCode;
        //货币代码2 ->{目前双币信用卡使用}
        private String currencyCode2;
        //所属网银机构名称
        private String branchName;
        //	卡类型描述,供页面展示{如：都市信用卡}
        private String cardDescription;
        //是否有旧账号
        private String hasOldAccountFlag;
        //验证因子HMAC加密串
        private String verifyFactor;
        //是否开通电子现金功能
        private String isECashAccount;
        //账号缓存标识
        private String accountKey;
        //是否一卡双账户  1:是  0：否
        private String isMedicalAccount;
        //是否电子卡账户  1:电子卡  0:非电子卡
        private String eCard;
        //卡片描述代码
        private String cardDescriptionCode;

        public String getAccountId() {
            return accountId;
        }

        public String getAccountName() {
            return accountName;
        }

        public String getAccountNumber() {
            return accountNumber;
        }

        public String getAccountIbkNum() {
            return accountIbkNum;
        }

        public String getAccountType() {
            return accountType;
        }

        public Integer getBranchId() {
            return branchId;
        }

        public String getNickName() {
            return nickName;
        }

        public String getAccountStatus() {
            return accountStatus;
        }

        public Integer getCustomerId() {
            return customerId;
        }

        public String getCurrencyCode() {
            return currencyCode;
        }

        public String getCurrencyCode2() {
            return currencyCode2;
        }

        public String getBranchName() {
            return branchName;
        }

        public String getCardDescription() {
            return cardDescription;
        }

        public String getHasOldAccountFlag() {
            return hasOldAccountFlag;
        }

        public String getVerifyFactor() {
            return verifyFactor;
        }

        public String getIsECashAccount() {
            return isECashAccount;
        }

        public String getAccountKey() {
            return accountKey;
        }

        public String getIsMedicalAccount() {
            return isMedicalAccount;
        }

        public String geteCard() {
            return eCard;
        }

        public String getCardDescriptionCode() {
            return cardDescriptionCode;
        }
    }

    public void setAllAccountList(List<PsnXpadResetResult> result) {
        allAcountList = new ArrayList<>();
        for (PsnXpadResetResult item : result) {
            padAllAcountDetail detail = new padAllAcountDetail();
            detail.accountId = item.accountId;
            detail.eCard = item.eCard;
            detail.hasOldAccountFlag = item.hasOldAccountFlag;
            detail.nickName = item.nickName;
            detail.isECashAccount = item.isECashAccount;
            detail.verifyFactor = item.verifyFactor;
            detail.isMedicalAccount = item.isMedicalAccount;
            detail.cardDescriptionCode = item.cardDescriptionCode;
            detail.accountIbkNum = item.accountIbkNum;
            detail.accountType = item.accountType;
            detail.branchId = item.branchId;
            detail.branchName = item.branchName;
            detail.cardDescription = item.cardDescription;
            detail.currencyCode = item.currencyCode;
            detail.currencyCode2 = item.currencyCode2;
            detail.customerId = item.customerId;
            detail.accountKey = item.accountKey;
            detail.accountName = item.accountName;
            detail.accountNumber = item.accountNumber;
            detail.accountStatus = item.accountStatus;
            allAcountList.add(detail);
        }
    }


    /**
     * 专属理财账户数据详情
     */
    private String openStatus;

    public String getOpenStatus() {
        return openStatus;
    }

    public void setOpenStatus(String openStatus) {
        this.openStatus = openStatus;
    }

    //理财账户 financialAccount对象
    public financialAccount financialAccount = new financialAccount();

    public class financialAccount {
        //产品币种
        private String currencyCode;
        //银行账号缓存标识
        private String accountKey;
        //使用客户标识
        private String customerId;
        //所属银行机构标识
        private String branchId;
        //账号
        private String accountNumber;
        //帐号ID
        private String accountId;
        //账户名
        private String accountName;
        //账户类型
        private String accountType;
        //账户状态
        private String accountStatus;
        //联行号
        private String accountIbkNum;
        //所属网银机构名称
        private String branchName;
        //卡类型描述,供页面展示{如：都市信用卡}
        private String cardDescription;
        private String cardDescriptionCode;
        private String isECashAccount;
        //是否有旧账号
        private String hasOldAccountFlag;
        //货币代码2 ->{目前双币信用卡使用}
        private String currencyCode2;
        //账户别名（"网上专属理财账户"）
        private String nickName;

        public String getCurrencyCode() {
            return currencyCode;
        }

        public void setCurrencyCode(String currencyCode) {
            this.currencyCode = currencyCode;
        }

        public String getAccountKey() {
            return accountKey;
        }

        public void setAccountKey(String accountKey) {
            this.accountKey = accountKey;
        }

        public String getCustomerId() {
            return customerId;
        }

        public void setCustomerId(String customerId) {
            this.customerId = customerId;
        }

        public String getBranchId() {
            return branchId;
        }

        public void setBranchId(String branchId) {
            this.branchId = branchId;
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

        public String getAccountStatus() {
            return accountStatus;
        }

        public void setAccountStatus(String accountStatus) {
            this.accountStatus = accountStatus;
        }

        public String getAccountIbkNum() {
            return accountIbkNum;
        }

        public void setAccountIbkNum(String accountIbkNum) {
            this.accountIbkNum = accountIbkNum;
        }

        public String getBranchName() {
            return branchName;
        }

        public void setBranchName(String branchName) {
            this.branchName = branchName;
        }

        public String getCardDescription() {
            return cardDescription;
        }

        public void setCardDescription(String cardDescription) {
            this.cardDescription = cardDescription;
        }

        public String getCardDescriptionCode() {
            return cardDescriptionCode;
        }

        public void setCardDescriptionCode(String cardDescriptionCode) {
            this.cardDescriptionCode = cardDescriptionCode;
        }

        public String getIsECashAccount() {
            return isECashAccount;
        }

        public void setIsECashAccount(String isECashAccount) {
            this.isECashAccount = isECashAccount;
        }

        public String getHasOldAccountFlag() {
            return hasOldAccountFlag;
        }

        public void setHasOldAccountFlag(String hasOldAccountFlag) {
            this.hasOldAccountFlag = hasOldAccountFlag;
        }

        public String getCurrencyCode2() {
            return currencyCode2;
        }

        public void setCurrencyCode2(String currencyCode2) {
            this.currencyCode2 = currencyCode2;
        }

        public String getNickName() {
            return nickName;
        }

        public void setNickName(String nickName) {
            this.nickName = nickName;
        }
    }

    //通过后台查回来的银行账户 MainAccountBean
    public mainAccount mainAccount = new mainAccount();;

    public class mainAccount {
        // 账号
        private String accountNumber;
        // 账号类型
        private String accountType;
        // 账号缓存标识
        private String accountKey;
        //账户名称
        private String nickName;

        public String getAccountNumber() {
            return accountNumber;
        }

        public void setAccountNumber(String accountNumber) {
            this.accountNumber = accountNumber;
        }

        public String getAccountType() {
            return accountType;
        }

        public void setAccountType(String accountType) {
            this.accountType = accountType;
        }

        public String getAccountKey() {
            return accountKey;
        }

        public void setAccountKey(String accountKey) {
            this.accountKey = accountKey;
        }

        public String getNickName() {
            return nickName;
        }

        public void setNickName(String nickName) {
            this.nickName = nickName;
        }
    }

    //银行账户对象 bankAccount
    public bankAccount bankAccount = new bankAccount();

    public class bankAccount {
        // 账号Id
        private String accountId;
        //货币代码2
        private String currencyCode2;
        // 账号缓存标识
        private String cardDescription;
        private String hasOldAccountFlag;
        private String accountName;
        private String accountNumber;
        private String accountIbkNum;
        private String accountType;
        private String branchId;
        private String nickName;
        private String accountStatus;
        private String customerId;
        private String currencyCode;
        private String branchName;
        private String cardDescriptionCode;
        private String isECashAccount;
        private String isMedicalAccount;
        private String ecard;
        private String verifyFactor;
        private String accountKey;

        public String getAccountId() {
            return accountId;
        }

        public void setAccountId(String accountId) {
            this.accountId = accountId;
        }

        public String getCurrencyCode2() {
            return currencyCode2;
        }

        public void setCurrencyCode2(String currencyCode2) {
            this.currencyCode2 = currencyCode2;
        }

        public String getCardDescription() {
            return cardDescription;
        }

        public void setCardDescription(String cardDescription) {
            this.cardDescription = cardDescription;
        }

        public String getHasOldAccountFlag() {
            return hasOldAccountFlag;
        }

        public void setHasOldAccountFlag(String hasOldAccountFlag) {
            this.hasOldAccountFlag = hasOldAccountFlag;
        }

        public String getAccountName() {
            return accountName;
        }

        public void setAccountName(String accountName) {
            this.accountName = accountName;
        }

        public String getAccountNumber() {
            return accountNumber;
        }

        public void setAccountNumber(String accountNumber) {
            this.accountNumber = accountNumber;
        }

        public String getAccountIbkNum() {
            return accountIbkNum;
        }

        public void setAccountIbkNum(String accountIbkNum) {
            this.accountIbkNum = accountIbkNum;
        }

        public String getAccountType() {
            return accountType;
        }

        public void setAccountType(String accountType) {
            this.accountType = accountType;
        }

        public String getBranchId() {
            return branchId;
        }

        public void setBranchId(String branchId) {
            this.branchId = branchId;
        }

        public String getNickName() {
            return nickName;
        }

        public void setNickName(String nickName) {
            this.nickName = nickName;
        }

        public String getAccountStatus() {
            return accountStatus;
        }

        public void setAccountStatus(String accountStatus) {
            this.accountStatus = accountStatus;
        }

        public String getCustomerId() {
            return customerId;
        }

        public void setCustomerId(String customerId) {
            this.customerId = customerId;
        }

        public String getCurrencyCode() {
            return currencyCode;
        }

        public void setCurrencyCode(String currencyCode) {
            this.currencyCode = currencyCode;
        }

        public String getBranchName() {
            return branchName;
        }

        public void setBranchName(String branchName) {
            this.branchName = branchName;
        }

        public String getCardDescriptionCode() {
            return cardDescriptionCode;
        }

        public void setCardDescriptionCode(String cardDescriptionCode) {
            this.cardDescriptionCode = cardDescriptionCode;
        }

        public String getIsECashAccount() {
            return isECashAccount;
        }

        public void setIsECashAccount(String isECashAccount) {
            this.isECashAccount = isECashAccount;
        }

        public String getIsMedicalAccount() {
            return isMedicalAccount;
        }

        public void setIsMedicalAccount(String isMedicalAccount) {
            this.isMedicalAccount = isMedicalAccount;
        }

        public String getEcard() {
            return ecard;
        }

        public void setEcard(String ecard) {
            this.ecard = ecard;
        }

        public String getVerifyFactor() {
            return verifyFactor;
        }

        public void setVerifyFactor(String verifyFactor) {
            this.verifyFactor = verifyFactor;
        }

        public String getAccountKey() {
            return accountKey;
        }

        public void setAccountKey(String accountKey) {
            this.accountKey = accountKey;
        }
    }


    /**
     * 详情页面数据
     * 账户查询结果
     */
    private String accOpenBank;
    private String accountType;
    private String accountStatus;
    private String accOpenDate;
    private BigDecimal availableBalance;
    private String CurrencyCode;
    private List<AccountDetaiListBean> accountDetaiList;

    public String getCurrencyCode() {
        return CurrencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        CurrencyCode = currencyCode;
    }

    public BigDecimal getAvailableBalance() {
        return availableBalance;
    }

    public void setAvailableBalance(BigDecimal availableBalance) {
        this.availableBalance = availableBalance;
    }

    public String getAccOpenBank() {
        return accOpenBank;
    }

    public void setAccOpenBank(String accOpenBank) {
        this.accOpenBank = accOpenBank;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getAccountStatus() {
        return accountStatus;
    }

    public void setAccountStatus(String accountStatus) {
        this.accountStatus = accountStatus;
    }

    public String getAccOpenDate() {
        return accOpenDate;
    }

    public void setAccOpenDate(String accOpenDate) {
        this.accOpenDate = accOpenDate;
    }


    public List<AccountDetaiListBean> getAccountDetaiList() {
        return accountDetaiList;
    }

    public void setAccountDetaiList(List<AccountDetaiListBean> accountDetaiList) {
        this.accountDetaiList = accountDetaiList;
    }

    public void setAccountDetail(PsnAccountQueryAccountDetailResult result) {
        accountDetaiList = new ArrayList<>();
        for (PsnAccountQueryAccountDetailResult.AccountDetaiListBean bean : result.getAccountDetaiList()) {
            AccountDetaiListBean info = new AccountDetaiListBean();
            BeanConvertor.toBean(bean, info);
            accountDetaiList.add(info);
        }
    }

    public AccountDetaiListBean accountDetaiListBean;

    public class AccountDetaiListBean {
        private String currencyCode;
        private String cashRemit;
        private BigDecimal bookBalance;
        private BigDecimal availableBalance;
        private String volumeNumber;
        private String type;
        private String interestRate;
        private String status;
        private BigDecimal monthBalance;
        private String cdNumber;
        private String cdPeriod;
        private LocalDate openDate;
        private LocalDate interestStartsDate;
        private LocalDate interestEndDate;
        private LocalDate settlementDate;
        private String convertType;
        private String pingNo;
        private String holdAmount;
        private String appointStatus;

        public String getCurrencyCode() {
            return currencyCode;
        }

        public void setCurrencyCode(String currencyCode) {
            this.currencyCode = currencyCode;
        }

        public String getCashRemit() {
            return cashRemit;
        }

        public void setCashRemit(String cashRemit) {
            this.cashRemit = cashRemit;
        }

        public BigDecimal getBookBalance() {
            return bookBalance;
        }

        public void setBookBalance(BigDecimal bookBalance) {
            this.bookBalance = bookBalance;
        }

        public BigDecimal getAvailableBalance() {
            return availableBalance;
        }

        public void setAvailableBalance(BigDecimal availableBalance) {
            this.availableBalance = availableBalance;
        }

        public String getVolumeNumber() {
            return volumeNumber;
        }

        public void setVolumeNumber(String volumeNumber) {
            this.volumeNumber = volumeNumber;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getInterestRate() {
            return interestRate;
        }

        public void setInterestRate(String interestRate) {
            this.interestRate = interestRate;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public BigDecimal getMonthBalance() {
            return monthBalance;
        }

        public void setMonthBalance(BigDecimal monthBalance) {
            this.monthBalance = monthBalance;
        }

        public String getCdNumber() {
            return cdNumber;
        }

        public void setCdNumber(String cdNumber) {
            this.cdNumber = cdNumber;
        }

        public String getCdPeriod() {
            return cdPeriod;
        }

        public void setCdPeriod(String cdPeriod) {
            this.cdPeriod = cdPeriod;
        }

        public LocalDate getOpenDate() {
            return openDate;
        }

        public void setOpenDate(LocalDate openDate) {
            this.openDate = openDate;
        }

        public LocalDate getInterestStartsDate() {
            return interestStartsDate;
        }

        public void setInterestStartsDate(LocalDate interestStartsDate) {
            this.interestStartsDate = interestStartsDate;
        }

        public LocalDate getInterestEndDate() {
            return interestEndDate;
        }

        public void setInterestEndDate(LocalDate interestEndDate) {
            this.interestEndDate = interestEndDate;
        }

        public LocalDate getSettlementDate() {
            return settlementDate;
        }

        public void setSettlementDate(LocalDate settlementDate) {
            this.settlementDate = settlementDate;
        }

        public String getConvertType() {
            return convertType;
        }

        public void setConvertType(String convertType) {
            this.convertType = convertType;
        }

        public String getPingNo() {
            return pingNo;
        }

        public void setPingNo(String pingNo) {
            this.pingNo = pingNo;
        }

        public String getHoldAmount() {
            return holdAmount;
        }

        public void setHoldAmount(String holdAmount) {
            this.holdAmount = holdAmount;
        }

        public String getAppointStatus() {
            return appointStatus;
        }

        public void setAppointStatus(String appointStatus) {
            this.appointStatus = appointStatus;
        }
    }


    /**
     * 专属帐号登记
     */
    // 账户类型
    private String accountType_OFA;
    //账户别名
    private String nickname_OFA;
    //账号（DisplayNumber）
    private String accountNumber_OFA;

    public String getAccountType_OFA() {
        return accountType_OFA;
    }

    public void setAccountType_OFA(String accountType_OFA) {
        this.accountType_OFA = accountType_OFA;
    }

    public String getNickname_OFA() {
        return nickname_OFA;
    }

    public void setNickname_OFA(String nickname_OFA) {
        this.nickname_OFA = nickname_OFA;
    }

    public String getAccountNumber_OFA() {
        return accountNumber_OFA;
    }

    public void setAccountNumber_OFA(String accountNumber_OFA) {
        this.accountNumber_OFA = accountNumber_OFA;
    }
}