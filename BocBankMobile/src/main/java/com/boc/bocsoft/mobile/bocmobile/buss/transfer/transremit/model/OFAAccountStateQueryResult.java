package com.boc.bocsoft.mobile.bocmobile.buss.transfer.transremit.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.boc.bocsoft.mobile.common.utils.BeanConvertor.anno.ConvertIfDiff;

/**
 * Created by WYme on 2016/9/12.
 */
public class OFAAccountStateQueryResult implements Parcelable {

    /**
     * accountId : 29475139
     * currencyCode2 : null
     * cardDescription : null
     * hasOldAccountFlag : null
     * accountName : 付瑜
     * accountNumber : 6013***********0499
     * accountIbkNum : 47669
     * accountType : 119
     * branchId : null
     * nickName : 长城电子借记卡
     * accountStatus : null
     * customerId : null
     * currencyCode : null
     * branchName : null
     * cardDescriptionCode : null
     * isECashAccount : null
     * isMedicalAccount : null
     * ecard : null
     * verifyFactor : null
     * accountKey : null
     */
    @ConvertIfDiff
    private BankAccountBean bankAccount;
    /**
     * bankAccount : {"accountId":29475139,"currencyCode2":null,"cardDescription":null,"hasOldAccountFlag":null,"accountName":"付瑜","accountNumber":"6013***********0499","accountIbkNum":"47669","accountType":"119","branchId":null,"nickName":"长城电子借记卡","accountStatus":null,"customerId":null,"currencyCode":null,"branchName":null,"cardDescriptionCode":null,"isECashAccount":null,"isMedicalAccount":null,"ecard":null,"verifyFactor":null,"accountKey":null}
     * openStatus : S
     * mainAccount : {"accountId":null,"currencyCode2":null,"cardDescription":null,"hasOldAccountFlag":null,"accountName":null,"accountNumber":"7627****8303","accountIbkNum":"47669","accountType":"188","branchId":null,"nickName":"","accountStatus":null,"customerId":null,"currencyCode":null,"branchName":null,"cardDescriptionCode":null,"isECashAccount":null,"isMedicalAccount":null,"ecard":null,"verifyFactor":null,"accountKey":null}
     * financialAccount : {"accountId":117945814,"currencyCode2":null,"cardDescription":null,"hasOldAccountFlag":null,"accountName":"付瑜","accountNumber":"7744****5663","accountIbkNum":"47669","accountType":"190","branchId":null,"nickName":"网上专属理财账户","accountStatus":null,"customerId":null,"currencyCode":null,"branchName":null,"cardDescriptionCode":null,"isECashAccount":null,"isMedicalAccount":null,"ecard":null,"verifyFactor":null,"accountKey":null}
     */

    private String openStatus;
    /**
     * accountId : null
     * currencyCode2 : null
     * cardDescription : null
     * hasOldAccountFlag : null
     * accountName : null
     * accountNumber : 7627****8303
     * accountIbkNum : 47669
     * accountType : 188
     * branchId : null
     * nickName :
     * accountStatus : null
     * customerId : null
     * currencyCode : null
     * branchName : null
     * cardDescriptionCode : null
     * isECashAccount : null
     * isMedicalAccount : null
     * ecard : null
     * verifyFactor : null
     * accountKey : null
     */
    @ConvertIfDiff
    private MainAccountBean mainAccount;
    /**
     * accountId : 117945814
     * currencyCode2 : null
     * cardDescription : null
     * hasOldAccountFlag : null
     * accountName : 付瑜
     * accountNumber : 7744****5663
     * accountIbkNum : 47669
     * accountType : 190
     * branchId : null
     * nickName : 网上专属理财账户
     * accountStatus : null
     * customerId : null
     * currencyCode : null
     * branchName : null
     * cardDescriptionCode : null
     * isECashAccount : null
     * isMedicalAccount : null
     * ecard : null
     * verifyFactor : null
     * accountKey : null
     */
    @ConvertIfDiff
    private FinancialAccountBean financialAccount;

    public BankAccountBean getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(BankAccountBean bankAccount) {
        this.bankAccount = bankAccount;
    }

    public String getOpenStatus() {
        return openStatus;
    }

    public void setOpenStatus(String openStatus) {
        this.openStatus = openStatus;
    }

    public MainAccountBean getMainAccount() {
        return mainAccount;
    }

    public void setMainAccount(MainAccountBean mainAccount) {
        this.mainAccount = mainAccount;
    }

    public FinancialAccountBean getFinancialAccount() {
        return financialAccount;
    }

    public void setFinancialAccount(FinancialAccountBean financialAccount) {
        this.financialAccount = financialAccount;
    }

    public static class BankAccountBean implements Parcelable{
        private int accountId;
        private String currencyCode2;
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

        public int getAccountId() {
            return accountId;
        }

        public void setAccountId(int accountId) {
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

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.accountId);
            dest.writeString(this.currencyCode2);
            dest.writeString(this.cardDescription);
            dest.writeString(this.hasOldAccountFlag);
            dest.writeString(this.accountName);
            dest.writeString(this.accountNumber);
            dest.writeString(this.accountIbkNum);
            dest.writeString(this.accountType);
            dest.writeString(this.branchId);
            dest.writeString(this.nickName);
            dest.writeString(this.accountStatus);
            dest.writeString(this.customerId);
            dest.writeString(this.currencyCode);
            dest.writeString(this.branchName);
            dest.writeString(this.cardDescriptionCode);
            dest.writeString(this.isECashAccount);
            dest.writeString(this.isMedicalAccount);
            dest.writeString(this.ecard);
            dest.writeString(this.verifyFactor);
            dest.writeString(this.accountKey);
        }

        public BankAccountBean() {
        }

        protected BankAccountBean(Parcel in) {
            this.accountId = in.readInt();
            this.currencyCode2 = in.readString();
            this.cardDescription = in.readString();
            this.hasOldAccountFlag = in.readString();
            this.accountName = in.readString();
            this.accountNumber = in.readString();
            this.accountIbkNum = in.readString();
            this.accountType = in.readString();
            this.branchId = in.readString();
            this.nickName = in.readString();
            this.accountStatus = in.readString();
            this.customerId = in.readString();
            this.currencyCode = in.readString();
            this.branchName = in.readString();
            this.cardDescriptionCode = in.readString();
            this.isECashAccount = in.readString();
            this.isMedicalAccount = in.readString();
            this.ecard = in.readString();
            this.verifyFactor = in.readString();
            this.accountKey = in.readString();
        }

        public static final Creator<BankAccountBean> CREATOR = new Creator<BankAccountBean>() {
            @Override
            public BankAccountBean createFromParcel(Parcel source) {
                return new BankAccountBean(source);
            }

            @Override
            public BankAccountBean[] newArray(int size) {
                return new BankAccountBean[size];
            }
        };
    }

    public static class MainAccountBean implements Parcelable{
        private String accountId;
        private String currencyCode2;
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

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.accountId);
            dest.writeString(this.currencyCode2);
            dest.writeString(this.cardDescription);
            dest.writeString(this.hasOldAccountFlag);
            dest.writeString(this.accountName);
            dest.writeString(this.accountNumber);
            dest.writeString(this.accountIbkNum);
            dest.writeString(this.accountType);
            dest.writeString(this.branchId);
            dest.writeString(this.nickName);
            dest.writeString(this.accountStatus);
            dest.writeString(this.customerId);
            dest.writeString(this.currencyCode);
            dest.writeString(this.branchName);
            dest.writeString(this.cardDescriptionCode);
            dest.writeString(this.isECashAccount);
            dest.writeString(this.isMedicalAccount);
            dest.writeString(this.ecard);
            dest.writeString(this.verifyFactor);
            dest.writeString(this.accountKey);
        }

        public MainAccountBean() {
        }

        protected MainAccountBean(Parcel in) {
            this.accountId = in.readString();
            this.currencyCode2 = in.readString();
            this.cardDescription = in.readString();
            this.hasOldAccountFlag = in.readString();
            this.accountName = in.readString();
            this.accountNumber = in.readString();
            this.accountIbkNum = in.readString();
            this.accountType = in.readString();
            this.branchId = in.readString();
            this.nickName = in.readString();
            this.accountStatus = in.readString();
            this.customerId = in.readString();
            this.currencyCode = in.readString();
            this.branchName = in.readString();
            this.cardDescriptionCode = in.readString();
            this.isECashAccount = in.readString();
            this.isMedicalAccount = in.readString();
            this.ecard = in.readString();
            this.verifyFactor = in.readString();
            this.accountKey = in.readString();
        }

        public static final Creator<MainAccountBean> CREATOR = new Creator<MainAccountBean>() {
            @Override
            public MainAccountBean createFromParcel(Parcel source) {
                return new MainAccountBean(source);
            }

            @Override
            public MainAccountBean[] newArray(int size) {
                return new MainAccountBean[size];
            }
        };
    }

    public static class FinancialAccountBean implements Parcelable{
        private int accountId;
        private String currencyCode2;
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

        public int getAccountId() {
            return accountId;
        }

        public void setAccountId(int accountId) {
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

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.accountId);
            dest.writeString(this.currencyCode2);
            dest.writeString(this.cardDescription);
            dest.writeString(this.hasOldAccountFlag);
            dest.writeString(this.accountName);
            dest.writeString(this.accountNumber);
            dest.writeString(this.accountIbkNum);
            dest.writeString(this.accountType);
            dest.writeString(this.branchId);
            dest.writeString(this.nickName);
            dest.writeString(this.accountStatus);
            dest.writeString(this.customerId);
            dest.writeString(this.currencyCode);
            dest.writeString(this.branchName);
            dest.writeString(this.cardDescriptionCode);
            dest.writeString(this.isECashAccount);
            dest.writeString(this.isMedicalAccount);
            dest.writeString(this.ecard);
            dest.writeString(this.verifyFactor);
            dest.writeString(this.accountKey);
        }

        public FinancialAccountBean() {
        }

        protected FinancialAccountBean(Parcel in) {
            this.accountId = in.readInt();
            this.currencyCode2 = in.readString();
            this.cardDescription = in.readString();
            this.hasOldAccountFlag = in.readString();
            this.accountName = in.readString();
            this.accountNumber = in.readString();
            this.accountIbkNum = in.readString();
            this.accountType = in.readString();
            this.branchId = in.readString();
            this.nickName = in.readString();
            this.accountStatus = in.readString();
            this.customerId = in.readString();
            this.currencyCode = in.readString();
            this.branchName = in.readString();
            this.cardDescriptionCode = in.readString();
            this.isECashAccount = in.readString();
            this.isMedicalAccount = in.readString();
            this.ecard = in.readString();
            this.verifyFactor = in.readString();
            this.accountKey = in.readString();
        }

        public static final Creator<FinancialAccountBean> CREATOR = new Creator<FinancialAccountBean>() {
            @Override
            public FinancialAccountBean createFromParcel(Parcel source) {
                return new FinancialAccountBean(source);
            }

            @Override
            public FinancialAccountBean[] newArray(int size) {
                return new FinancialAccountBean[size];
            }
        };
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.bankAccount, flags);
        dest.writeString(this.openStatus);
        dest.writeParcelable(this.mainAccount, flags);
        dest.writeParcelable(this.financialAccount, flags);
    }

    public OFAAccountStateQueryResult() {
    }

    protected OFAAccountStateQueryResult(Parcel in) {
        this.bankAccount = in.readParcelable(BankAccountBean.class.getClassLoader());
        this.openStatus = in.readString();
        this.mainAccount = in.readParcelable(MainAccountBean.class.getClassLoader());
        this.financialAccount = in.readParcelable(FinancialAccountBean.class.getClassLoader());
    }

    public static final Creator<OFAAccountStateQueryResult> CREATOR = new Creator<OFAAccountStateQueryResult>() {
        @Override
        public OFAAccountStateQueryResult createFromParcel(Parcel source) {
            return new OFAAccountStateQueryResult(source);
        }

        @Override
        public OFAAccountStateQueryResult[] newArray(int size) {
            return new OFAAccountStateQueryResult[size];
        }
    };
}
