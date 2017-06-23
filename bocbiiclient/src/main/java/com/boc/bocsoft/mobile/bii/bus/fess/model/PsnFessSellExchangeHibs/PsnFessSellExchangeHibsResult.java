package com.boc.bocsoft.mobile.bii.bus.fess.model.PsnFessSellExchangeHibs;

/**4.17 017PsnFessSellExchangeHibs	结汇（HIBS新)
 * Created by gwluo on 2016/11/18.
 * 需要与“PsnFessQueryAccount查询结售汇账户列表”接口使用同一conversation
 */

public class PsnFessSellExchangeHibsResult {
    private String bankSelfNum;//	银行自身交易流水号	String
    private String currency;//	币种	String
    private String cashRemit;//	钞汇	String
    private String exchangeRate;//	核心成交汇率牌价（渠道优惠后牌价）	String
    private String referenceRate;//	优惠前核心基准牌价	String
    private String returnCnyAmt;//	折合人民币金额	String
    private String transStatus;//	交易状态	String	A 交易成功 B 交易失败 K 银行处理中
    private String transactionId;//	交易流水号	String

    public String getBankSelfNum() {
        return bankSelfNum;
    }

    public void setBankSelfNum(String bankSelfNum) {
        this.bankSelfNum = bankSelfNum;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
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

    public String getTransStatus() {
        return transStatus;
    }

    public void setTransStatus(String transStatus) {
        this.transStatus = transStatus;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }
}
