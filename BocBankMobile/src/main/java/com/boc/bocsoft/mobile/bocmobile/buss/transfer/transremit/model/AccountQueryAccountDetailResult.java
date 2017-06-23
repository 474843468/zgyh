package com.boc.bocsoft.mobile.bocmobile.buss.transfer.transremit.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.boc.bocsoft.mobile.bocmobile.base.model.FactorBean;
import com.boc.bocsoft.mobile.common.utils.BeanConvertor.anno.ListItemType;

import org.threeten.bp.LocalDate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 查询账户详情
 * Created by wangyuan on 2016/6/25.
 */
public class AccountQueryAccountDetailResult implements Parcelable {

    private String accOpenBank;
    private String accountType;
    private String accountStatus;
    private String accOpenDate;
    @ListItemType(instantiate = AccountDetaiListBean.class)
    private List<AccountDetaiListBean> accountDetaiList;

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

    public static class AccountDetaiListBean {
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
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.accOpenBank);
        dest.writeString(this.accountType);
        dest.writeString(this.accountStatus);
        dest.writeString(this.accOpenDate);
        dest.writeList(this.accountDetaiList);
    }

    public AccountQueryAccountDetailResult() {
    }

    protected AccountQueryAccountDetailResult(Parcel in) {
        this.accOpenBank = in.readString();
        this.accountType = in.readString();
        this.accountStatus = in.readString();
        this.accOpenDate = in.readString();
        this.accountDetaiList = new ArrayList<AccountDetaiListBean>();
        in.readList(this.accountDetaiList, AccountDetaiListBean.class.getClassLoader());
    }

    public static final Parcelable.Creator<AccountQueryAccountDetailResult> CREATOR = new Parcelable.Creator<AccountQueryAccountDetailResult>() {
        @Override
        public AccountQueryAccountDetailResult createFromParcel(Parcel source) {
            return new AccountQueryAccountDetailResult(source);
        }

        @Override
        public AccountQueryAccountDetailResult[] newArray(int size) {
            return new AccountQueryAccountDetailResult[size];
        }
    };
}
