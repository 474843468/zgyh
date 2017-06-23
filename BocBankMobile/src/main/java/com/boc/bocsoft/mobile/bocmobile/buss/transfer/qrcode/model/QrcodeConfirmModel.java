package com.boc.bocsoft.mobile.bocmobile.buss.transfer.qrcode.model;

import com.boc.bocsoft.mobile.bii.bus.login.model.PsnSvrRegisterDevicePre.FactorListBean;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.securityverify.SecurityFactorModel;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by wangtong on 2016/7/27.
 */
public class QrcodeConfirmModel {

    private QrcodeTransModel qrcodeTransModel;

    private String payeeAccountType;

    private String payeeBankNum;

    private String accountId;

    //安全因子
    private SecurityFactorModel factorModel;
    //随机数
    private String randomNum;
    //预交易返回安全因子
    private List<FactorListBean> prefactorList;
    //安全加密随机数
    private String[] encryptRandomNums;
    //安全加密密文
    private String[] encryptPasswords;

    private String mPlainData;

    private String mSignData;

    private String currencyCode;

    //密码加密随机数
    private String[] encryptRandomNumsPass;
    //密码加密密文
    private String[] encryptPasswordsPass;
    //手续费
    private String commisionCharge;
    //实收费用
    private BigDecimal finalCommissionCharge;
    //网银交易序号
    private long transactionId;
    //账户余额
    private BigDecimal remainAmount;
    //查询结果
    private boolean checkStates;
    //信用卡查询结果
    private String currency;
    //整体可用余额
    private BigDecimal totalBalance;
    //是否是信用卡查询
    private boolean IsCredit;

    private String warning;

    private String warnType;

    public String getWarning() {
        return warning;
    }

    public void setWarning(String warning) {
        this.warning = warning;
    }

    public String getWarnType() {
        return warnType;
    }

    public void setWarnType(String warnType) {
        this.warnType = warnType;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public BigDecimal getTotalBalance() {
        return totalBalance;
    }

    public void setTotalBalance(BigDecimal totalBalance) {
        this.totalBalance = totalBalance;
    }

    public boolean isCredit() {
        return IsCredit;
    }

    public void setCredit(boolean credit) {
        IsCredit = credit;
    }

    public boolean isCheckStates() {
        return checkStates;
    }

    public void setCheckStates(boolean checkStates) {
        this.checkStates = checkStates;
    }


    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public BigDecimal getRemainAmount() {
        return remainAmount;
    }

    public void setRemainAmount(BigDecimal remainAmount) {
        this.remainAmount = remainAmount;
    }

    public long getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(long transactionId) {
        this.transactionId = transactionId;
    }

    public String getmSignData() {
        return mSignData;
    }

    public void setmSignData(String mSignData) {
        this.mSignData = mSignData;
    }

    public BigDecimal getFinalCommissionCharge() {
        return finalCommissionCharge;
    }

    public void setFinalCommissionCharge(BigDecimal finalCommissionCharge) {
        this.finalCommissionCharge = finalCommissionCharge;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getCommisionCharge() {
        return commisionCharge;
    }

    public void setCommisionCharge(String commisionCharge) {
        this.commisionCharge = commisionCharge;
    }

    public String[] getEncryptRandomNumsPass() {
        return encryptRandomNumsPass;
    }

    public void setEncryptRandomNumsPass(String[] encryptRandomNumsPass) {
        this.encryptRandomNumsPass = encryptRandomNumsPass;
    }

    public String[] getEncryptPasswordsPass() {
        return encryptPasswordsPass;
    }

    public void setEncryptPasswordsPass(String[] encryptPasswordsPass) {
        this.encryptPasswordsPass = encryptPasswordsPass;
    }

    public QrcodeTransModel getQrcodeTransModel() {
        return qrcodeTransModel;
    }

    public void setQrcodeTransModel(QrcodeTransModel qrcodeTransModel) {
        this.qrcodeTransModel = qrcodeTransModel;
    }

    public SecurityFactorModel getFactorModel() {
        return factorModel;
    }

    public void setFactorModel(SecurityFactorModel factorModel) {
        this.factorModel = factorModel;
    }

    public String getRandomNum() {
        return randomNum;
    }

    public void setRandomNum(String randomNum) {
        this.randomNum = randomNum;
    }

    public List<FactorListBean> getPrefactorList() {
        return prefactorList;
    }

    public void setPrefactorList(List<FactorListBean> prefactorList) {
        this.prefactorList = prefactorList;
    }

    public String[] getEncryptRandomNums() {
        return encryptRandomNums;
    }

    public void setEncryptRandomNums(String[] encryptRandomNums) {
        this.encryptRandomNums = encryptRandomNums;
    }

    public String[] getEncryptPasswords() {
        return encryptPasswords;
    }

    public void setEncryptPasswords(String[] encryptPasswords) {
        this.encryptPasswords = encryptPasswords;
    }

    public String getmPlainData() {
        return mPlainData;
    }

    public void setmPlainData(String mPlainData) {
        this.mPlainData = mPlainData;
    }

    public String getPayeeAccountType() {
        return payeeAccountType;
    }

    public void setPayeeAccountType(String payeeAccountType) {
        this.payeeAccountType = payeeAccountType;
    }

    public String getPayeeBankNum() {
        return payeeBankNum;
    }

    public void setPayeeBankNum(String payeeBankNum) {
        this.payeeBankNum = payeeBankNum;
    }

    public String getSignedData() {
        return mSignData;
    }

    public void setSignedData(String signedData) {
        this.mSignData = signedData;
    }

}
