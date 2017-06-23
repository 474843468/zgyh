package com.boc.bocsoft.mobile.bii.bus.account.model.PsnCrcdVirtualCardApplySubmit;

import com.boc.bocsoft.mobile.bii.bus.account.model.PublicSecurityParams;

/**
 * @author wangyang
 *         16/7/25 14:48
 *         申请虚拟银行卡提交
 */
public class PsnCrcdVirtualCardApplySubmitParams extends PublicSecurityParams{

    /** 账户标识 */
    private String accountId;
    /** 账户户名 */
    private String virCardAccountName;
    /** 生效日期 */
    private String virCardStartDate;
    /** 失效日期 */
    private String virCardEndDate;
    /** 单笔限额交易 */
    private String singLeamt;
    /** 累计交易限额 */
    private String totaLeamt;
    /** 币种 */
    private String virCardCurrency;
    /** 系统当前日期 */
    private String virCardCurrentDate;
    /** 客户Id */
    private String virCardCustomerId;

    public String getVirCardCurrentDate() {
        return virCardCurrentDate;
    }

    public void setVirCardCurrentDate(String virCardCurrentDate) {
        this.virCardCurrentDate = virCardCurrentDate;
    }

    public String getVirCardCustomerId() {
        return virCardCustomerId;
    }

    public void setVirCardCustomerId(String virCardCustomerId) {
        this.virCardCustomerId = virCardCustomerId;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getVirCardAccountName() {
        return virCardAccountName;
    }

    public void setVirCardAccountName(String virCardAccountName) {
        this.virCardAccountName = virCardAccountName;
    }

    public String getVirCardStartDate() {
        return virCardStartDate;
    }

    public void setVirCardStartDate(String virCardStartDate) {
        this.virCardStartDate = virCardStartDate;
    }

    public String getVirCardEndDate() {
        return virCardEndDate;
    }

    public void setVirCardEndDate(String virCardEndDate) {
        this.virCardEndDate = virCardEndDate;
    }

    public String getSingLeamt() {
        return singLeamt;
    }

    public void setSingLeamt(String singLeamt) {
        this.singLeamt = singLeamt;
    }

    public String getTotaLeamt() {
        return totaLeamt;
    }

    public void setTotaLeamt(String totaLeamt) {
        this.totaLeamt = totaLeamt;
    }

    public String getVirCardCurrency() {
        return virCardCurrency;
    }

    public void setVirCardCurrency(String virCardCurrency) {
        this.virCardCurrency = virCardCurrency;
    }
}
