package com.boc.bocsoft.mobile.bocmobile.buss.loan.common.facilityinquiry.model;

import android.os.Parcel;
import android.os.Parcelable;
import java.math.BigDecimal;
import java.util.List;
import org.threeten.bp.LocalDate;

/**
 * Created by XieDu on 2016/7/12.
 */
public class FacilityInquiryViewModel {
    /**
     * 额度查询列表
     */
    private List<FacilityInquiryBean> facilityInquiryList;

    public List<FacilityInquiryBean> getFacilityInquiryList() {
        return facilityInquiryList;
    }

    public void setFacilityInquiryList(List<FacilityInquiryBean> facilityInquiryList) {
        this.facilityInquiryList = facilityInquiryList;
    }

    public static class FacilityInquiryBean implements Parcelable {

        private String currencyCode;
        /**
         * 额度金额
         */
        private BigDecimal quota;
        /**
         * 到期日
         */
        private LocalDate loanToDate;
        /**
         * 贷款品种
         */
        private String loanType;
        /**
         * 额度状态
         * 05:正常
         * 10：取消
         * 20：冻结
         * 40：关闭
         */
        private String quotaStatus;
        /**
         * 额度号码
         */
        private String quotaNumber;
        /**
         * 已用额度
         */
        private BigDecimal quotaUsed;
        /**
         * 可用额度
         */
        private BigDecimal availableQuota;

        public String getCurrencyCode() {
            return currencyCode;
        }

        public void setCurrencyCode(String currencyCode) {
            this.currencyCode = currencyCode;
        }

        public BigDecimal getQuota() {
            return quota;
        }

        public void setQuota(BigDecimal quota) {
            this.quota = quota;
        }

        public LocalDate getLoanToDate() {
            return loanToDate;
        }

        public void setLoanToDate(LocalDate loanToDate) {
            this.loanToDate = loanToDate;
        }

        public String getLoanType() {
            return loanType;
        }

        public void setLoanType(String loanType) {
            this.loanType = loanType;
        }

        public String getQuotaStatus() {
            return quotaStatus;
        }

        public void setQuotaStatus(String quotaStatus) {
            this.quotaStatus = quotaStatus;
        }

        public String getQuotaNumber() {
            return quotaNumber;
        }

        public void setQuotaNumber(String quotaNumber) {
            this.quotaNumber = quotaNumber;
        }

        public BigDecimal getQuotaUsed() {
            return quotaUsed;
        }

        public void setQuotaUsed(BigDecimal quotaUsed) {
            this.quotaUsed = quotaUsed;
        }

        public BigDecimal getAvailableQuota() {
            return availableQuota;
        }

        public void setAvailableQuota(BigDecimal availableQuota) {
            this.availableQuota = availableQuota;
        }

        @Override
        public String toString() {
            return "FacilityInquiryBean{" +
                    "currencyCode='" + currencyCode + '\'' +
                    ", quota=" + quota +
                    ", loanToDate=" + loanToDate +
                    ", loanType='" + loanType + '\'' +
                    ", quotaStatus='" + quotaStatus + '\'' +
                    ", quotaNumber='" + quotaNumber + '\'' +
                    ", quotaUsed=" + quotaUsed +
                    ", availableQuota=" + availableQuota +
                    '}';
        }

        @Override
        public int describeContents() { return 0; }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.currencyCode);
            dest.writeSerializable(this.quota);
            dest.writeSerializable(this.loanToDate);
            dest.writeString(this.loanType);
            dest.writeString(this.quotaStatus);
            dest.writeString(this.quotaNumber);
            dest.writeSerializable(this.quotaUsed);
            dest.writeSerializable(this.availableQuota);
        }

        public FacilityInquiryBean() {}

        protected FacilityInquiryBean(Parcel in) {
            this.currencyCode = in.readString();
            this.quota = (BigDecimal) in.readSerializable();
            this.loanToDate = (LocalDate) in.readSerializable();
            this.loanType = in.readString();
            this.quotaStatus = in.readString();
            this.quotaNumber = in.readString();
            this.quotaUsed = (BigDecimal) in.readSerializable();
            this.availableQuota = (BigDecimal) in.readSerializable();
        }

        public static final Parcelable.Creator<FacilityInquiryBean> CREATOR =
                new Parcelable.Creator<FacilityInquiryBean>() {
                    @Override
                    public FacilityInquiryBean createFromParcel(
                            Parcel source) {return new FacilityInquiryBean(source);}

                    @Override
                    public FacilityInquiryBean[] newArray(
                            int size) {return new FacilityInquiryBean[size];}
                };
    }
}
