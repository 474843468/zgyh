package com.boc.bocsoft.mobile.bii.bus.account.model.PsnCrcdVirtualCardCancel;

import com.boc.bocsoft.mobile.bii.bus.account.model.PublicParams;

/**
 * @author wangyang
 *         16/7/26 15:49
 *         虚拟银行卡注销
 */
public class PsnCrcdVirtualCardCancelParams extends PublicParams{

    /** 账户标识 */
    private String accountId;
    /** 虚拟卡卡号 */
    private String virtualCardNo;
    /** 生效日期 */
    private String virCardStartDate;
    /** 失效日期 */
    private String virCardEndDate;
    /** 单笔交易限额 */
    private String singLeamt;
    /** 累计交易限额 */
    private String totaLeamt;
    /** 最新修改用户 */
    private String lastUpdateUser;
    /** 最后更新次数 */
    private String lastUpdates;

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
