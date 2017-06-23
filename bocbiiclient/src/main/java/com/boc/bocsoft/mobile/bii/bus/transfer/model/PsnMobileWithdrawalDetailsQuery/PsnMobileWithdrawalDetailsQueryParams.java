package com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnMobileWithdrawalDetailsQuery;

/**
 * 手机取款 -- 取款详情查询
 * Created by wangf on 2016/7/5.
 */
public class PsnMobileWithdrawalDetailsQueryParams {

    /**
     * transactionId :
     * receiptAmount : 100
     * currencyCode : 001
     * payeeName : lisi
     * payeeMobile : 13652365478
     */

    //网银交易序号
    private String transactionId;
    //交易金额
    private String receiptAmount;
    //交易币种
    private String currencyCode;
    //收款人姓名
    private String payeeName;
    //收款人手机号
    private String payeeMobile;

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getReceiptAmount() {
        return receiptAmount;
    }

    public void setReceiptAmount(String receiptAmount) {
        this.receiptAmount = receiptAmount;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public String getPayeeName() {
        return payeeName;
    }

    public void setPayeeName(String payeeName) {
        this.payeeName = payeeName;
    }

    public String getPayeeMobile() {
        return payeeMobile;
    }

    public void setPayeeMobile(String payeeMobile) {
        this.payeeMobile = payeeMobile;
    }
}
