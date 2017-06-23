package com.boc.bocsoft.mobile.bii.bus.fess.model.PsnFessQueryHibsExchangeTransDetail;

/**
 * 4.19 019PsnFessQueryHibsExchangeTransDetail查询全渠道结购汇交易详情
 * Created by gwluo on 2016/11/18.
 * 需要与“PsnFessQueryHibsExchangeTrans 查询全渠道结购汇交易列表”接口共用同一个conversation
 */

public class PsnFessQueryHibsExchangeTransDetailResult {
    private String channel;//	交易渠道	String 	参见附录“渠道标识”
    private String trsChannelNum;//	渠道流水号	String
    private String bankSelfNum;//	银行自身交易流水号	String
    private String paymentDate;//	交易日期	String
    private String paymentTime;//	交易时间	String
    private String status;//	交易状态	String 	00成功
    private String furInfo;//	资金来源	String 	结售汇的资金属性，结汇时叫做资金来源，购汇叫做资金用途
    private String transType;//	结售汇交易类型	String 	01结汇 11购汇
    private String amount;// 外币交易金额	String
    private String currencyCode;//	外币币种	String
    private String cashRemit;//	钞汇	String
    private String exchangeRate;//	交易成交牌价(渠道优惠后牌价)	String
    private String referenceRate;//	优惠前核心基准牌价	String
    private String returnCnyAmt;//	人民币金额	String
    private String accountNumber;//	资金账号	String

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getTrsChannelNum() {
        return trsChannelNum;
    }

    public void setTrsChannelNum(String trsChannelNum) {
        this.trsChannelNum = trsChannelNum;
    }

    public String getBankSelfNum() {
        return bankSelfNum;
    }

    public void setBankSelfNum(String bankSelfNum) {
        this.bankSelfNum = bankSelfNum;
    }

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

    public String getReferenceRate() {
        return referenceRate;
    }

    public void setReferenceRate(String referenceRate) {
        this.referenceRate = referenceRate;
    }

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
}
