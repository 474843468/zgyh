package com.boc.bocsoft.mobile.bocmobile.buss.loan.common.prepay.model;

import org.threeten.bp.LocalDate;

import java.math.BigDecimal;
import java.util.List;

/**
 * 提前还款根据账户ID查询账户详情获取可用余额
 *
 * Created by qiangchen on 2016/7/28.
 */
public class PrepayAccountDetailModel {

    //开户行
    private String accOpenBank;
    //账户类型
    private String accountType;
    // 账户状态
    private String accountStatus;
    //账户状态
    private String accOpenDate;
    //开户日期  YYYY/MM/DD
    private List<AccountDetaiListBean> accountDetaiList;

    public String getAccOpenBank() {
        return accOpenBank;
    }

    public void setAccOpenBank(String accOpenBank) {
        this.accOpenBank = accOpenBank;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getAccountStatus() {
        return accountStatus;
    }

    public void setAccountStatus(String accountStatus) {
        this.accountStatus = accountStatus;
    }

    public String getAccOpenDate() {
        return accOpenDate;
    }

    public void setAccOpenDate(String accOpenDate) {
        this.accOpenDate = accOpenDate;
    }

    public List<AccountDetaiListBean> getAccountDetaiList() {
        return accountDetaiList;
    }

    public void setAccountDetaiList(List<AccountDetaiListBean> accountDetaiList) {
        this.accountDetaiList = accountDetaiList;
    }

    public static class AccountDetaiListBean {
        //币种
        private String currencyCode;
        //钞汇标识
        private String cashRemit;
        //当前余额
        private BigDecimal bookBalance;
        //可用余额
        private BigDecimal availableBalance;
        //存折册号
        private String volumeNumber;
        //存单类型
        private String type;
        //利率
        private String interestRate;
        //子账户状态
        private String status;
        //月存、支金额
        private BigDecimal monthBalance;
        //存单号
        private String cdNumber;
        //存期
        private String cdPeriod;
        //开户日期
        private LocalDate openDate;
        //起息日
        private LocalDate interestStartsDate;
        //到期日
        private LocalDate interestEndDate;
        //结清日期
        private LocalDate settlementDate;
        //自动转存标识
        private String convertType;
        //凭证号码
        private String pingNo;
        //
        private String holdAmount;
        //约定转存状态
        private String appointStatus;

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

        public BigDecimal getBookBalance() {
            return bookBalance;
        }

        public void setBookBalance(BigDecimal bookBalance) {
            this.bookBalance = bookBalance;
        }

        public BigDecimal getAvailableBalance() {
            return availableBalance;
        }

        public void setAvailableBalance(BigDecimal availableBalance) {
            this.availableBalance = availableBalance;
        }

        public String getVolumeNumber() {
            return volumeNumber;
        }

        public void setVolumeNumber(String volumeNumber) {
            this.volumeNumber = volumeNumber;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getInterestRate() {
            return interestRate;
        }

        public void setInterestRate(String interestRate) {
            this.interestRate = interestRate;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public BigDecimal getMonthBalance() {
            return monthBalance;
        }

        public void setMonthBalance(BigDecimal monthBalance) {
            this.monthBalance = monthBalance;
        }

        public String getCdNumber() {
            return cdNumber;
        }

        public void setCdNumber(String cdNumber) {
            this.cdNumber = cdNumber;
        }

        public String getCdPeriod() {
            return cdPeriod;
        }

        public void setCdPeriod(String cdPeriod) {
            this.cdPeriod = cdPeriod;
        }

        public LocalDate getOpenDate() {
            return openDate;
        }

        public void setOpenDate(LocalDate openDate) {
            this.openDate = openDate;
        }

        public LocalDate getInterestStartsDate() {
            return interestStartsDate;
        }

        public void setInterestStartsDate(LocalDate interestStartsDate) {
            this.interestStartsDate = interestStartsDate;
        }

        public LocalDate getInterestEndDate() {
            return interestEndDate;
        }

        public void setInterestEndDate(LocalDate interestEndDate) {
            this.interestEndDate = interestEndDate;
        }

        public LocalDate getSettlementDate() {
            return settlementDate;
        }

        public void setSettlementDate(LocalDate settlementDate) {
            this.settlementDate = settlementDate;
        }

        public String getConvertType() {
            return convertType;
        }

        public void setConvertType(String convertType) {
            this.convertType = convertType;
        }

        public String getPingNo() {
            return pingNo;
        }

        public void setPingNo(String pingNo) {
            this.pingNo = pingNo;
        }

        public String getHoldAmount() {
            return holdAmount;
        }

        public void setHoldAmount(String holdAmount) {
            this.holdAmount = holdAmount;
        }

        public String getAppointStatus() {
            return appointStatus;
        }

        public void setAppointStatus(String appointStatus) {
            this.appointStatus = appointStatus;
        }
    }

}
