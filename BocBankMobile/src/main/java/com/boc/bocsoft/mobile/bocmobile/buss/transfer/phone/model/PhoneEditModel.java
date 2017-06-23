package com.boc.bocsoft.mobile.bocmobile.buss.transfer.phone.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.boc.bocsoft.mobile.bii.bus.login.model.PsnSvrRegisterDevicePre.FactorListBean;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.securityverify.SecurityFactorModel;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by wangtong on 2016/7/27.
 */
public class PhoneEditModel implements Parcelable {

    private String payerAccount;
    private String payerName;
    private String conversionID;
    private String randomNum;
    private BigDecimal remainAmount;
    private String remainCurrency;
    private String trfAmount;
    private String trfCurrency;
    private String payeeMobile;
    private String payeeName;
    private String tips;
    private String accountId;
    private String isHaveAccount;
    public static SecurityFactorModel factorModel;
    public static List<FactorListBean> prefactorList;
    private String conversationId;
    private String defaultFactorName;
    private String selectedFactorId;
    private String tokenId;
    private String payeeAccountNum;
    private String needPassword;
    private String toAccountType;
    private String accountType;
    private String payeeBankNum;
    private boolean queryStates;
    private String currentBalanceflag;
    private String loanBalanceLimitFlag;
    private String warning;
    private String warnType;

    public String getPayerName() {
        return payerName;
    }

    public void setPayerName(String payerName) {
        this.payerName = payerName;
    }

    public String getWarnType() {
        return warnType;
    }

    public void setWarnType(String warnType) {
        this.warnType = warnType;
    }

    public String getWarning() {
        return warning;
    }

    public void setWarning(String warning) {
        this.warning = warning;
    }

    public String getLoanBalanceLimitFlag() {
        return loanBalanceLimitFlag;
    }

    public void setLoanBalanceLimitFlag(String loanBalanceLimitFlag) {
        this.loanBalanceLimitFlag = loanBalanceLimitFlag;
    }

    public String getCurrentBalanceflag() {
        return currentBalanceflag;
    }

    public void setCurrentBalanceflag(String currentBalanceflag) {
        this.currentBalanceflag = currentBalanceflag;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public boolean isQueryStates() {
        return queryStates;
    }

    public void setQueryStates(boolean queryStates) {
        this.queryStates = queryStates;
    }

    public String getPayeeBankNum() {
        return payeeBankNum;
    }

    public void setPayeeBankNum(String payeeBankNum) {
        this.payeeBankNum = payeeBankNum;
    }

    public String getNeedPassword() {
        return needPassword;
    }

    public void setNeedPassword(String needPassword) {
        this.needPassword = needPassword;
    }

    public String getSelectedFactorId() {
        return selectedFactorId;
    }

    public void setSelectedFactorId(String selectedFactorId) {
        this.selectedFactorId = selectedFactorId;
    }

    public String getToAccountType() {
        return toAccountType;
    }

    public void setToAccountType(String toAccountType) {
        this.toAccountType = toAccountType;
    }

    public String getPayeeAccountNum() {
        return payeeAccountNum;
    }

    public void setPayeeAccountNum(String payeeAccountNum) {
        this.payeeAccountNum = payeeAccountNum;
    }

    public String getTokenId() {
        return tokenId;
    }

    public void setTokenId(String tokenId) {
        this.tokenId = tokenId;
    }

    public String getDefaultFactorName() {
        return defaultFactorName;
    }

    public void setDefaultFactorName(String defaultFactorName) {
        this.defaultFactorName = defaultFactorName;
    }

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    public String getConversionID() {
        return conversionID;
    }

    public void setConversionID(String conversionID) {
        this.conversionID = conversionID;
    }

    public String getRandomNum() {
        return randomNum;
    }

    public void setRandomNum(String randomNum) {
        this.randomNum = randomNum;
    }

    public String getIsHaveAccount() {
        return isHaveAccount;
    }

    public void setIsHaveAccount(String isHaveAccount) {
        this.isHaveAccount = isHaveAccount;
    }

    public String getRemainCurrency() {
        return remainCurrency;
    }

    public void setRemainCurrency(String remainCurrency) {
        this.remainCurrency = remainCurrency;
    }

    public String getTrfCurrency() {
        return trfCurrency;
    }

    public void setTrfCurrency(String trfCurrency) {
        this.trfCurrency = trfCurrency;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getPayerAccount() {
        return payerAccount;
    }

    public void setPayerAccount(String payerAccount) {
        this.payerAccount = payerAccount;
    }

    public BigDecimal getRemainAmount() {
        return remainAmount;
    }

    public void setRemainAmount(BigDecimal remainAmount) {
        this.remainAmount = remainAmount;
    }

    public String getTrfAmount() {
        return trfAmount;
    }

    public void setTrfAmount(String trfAmount) {
        this.trfAmount = trfAmount;
    }

    public String getPayeeMobile() {
        return payeeMobile;
    }

    public void setPayeeMobile(String payeeMobile) {
        this.payeeMobile = payeeMobile;
    }

    public String getPayeeName() {
        return payeeName;
    }

    public void setPayeeName(String payeeName) {
        this.payeeName = payeeName;
    }

    public String getTips() {
        return tips;
    }

    public void setTips(String tips) {
        this.tips = tips;
    }


    public PhoneEditModel() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.payerAccount);
        dest.writeString(this.conversionID);
        dest.writeString(this.randomNum);
        dest.writeSerializable(this.remainAmount);
        dest.writeString(this.remainCurrency);
        dest.writeString(this.trfAmount);
        dest.writeString(this.trfCurrency);
        dest.writeString(this.payeeMobile);
        dest.writeString(this.payeeName);
        dest.writeString(this.tips);
        dest.writeString(this.accountId);
        dest.writeString(this.isHaveAccount);
        dest.writeString(this.conversationId);
        dest.writeString(this.defaultFactorName);
        dest.writeString(this.selectedFactorId);
        dest.writeString(this.tokenId);
        dest.writeString(this.payeeAccountNum);
        dest.writeString(this.needPassword);
        dest.writeString(this.toAccountType);
        dest.writeString(this.accountType);
        dest.writeString(this.payeeBankNum);
        dest.writeByte(queryStates ? (byte) 1 : (byte) 0);
    }

    protected PhoneEditModel(Parcel in) {
        this.payerAccount = in.readString();
        this.conversionID = in.readString();
        this.randomNum = in.readString();
        this.remainAmount = (BigDecimal) in.readSerializable();
        this.remainCurrency = in.readString();
        this.trfAmount = in.readString();
        this.trfCurrency = in.readString();
        this.payeeMobile = in.readString();
        this.payeeName = in.readString();
        this.tips = in.readString();
        this.accountId = in.readString();
        this.isHaveAccount = in.readString();
        this.conversationId = in.readString();
        this.defaultFactorName = in.readString();
        this.selectedFactorId = in.readString();
        this.tokenId = in.readString();
        this.payeeAccountNum = in.readString();
        this.needPassword = in.readString();
        this.toAccountType = in.readString();
        this.accountType = in.readString();
        this.payeeBankNum = in.readString();
        this.queryStates = in.readByte() != 0;
    }

    public static final Creator<PhoneEditModel> CREATOR = new Creator<PhoneEditModel>() {
        @Override
        public PhoneEditModel createFromParcel(Parcel source) {
            return new PhoneEditModel(source);
        }

        @Override
        public PhoneEditModel[] newArray(int size) {
            return new PhoneEditModel[size];
        }
    };
}
