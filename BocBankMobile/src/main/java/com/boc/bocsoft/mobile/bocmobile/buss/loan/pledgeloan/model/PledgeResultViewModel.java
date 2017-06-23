package com.boc.bocsoft.mobile.bocmobile.buss.loan.pledgeloan.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 作者：XieDu
 * 创建时间：2016/8/19 13:59
 * 描述：
 */
public class PledgeResultViewModel implements Parcelable {

    private String conversationId;
    /**
     * 网银交易流水号
     */
    private String transId;
    private String currencyCode;

    /**
     * 本次用款金额
     */
    private String amount;
    private String loanPeriod;
    private String loanRate;
    /**
     * 收款账户
     */
    private String toActNum;

    /**
     * 还款账户
     */
    private String payActNum;

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    public String getTransId() {
        return transId;
    }

    public void setTransId(String transId) {
        this.transId = transId;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getToActNum() {
        return toActNum;
    }

    public void setToActNum(String toActNum) {
        this.toActNum = toActNum;
    }

    public String getPayActNum() {
        return payActNum;
    }

    public void setPayActNum(String payActNum) {
        this.payActNum = payActNum;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public String getLoanPeriod() {
        return loanPeriod;
    }

    public void setLoanPeriod(String loanPeriod) {
        this.loanPeriod = loanPeriod;
    }

    public String getLoanRate() {
        return loanRate;
    }

    public void setLoanRate(String loanRate) {
        this.loanRate = loanRate;
    }

    @Override
    public int describeContents() { return 0; }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.conversationId);
        dest.writeString(this.transId);
        dest.writeString(this.currencyCode);
        dest.writeString(this.amount);
        dest.writeString(this.loanPeriod);
        dest.writeString(this.loanRate);
        dest.writeString(this.toActNum);
        dest.writeString(this.payActNum);
    }

    public PledgeResultViewModel() {}

    protected PledgeResultViewModel(Parcel in) {
        this.conversationId = in.readString();
        this.transId = in.readString();
        this.currencyCode = in.readString();
        this.amount = in.readString();
        this.loanPeriod = in.readString();
        this.loanRate = in.readString();
        this.toActNum = in.readString();
        this.payActNum = in.readString();
    }

    public static final Creator<PledgeResultViewModel> CREATOR =
            new Creator<PledgeResultViewModel>() {
                @Override
                public PledgeResultViewModel createFromParcel(
                        Parcel source) {return new PledgeResultViewModel(source);}

                @Override
                public PledgeResultViewModel[] newArray(
                        int size) {return new PledgeResultViewModel[size];}
            };
}
