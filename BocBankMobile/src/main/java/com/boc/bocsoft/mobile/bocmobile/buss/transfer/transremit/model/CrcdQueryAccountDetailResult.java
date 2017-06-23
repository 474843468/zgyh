package com.boc.bocsoft.mobile.bocmobile.buss.transfer.transremit.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.boc.bocsoft.mobile.common.utils.BeanConvertor.anno.ListItemType;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangyuan on 2016/6/25.
 */
public class CrcdQueryAccountDetailResult implements Parcelable {
    /**
     * 消费积分
     */
    private String consumptionPoint;
    @ListItemType(instantiate = CrcdAccountDetailListBean.class)
    private List<CrcdAccountDetailListBean> crcdAccountDetailList;

    public String getConsumptionPoint() {
        return consumptionPoint;
    }

    public void setConsumptionPoint(String consumptionPoint) {
        this.consumptionPoint = consumptionPoint;
    }

    public List<CrcdAccountDetailListBean> getCrcdAccountDetailList() {
        return crcdAccountDetailList;
    }

    public void setCrcdAccountDetailList(List<CrcdAccountDetailListBean> crcdAccountDetailList) {
        this.crcdAccountDetailList = crcdAccountDetailList;
    }

    public static class CrcdAccountDetailListBean {
        private BigDecimal totalLimit;
        private BigDecimal totalBalance;
        private BigDecimal cashLimit;
        private BigDecimal cashBalance;
        private BigDecimal installmentLimit;
        private BigDecimal installmentBalance;
        private BigDecimal currentBalance;
        private BigDecimal savingInterest;
        private BigDecimal savingInterestTax;
        private BigDecimal loanBalanceLimit;
        private String currency;
        private String currentBalanceflag;
        private String loanBalanceLimitFlag;

        public BigDecimal getTotalLimit() {
            return totalLimit;
        }

        public void setTotalLimit(BigDecimal totalLimit) {
            this.totalLimit = totalLimit;
        }

        public BigDecimal getTotalBalance() {
            return totalBalance;
        }

        public void setTotalBalance(BigDecimal totalBalance) {
            this.totalBalance = totalBalance;
        }

        public BigDecimal getCashLimit() {
            return cashLimit;
        }

        public void setCashLimit(BigDecimal cashLimit) {
            this.cashLimit = cashLimit;
        }

        public BigDecimal getCashBalance() {
            return cashBalance;
        }

        public void setCashBalance(BigDecimal cashBalance) {
            this.cashBalance = cashBalance;
        }

        public BigDecimal getInstallmentLimit() {
            return installmentLimit;
        }

        public void setInstallmentLimit(BigDecimal installmentLimit) {
            this.installmentLimit = installmentLimit;
        }

        public BigDecimal getInstallmentBalance() {
            return installmentBalance;
        }

        public void setInstallmentBalance(BigDecimal installmentBalance) {
            this.installmentBalance = installmentBalance;
        }

        public BigDecimal getCurrentBalance() {
            return currentBalance;
        }

        public void setCurrentBalance(BigDecimal currentBalance) {
            this.currentBalance = currentBalance;
        }

        public BigDecimal getSavingInterest() {
            return savingInterest;
        }

        public void setSavingInterest(BigDecimal savingInterest) {
            this.savingInterest = savingInterest;
        }

        public BigDecimal getSavingInterestTax() {
            return savingInterestTax;
        }

        public void setSavingInterestTax(BigDecimal savingInterestTax) {
            this.savingInterestTax = savingInterestTax;
        }

        public BigDecimal getLoanBalanceLimit() {
            return loanBalanceLimit;
        }

        public void setLoanBalanceLimit(BigDecimal loanBalanceLimit) {
            this.loanBalanceLimit = loanBalanceLimit;
        }

        public String getCurrency() {
            return currency;
        }

        public void setCurrency(String currency) {
            this.currency = currency;
        }

        public String getCurrentBalanceflag() {
            return currentBalanceflag;
        }

        public void setCurrentBalanceflag(String currentBalanceflag) {
            this.currentBalanceflag = currentBalanceflag;
        }

        public String getLoanBalanceLimitFlag() {
            return loanBalanceLimitFlag;
        }

        public void setLoanBalanceLimitFlag(String loanBalanceLimitFlag) {
            this.loanBalanceLimitFlag = loanBalanceLimitFlag;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.consumptionPoint);
        dest.writeList(this.crcdAccountDetailList);
    }

    public CrcdQueryAccountDetailResult() {
    }

    protected CrcdQueryAccountDetailResult(Parcel in) {
        this.consumptionPoint = in.readString();
        this.crcdAccountDetailList = new ArrayList<CrcdAccountDetailListBean>();
        in.readList(this.crcdAccountDetailList, CrcdAccountDetailListBean.class.getClassLoader());
    }

    public static final Parcelable.Creator<CrcdQueryAccountDetailResult> CREATOR = new Parcelable.Creator<CrcdQueryAccountDetailResult>() {
        @Override
        public CrcdQueryAccountDetailResult createFromParcel(Parcel source) {
            return new CrcdQueryAccountDetailResult(source);
        }

        @Override
        public CrcdQueryAccountDetailResult[] newArray(int size) {
            return new CrcdQueryAccountDetailResult[size];
        }
    };
}
