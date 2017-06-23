package com.boc.bocsoft.mobile.bocmobile.buss.creditcard.creditcardhomepage.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 信用卡信息列表
 * Created by liuweidong on 2016/12/1.
 */
public class CrcdInfoBean implements Parcelable {
    private String billAmout;// 本期账单金额
    private String billLimitAmout;// 本期最低还款金额
    private String cashBalance;// 取现可用额
    private String cashLimit;// 取现额度
    private String currency;// 币种
    private String dividedPayAvaiBalance;// 分期可用额
    private String dividedPayLimit;// 分期额度
    private String haveNotRepayAmout;// 本期未还款金额
    private String realTimeBalance;// 实时余额 - 该字段值为非负数，由rtBalanceFlag字段判断是存款还是欠款或者余额0
    private String rtBalanceFlag;// 实时余额标志位 - “0”-欠款 “1”-存款 “2”-余额0
    private String toltalBalance;// 整体可用额
    private String totalLimt;// 整体额度 - 该字段用于页面“信用卡额度”

    public String getBillAmout() {
        return billAmout;
    }

    public void setBillAmout(String billAmout) {
        this.billAmout = billAmout;
    }

    public String getBillLimitAmout() {
        return billLimitAmout;
    }

    public void setBillLimitAmout(String billLimitAmout) {
        this.billLimitAmout = billLimitAmout;
    }

    public String getCashBalance() {
        return cashBalance;
    }

    public void setCashBalance(String cashBalance) {
        this.cashBalance = cashBalance;
    }

    public String getCashLimit() {
        return cashLimit;
    }

    public void setCashLimit(String cashLimit) {
        this.cashLimit = cashLimit;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getDividedPayAvaiBalance() {
        return dividedPayAvaiBalance;
    }

    public void setDividedPayAvaiBalance(String dividedPayAvaiBalance) {
        this.dividedPayAvaiBalance = dividedPayAvaiBalance;
    }

    public String getDividedPayLimit() {
        return dividedPayLimit;
    }

    public void setDividedPayLimit(String dividedPayLimit) {
        this.dividedPayLimit = dividedPayLimit;
    }

    public String getHaveNotRepayAmout() {
        return haveNotRepayAmout;
    }

    public void setHaveNotRepayAmout(String haveNotRepayAmout) {
        this.haveNotRepayAmout = haveNotRepayAmout;
    }

    public String getRealTimeBalance() {
        return realTimeBalance;
    }

    public void setRealTimeBalance(String realTimeBalance) {
        this.realTimeBalance = realTimeBalance;
    }

    public String getRtBalanceFlag() {
        return rtBalanceFlag;
    }

    public void setRtBalanceFlag(String rtBalanceFlag) {
        this.rtBalanceFlag = rtBalanceFlag;
    }

    public String getToltalBalance() {
        return toltalBalance;
    }

    public void setToltalBalance(String toltalBalance) {
        this.toltalBalance = toltalBalance;
    }

    public String getTotalLimt() {
        return totalLimt;
    }

    public void setTotalLimt(String totalLimt) {
        this.totalLimt = totalLimt;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.billAmout);
        dest.writeString(this.billLimitAmout);
        dest.writeString(this.cashBalance);
        dest.writeString(this.cashLimit);
        dest.writeString(this.currency);
        dest.writeString(this.dividedPayAvaiBalance);
        dest.writeString(this.dividedPayLimit);
        dest.writeString(this.haveNotRepayAmout);
        dest.writeString(this.realTimeBalance);
        dest.writeString(this.rtBalanceFlag);
        dest.writeString(this.toltalBalance);
        dest.writeString(this.totalLimt);
    }

    public CrcdInfoBean() {
    }

    protected CrcdInfoBean(Parcel in) {
        this.billAmout = in.readString();
        this.billLimitAmout = in.readString();
        this.cashBalance = in.readString();
        this.cashLimit = in.readString();
        this.currency = in.readString();
        this.dividedPayAvaiBalance = in.readString();
        this.dividedPayLimit = in.readString();
        this.haveNotRepayAmout = in.readString();
        this.realTimeBalance = in.readString();
        this.rtBalanceFlag = in.readString();
        this.toltalBalance = in.readString();
        this.totalLimt = in.readString();
    }

    public static final Parcelable.Creator<CrcdInfoBean> CREATOR = new Parcelable.Creator<CrcdInfoBean>() {
        @Override
        public CrcdInfoBean createFromParcel(Parcel source) {
            return new CrcdInfoBean(source);
        }

        @Override
        public CrcdInfoBean[] newArray(int size) {
            return new CrcdInfoBean[size];
        }
    };
}
