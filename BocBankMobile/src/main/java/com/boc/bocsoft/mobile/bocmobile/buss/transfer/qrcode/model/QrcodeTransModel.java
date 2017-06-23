package com.boc.bocsoft.mobile.bocmobile.buss.transfer.qrcode.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.boc.bocsoft.mobile.bii.bus.login.model.PsnSvrRegisterDevicePre.FactorListBean;
import com.boc.bocsoft.mobile.bocmobile.base.widget.dialogview.securityverify.SecurityFactorModel;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangtong on 2016/7/28.
 */
public class QrcodeTransModel implements Parcelable {
    private String payerAccount;
    private BigDecimal remainAmount;
    private String remainCurrency;
    private String trfAmount;
    private String trfCurrency;
    private String payeeAccount;
    private String payeeMobile;
    private String payeeName;
    private String tips;
    private String accountId;
    private String payeeBankNum;
    private String mPlainData;
    private String payerAccountType;
    private String payeeAccountType;
    private String conversationId;
    private String warning;
    private String warnType;
    //随机数
    private String randomNum;
    //安全因子
    private SecurityFactorModel factorModel;
    //预交易返回安全因子
    private List<FactorListBean> prefactorList;
    //最终选择的安全因子
    private String selectedFactorId;
    private String needPassword;
    //手续费
    private String commisionCharge;
    private String defaultFactorName;
    //信用卡查询结果
    private String currency;
    //整体可用余额
    private BigDecimal totalBalance;
    //查询结果
    private boolean checkStates;
    //是否是信用卡查询
    private boolean IsCredit;
    private String loanBalanceLimitFlag;

    public String getPayerAccountType() {
        return payerAccountType;
    }

    public void setPayerAccountType(String payerAccountType) {
        this.payerAccountType = payerAccountType;
    }

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

    public String getLoanBalanceLimitFlag() {
        return loanBalanceLimitFlag;
    }

    public void setLoanBalanceLimitFlag(String loanBalanceLimitFlag) {
        this.loanBalanceLimitFlag = loanBalanceLimitFlag;
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

    public String getDefaultFactorName() {
        return defaultFactorName;
    }

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    public void setDefaultFactorName(String defaultFactorName) {
        this.defaultFactorName = defaultFactorName;
    }

    public String getCommisionCharge() {
        return commisionCharge;
    }

    public void setCommisionCharge(String commisionCharge) {
        this.commisionCharge = commisionCharge;
    }

    public String getmPlainData() {
        return mPlainData;
    }

    public void setmPlainData(String mPlainData) {
        this.mPlainData = mPlainData;
    }

    public List<FactorListBean> getPrefactorList() {
        return prefactorList;
    }

    public void setPrefactorList(List<FactorListBean> prefactorList) {
        this.prefactorList = prefactorList;
    }

    public String getNeedPassword() {
        return needPassword;
    }

    public void setNeedPassword(String needPassword) {
        this.needPassword = needPassword;
    }

    public String getPayeeAccountType() {
        return payeeAccountType;
    }

    public void setPayeeAccountType(String payeeAccountType) {
        this.payeeAccountType = payeeAccountType;
    }

    public String getSelectedFactorId() {
        return selectedFactorId;
    }

    public void setSelectedFactorId(String selectedFactorId) {
        this.selectedFactorId = selectedFactorId;
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

    public String getPayeeBankNum() {
        return payeeBankNum;
    }

    public void setPayeeBankNum(String payeeBankNum) {
        this.payeeBankNum = payeeBankNum;
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

    public String getPayeeAccount() {
        return payeeAccount;
    }

    public void setPayeeAccount(String payeeAccount) {
        this.payeeAccount = payeeAccount;
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


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.payerAccount);
        dest.writeSerializable(this.remainAmount);
        dest.writeString(this.remainCurrency);
        dest.writeString(this.trfAmount);
        dest.writeString(this.trfCurrency);
        dest.writeString(this.payeeAccount);
        dest.writeString(this.payeeMobile);
        dest.writeString(this.payeeName);
        dest.writeString(this.tips);
        dest.writeString(this.accountId);
        dest.writeString(this.payeeBankNum);
        dest.writeString(this.mPlainData);
        dest.writeString(this.payeeAccountType);
        dest.writeString(this.conversationId);
        dest.writeString(this.randomNum);
        dest.writeList(this.prefactorList);
        dest.writeString(this.selectedFactorId);
        dest.writeString(this.needPassword);
        dest.writeString(this.commisionCharge);
        dest.writeString(this.defaultFactorName);
        dest.writeString(this.currency);
        dest.writeSerializable(this.totalBalance);
        dest.writeByte(checkStates ? (byte) 1 : (byte) 0);
        dest.writeByte(IsCredit ? (byte) 1 : (byte) 0);
    }

    public QrcodeTransModel() {
    }

    protected QrcodeTransModel(Parcel in) {
        this.payerAccount = in.readString();
        this.remainAmount = (BigDecimal) in.readSerializable();
        this.remainCurrency = in.readString();
        this.trfAmount = in.readString();
        this.trfCurrency = in.readString();
        this.payeeAccount = in.readString();
        this.payeeMobile = in.readString();
        this.payeeName = in.readString();
        this.tips = in.readString();
        this.accountId = in.readString();
        this.payeeBankNum = in.readString();
        this.mPlainData = in.readString();
        this.payeeAccountType = in.readString();
        this.conversationId = in.readString();
        this.randomNum = in.readString();
        this.factorModel = in.readParcelable(SecurityFactorModel.class.getClassLoader());
        this.prefactorList = new ArrayList<FactorListBean>();
        in.readList(this.prefactorList, FactorListBean.class.getClassLoader());
        this.selectedFactorId = in.readString();
        this.needPassword = in.readString();
        this.commisionCharge = in.readString();
        this.defaultFactorName = in.readString();
        this.currency = in.readString();
        this.totalBalance = (BigDecimal) in.readSerializable();
        this.checkStates = in.readByte() != 0;
        this.IsCredit = in.readByte() != 0;
    }

    public static final Creator<QrcodeTransModel> CREATOR = new Creator<QrcodeTransModel>() {
        @Override
        public QrcodeTransModel createFromParcel(Parcel source) {
            return new QrcodeTransModel(source);
        }

        @Override
        public QrcodeTransModel[] newArray(int size) {
            return new QrcodeTransModel[size];
        }
    };
}
