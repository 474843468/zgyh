package com.boc.bocsoft.mobile.bocmobile.buss.account.transdetail.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.threeten.bp.LocalDate;

import java.math.BigDecimal;
import java.util.List;

/**
 * 查询账户详情View层数据模型
 * Created by niuguobin on 2016/6/6.
 */
public class AccountDetailViewModel implements Parcelable{
    public AccountDetailViewModel(){

    }
    private int position;
    private String accOpenBank;
    private String accountType;
    private String accountStatus;
    private String accOpenDate;
    /**
     * currencyCode : 001
     * cashRemit : 00
     * bookBalance : 88918.05
     * availableBalance : 88918.05
     * volumeNumber : null
     * type : null
     * interestRate : null
     * status : 00
     * monthBalance : null
     * cdNumber : null
     * cdPeriod : null
     * openDate : 2025/02/10
     * interestStartsDate : null
     * interestEndDate : null
     * settlementDate : null
     * convertType : null
     * pingNo : null
     * holdAmount : null
     * appointStatus : null
     */

    private List<AccountDetaiListBean> accountDetaiList;

    public AccountDetailViewModel(Parcel in) {
        position = in.readInt();
        accOpenBank = in.readString();
        accountType = in.readString();
        accountStatus = in.readString();
        accOpenDate = in.readString();
        accountDetaiList = in.createTypedArrayList(AccountDetaiListBean.CREATOR);
    }

    public static final Creator<AccountDetailViewModel> CREATOR = new Creator<AccountDetailViewModel>() {
        @Override
        public AccountDetailViewModel createFromParcel(Parcel in) {
            return new AccountDetailViewModel(in);
        }

        @Override
        public AccountDetailViewModel[] newArray(int size) {
            return new AccountDetailViewModel[size];
        }
    };

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(position);
        dest.writeString(accOpenBank);
        dest.writeString(accountType);
        dest.writeString(accountStatus);
        dest.writeString(accOpenDate);
        dest.writeArray(accountDetaiList.toArray());
    }

    public static class AccountDetaiListBean implements Parcelable {
        public AccountDetaiListBean(){

        }
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

        public AccountDetaiListBean(Parcel in) {
            currencyCode = in.readString();
            cashRemit = in.readString();
            volumeNumber = in.readString();
            type = in.readString();
            interestRate = in.readString();
            status = in.readString();
            cdNumber = in.readString();
            cdPeriod = in.readString();
            convertType = in.readString();
            pingNo = in.readString();
            holdAmount = in.readString();
            appointStatus = in.readString();
        }

        public static final Creator<AccountDetaiListBean> CREATOR = new Creator<AccountDetaiListBean>() {
            @Override
            public AccountDetaiListBean createFromParcel(Parcel in) {
                return new AccountDetaiListBean(in);
            }

            @Override
            public AccountDetaiListBean[] newArray(int size) {
                return new AccountDetaiListBean[size];
            }
        };

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

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(currencyCode);
            dest.writeString(cashRemit);
            dest.writeString(String.valueOf(bookBalance));
            dest.writeString(String.valueOf(availableBalance));
            dest.writeString(volumeNumber);
            dest.writeString(type);
            dest.writeString(interestRate);
            dest.writeString(status);
            dest.writeString(String.valueOf(monthBalance));
            dest.writeString(cdNumber);
            dest.writeString(cdPeriod);
            dest.writeString(String.valueOf(openDate));
            dest.writeString(String.valueOf(interestStartsDate));
            dest.writeString(String.valueOf(interestEndDate));
            dest.writeString(String.valueOf(settlementDate));
            dest.writeString(convertType);
            dest.writeString(pingNo);
            dest.writeString(holdAmount);
            dest.writeString(appointStatus);
        }
    }
}
