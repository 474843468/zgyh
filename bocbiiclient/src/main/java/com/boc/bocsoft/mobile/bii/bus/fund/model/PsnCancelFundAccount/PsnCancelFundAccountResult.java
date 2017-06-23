package com.boc.bocsoft.mobile.bii.bus.fund.model.PsnCancelFundAccount;

/**
 * Created by lyf7084 on 2016/11/24.
 */
public class PsnCancelFundAccountResult {

    /**
     * investAccount : 111111111111111
     * identityNumber : 12345678901234567890123456789012
     * accountNo : 2222222222222222222
     * identifyType : 01
     * customerName : zzzzzzzzzzxxxxxxxxxxx
     */
    private String investAccount;
    private String identityNumber;
    private String accountNo;
    private String identifyType;
    private String customerName;

    public void setInvestAccount(String investAccount) {
        this.investAccount = investAccount;
    }

    public void setIdentityNumber(String identityNumber) {
        this.identityNumber = identityNumber;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    public void setIdentifyType(String identifyType) {
        this.identifyType = identifyType;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getInvestAccount() {
        return investAccount;
    }

    public String getIdentityNumber() {
        return identityNumber;
    }

    public String getAccountNo() {
        return accountNo;
    }

    public String getIdentifyType() {
        return identifyType;
    }

    public String getCustomerName() {
        return customerName;
    }
}
