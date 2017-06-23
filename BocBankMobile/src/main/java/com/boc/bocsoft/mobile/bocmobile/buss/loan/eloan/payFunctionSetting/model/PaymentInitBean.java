package com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.payFunctionSetting.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 作者：XieDu
 * 创建时间：2016/9/14 17:29
 * 描述：
 */
public class PaymentInitBean implements Parcelable {
    /**
     * 额度编号
     */
    private String quoteNo;
    /**
     * 贷款账号
     */
    private String loanNo;
    /**
     * 额度/账户签约标识
     * 取值范围：额度为F 账户为A
     */
    private String quoteFlag;
    /**
     * 签约类型
     * 01：WLCF
     * 02：PLCF
     * 03: 账户签约
     */
    private String quoteType;
    /**
     * 还款账户
     */
    private String payAccount;

    private String rate;

    private PaymentInfo paymentInfo;

    public String getQuoteNo() {
        return quoteNo;
    }

    public void setQuoteNo(String quoteNo) {
        this.quoteNo = quoteNo;
    }

    public String getLoanNo() {
        return loanNo;
    }

    public void setLoanNo(String loanNo) {
        this.loanNo = loanNo;
    }

    public String getQuoteFlag() {
        return quoteFlag;
    }

    public void setQuoteFlag(String quoteFlag) {
        this.quoteFlag = quoteFlag;
    }

    public String getQuoteType() {
        return quoteType;
    }

    public String getQuoteTypeCode() {
        if ("01".equals(quoteType)) {
            return "WLCF";
        }
        if ("02".equals(quoteType)) {
            return "PLCF";
        }
        if ("03".equals(quoteType)) {
            return "PLFB";
        }
        return quoteType;
    }

    public void setQuoteType(String quoteType) {
        this.quoteType = quoteType;
    }

    public String getPayAccount() {
        return payAccount;
    }

    public void setPayAccount(String payAccount) {
        this.payAccount = payAccount;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public PaymentInfo getPaymentInfo() {
        return paymentInfo;
    }

    public void setPaymentInfo(PaymentInfo paymentInfo) {
        this.paymentInfo = paymentInfo;
    }

    @Override
    public int describeContents() { return 0; }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.quoteNo);
        dest.writeString(this.loanNo);
        dest.writeString(this.quoteFlag);
        dest.writeString(this.quoteType);
        dest.writeString(this.payAccount);
        dest.writeString(this.rate);
        dest.writeParcelable(this.paymentInfo, flags);
    }

    public PaymentInitBean() {}

    protected PaymentInitBean(Parcel in) {
        this.quoteNo = in.readString();
        this.loanNo = in.readString();
        this.quoteFlag = in.readString();
        this.quoteType = in.readString();
        this.payAccount = in.readString();
        this.rate = in.readString();
        this.paymentInfo = in.readParcelable(PaymentInfo.class.getClassLoader());
    }

    public static final Creator<PaymentInitBean> CREATOR = new Creator<PaymentInitBean>() {
        @Override
        public PaymentInitBean createFromParcel(Parcel source) {return new PaymentInitBean(source);}

        @Override
        public PaymentInitBean[] newArray(int size) {return new PaymentInitBean[size];}
    };
}
