package com.boc.bocsoft.mobile.bii.bus.account.model.PsnCrcdQueryVIRCardInfo;

import org.threeten.bp.LocalDate;

import java.math.BigDecimal;

/**
 * @author wangyang
 *         2016/10/28 21:55
 *         查询虚拟卡结果
 */
public class PsnCrcdQueryVIRCardInfoResult {

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
    private LocalDate startDate;
    /**
     * 失效日期
     */
    private LocalDate endDate;
    /**
     * 单笔交易限额
     */
    private BigDecimal singleLimit;
    /**
     * 累计交易限额
     */
    private BigDecimal totalLimit;
    /**
     * 已累计交易金额
     */
    private BigDecimal aTotalLimit;
    /**
     * 状态 0 已注销,1有效
     */
    private String vCardStatus;
    /**
     * 建卡渠道-- 4=家居银行,2=手机银行,1=网上银行,CSR=电话银行,EISS=客服人工,JFEN=积分365,BCSP=缤纷生活
     */
    private String applyChannel;

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

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public BigDecimal getSingleLimit() {
        return singleLimit;
    }

    public void setSingleLimit(BigDecimal singleLimit) {
        this.singleLimit = singleLimit;
    }

    public BigDecimal getTotalLimit() {
        return totalLimit;
    }

    public void setTotalLimit(BigDecimal totalLimit) {
        this.totalLimit = totalLimit;
    }

    public BigDecimal getaTotalLimit() {
        return aTotalLimit;
    }

    public void setaTotalLimit(BigDecimal aTotalLimit) {
        this.aTotalLimit = aTotalLimit;
    }

    public String getvCardStatus() {
        return vCardStatus;
    }

    public void setvCardStatus(String vCardStatus) {
        this.vCardStatus = vCardStatus;
    }

    public String getApplyChannel() {
        return applyChannel;
    }

    public void setApplyChannel(String applyChannel) {
        this.applyChannel = applyChannel;
    }
}
