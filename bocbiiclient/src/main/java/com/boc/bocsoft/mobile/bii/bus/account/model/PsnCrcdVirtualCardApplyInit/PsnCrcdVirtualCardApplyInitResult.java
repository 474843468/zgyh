package com.boc.bocsoft.mobile.bii.bus.account.model.PsnCrcdVirtualCardApplyInit;

import org.threeten.bp.LocalDate;

import java.math.BigDecimal;

/**
 * @author wangyang
 *         16/7/25 14:33
 *         申请虚拟银行卡初始化
 */
public class PsnCrcdVirtualCardApplyInitResult {

    /** 账户户名 */
    private String accountName;
    /** 生效日期 */
    private LocalDate startDate;
    /** 失效日期--实体卡 */
    private LocalDate endDate;
    /** 客户标识 */
    private String customerId;
    /** 单笔交易限额最大值--页面提示信息 */
    private BigDecimal maxSingLeamt;

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
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

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public BigDecimal getMaxSingLeamt() {
        return maxSingLeamt;
    }

    public void setMaxSingLeamt(BigDecimal maxSingLeamt) {
        this.maxSingLeamt = maxSingLeamt;
    }
}
