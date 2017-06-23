package com.boc.bocsoft.mobile.bocmobile.buss.loan.common.facilityinquiry.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.widget.ImageButton;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by XieDu on 2016/7/12.
 */
public class FacilityUseRecordViewModel {

    private List<FacilityUsedRecBean> moneyUseRecordList;

    public List<FacilityUsedRecBean> getMoneyUseRecordList() {
        return moneyUseRecordList;
    }

    public void setMoneyUseRecordList(List<FacilityUsedRecBean> moneyUseRecordList) {
        this.moneyUseRecordList = moneyUseRecordList;
    }

    public static class FacilityUsedRecBean implements Parcelable{
        /**
         * currencyCode : CNY
         * accountNumber : 446862278649
         * loanToDate : 2017/10/02
         * loanType : 130003
         * interestType : B
         * loanAmount : 30000
         * loanPeriod : 24
         * loanPeriodUnit : M
         * noclosedInterest : null
         * overdueIssue : 3
         * payedIssueSum : 0
         * remainCapital : 10000
         * remainIssue : 23
         * thisIssueRepayAmount : 55.11
         * thisIssueRepayDate : 2016/01/21
         * thisIssueRepayInterest : 17.7778
         * payAccountNumber :
         * loanRate : 6.4
         * loanDate : 2015/10/02
         * overdueIssueSum : 2
         */

        private String currencyCode; //币种
        private String accountNumber; //贷款账号
        private String loanToDate;  //到期日
        private String loanType; //贷款品种
        private String interestType; //计息方式(还款方式)
        private BigDecimal loanAmount; //贷款金额
        private int loanPeriod; //期限
        private String loanPeriodUnit; //贷款期限单位
        private String noclosedInterest; //当前已计未结息
        private String overdueIssue; //逾期期数
        private String payedIssueSum; //已还期数
        private BigDecimal remainCapital; //剩余应还本金
        private String remainIssue; //贷款剩余期数
        private BigDecimal thisIssueRepayAmount; //本期应还款总额（本金+利息）
        private String thisIssueRepayDate; //本期还款日
        private BigDecimal thisIssueRepayInterest; //本期截止当前应还利息
        private String payAccountNumber; //还款账号
        private String loanRate; //贷款利率
        private String loanDate; //贷款放款日期
        private String overdueIssueSum; //累计逾期期数

        public String getCurrencyCode() {
            return currencyCode;
        }

        public void setCurrencyCode(String currencyCode) {
            this.currencyCode = currencyCode;
        }

        public String getAccountNumber() {
            return accountNumber;
        }

        public void setAccountNumber(String accountNumber) {
            this.accountNumber = accountNumber;
        }

        public String getLoanToDate() {
            return loanToDate;
        }

        public void setLoanToDate(String loanToDate) {
            this.loanToDate = loanToDate;
        }

        public String getLoanType() {
            return loanType;
        }

        public void setLoanType(String loanType) {
            this.loanType = loanType;
        }

        public String getInterestType() {
            return interestType;
        }

        public void setInterestType(String interestType) {
            this.interestType = interestType;
        }

        public BigDecimal getLoanAmount() {
            return loanAmount;
        }

        public void setLoanAmount(BigDecimal loanAmount) {
            this.loanAmount = loanAmount;
        }

        public int getLoanPeriod() {
            return loanPeriod;
        }

        public void setLoanPeriod(int loanPeriod) {
            this.loanPeriod = loanPeriod;
        }

        public String getLoanPeriodUnit() {
            return loanPeriodUnit;
        }

        public void setLoanPeriodUnit(String loanPeriodUnit) {
            this.loanPeriodUnit = loanPeriodUnit;
        }

        public String getNoclosedInterest() {
            return noclosedInterest;
        }

        public void setNoclosedInterest(String noclosedInterest) {
            this.noclosedInterest = noclosedInterest;
        }

        public String getOverdueIssue() {
            return overdueIssue;
        }

        public void setOverdueIssue(String overdueIssue) {
            this.overdueIssue = overdueIssue;
        }

        public String getPayedIssueSum() {
            return payedIssueSum;
        }

        public void setPayedIssueSum(String payedIssueSum) {
            this.payedIssueSum = payedIssueSum;
        }

        public BigDecimal getRemainCapital() {
            return remainCapital;
        }

        public void setRemainCapital(BigDecimal remainCapital) {
            this.remainCapital = remainCapital;
        }

        public String getRemainIssue() {
            return remainIssue;
        }

        public void setRemainIssue(String remainIssue) {
            this.remainIssue = remainIssue;
        }

        public BigDecimal getThisIssueRepayAmount() {
            return thisIssueRepayAmount;
        }

        public void setThisIssueRepayAmount(BigDecimal thisIssueRepayAmount) {
            this.thisIssueRepayAmount = thisIssueRepayAmount;
        }

        public String getThisIssueRepayDate() {
            return thisIssueRepayDate;
        }

        public void setThisIssueRepayDate(String thisIssueRepayDate) {
            this.thisIssueRepayDate = thisIssueRepayDate;
        }

        public BigDecimal getThisIssueRepayInterest() {
            return thisIssueRepayInterest;
        }

        public void setThisIssueRepayInterest(BigDecimal thisIssueRepayInterest) {
            this.thisIssueRepayInterest = thisIssueRepayInterest;
        }

        public String getPayAccountNumber() {
            return payAccountNumber;
        }

        public void setPayAccountNumber(String payAccountNumber) {
            this.payAccountNumber = payAccountNumber;
        }

        public String getLoanRate() {
            return loanRate;
        }

        public void setLoanRate(String loanRate) {
            this.loanRate = loanRate;
        }

        public String getLoanDate() {
            return loanDate;
        }

        public void setLoanDate(String loanDate) {
            this.loanDate = loanDate;
        }

        public String getOverdueIssueSum() {
            return overdueIssueSum;
        }

        public void setOverdueIssueSum(String overdueIssueSum) {
            this.overdueIssueSum = overdueIssueSum;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {

            dest.writeString(currencyCode);
            dest.writeString(accountNumber);
            dest.writeString(loanToDate);
            dest.writeString(loanType);
            dest.writeString(interestType);
            dest.writeSerializable(loanAmount);
            dest.writeInt(loanPeriod);
            dest.writeString(loanPeriodUnit);
            dest.writeString(noclosedInterest);
            dest.writeString(overdueIssue);
            dest.writeString(payedIssueSum);
            dest.writeSerializable(remainCapital);
            dest.writeString(remainIssue);
            dest.writeSerializable(thisIssueRepayAmount);
            dest.writeString(thisIssueRepayDate);
            dest.writeSerializable(thisIssueRepayInterest);
            dest.writeString(payAccountNumber);
            dest.writeString(loanRate);
            dest.writeString(loanDate);
            dest.writeString(overdueIssueSum);
        }

        public FacilityUsedRecBean(){
            super();
        }

        public FacilityUsedRecBean(Parcel in){
            currencyCode = in.readString();
            accountNumber = in.readString();
            loanToDate = in.readString();
            loanType = in.readString();
            interestType = in.readString();
            loanAmount = (BigDecimal) in.readSerializable();
            loanPeriod = in.readInt();
            loanPeriodUnit = in.readString();
            noclosedInterest = in.readString();
            overdueIssue = in.readString();
            payedIssueSum = in.readString();
            remainCapital = (BigDecimal)in.readSerializable();
            remainIssue = in.readString();
            thisIssueRepayAmount = (BigDecimal)in.readSerializable();
            thisIssueRepayDate = in.readString();
            thisIssueRepayInterest = (BigDecimal)in.readSerializable();
            payAccountNumber = in.readString();
            loanRate = in.readString();
            loanDate = in.readString();
            overdueIssueSum = in.readString();
        }

        public static Parcelable.Creator<FacilityUsedRecBean> CREATOR = new Parcelable.Creator<FacilityUsedRecBean>(){
            @Override
            public FacilityUsedRecBean createFromParcel(Parcel source) {
                return new FacilityUsedRecBean(source);
            }

            @Override
            public FacilityUsedRecBean[] newArray(int size) {
                return new FacilityUsedRecBean[size];
            }
        };

        @Override
        public String toString() {
            return "FacilityUsedRecBean{" +
                    "currencyCode='" + currencyCode + '\'' +
                    ", accountNumber='" + accountNumber + '\'' +
                    ", loanToDate=" + loanToDate +
                    ", loanType='" + loanType + '\'' +
                    ", interestType='" + interestType + '\'' +
                    ", loanAmount=" + loanAmount +
                    ", loanPeriod=" + loanPeriod +
                    ", loanPeriodUnit='" + loanPeriodUnit + '\'' +
                    ", noclosedInterest='" + noclosedInterest + '\'' +
                    ", overdueIssue=" + overdueIssue +
                    ", payedIssueSum=" + payedIssueSum +
                    ", remainCapital=" + remainCapital +
                    ", remainIssue=" + remainIssue +
                    ", thisIssueRepayAmount=" + thisIssueRepayAmount +
                    ", thisIssueRepayDate=" + thisIssueRepayDate +
                    ", thisIssueRepayInterest=" + thisIssueRepayInterest +
                    ", payAccountNumber='" + payAccountNumber + '\'' +
                    ", loanRate=" + loanRate +
                    ", loanDate=" + loanDate +
                    ", overdueIssueSum=" + overdueIssueSum +
                    '}';
        }
    }
}
