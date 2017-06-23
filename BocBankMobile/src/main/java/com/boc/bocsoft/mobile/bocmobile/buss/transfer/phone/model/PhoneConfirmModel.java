package com.boc.bocsoft.mobile.bocmobile.buss.transfer.phone.model;

import com.boc.bocsoft.mobile.bii.bus.login.model.PsnSvrRegisterDevicePre.FactorListBean;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.securityverify.SecurityFactorModel;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by wangtong on 2016/7/27.
 */
public class PhoneConfirmModel {

    private PhoneEditModel  phoneEditModel;

    //安全因子
    private SecurityFactorModel factorModel;
    //预交易返回安全因子
    private List<FactorListBean> prefactorList;
    //安全加密随机数
    private String[] encryptRandomNums;
    //安全加密密文
    private String[] encryptPasswords;

    private String mPlainData;

    private String mSignData;

    //密码加密随机数
    private String[] encryptRandomNumsPass;
    //密码加密密文
    private String[] encryptPasswordsPass;
    //手续费
    private String commisionCharge;
    // 转账卡号
    private String fromAccoutnNum;
    // 转账币种
    private String remainCurrency;
    //账户余额
    private BigDecimal remainAmount;
    //交易序号
    private String transNum;
    //收款人联行号
    private String fromIbkNum;

    private boolean queryStates;

//    private String accountType;

    private String accountId;

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

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

//    public String getAccountType() {
//        return accountType;
//    }
//
//    public void setAccountType(String accountType) {
//        this.accountType = accountType;
//    }

    public boolean isQueryStates() {
        return queryStates;
    }

    public void setQueryStates(boolean queryStates) {
        this.queryStates = queryStates;
    }

    public String getFromIbkNum() {
        return fromIbkNum;
    }

    public void setFromIbkNum(String fromIbkNum) {
        this.fromIbkNum = fromIbkNum;
    }

    public String getTransNum() {
        return transNum;
    }

    public void setTransNum(String transNum) {
        this.transNum = transNum;
    }


    public BigDecimal getRemainAmount() {
        return remainAmount;
    }

    public void setRemainAmount(BigDecimal remainAmount) {
        this.remainAmount = remainAmount;
    }

    public String getCommisionCharge() {
        return commisionCharge;
    }

    public void setCommisionCharge(String commisionCharge) {
        this.commisionCharge = commisionCharge;
    }

    public String getFromAccoutnNum() {
        return fromAccoutnNum;
    }

    public void setFromAccoutnNum(String fromAccoutnNum) {
        this.fromAccoutnNum = fromAccoutnNum;
    }

    public String getRemainCurrency() {
        return remainCurrency;
    }

    public void setRemainCurrency(String remainCurrency) {
        this.remainCurrency = remainCurrency;
    }

    public String getmSignData() {
        return mSignData;
    }

    public void setmSignData(String mSignData) {
        this.mSignData = mSignData;
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

    public PhoneEditModel getPhoneEditModel() {
        return phoneEditModel;
    }

    public void setPhoneEditModel(PhoneEditModel phoneEditModel) {
        this.phoneEditModel = phoneEditModel;
    }

    public SecurityFactorModel getFactorModel() {
        return factorModel;
    }

    public void setFactorModel(SecurityFactorModel factorModel) {
        this.factorModel = factorModel;
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
}
