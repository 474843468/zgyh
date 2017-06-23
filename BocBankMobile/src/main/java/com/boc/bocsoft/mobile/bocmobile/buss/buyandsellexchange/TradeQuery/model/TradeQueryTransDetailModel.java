package com.boc.bocsoft.mobile.bocmobile.buss.buyandsellexchange.TradeQuery.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 交易详情查询数据模型
 * Created by wzn7074 on 2016/11/28.
 */
public class TradeQueryTransDetailModel implements Parcelable {


    //会话
    /**
     * bankSelfNum:"00096411"
     * paymentDate:"2021/12/21"
     * refNum:"G1100000001012016122000000048"
     */
    private String conversationId; //会话Id

    private String refNum;//业务参号

    private String tranRetCode;//交易结果返回码

    public String getRefNum() {
        return refNum;
    }

    public void setRefNum(String refNum) {
        this.refNum = refNum;
    }

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    public String getTranRetCode() {
        return tranRetCode;
    }

    public void setTranRetCode(String tranRetCode) {
        this.tranRetCode = tranRetCode;
    }
    /**
     * amount : 12342.00
     * channel : 01
     * trsChannelNum : 121162024039002
     * accountNumber : 4563***********4788
     * cashRemit : 02
     * transType : 11
     * exchangeRate : 6.707400
     * returnCnyAmt : 827.83
     * furInfo : 321
     * bankSelfNum : 00096411
     * paymentDate : 2016/12/20
     * paymentTime : 16:20:25
     * currencyCode : 027
     * status : 00
     */
    private String amount; //外币交易金额
    private String channel;//交易渠道
    private String trsChannelNum;//渠道流水号
    private String accountNumber;//资金账号
    private String cashRemit;//钞汇
    private String transType;//结售汇交易类型
    private String exchangeRate;//交易成交牌价（渠道优惠后牌价）
    private String returnCnyAmt;//人民币金额
    private String furInfo;//资金来源

    private String bankSelfNum;//银行自身交易流水号
    private String paymentDate;//交易日期

    private String paymentTime;//交易时间
    private String currencyCode;//外币币种
    private String status;//交易状态

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public void setTrsChannelNum(String trsChannelNum) {
        this.trsChannelNum = trsChannelNum;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public void setCashRemit(String cashRemit) {
        this.cashRemit = cashRemit;
    }

    public void setTransType(String transType) {
        this.transType = transType;
    }

    public void setExchangeRate(String exchangeRate) {
        this.exchangeRate = exchangeRate;
    }

    public void setReturnCnyAmt(String returnCnyAmt) {
        this.returnCnyAmt = returnCnyAmt;
    }

    public void setFurInfo(String furInfo) {
        this.furInfo = furInfo;
    }

    public void setBankSelfNum(String bankSelfNum) {
        this.bankSelfNum = bankSelfNum;
    }

    public void setPaymentDate(String paymentDate) {
        this.paymentDate = paymentDate;
    }

    public void setPaymentTime(String paymentTime) {
        this.paymentTime = paymentTime;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAmount() {
        return amount;
    }

    public String getChannel() {
        return channel;
    }

    public String getTrsChannelNum() {
        return trsChannelNum;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public String getCashRemit() {
        return cashRemit;
    }

    public String getTransType() {
        return transType;
    }

    public String getExchangeRate() {
        return exchangeRate;
    }

    public String getReturnCnyAmt() {
        return returnCnyAmt;
    }

    public String getFurInfo() {
        return furInfo;
    }

    public String getBankSelfNum() {
        return bankSelfNum;
    }

    public String getPaymentDate() {
        return paymentDate;
    }

    public String getPaymentTime() {
        return paymentTime;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public String getStatus() {
        return status;
    }



//    private String currencyCode;//	外币币种	String
//    private String cashRemit;//	钞汇	String
//    private String amount;// 外币交易金额	String
//    private String status;//	交易状态	String 	00成功
//    private String exchangeRate;//	交易成交牌价(渠道优惠后牌价)	String
//    private String returnCnyAmt;//	人民币金额	String
//    private String transType;//	结售汇交易类型	String 	01结汇 11购汇
//    private String accountNumber;//	资金账号	String
//    private String paymentDate;//	交易日期	String
//    private String paymentTime;//	交易时间	String
//    private String furInfo;//	资金来源	String 	结售汇的资金属性，结汇时叫做资金来源，购汇叫做资金用途
//    private String channel;//	交易渠道	String 	参见附录“渠道标识”
////
//////    private String trsChannelNum;//	渠道流水号	String
//////    private String bankSelfNum;//	银行自身交易流水号	String
//////    private String referenceRate;//	优惠前核心基准牌价	String
////
////    public String getConversationId() {
////        return conversationId;
////    }
////
////    public void setConversationId(String conversationId) {
////        this.conversationId = conversationId;
////    }
////
////    public String getChannel() {
////        return channel;
////    }
////
////    public void setChannel(String channel) {
////        this.channel = channel;
////    }
////
//////    public String getTrsChannelNum() {
//////        return trsChannelNum;
//////    }
//////
//////    public void setTrsChannelNum(String trsChannelNum) {
//////        this.trsChannelNum = trsChannelNum;
//////    }
//////
//////    public String getBankSelfNum() {
//////        return bankSelfNum;
//////    }
//////
//////    public void setBankSelfNum(String bankSelfNum) {
//////        this.bankSelfNum = bankSelfNum;
//////    }
////
////    public String getPaymentDate() {
////        return paymentDate;
////    }
////
////    public void setPaymentDate(String paymentDate) {
////        this.paymentDate = paymentDate;
////    }
////
////    public String getPaymentTime() {
////        return paymentTime;
////    }
////
////    public void setPaymentTime(String paymentTime) {
////        this.paymentTime = paymentTime;
////    }
////
////    public String getStatus() {
////        return status;
////    }
////
////    public void setStatus(String status) {
////        this.status = status;
////    }
////
////    public String getFurInfo() {
////        return furInfo;
////    }
////
////    public void setFurInfo(String furInfo) {
////        this.furInfo = furInfo;
////    }
////
////    public String getTransType() {
////        return transType;
////    }
////
////    public void setTransType(String transType) {
////        this.transType = transType;
////    }
////
////    public String getAmount() {
////        return amount;
////    }
////
////    public void setAmount(String amount) {
////        this.amount = amount;
////    }
////
////    public String getCurrencyCode() {
////        return currencyCode;
////    }
////
////    public void setCurrencyCode(String currencyCode) {
////        this.currencyCode = currencyCode;
////    }
////
////    public String getCashRemit() {
////        return cashRemit;
////    }
////
////    public void setCashRemit(String cashRemit) {
////        this.cashRemit = cashRemit;
////    }
////
////    public String getExchangeRate() {
////        return exchangeRate;
////    }
////
////    public void setExchangeRate(String exchangeRate) {
////        this.exchangeRate = exchangeRate;
////    }
////
//////    public String getReferenceRate() {
//////        return referenceRate;
//////    }
//////
//////    public void setReferenceRate(String referenceRate) {
//////        this.referenceRate = referenceRate;
//////    }
////
////    public String getReturnCnyAmt() {
////        return returnCnyAmt;
////    }
////
////    public void setReturnCnyAmt(String returnCnyAmt) {
////        this.returnCnyAmt = returnCnyAmt;
////    }
////
////    public String getAccountNumber() {
////        return accountNumber;
////    }
////
////    public void setAccountNumber(String accountNumber) {
////        this.accountNumber = accountNumber;
////    }
////
////    @Override
////    public int describeContents() {
////        return 0;
////    }
////
////    @Override
////    public void writeToParcel(Parcel dest, int flags) {
////        dest.writeString(this.conversationId);
////        dest.writeString(this.currencyCode);
////        dest.writeString(this.cashRemit);
////        dest.writeString(this.amount);
////        dest.writeString(this.status);
////        dest.writeString(this.exchangeRate);
////        dest.writeString(this.returnCnyAmt);
////        dest.writeString(this.transType);
////        dest.writeString(this.accountNumber);
////        dest.writeString(this.paymentDate);
////        dest.writeString(this.paymentTime);
////        dest.writeString(this.furInfo);
////        dest.writeString(this.channel);
////    }
////
////    public TradeQueryTransDetailModel() {
////    }
////
////    private TradeQueryTransDetailModel(Parcel in) {
////        this.conversationId = in.readString();
////        this.currencyCode = in.readString();
////        this.cashRemit = in.readString();
////        this.amount = in.readString();
////        this.status = in.readString();
////        this.exchangeRate = in.readString();
////        this.returnCnyAmt = in.readString();
////        this.transType = in.readString();
////        this.accountNumber = in.readString();
////        this.paymentDate = in.readString();
////        this.paymentTime = in.readString();
////        this.furInfo = in.readString();
////        this.channel = in.readString();
////    }
////
////    public static final Creator<TradeQueryTransDetailModel> CREATOR = new Creator<TradeQueryTransDetailModel>() {
////        public TradeQueryTransDetailModel createFromParcel(Parcel source) {
////            return new TradeQueryTransDetailModel(source);
////        }
////
////        public TradeQueryTransDetailModel[] newArray(int size) {
////            return new TradeQueryTransDetailModel[size];
////        }
////    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.conversationId);
        dest.writeString(this.amount);
        dest.writeString(this.channel);
        dest.writeString(this.trsChannelNum);
        dest.writeString(this.accountNumber);
        dest.writeString(this.cashRemit);
        dest.writeString(this.transType);
        dest.writeString(this.exchangeRate);
        dest.writeString(this.returnCnyAmt);
        dest.writeString(this.furInfo);
        dest.writeString(this.bankSelfNum);
        dest.writeString(this.paymentDate);
        dest.writeString(this.paymentTime);
        dest.writeString(this.currencyCode);
        dest.writeString(this.status);
    }

    public TradeQueryTransDetailModel() {
    }

    private TradeQueryTransDetailModel(Parcel in) {
        this.conversationId = in.readString();
        this.amount = in.readString();
        this.channel = in.readString();
        this.trsChannelNum = in.readString();
        this.accountNumber = in.readString();
        this.cashRemit = in.readString();
        this.transType = in.readString();
        this.exchangeRate = in.readString();
        this.returnCnyAmt = in.readString();
        this.furInfo = in.readString();
        this.bankSelfNum = in.readString();
        this.paymentDate = in.readString();
        this.paymentTime = in.readString();
        this.currencyCode = in.readString();
        this.status = in.readString();
    }

    public static final Creator<TradeQueryTransDetailModel> CREATOR = new Creator<TradeQueryTransDetailModel>() {
        public TradeQueryTransDetailModel createFromParcel(Parcel source) {
            return new TradeQueryTransDetailModel(source);
        }

        public TradeQueryTransDetailModel[] newArray(int size) {
            return new TradeQueryTransDetailModel[size];
        }
    };
}


