package com.boc.bocsoft.mobile.bocmobile.buss.account.overview.model;

import com.boc.bocsoft.mobile.bocmobile.buss.account.utils.AccountTypeUtil;

import org.threeten.bp.LocalDate;

import java.math.BigDecimal;

/**
 * 定期最近交易列表数据项
 * Created by niuguobin on 2016/7/20.
 */
public class TermlyViewModel implements Comparable<TermlyViewModel>{

    private final String CONVERT_TYPE_AUTO = "R";

    /** 状态-正常 */
    public static final String STATUS_NORMAL = "00";
    /** 状态-已销户 */
    public static final String STATUS_CANCEL = "01";
    /** 状态-不动 */
    public static final String STATUS_BD = "03";
    /** 状态-挂失 */
    public static final String STATUS_LOSS = "05";
    /** 状态-冻结 */
    public static final String STATUS_FREEZE = "06";
    /** 状态-其他 */
    public static final String STATUS_OTHER = "09";

    /** 账户Id */
    private String accountId;
    /**
     * 币种
     */
    private String currencyCode;
    /**
     * 钞汇
     */
    private String cashRemit;
    /**
     * 可用余额
     */
    private BigDecimal availableBalance;
    /**
     * 存单类型
     */
    private String type;
    /**
     * 起始日
     */
    private LocalDate interestStartsDate;
    /**
     * 到期日
     */
    private LocalDate interestEndDate;
    /**
     * 存期
     */
    private String cdPeriod;
    /**
     * 子账户状态
     */
    private String status;
    /**
     * 利率
     */
    private String interestRate;
    /**
     * 开户日期
     */
    private LocalDate openDate;
    /**
     * 转存标志
     */
    private String convertType;
    /**
     * 存折册号
     */
    private String volumeNumber;
    /**
     * 存单号
     */
    private String cdNumber;
    /**
     * 凭证号
     */
    private String pingNo;
    /**
     * 结清日期
     */
    private LocalDate settlementDate;
    /**
     * 约定转存状态
     */
    private String appointStatus;
    /**
     * 当前余额
     */
    private BigDecimal bookBalance ;
    /**
     * 月存,支金额
     */
    private BigDecimal monthBalance;

    public BigDecimal getBookBalance() {
        return bookBalance;
    }

    public void setBookBalance(BigDecimal bookBalance) {
        this.bookBalance = bookBalance;
    }

    public BigDecimal getMonthBalance() {
        return monthBalance;
    }

    public void setMonthBalance(BigDecimal monthBalance) {
        this.monthBalance = monthBalance;
    }

    public String getPingNo() {
        return pingNo;
    }

    public void setPingNo(String pingNo) {
        this.pingNo = pingNo;
    }

    public LocalDate getSettlementDate() {
        return settlementDate;
    }

    public void setSettlementDate(LocalDate settlementDate) {
        this.settlementDate = settlementDate;
    }

    public String getAppointStatus() {
        return appointStatus;
    }

    public void setAppointStatus(String appointStatus) {
        this.appointStatus = appointStatus;
    }

    public LocalDate getInterestStartsDate() {
        return interestStartsDate;
    }

    public void setInterestStartsDate(LocalDate interestStartsDate) {
        this.interestStartsDate = interestStartsDate;
    }

    public LocalDate getOpenDate() {
        return openDate;
    }

    public void setOpenDate(LocalDate openDate) {
        this.openDate = openDate;
    }

    public String getConvertType() {
        return convertType;
    }

    public void setConvertType(String convertType) {
        this.convertType = convertType;
    }

    public String getVolumeNumber() {
        return volumeNumber;
    }

    public void setVolumeNumber(String volumeNumber) {
        this.volumeNumber = volumeNumber;
    }

    public String getCdNumber() {
        return cdNumber;
    }

    public void setCdNumber(String cdNumber) {
        this.cdNumber = cdNumber;
    }

    public String getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(String interestRate) {
        this.interestRate = interestRate;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public String getCashRemit() {
        return cashRemit;
    }

    public void setCashRemit(String cashRemit) {
        this.cashRemit = cashRemit;
    }

    public BigDecimal getAvailableBalance() {
        return availableBalance;
    }

    public void setAvailableBalance(BigDecimal availableBalance) {
        this.availableBalance = availableBalance;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public LocalDate getInterestEndDate() {
        return interestEndDate;
    }

    public void setInterestEndDate(LocalDate interestEndDate) {
        this.interestEndDate = interestEndDate;
    }

    public String getCdPeriod() {
        return cdPeriod;
    }

    public void setCdPeriod(String cdPeriod) {
        this.cdPeriod = cdPeriod;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isAutoConvertType(){
        return CONVERT_TYPE_AUTO.equals(convertType);
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    @Override
    public String toString() {
        return "TermlyViewModel{" +
                "CONVERT_TYPE_AUTO='" + CONVERT_TYPE_AUTO + '\'' +
                ", currencyCode='" + currencyCode + '\'' +
                ", cashRemit='" + cashRemit + '\'' +
                ", availableBalance=" + availableBalance +
                ", type='" + type + '\'' +
                ", interestStartsDate=" + interestStartsDate +
                ", interestEndDate=" + interestEndDate +
                ", cdPeriod='" + cdPeriod + '\'' +
                ", status='" + status + '\'' +
                ", interestRate='" + interestRate + '\'' +
                ", openDate=" + openDate +
                ", convertType='" + convertType + '\'' +
                ", volumeNumber='" + volumeNumber + '\'' +
                ", cdNumber='" + cdNumber + '\'' +
                '}';
    }

    @Override
    public int compareTo(TermlyViewModel another) {
        if(AccountTypeUtil.REGULAR_TYPE_DHLB.equals(getType()))
            return -1;

        if(getInterestEndDate() == null)
            return 1;

        if(another.getInterestEndDate() == null)
            return -1;

        return getInterestEndDate().compareTo(another.getInterestEndDate());
    }
}
