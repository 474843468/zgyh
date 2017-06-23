package com.boc.bocsoft.mobile.bocmobile.buss.fund.accountmanagement.model;

/**
 * 由048接口 PsnCancelFundAccount 基金交易账户销户
 * 上送参数：token
 * 返回参数：customerName，investAccount，identifyType，identityNumber
 * Created by lyf7084 on 2016/12/13.
 */
public class TransAccountCancelResModel {
    /**
     * 基金交易账号
     */
    private String investAccount;

    /**
     * 证件号码
     */
    private String identityNumber;

    /**
     *
     */
    private String accountNo;

    /**
     * 证件类型
     */
    private String identifyType;

    /**
     * 姓名
     */
    private String customerName;

    public String getInvestAccount() {
        return investAccount;
    }

    public void setInvestAccount(String investAccount) {
        this.investAccount = investAccount;
    }

    public String getIdentityNumber() {
        return identityNumber;
    }

    public void setIdentityNumber(String identityNumber) {
        this.identityNumber = identityNumber;
    }

    public String getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    public String getIdentifyType() {
        return identifyType;
    }

    public void setIdentifyType(String identifyType) {
        this.identifyType = identifyType;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }


}
