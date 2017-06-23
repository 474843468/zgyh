package com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransGetNationalTransferCommissionCharge;

/**
 * Created by WM on 2016/7/1.
 */
public class PsnTransGetNationalTransferCommissionChargeParams {

    private String  amount;//"12.00"
    private String  cashRemit;//"00"
    private String  cnapsCode;//"103894078326"
    private String  currency;//"001"
    private String  fromAccountId;//"100904297"
    private String  payeeActno;//"4353543535354353534"
    private String  payeeName;//"张三"p
    private String  remark;//""
    private String  serviceId;//"PB032"
    private String  toOrgName;//"中国民生银行喀什兵团支行营业部"

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getCashRemit() {
        return cashRemit;
    }

    public void setCashRemit(String cashRemit) {
        this.cashRemit = cashRemit;
    }

    public String getCnapsCode() {
        return cnapsCode;
    }

    public void setCnapsCode(String cnapsCode) {
        this.cnapsCode = cnapsCode;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getFromAccountId() {
        return fromAccountId;
    }

    public void setFromAccountId(String fromAccountId) {
        this.fromAccountId = fromAccountId;
    }

    public String getPayeeActno() {
        return payeeActno;
    }

    public void setPayeeActno(String payeeActno) {
        this.payeeActno = payeeActno;
    }

    public String getPayeeName() {
        return payeeName;
    }

    public void setPayeeName(String payeeName) {
        this.payeeName = payeeName;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getToOrgName() {
        return toOrgName;
    }

    public void setToOrgName(String toOrgName) {
        this.toOrgName = toOrgName;
    }
}
