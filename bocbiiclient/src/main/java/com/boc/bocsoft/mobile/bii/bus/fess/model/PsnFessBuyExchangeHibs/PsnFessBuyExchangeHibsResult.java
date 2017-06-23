package com.boc.bocsoft.mobile.bii.bus.fess.model.PsnFessBuyExchangeHibs;

/**
 * I49 4.16 016PsnFessBuyExchangeHibs	购汇(HIBS新)
 * Created by wzn7074 on 2016/11/16.
 */
public class PsnFessBuyExchangeHibsResult {

    /**
     * cashRemit : 01
     * exchangeRate : 70.0010010
     * returnCnyAmt : 13456
     * currency : 001
     * bankSelfNum : 78887999
     * transStatus : A
     * transactionId : 7888799999
     */
    private String cashRemit;              //#钞汇
    private String exchangeRate;       //#核心成交汇率牌价（渠道优惠后牌价）
    private String returnCnyAmt;       //#折合人民币金额
    private String currency;                 //#币种
    private String bankSelfNum;        //#银行自身交易流水号
    /**
     * A 交易成功
     * B 交易失败
     * K 银行处理中
     */
    private String transStatus;             //#交易状态
    private String referenceRate;        //优惠前核心基准牌价

    private String transactionId;         //#交易流水号

    public void setCashRemit(String cashRemit) {
        this.cashRemit = cashRemit;
    }

    public void setExchangeRate(String exchangeRate) {
        this.exchangeRate = exchangeRate;
    }

    public void setReturnCnyAmt(String returnCnyAmt) {
        this.returnCnyAmt = returnCnyAmt;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public void setBankSelfNum(String bankSelfNum) {
        this.bankSelfNum = bankSelfNum;
    }

    public void setTransStatus(String transStatus) {
        this.transStatus = transStatus;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public void setReferenceRate(String referenceRate) {
        this.referenceRate = referenceRate;
    }

    public String getCashRemit() {
        return cashRemit;
    }

    public String getExchangeRate() {
        return exchangeRate;
    }

    public String getReturnCnyAmt() {
        return returnCnyAmt;
    }

    public String getCurrency() {
        return currency;
    }

    public String getBankSelfNum() {
        return bankSelfNum;
    }

    public String getTransStatus() {
        return transStatus;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public String getReferenceRate() {
        return referenceRate;
    }
}
