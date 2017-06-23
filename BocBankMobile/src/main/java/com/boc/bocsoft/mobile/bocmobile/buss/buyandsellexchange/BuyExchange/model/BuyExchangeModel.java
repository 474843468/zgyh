package com.boc.bocsoft.mobile.bocmobile.buss.buyandsellexchange.BuyExchange.model;

import com.boc.bocsoft.mobile.bii.bus.fess.model.PsnFessQueryAccountBalance.PsnFessQueryAccountBalanceResult;
import com.boc.bocsoft.mobile.bocmobile.base.model.AccountBean;
import com.boc.bocsoft.mobile.bocmobile.buss.system.life.LifeTools;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 结构汇委托页model
 * Created by gwluo on 2016/12/8.
 */

public class BuyExchangeModel implements Serializable, Cloneable {
    private String returnCnyAmt;       //#折合人民币金额
    private String bankSelfNum;        //#银行自身交易流水号
    /**
     * A 交易成功
     * B 交易失败
     * K 银行处理中
     */
    private String transStatus;             //#交易状态

    private String transactionId;         //#交易流水号

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getReturnCnyAmt() {
        return returnCnyAmt;
    }

    public void setReturnCnyAmt(String returnCnyAmt) {
        this.returnCnyAmt = returnCnyAmt;
    }

    public String getBankSelfNum() {
        return bankSelfNum;
    }

    public void setBankSelfNum(String bankSelfNum) {
        this.bankSelfNum = bankSelfNum;
    }

    public String getTransStatus() {
        return transStatus;
    }

    public void setTransStatus(String transStatus) {
        this.transStatus = transStatus;
    }

    private String moneyUse;//资金用途

    public String getMoneyUse() {
        return moneyUse;
    }

    public void setMoneyUse(String moneyUse) {
        this.moneyUse = moneyUse;
    }

    private String referenceRate;//基准汇率
    private String finalReferenceRate;//核心基准汇率
    private String exchangeRate;//优惠后汇率
    private String finalExchangeRate;//核心优惠后汇率

    public String getFinalReferenceRate() {
        return finalReferenceRate;
    }

    public void setFinalReferenceRate(String finalReferenceRate) {
        this.finalReferenceRate = finalReferenceRate;
    }

    public String getFinalExchangeRate() {
        return finalExchangeRate;
    }

    public void setFinalExchangeRate(String finalExchangeRate) {
        this.finalExchangeRate = finalExchangeRate;
    }

    public String getExchangeRate() {
        return exchangeRate;
    }

    public void setExchangeRate(String exchangeRate) {
        this.exchangeRate = exchangeRate;
    }

    public void setReferenceRate(String referenceRate) {
        this.referenceRate = referenceRate;
    }

    public String getReferenceRate() {
        return referenceRate;
    }

    private String currency;//	币种
    private String transAmount;//金额

    private String cashRemit;//	钞汇
    private String cashRemitRate;//	钞汇牌价
    private String RMBCost;//	花费人民币
    private String availableBalance = "";//	可用余额
    private String payerAccount;//	账号
    private String payerName;//	名称
    private String accountId;//	账户id
    private String availableBalanceCUR;//人民币可购外币最大金额
    private String annRmeAmtCUR;//本年额度内剩余可/售结汇外币金额
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

    public String getIdentityType() {
        return identityType;
    }

    public void setIdentityType(String identityType) {
        this.identityType = identityType;
    }

    public String getAnnRmeAmtCUR() {
        return annRmeAmtCUR;
    }

    public void setAnnRmeAmtCUR(String annRmeAmtCUR) {
        this.annRmeAmtCUR = annRmeAmtCUR;
    }

    public String getAvailableBalanceCUR() {
        return availableBalanceCUR;
    }

    public void setAvailableBalanceCUR(String availableBalanceCUR) {
        this.availableBalanceCUR = availableBalanceCUR;
    }

    public String getRMBCost() {
        return RMBCost;
    }

    public void setRMBCost(String RMBCost) {
        this.RMBCost = RMBCost;
    }

    public String getCashRemitRate() {
        return cashRemitRate;
    }

    public void setCashRemitRate(String cashRemitRate) {
        this.cashRemitRate = cashRemitRate;
    }

    public String getTransAmount() {
        return transAmount;
    }

    public void setTransAmount(String transAmount) {
        this.transAmount = transAmount;
    }

    public String getPayerAccount() {
        return payerAccount;
    }

    public void setPayerAccount(String payerAccount) {
        this.payerAccount = payerAccount;
    }

    public String getPayerName() {
        return payerName;
    }

    public void setPayerName(String payerName) {
        this.payerName = payerName;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getCashRemit() {
        return cashRemit;
    }

    public void setCashRemit(String cashRemit) {
        this.cashRemit = cashRemit;
    }

    public String getAvailableBalance() {
        return availableBalance;
    }

    public void setAvailableBalance(String availableBalance) {
        this.availableBalance = availableBalance;
    }

    private String conversationId;

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    private String annAmtUSD;//本年额度内已结/购汇金额折美元	String
    private String annRmeAmtUSD;//本年额度内剩余可/售结汇金额折美元	String
    private String finalAnnAmtUSD;//交易完成后，本年额度内已结/购汇金额折美元	String
    private String finalAnnRmeAmtUSD;//交易完成后，本年额度内剩余可/售结汇金额折美元	String
    private String typeStatus;//个人主体分类状态代码	String	01正常  02预关注  03关注名单
    private String signStatus;//	确认书签署状态	String	0未告知  1已告知
    private String custName;//	交易主体姓名	String	用于风险提示函与关注名单告知书提示使用

    public String getCustName() {
        return custName;
    }

    public void setCustName(String custName) {
        this.custName = custName;
    }

    public String getFinalAnnAmtUSD() {
        return finalAnnAmtUSD;
    }

    public void setFinalAnnAmtUSD(String finalAnnAmtUSD) {
        this.finalAnnAmtUSD = finalAnnAmtUSD;
    }

    public String getFinalAnnRmeAmtUSD() {
        return finalAnnRmeAmtUSD;
    }

    public void setFinalAnnRmeAmtUSD(String finalAnnRmeAmtUSD) {
        this.finalAnnRmeAmtUSD = finalAnnRmeAmtUSD;
    }

    public String getTypeStatus() {
        return typeStatus;
    }

    public void setTypeStatus(String typeStatus) {
        this.typeStatus = typeStatus;
    }

    public String getSignStatus() {
        return signStatus;
    }

    public void setSignStatus(String signStatus) {
        this.signStatus = signStatus;
    }

    public String getAnnAmtUSD() {
        return annAmtUSD;
    }

    public void setAnnAmtUSD(String annAmtUSD) {
        this.annAmtUSD = annAmtUSD;
    }

    public String getAnnRmeAmtUSD() {
        return annRmeAmtUSD;
    }

    public void setAnnRmeAmtUSD(String annRmeAmtUSD) {
        this.annRmeAmtUSD = annRmeAmtUSD;
    }

    /**
     * 账户集合
     */
    private ArrayList<AccountBean> accList = new ArrayList<>();

    /**
     * 余额集合
     */
    private LinkedHashMap<String, List<PsnFessQueryAccountBalanceResult>> balanceMap = new LinkedHashMap<>();

    public ArrayList<AccountBean> getAccList() {
        return accList;
    }

    public void setAccList(ArrayList<AccountBean> accList) {
        this.accList = accList;
    }

    public LinkedHashMap<String, List<PsnFessQueryAccountBalanceResult>> getBalanceMap() {
        return balanceMap;
    }

    public void setBalanceMap(LinkedHashMap<String, List<PsnFessQueryAccountBalanceResult>> balanceMap) {
        this.balanceMap = balanceMap;
    }

    @Override
    public BuyExchangeModel clone() throws CloneNotSupportedException {
        BuyExchangeModel model = (BuyExchangeModel) super.clone();
        model.balanceMap = (LinkedHashMap<String, List<PsnFessQueryAccountBalanceResult>>) balanceMap.clone();
        model.accList = (ArrayList<AccountBean>) accList.clone();
        return model;
    }
}
