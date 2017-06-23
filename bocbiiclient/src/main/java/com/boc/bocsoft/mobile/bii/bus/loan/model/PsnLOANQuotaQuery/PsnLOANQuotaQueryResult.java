package com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLOANQuotaQuery;

import java.math.BigDecimal;
import org.threeten.bp.LocalDate;

/**
 * Created by XieDu on 2016/7/8.
 */
public class PsnLOANQuotaQueryResult {

    private String currencyCode;
    /**
     * 额度金额
     */
    private BigDecimal quota;
    /**
     * 到期日
     */
    private LocalDate loanToDate;
    /**
     * 贷款品种
     */
    private String loanType;
    /**
     * 额度状态
     * 05:正常
     * 10：取消
     * 20：冻结
     * 40：关闭
     */
    private String quotaStatus;
    /**
     * 额度号码
     */
    private String quotaNumber;
    /**
     * 已用额度
     */
    private BigDecimal quotaUsed;
    /**
     * 可用额度
     */
    private BigDecimal availableQuota;

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public BigDecimal getQuota() {
        return quota;
    }

    public void setQuota(BigDecimal quota) {
        this.quota = quota;
    }

    public LocalDate getLoanToDate() {
        return loanToDate;
    }

    public void setLoanToDate(LocalDate loanToDate) {
        this.loanToDate = loanToDate;
    }

    public String getLoanType() {
        return loanType;
    }

    public void setLoanType(String loanType) {
        this.loanType = loanType;
    }

    public String getQuotaStatus() {
        return quotaStatus;
    }

    public void setQuotaStatus(String quotaStatus) {
        this.quotaStatus = quotaStatus;
    }

    public String getQuotaNumber() {
        return quotaNumber;
    }

    public void setQuotaNumber(String quotaNumber) {
        this.quotaNumber = quotaNumber;
    }

    public BigDecimal getQuotaUsed() {
        return quotaUsed;
    }

    public void setQuotaUsed(BigDecimal quotaUsed) {
        this.quotaUsed = quotaUsed;
    }

    public BigDecimal getAvailableQuota() {
        return availableQuota;
    }

    public void setAvailableQuota(BigDecimal availableQuota) {
        this.availableQuota = availableQuota;
    }

    @Override
    public String toString() {
        return "PsnLOANQuotaQueryResult{" +
                "currencyCode='" + currencyCode + '\'' +
                ", quota=" + quota +
                ", loanToDate=" + loanToDate +
                ", loanType='" + loanType + '\'' +
                ", quotaStatus='" + quotaStatus + '\'' +
                ", quotaNumber='" + quotaNumber + '\'' +
                ", quotaUsed=" + quotaUsed +
                ", availableQuota=" + availableQuota +
                '}';
    }
}
