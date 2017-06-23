package com.boc.bocsoft.mobile.bocmobile.buss.buyandsellexchange.TradeQuery.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by wzn7074 on 2016/12/14.
 */
public class TradeQueryTransDetailBean implements Parcelable {

/**
 * 交易查询数据模型
 * Created by wzn7074 on 2016/11/28.
 */
    private String conversationId; //会话Id

    private String currencyCode;//	外币币种	String
    private String cashRemit;//	钞汇	String
    private String amount;// 外币交易金额	String
    private String status;//	交易状态	String 	00成功
    private String exchangeRate;//	交易成交牌价(渠道优惠后牌价)	String
    private String returnCnyAmt;//	人民币金额	String
    private String transType;//	结售汇交易类型	String 	01结汇 11购汇
    private String accountNumber;//	资金账号	String
    private String paymentDate;//	交易日期	String
    private String paymentTime;//	交易时间	String
    private String furInfo;//	资金来源	String 	结售汇的资金属性，结汇时叫做资金来源，购汇叫做资金用途
    private String channel;//	交易渠道	String 	参见附录“渠道标识”

//    private String trsChannelNum;//	渠道流水号	String
//    private String bankSelfNum;//	银行自身交易流水号	String
//    private String referenceRate;//	优惠前核心基准牌价	String

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

//    public String getTrsChannelNum() {
//        return trsChannelNum;
//    }
//
//    public void setTrsChannelNum(String trsChannelNum) {
//        this.trsChannelNum = trsChannelNum;
//    }
//
//    public String getBankSelfNum() {
//        return bankSelfNum;
//    }
//
//    public void setBankSelfNum(String bankSelfNum) {
//        this.bankSelfNum = bankSelfNum;
//    }

    public String getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(String paymentDate) {
        this.paymentDate = paymentDate;
    }

    public String getPaymentTime() {
        return paymentTime;
    }

    public void setPaymentTime(String paymentTime) {
        this.paymentTime = paymentTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getFurInfo() {
        return furInfo;
    }

    public void setFurInfo(String furInfo) {
        this.furInfo = furInfo;
    }

    public String getTransType() {
        return transType;
    }

    public void setTransType(String transType) {
        this.transType = transType;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

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

    public String getExchangeRate() {
        return exchangeRate;
    }

    public void setExchangeRate(String exchangeRate) {
        this.exchangeRate = exchangeRate;
    }

//    public String getReferenceRate() {
//        return referenceRate;
//    }
//
//    public void setReferenceRate(String referenceRate) {
//        this.referenceRate = referenceRate;
//    }

    public String getReturnCnyAmt() {
        return returnCnyAmt;
    }

    public void setReturnCnyAmt(String returnCnyAmt) {
        this.returnCnyAmt = returnCnyAmt;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.conversationId);
        dest.writeString(this.currencyCode);
        dest.writeString(this.cashRemit);
        dest.writeString(this.amount);
        dest.writeString(this.status);
        dest.writeString(this.exchangeRate);
        dest.writeString(this.returnCnyAmt);
        dest.writeString(this.transType);
        dest.writeString(this.accountNumber);
        dest.writeString(this.paymentDate);
        dest.writeString(this.paymentTime);
        dest.writeString(this.furInfo);
        dest.writeString(this.channel);
    }

    public TradeQueryTransDetailBean() {
    }

    private TradeQueryTransDetailBean(Parcel in) {
        this.conversationId = in.readString();
        this.currencyCode = in.readString();
        this.cashRemit = in.readString();
        this.amount = in.readString();
        this.status = in.readString();
        this.exchangeRate = in.readString();
        this.returnCnyAmt = in.readString();
        this.transType = in.readString();
        this.accountNumber = in.readString();
        this.paymentDate = in.readString();
        this.paymentTime = in.readString();
        this.furInfo = in.readString();
        this.channel = in.readString();
    }

    public static final Creator<TradeQueryTransDetailBean> CREATOR = new Creator<TradeQueryTransDetailBean>() {
        public TradeQueryTransDetailBean createFromParcel(Parcel source) {
            return new TradeQueryTransDetailBean(source);
        }

        public TradeQueryTransDetailBean[] newArray(int size) {
            return new TradeQueryTransDetailBean[size];
        }
    };
}

