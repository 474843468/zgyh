package com.boc.bocsoft.mobile.bii.bus.qrcodepay.model.QRPayGetPayerResult;

/**
 * Created by fanbin on 16/10/8.
 */
public class QRPayGetPayerResultResult {
    //状态 0：成功 1：失败 2：未明
    private String tranStatus;
    //金额
    private String amount;
    //币种
    private String currencyCode;
    //网银账户流水号
    private String actSeq;
    //付款凭证号
    private String voucherNum;
    //付款方附言
    private String payerComments;
    //收款方卡号
    private String payeeAccNo;
    //收款方姓名
    private String payeeName;
    //收款方附言  交易类型为02：转账时有意义
    private String payeeComments;

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

    public String getActSeq() {
        return actSeq;
    }

    public void setActSeq(String actSeq) {
        this.actSeq = actSeq;
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

    public String getPayeeAccNo() {
        return payeeAccNo;
    }

    public void setPayeeAccNo(String payeeAccNo) {
        this.payeeAccNo = payeeAccNo;
    }

    public String getPayeeName() {
        return payeeName;
    }

    public void setPayeeName(String payeeName) {
        this.payeeName = payeeName;
    }

    public String getPayeeComments() {
        return payeeComments;
    }

    public void setPayeeComments(String payeeComments) {
        this.payeeComments = payeeComments;
    }
}
