package com.boc.bocsoft.mobile.bii.bus.crcd.model.PsnReSetSmsPaperCheck;

/**
 * Created by Administrator on 2016/12/30.
 */
public class PsnReSetSmsPaperCheckParams {
    private String accountId;
    private String billDate;
    private String BillAddress;

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getBillDate() {
        return billDate;
    }

    public void setBillDate(String billDate) {
        this.billDate = billDate;
    }

    public String getBillAddress() {
        return BillAddress;
    }

    public void setBillAddress(String billAddress) {
        BillAddress = billAddress;
    }
}
