package com.boc.bocsoft.mobile.bocmobile.buss.transfer.payment.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.math.BigDecimal;

/**
 * Created by wangtong on 2016/6/30.
 */
public class ConfirmPaymentModel implements Parcelable {

    //支付账户余额
    private BigDecimal remainAmount;
    //支付账户货币单位
    private String remainCurrency;
    //支付详情
    private PaymentDetailModel detail;

    private String accountType;

    private String accountId;
    private boolean queryStates;

    private String warning;

    private String warnType;

    private String loanBalanceLimitFlag;

    public String getLoanBalanceLimitFlag() {
        return loanBalanceLimitFlag;
    }

    public void setLoanBalanceLimitFlag(String loanBalanceLimitFlag) {
        this.loanBalanceLimitFlag = loanBalanceLimitFlag;
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

    public String getRemainCurrency() {
        return remainCurrency;
    }

    public void setRemainCurrency(String remainCurrency) {
        this.remainCurrency = remainCurrency;
    }

    public boolean isQueryStates() {
        return queryStates;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public void setQueryStates(boolean queryStates) {
        this.queryStates = queryStates;


    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public PaymentDetailModel getDetail() {
        return detail;
    }

    public void setDetail(PaymentDetailModel detail) {
        this.detail = detail;
    }

    public BigDecimal getRemainAmount() {
        return remainAmount;
    }

    public void setRemainAmount(BigDecimal remainAmount) {
        this.remainAmount = remainAmount;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeSerializable(this.remainAmount);
        dest.writeString(this.remainCurrency);
        dest.writeParcelable(this.detail, flags);
        dest.writeString(this.accountId);
        dest.writeByte(queryStates ? (byte) 1 : (byte) 0);
    }

    public ConfirmPaymentModel() {
    }

    protected ConfirmPaymentModel(Parcel in) {
        this.remainAmount = (BigDecimal) in.readSerializable();
        this.remainCurrency = in.readString();
        this.detail = in.readParcelable(PaymentDetailModel.class.getClassLoader());
        this.accountId = in.readString();
        this.queryStates = in.readByte() != 0;
    }

    public static final Creator<ConfirmPaymentModel> CREATOR = new Creator<ConfirmPaymentModel>() {
        @Override
        public ConfirmPaymentModel createFromParcel(Parcel source) {
            return new ConfirmPaymentModel(source);
        }

        @Override
        public ConfirmPaymentModel[] newArray(int size) {
            return new ConfirmPaymentModel[size];
        }
    };
}
