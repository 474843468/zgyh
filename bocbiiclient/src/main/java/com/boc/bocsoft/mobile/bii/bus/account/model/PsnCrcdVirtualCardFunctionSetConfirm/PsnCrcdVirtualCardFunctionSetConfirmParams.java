package com.boc.bocsoft.mobile.bii.bus.account.model.PsnCrcdVirtualCardFunctionSetConfirm;

import com.boc.bocsoft.mobile.bii.bus.account.model.PublicParams;

/**
 * @author wangyang
 *         16/7/26 14:47
 *         虚拟银行卡交易限额修改预交易
 */
public class PsnCrcdVirtualCardFunctionSetConfirmParams extends PublicParams{

    /** 账户标识 */
    private String accountId;
    /** 虚拟卡卡号 */
    private String virtualCardNo;
    /** 账户户名 */
    private String virCardAccountName;
    /** 生效日期 */
    private String virCardStartDate;
    /** 失效日期 */
    private String virCardEndDate;
    /** 已累计交易金额 */
    private String atotaLeamt;
    /** 单笔交易限额 */
    private String singLeamt;
    /** 累计交易限额 */
    private String totaLeamt;
    /** 币种 */
    private String virCardCurrency;
    /** 最新修改用户 */
    private String lastUpdateUser ;
    /** 最后更新次数 */
    private String lastUpdates ;

    /** 客户ID */
    private String customerId;
    private String virCardCustomerId;

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
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

    public String getVirtualCardNo() {
        return virtualCardNo;
    }

    public void setVirtualCardNo(String virtualCardNo) {
        this.virtualCardNo = virtualCardNo;
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

    public String getAtotaLeamt() {
        return atotaLeamt;
    }

    public void setAtotaLeamt(String atotaLeamt) {
        this.atotaLeamt = atotaLeamt;
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

    public String getLastUpdateUser() {
        return lastUpdateUser;
    }

    public void setLastUpdateUser(String lastUpdateUser) {
        this.lastUpdateUser = lastUpdateUser;
    }

    public String getLastUpdates() {
        return lastUpdates;
    }

    public void setLastUpdates(String lastUpdates) {
        this.lastUpdates = lastUpdates;
    }
}
