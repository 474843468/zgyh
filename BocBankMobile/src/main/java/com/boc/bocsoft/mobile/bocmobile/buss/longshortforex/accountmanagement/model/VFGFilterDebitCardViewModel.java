package com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.accountmanagement.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * ViewModel：过滤出符合条件的借记卡账户
 * Created by zhx on 2016/12/8
 */
public class VFGFilterDebitCardViewModel implements Parcelable{
    private List<DebitCardEntity> cardList;

    public List<DebitCardEntity> getCardList() {
        return cardList;
    }

    public void setCardList(List<DebitCardEntity> cardList) {
        this.cardList = cardList;
    }

    public static class DebitCardEntity implements Parcelable {
        private String currencyCode;
        private String accountId;
        private String cardDescriptionCode;
        private String cardDescription;
        private String isECashAccount;
        // 账号
        private String accountNumber;
        private String accountName;
        private String customerId;
        // 别名
        private String nickName;
        private String branchId;
        private String accountStatus;
        // 类型
        private String accountType;
        private String branchName;
        private String accountIbkNum;
        private String currencyCode2;
        private String hasOldAccountFlag;

        public String getCurrencyCode() {
            return currencyCode;
        }

        public void setCurrencyCode(String currencyCode) {
            this.currencyCode = currencyCode;
        }

        public String getAccountId() {
            return accountId;
        }

        public void setAccountId(String accountId) {
            this.accountId = accountId;
        }

        public String getCardDescriptionCode() {
            return cardDescriptionCode;
        }

        public void setCardDescriptionCode(String cardDescriptionCode) {
            this.cardDescriptionCode = cardDescriptionCode;
        }

        public String getCardDescription() {
            return cardDescription;
        }

        public void setCardDescription(String cardDescription) {
            this.cardDescription = cardDescription;
        }

        public String getIsECashAccount() {
            return isECashAccount;
        }

        public void setIsECashAccount(String isECashAccount) {
            this.isECashAccount = isECashAccount;
        }

        public String getAccountNumber() {
            return accountNumber;
        }

        public void setAccountNumber(String accountNumber) {
            this.accountNumber = accountNumber;
        }

        public String getAccountName() {
            return accountName;
        }

        public void setAccountName(String accountName) {
            this.accountName = accountName;
        }

        public String getCustomerId() {
            return customerId;
        }

        public void setCustomerId(String customerId) {
            this.customerId = customerId;
        }

        public String getNickName() {
            return nickName;
        }

        public void setNickName(String nickName) {
            this.nickName = nickName;
        }

        public String getBranchId() {
            return branchId;
        }

        public void setBranchId(String branchId) {
            this.branchId = branchId;
        }

        public String getAccountStatus() {
            return accountStatus;
        }

        public void setAccountStatus(String accountStatus) {
            this.accountStatus = accountStatus;
        }

        public String getAccountType() {
            return accountType;
        }

        public void setAccountType(String accountType) {
            this.accountType = accountType;
        }

        public String getBranchName() {
            return branchName;
        }

        public void setBranchName(String branchName) {
            this.branchName = branchName;
        }

        public String getAccountIbkNum() {
            return accountIbkNum;
        }

        public void setAccountIbkNum(String accountIbkNum) {
            this.accountIbkNum = accountIbkNum;
        }

        public String getCurrencyCode2() {
            return currencyCode2;
        }

        public void setCurrencyCode2(String currencyCode2) {
            this.currencyCode2 = currencyCode2;
        }

        public String getHasOldAccountFlag() {
            return hasOldAccountFlag;
        }

        public void setHasOldAccountFlag(String hasOldAccountFlag) {
            this.hasOldAccountFlag = hasOldAccountFlag;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.currencyCode);
            dest.writeString(this.accountId);
            dest.writeString(this.cardDescriptionCode);
            dest.writeString(this.cardDescription);
            dest.writeString(this.isECashAccount);
            dest.writeString(this.accountNumber);
            dest.writeString(this.accountName);
            dest.writeString(this.customerId);
            dest.writeString(this.nickName);
            dest.writeString(this.branchId);
            dest.writeString(this.accountStatus);
            dest.writeString(this.accountType);
            dest.writeString(this.branchName);
            dest.writeString(this.accountIbkNum);
            dest.writeString(this.currencyCode2);
            dest.writeString(this.hasOldAccountFlag);
        }

        public DebitCardEntity() {
        }

        private DebitCardEntity(Parcel in) {
            this.currencyCode = in.readString();
            this.accountId = in.readString();
            this.cardDescriptionCode = in.readString();
            this.cardDescription = in.readString();
            this.isECashAccount = in.readString();
            this.accountNumber = in.readString();
            this.accountName = in.readString();
            this.customerId = in.readString();
            this.nickName = in.readString();
            this.branchId = in.readString();
            this.accountStatus = in.readString();
            this.accountType = in.readString();
            this.branchName = in.readString();
            this.accountIbkNum = in.readString();
            this.currencyCode2 = in.readString();
            this.hasOldAccountFlag = in.readString();
        }

        public static final Parcelable.Creator<DebitCardEntity> CREATOR = new Parcelable.Creator<DebitCardEntity>() {
            public DebitCardEntity createFromParcel(Parcel source) {
                return new DebitCardEntity(source);
            }

            public DebitCardEntity[] newArray(int size) {
                return new DebitCardEntity[size];
            }
        };
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(cardList);
    }

    public VFGFilterDebitCardViewModel() {
    }

    private VFGFilterDebitCardViewModel(Parcel in) {
        in.readTypedList(cardList, DebitCardEntity.CREATOR);
    }

    public static final Creator<VFGFilterDebitCardViewModel> CREATOR = new Creator<VFGFilterDebitCardViewModel>() {
        public VFGFilterDebitCardViewModel createFromParcel(Parcel source) {
            return new VFGFilterDebitCardViewModel(source);
        }

        public VFGFilterDebitCardViewModel[] newArray(int size) {
            return new VFGFilterDebitCardViewModel[size];
        }
    };
}
