package com.boc.bocsoft.mobile.bocmobile.buss.wealthmanagement.protocolpurchase.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 4.10 010周期性产品续约协议签约/签约初始化————响应
 * Created by wangf on 2016/11/9.
 */
public class PsnXpadSignInitBean implements Parcelable {

    private String productCode;//产品代码
    private String productName;//产品名称
    private String curCode;//币种
    private String remainCycleCount;//最大可购买期数
    private String serialName;//产品系列名称
    private String serialCode;//产品系列编号
    private String periodContinue;//周期是否连续
    private String accountId;//网银账户标识
    private String accountType;//账户类型(账户，卡，存折等)
    private String currency;//币种,返回的为国际化标识
    private String totalLimit;//总授信额度
    private String totalBalance;//总可用额
    private String cashLimit;//取现额度
    private String cashBalance;//取现可用额
    private String installmentLimit;//分期额度
    private String installmentBalance;//分期可用额
    private String currentBalance;//账面余额
    private String basePoint;//积分
    private String savingInterest;//存款利息
    private String savingInterestTax;//存款利息税
    private String showFlag;//是否显示存款利息和存款利息税(现只有借贷卡显示)


    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getCurCode() {
        return curCode;
    }

    public void setCurCode(String curCode) {
        this.curCode = curCode;
    }

    public String getRemainCycleCount() {
        return remainCycleCount;
    }

    public void setRemainCycleCount(String remainCycleCount) {
        this.remainCycleCount = remainCycleCount;
    }

    public String getSerialName() {
        return serialName;
    }

    public void setSerialName(String serialName) {
        this.serialName = serialName;
    }

    public String getSerialCode() {
        return serialCode;
    }

    public void setSerialCode(String serialCode) {
        this.serialCode = serialCode;
    }

    public String getPeriodContinue() {
        return periodContinue;
    }

    public void setPeriodContinue(String periodContinue) {
        this.periodContinue = periodContinue;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getTotalLimit() {
        return totalLimit;
    }

    public void setTotalLimit(String totalLimit) {
        this.totalLimit = totalLimit;
    }

    public String getTotalBalance() {
        return totalBalance;
    }

    public void setTotalBalance(String totalBalance) {
        this.totalBalance = totalBalance;
    }

    public String getCashLimit() {
        return cashLimit;
    }

    public void setCashLimit(String cashLimit) {
        this.cashLimit = cashLimit;
    }

    public String getCashBalance() {
        return cashBalance;
    }

    public void setCashBalance(String cashBalance) {
        this.cashBalance = cashBalance;
    }

    public String getInstallmentLimit() {
        return installmentLimit;
    }

    public void setInstallmentLimit(String installmentLimit) {
        this.installmentLimit = installmentLimit;
    }

    public String getInstallmentBalance() {
        return installmentBalance;
    }

    public void setInstallmentBalance(String installmentBalance) {
        this.installmentBalance = installmentBalance;
    }

    public String getCurrentBalance() {
        return currentBalance;
    }

    public void setCurrentBalance(String currentBalance) {
        this.currentBalance = currentBalance;
    }

    public String getBasePoint() {
        return basePoint;
    }

    public void setBasePoint(String basePoint) {
        this.basePoint = basePoint;
    }

    public String getSavingInterest() {
        return savingInterest;
    }

    public void setSavingInterest(String savingInterest) {
        this.savingInterest = savingInterest;
    }

    public String getSavingInterestTax() {
        return savingInterestTax;
    }

    public void setSavingInterestTax(String savingInterestTax) {
        this.savingInterestTax = savingInterestTax;
    }

    public String getShowFlag() {
        return showFlag;
    }

    public void setShowFlag(String showFlag) {
        this.showFlag = showFlag;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.productCode);
        dest.writeString(this.productName);
        dest.writeString(this.curCode);
        dest.writeString(this.remainCycleCount);
        dest.writeString(this.serialName);
        dest.writeString(this.serialCode);
        dest.writeString(this.periodContinue);
        dest.writeString(this.accountId);
        dest.writeString(this.accountType);
        dest.writeString(this.currency);
        dest.writeString(this.totalLimit);
        dest.writeString(this.totalBalance);
        dest.writeString(this.cashLimit);
        dest.writeString(this.cashBalance);
        dest.writeString(this.installmentLimit);
        dest.writeString(this.installmentBalance);
        dest.writeString(this.currentBalance);
        dest.writeString(this.basePoint);
        dest.writeString(this.savingInterest);
        dest.writeString(this.savingInterestTax);
        dest.writeString(this.showFlag);
    }

    public PsnXpadSignInitBean() {
    }

    protected PsnXpadSignInitBean(Parcel in) {
        this.productCode = in.readString();
        this.productName = in.readString();
        this.curCode = in.readString();
        this.remainCycleCount = in.readString();
        this.serialName = in.readString();
        this.serialCode = in.readString();
        this.periodContinue = in.readString();
        this.accountId = in.readString();
        this.accountType = in.readString();
        this.currency = in.readString();
        this.totalLimit = in.readString();
        this.totalBalance = in.readString();
        this.cashLimit = in.readString();
        this.cashBalance = in.readString();
        this.installmentLimit = in.readString();
        this.installmentBalance = in.readString();
        this.currentBalance = in.readString();
        this.basePoint = in.readString();
        this.savingInterest = in.readString();
        this.savingInterestTax = in.readString();
        this.showFlag = in.readString();
    }

    public static final Parcelable.Creator<PsnXpadSignInitBean> CREATOR = new Parcelable.Creator<PsnXpadSignInitBean>() {
        @Override
        public PsnXpadSignInitBean createFromParcel(Parcel source) {
            return new PsnXpadSignInitBean(source);
        }

        @Override
        public PsnXpadSignInitBean[] newArray(int size) {
            return new PsnXpadSignInitBean[size];
        }
    };
}
