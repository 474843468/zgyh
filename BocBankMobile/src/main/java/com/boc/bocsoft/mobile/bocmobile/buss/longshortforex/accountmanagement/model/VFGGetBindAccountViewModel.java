package com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.accountmanagement.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * ViewModel：双向宝-账户管理-双向宝交易账户
 * Created by zhx on 2016/11/29
 */
public class VFGGetBindAccountViewModel implements Parcelable {
    // 网银账户标识
    private String accountId;
    // 账户名称
    private String accountName;
    // 账号
    private String accountNumber;
    // 联行号
    private String accountIbkNum;
    // 账户类型
    private String accountType;
    // 所属银行机构标识
    private String branchId;
    // 账户别名
    private String nickName;
    // 账户状态
    private String accountStatus;
    // 使用客户标识
    private String customerId;
    // 货币码
    private String currencyCode;
    // 货币代码2 ->{目前双币信用卡使用}
    private String currencyCode2;
    // 所属网银机构名称
    private String branchName;
    // 卡类型描述
    private String cardDescription;
    // 是否有旧账
    private String hasOldAccountFlag;
    // 信用卡品种代码虚拟信用卡需要
    private String cardDescriptionCode;
    // 是否开通电子现金功能
    private String isECashAccount;

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public void setCardDescriptionCode(String cardDescriptionCode) {
        this.cardDescriptionCode = cardDescriptionCode;
    }

    public void setCardDescription(String cardDescription) {
        this.cardDescription = cardDescription;
    }

    public void setIsECashAccount(String isECashAccount) {
        this.isECashAccount = isECashAccount;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public void setBranchId(String branchId) {
        this.branchId = branchId;
    }

    public void setAccountStatus(String accountStatus) {
        this.accountStatus = accountStatus;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public void setAccountIbkNum(String accountIbkNum) {
        this.accountIbkNum = accountIbkNum;
    }

    public void setCurrencyCode2(String currencyCode2) {
        this.currencyCode2 = currencyCode2;
    }

    public void setHasOldAccountFlag(String hasOldAccountFlag) {
        this.hasOldAccountFlag = hasOldAccountFlag;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public String getAccountId() {
        return accountId;
    }

    public String getCardDescriptionCode() {
        return cardDescriptionCode;
    }

    public String getCardDescription() {
        return cardDescription;
    }

    public String getIsECashAccount() {
        return isECashAccount;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public String getAccountName() {
        return accountName;
    }

    public String getCustomerId() {
        return customerId;
    }

    public String getNickName() {
        return nickName;
    }

    public String getBranchId() {
        return branchId;
    }

    public String getAccountStatus() {
        return accountStatus;
    }

    public String getAccountType() {
        return accountType;
    }

    public String getBranchName() {
        return branchName;
    }

    public String getAccountIbkNum() {
        return accountIbkNum;
    }

    public String getCurrencyCode2() {
        return currencyCode2;
    }

    public String getHasOldAccountFlag() {
        return hasOldAccountFlag;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.accountId);
        dest.writeString(this.accountName);
        dest.writeString(this.accountNumber);
        dest.writeString(this.accountIbkNum);
        dest.writeString(this.accountType);
        dest.writeString(this.branchId);
        dest.writeString(this.nickName);
        dest.writeString(this.accountStatus);
        dest.writeString(this.customerId);
        dest.writeString(this.currencyCode);
        dest.writeString(this.currencyCode2);
        dest.writeString(this.branchName);
        dest.writeString(this.cardDescription);
        dest.writeString(this.hasOldAccountFlag);
        dest.writeString(this.cardDescriptionCode);
        dest.writeString(this.isECashAccount);
    }

    public VFGGetBindAccountViewModel() {
    }

    private VFGGetBindAccountViewModel(Parcel in) {
        this.accountId = in.readString();
        this.accountName = in.readString();
        this.accountNumber = in.readString();
        this.accountIbkNum = in.readString();
        this.accountType = in.readString();
        this.branchId = in.readString();
        this.nickName = in.readString();
        this.accountStatus = in.readString();
        this.customerId = in.readString();
        this.currencyCode = in.readString();
        this.currencyCode2 = in.readString();
        this.branchName = in.readString();
        this.cardDescription = in.readString();
        this.hasOldAccountFlag = in.readString();
        this.cardDescriptionCode = in.readString();
        this.isECashAccount = in.readString();
    }

    public static final Parcelable.Creator<VFGGetBindAccountViewModel> CREATOR = new Parcelable.Creator<VFGGetBindAccountViewModel>() {
        public VFGGetBindAccountViewModel createFromParcel(Parcel source) {
            return new VFGGetBindAccountViewModel(source);
        }

        public VFGGetBindAccountViewModel[] newArray(int size) {
            return new VFGGetBindAccountViewModel[size];
        }
    };
}