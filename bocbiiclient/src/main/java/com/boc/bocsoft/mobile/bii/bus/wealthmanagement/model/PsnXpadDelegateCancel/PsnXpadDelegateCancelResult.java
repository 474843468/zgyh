package com.boc.bocsoft.mobile.bii.bus.wealthmanagement.model.PsnXpadDelegateCancel;

/**
 * 常规委托交易撤单
 * Created by zhx on 2016/9/5
 */
public class PsnXpadDelegateCancelResult {

    /**
     * cashRemit : 01
     * transType : 02
     * transactionId : 345678908
     * currencyCode : 001
     * prodName : 理财计划000xxx
     * athDate : 2016/10/10
     * cancelDate : 2016/10/11
     * athAmount : 100
     * prodCode : JDIE876943
     * athNumber : 100
     */

    // 产品代码
    private String prodCode;
    // 产品名称
    private String prodName;
    // 交易类型（00：认购 01：申购 02：赎回 03：红利再投 04：红利发放 05：（经过）利息返还 06：本金返还 07：起息前赎回 08：利息折份额 09:赎回亏损 10:赎回盈利 11:产品转让）
    private String transType;
    // 交易币种（001：人民币元 014：美元 012：英镑 013：港币 028: 加拿大元 029：澳元 038：欧元 027：日元）
    private String currencyCode;
    // 钞汇标识（01：钞 02：汇 00：人民币钞汇）
    private String cashRemit;
    // 委托份额
    private String athNumber;
    // 委托金额
    private String athAmount;
    // 委托日期
    private String athDate;
    // 委托撤销日期
    private String cancelDate;
    // 交易流水号（网银）
    private String transactionId;

    public void setCashRemit(String cashRemit) {
        this.cashRemit = cashRemit;
    }

    public void setTransType(String transType) {
        this.transType = transType;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public void setProdName(String prodName) {
        this.prodName = prodName;
    }

    public void setAthDate(String athDate) {
        this.athDate = athDate;
    }

    public void setCancelDate(String cancelDate) {
        this.cancelDate = cancelDate;
    }

    public void setAthAmount(String athAmount) {
        this.athAmount = athAmount;
    }

    public void setProdCode(String prodCode) {
        this.prodCode = prodCode;
    }

    public void setAthNumber(String athNumber) {
        this.athNumber = athNumber;
    }

    public String getCashRemit() {
        return cashRemit;
    }

    public String getTransType() {
        return transType;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public String getProdName() {
        return prodName;
    }

    public String getAthDate() {
        return athDate;
    }

    public String getCancelDate() {
        return cancelDate;
    }

    public String getAthAmount() {
        return athAmount;
    }

    public String getProdCode() {
        return prodCode;
    }

    public String getAthNumber() {
        return athNumber;
    }
}
