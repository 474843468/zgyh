package com.boc.bocsoft.mobile.bocmobile.buss.qrcodepay.collection.model;

/**
 * Created by fanbin on 16/9/29.
 */
public class QRPayGetPayeeResultModel {
    /**
     * 收款结果查询
     */
    //状态
    private String tranStatus;
    //金额
    private String amount;
    //币种
    private String currencyCode;
    //付款凭证号
    private String voucherNum;
    //付款方附言
    private String payerComments;

    public String getTranStatus() {
        return tranStatus;
    }

    public void setTranStatus(String tranStatus) {
        this.tranStatus = tranStatus;
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

    public String getVoucherNum() {
        return voucherNum;
    }

    public void setVoucherNum(String voucherNum) {
        this.voucherNum = voucherNum;
    }

    public String getPayerComments() {
        return payerComments;
    }

    public void setPayerComments(String payerComments) {
        this.payerComments = payerComments;
    }


}
