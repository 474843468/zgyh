package com.boc.bocsoft.mobile.bii.bus.account.model.PsnCrcdVirtualCardQuery;

import java.math.BigDecimal;

/**
 * 虚拟卡信息
 */
public class VirCardInfo {

    /**
     * 实体卡号
     */
    private String creditCardNo;
    /**
     * 虚拟卡号
     */
    private String virtualCardNo;
    /**
     * 生效日期
     */
    private long startDate;
    /**
     * 失效日期
     */
    private long endDate;
    /**
     * 单笔交易限额
     */
    private BigDecimal singLeamt;
    /**
     * 累计交易限额
     */
    private BigDecimal totaLeamt;
    /**
     * 已累计交易金额
     */
    private BigDecimal atotaLeamt;
    /**
     * 状态 0 已注销,1有效
     */
    private String status;
    /**
     * 建卡渠道-- 4=家居银行,2=手机银行,1=网上银行,CSR=电话银行,EISS=客服人工,JFEN=积分365,BCSP=缤纷生活
     */
    private String creatChannel;
    /**
     * 关联网银状态--1:成功（虚拟卡未关联进网银）,0:失败（虚拟卡已关联进网银）
     */
    private String isRelatedNetwork;
    /**
     * 最新修改用户
     */
    private String lastUpdateUser;
    /**
     * 最后更新次数
     */
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

    public long getStartDate() {
        return startDate;
    }

    public void setStartDate(long startDate) {
        this.startDate = startDate;
    }

    public long getEndDate() {
        return endDate;
    }

    public void setEndDate(long endDate) {
        this.endDate = endDate;
    }

    public BigDecimal getSingLeamt() {
        return singLeamt;
    }

    public void setSingLeamt(BigDecimal singLeamt) {
        this.singLeamt = singLeamt;
    }

    public BigDecimal getTotaLeamt() {
        return totaLeamt;
    }

    public void setTotaLeamt(BigDecimal totaLeamt) {
        this.totaLeamt = totaLeamt;
    }

    public BigDecimal getAtotaLeamt() {
        return atotaLeamt;
    }

    public void setAtotaLeamt(BigDecimal atotaLeamt) {
        this.atotaLeamt = atotaLeamt;
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