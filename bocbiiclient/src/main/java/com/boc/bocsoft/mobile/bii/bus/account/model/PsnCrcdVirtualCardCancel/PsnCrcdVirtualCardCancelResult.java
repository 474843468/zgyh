package com.boc.bocsoft.mobile.bii.bus.account.model.PsnCrcdVirtualCardCancel;

/**
 * @author wangyang
 *         16/7/26 15:52
 *         虚拟银行卡注销
 */
public class PsnCrcdVirtualCardCancelResult {

    /** 实体卡号 */
    private String creditCardNo;
    /** 虚拟卡号 */
    private String virtualCardNo;
    /** 账户户名 */
    private String virCardAccountName;
    /** 生效日期 */
    private String startDate;
    /** 失效日期 */
    private String endDate;
    /** 已累计交易金额 */
    private double atotaLeamt;
    /** 单笔交易限额 */
    private double singLeamt;
    /** 累计交易限额 */
    private double totaLeamt;
    /** 状态 1 有效,2 已注销 */
    private String status;
    /** 建卡渠道  4=家居银行,2=手机银行,1=网上银行,CSR=电话银行,EISS=客服人工,JFEN=积分365,BCSP=缤纷生活 */
    private String creatChannel;
    /** 关联网银状态  1 成功,0 失败 */
    private String isRelatedNetwork;
    /** 最新修改用户 */
    private String lastUpdateUser;
    /** 最后更新次数 */
    private String lastUpdates;

    public String getCreditCardNo() {
        return creditCardNo;
    }

    public void setCreditCardNo(String creditCardNo) {
        this.creditCardNo = creditCardNo;
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

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public double getAtotaLeamt() {
        return atotaLeamt;
    }

    public void setAtotaLeamt(double atotaLeamt) {
        this.atotaLeamt = atotaLeamt;
    }

    public double getSingLeamt() {
        return singLeamt;
    }

    public void setSingLeamt(double singLeamt) {
        this.singLeamt = singLeamt;
    }

    public double getTotaLeamt() {
        return totaLeamt;
    }

    public void setTotaLeamt(double totaLeamt) {
        this.totaLeamt = totaLeamt;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreatChannel() {
        return creatChannel;
    }

    public void setCreatChannel(String creatChannel) {
        this.creatChannel = creatChannel;
    }

    public String getIsRelatedNetwork() {
        return isRelatedNetwork;
    }

    public void setIsRelatedNetwork(String isRelatedNetwork) {
        this.isRelatedNetwork = isRelatedNetwork;
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
