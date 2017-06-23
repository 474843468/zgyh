package com.boc.bocsoft.mobile.bii.bus.account.model.PsnNcpayOpenPre;

import com.boc.bocsoft.mobile.bii.bus.account.model.PublicParams;

/**
 * @author wangyang
 *         2016/10/11 15:59
 *         限额服务开通预交易
 */
public class PsnNcpayOpenPreParams extends PublicParams{

    /** 账户Id */
    private String accountId;
    /** 账户AccountNumber */
    private String accountNumber;
    /** 当前限额 */
    private String currentQuota;
    /** 交易类型 */
    private String operateType = "开通";
    /** 交易日期 */
    private String tranDate;
    /** 服务类型 */
    private String serviceType;
    /** 客户名 */
    private String customerName;

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getOperateType() {
        return operateType;
    }

    public void setOperateType(String operateType) {
        this.operateType = operateType;
    }

    public String getTranDate() {
        return tranDate;
    }

    public void setTranDate(String tranDate) {
        this.tranDate = tranDate;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getCurrentQuota() {
        return currentQuota;
    }

    public void setCurrentQuota(String currentQuota) {
        this.currentQuota = currentQuota;
    }

}
