package com.boc.bocsoft.mobile.bocmobile.buss.fund.fundsexchange.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by taoyongzhen on 2016/12/14.
 */

public class FundConversionConfirmModel implements Parcelable {
    //上报参数


    /**
     * fromFundCode : 220020
     * toFundCode : 220020
     * feeType : 1
     * amount : 1
     * sellFlag : 0
     * token : 123456
     */
    private String conversationId;
    private String fromFundName;
    private String toFundName;
    private String fromFundCode;
    private String toFundCode;
    private String feeType;
    private String amount;
    private String token;
    private String sellFlag;
    private String fromFundCurrency;

    public String getFromFundCurrency() {
        return fromFundCurrency;
    }

    public void setFromFundCurrency(String fromFundCurrency) {
        this.fromFundCurrency = fromFundCurrency;
    }

    public String getFromFundName() {
        return fromFundName;
    }

    public void setFromFundName(String fromFundName) {
        this.fromFundName = fromFundName;
    }

    public String getToFundName() {
        return toFundName;
    }

    public void setToFundName(String toFundName) {
        this.toFundName = toFundName;
    }

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    public String getFromFundCode() {
        return fromFundCode;
    }

    public void setFromFundCode(String fromFundCode) {
        this.fromFundCode = fromFundCode;
    }

    public String getToFundCode() {
        return toFundCode;
    }

    public void setToFundCode(String toFundCode) {
        this.toFundCode = toFundCode;
    }

    public String getFeeType() {
        return feeType;
    }

    public void setFeeType(String feeType) {
        this.feeType = feeType;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getSellFlag() {
        return sellFlag;
    }

    public void setSellFlag(String sellFlag) {
        this.sellFlag = sellFlag;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    //返回结果

    /**
     * fundCode :
     * fundName :
     * canBuy : false
     * tranState : 1
     * transactionId
     * fundSeq
     * feeType
     * isMatchEval
     * affirmFlag
     */

    private String fundCode;
    private String fundName;
    private String canBuy;
    private String tranState;

    private String transactionId;
    private String fundSeq;
    private String isMatchEval;
    private String affirmFlag;

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getFundSeq() {
        return fundSeq;
    }

    public void setFundSeq(String fundSeq) {
        this.fundSeq = fundSeq;
    }

    public String getIsMatchEval() {
        return isMatchEval;
    }

    public void setIsMatchEval(String isMatchEval) {
        this.isMatchEval = isMatchEval;
    }

    public String getAffirmFlag() {
        return affirmFlag;
    }

    public void setAffirmFlag(String affirmFlag) {
        this.affirmFlag = affirmFlag;
    }

    public String getFundCode() {
        return fundCode;
    }

    public void setFundCode(String fundCode) {
        this.fundCode = fundCode;
    }

    public String getFundName() {
        return fundName;
    }

    public void setFundName(String fundName) {
        this.fundName = fundName;
    }

    public String getCanBuy() {
        return canBuy;
    }

    public void setCanBuy(String canBuy) {
        this.canBuy = canBuy;
    }

    public String getTranState() {
        return tranState;
    }

    public void setTranState(String tranState) {
        this.tranState = tranState;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.fromFundCode);
        dest.writeString(this.toFundCode);
        dest.writeString(this.feeType);
        dest.writeString(this.amount);
        dest.writeString(this.sellFlag);
        dest.writeString(this.token);
        dest.writeString(this.fundCode);
        dest.writeString(this.fundName);
        dest.writeString(this.canBuy);
        dest.writeString(this.tranState);
        dest.writeString(this.transactionId);
        dest.writeString(this.fundSeq);
        dest.writeString(this.feeType);
        dest.writeString(this.isMatchEval);
        dest.writeString(this.affirmFlag);
        dest.writeString(this.fromFundName);
        dest.writeString(this.toFundName);
        dest.writeString(this.fromFundCurrency);
    }

    public FundConversionConfirmModel() {
    }

    protected FundConversionConfirmModel(Parcel in) {
        this.fromFundCode = in.readString();
        this.toFundCode = in.readString();
        this.feeType = in.readString();
        this.amount = in.readString();
        this.sellFlag = in.readString();
        this.token = in.readString();
        this.fundCode = in.readString();
        this.fundName = in.readString();
        this.canBuy = in.readString();
        this.tranState = in.readString();
        this.transactionId = in.readString();
        this.fundSeq = in.readString();
        this.feeType = in.readString();
        this.isMatchEval = in.readString();
        this.affirmFlag = in.readString();
        this.fromFundName = in.readString();
        this.toFundName = in.readString();
        this.fromFundCurrency = in.readString();
    }

    public static final Parcelable.Creator<FundConversionConfirmModel> CREATOR = new Parcelable.Creator<FundConversionConfirmModel>() {
        @Override
        public FundConversionConfirmModel createFromParcel(Parcel source) {
            return new FundConversionConfirmModel(source);
        }

        @Override
        public FundConversionConfirmModel[] newArray(int size) {
            return new FundConversionConfirmModel[size];
        }
    };
}
