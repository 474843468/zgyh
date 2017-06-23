package com.boc.bocsoft.mobile.bocmobile.buss.longshortforex.transactiondetails.model;

import org.threeten.bp.LocalDateTime;

/**
 * Created by zc7067 on 2016/11/17.
 *
 * PsnVFGCancelOrder 委托撤单
 */
public class XpadPsnVFGCancelOrderModel {
    /**
     * "token": "d9q1oapm",
     * "currencyCode": "001",
     * "consignNumber": "111",
     * "currencycode1": "001",
     * "currencycode2": "002",
     * "direction": "B",
     * "openPositionFlag": "1",
     * "exchangeTranType": "2",
     * "amount": "100",
     * "paymentDate": "2015/01/01",
     * "dueDate": "2015/01/01"
     * conversationId:true
     */
    //防重标志
    private String token;
    //结算币种
    private String currencyCode;
    //委托序号
    private String consignNumber;
    //货币对
    private String currencycode1;
    //货币对
    private String currencycode2;
    //买卖方向 :B - 买入,S - 卖出
    private String direction;
    //建仓标识
    private String openPositionFlag;
    //委托类型
    private String exchangeTranType;
    //交易金额
    private String amount;
    //委托时间
    private String paymentDate;
    //失效时间
    private String dueDate;
    //会话
    private String conversationId;

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getAmount() {
        return amount;
    }

    public void setConsignNumber(String consignNumber) {
        this.consignNumber = consignNumber;
    }

    public String getConsignNumber() {
        return consignNumber;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencycode1(String currencycode1) {
        this.currencycode1 = currencycode1;
    }

    public String getCurrencycode1() {
        return currencycode1;
    }

    public void setCurrencycode2(String currencycode2) {
        this.currencycode2 = currencycode2;
    }

    public String getCurrencycode2() {
        return currencycode2;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public String getDirection() {
        return direction;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setExchangeTranType(String exchangeTranType) {
        this.exchangeTranType = exchangeTranType;
    }

    public String getExchangeTranType() {
        return exchangeTranType;
    }

    public void setOpenPositionFlag(String openPositionFlag) {
        this.openPositionFlag = openPositionFlag;
    }

    public String getOpenPositionFlag() {
        return openPositionFlag;
    }

    public void setPaymentDate(String paymentDate) {
        this.paymentDate = paymentDate;
    }

    public String getPaymentDate() {
        return paymentDate;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    public String getConversationId() {
        return conversationId;
    }

    //响应字段
    /**
     *  "transactionId": 691772041,
     *  "exchangeTransDate": "2020/11/01 02:11:22"
     */
    //网银交易序号
    private String transactionId;
    //撤单时间
    private LocalDateTime exchangeTransDate;

    public void setExchangeTransDate(LocalDateTime exchangeTransDate) {
        this.exchangeTransDate = exchangeTransDate;
    }

    public LocalDateTime getExchangeTransDate() {
        return exchangeTransDate;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getTransactionId() {
        return transactionId;
    }

}
