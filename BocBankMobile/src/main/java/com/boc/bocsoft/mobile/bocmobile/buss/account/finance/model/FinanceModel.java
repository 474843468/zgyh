package com.boc.bocsoft.mobile.bocmobile.buss.account.finance.model;

import com.boc.bocsoft.mobile.bocmobile.ApplicationConst;
import com.boc.bocsoft.mobile.common.utils.StringUtils;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author wangyang
 *         16/6/20 15:52
 *         电子现金账户详情
 */
public class FinanceModel implements Serializable {

    /**
     * 纯电子现金账户类型
     */
    private final String TYPE_FINANCE_ACCOUNT = "300";

    public final static String TYPE_NOT_ACCOUNT = ApplicationConst.ACC_TYPE_BRO;

    /**
     * 账户Id
     */
    private String accountId;
    /**
     * 绑定银行账号
     */
    private String bankAccountId;
    /**
     * 绑定银行账号卡号
     */
    private String bankAccountNumber;
    /**
     * 账户别名
     */
    private String nickName;
    /**
     * 电子现金账户
     */
    private String finanICNumber;
    /**
     * 账户类型
     */
    private String finanICType;
    /**
     * 电子现金账户状态
     */
    private String accountState;
    /**
     * 电子现金卡片余额上限
     */
    private BigDecimal eCashUpperLimit;
    /**
     * 电子现金单笔交易限额
     */
    private BigDecimal singleLimit;
    /**
     * 电子现金账户余额
     */
    private BigDecimal totalBalance;
    /**
     * 补登账户余额
     */
    private BigDecimal supplyBalance;
    /**
     * 币种
     */
    private String currency;
    /**
     * 最大可充值金额
     */
    private BigDecimal maxLoadingAmount;

    public FinanceModel() {
    }

    public FinanceModel(String finanICNumber) {
        this.finanICNumber = finanICNumber;
    }

    public String getBankAccountNumber() {
        return bankAccountNumber;
    }

    public void setBankAccountNumber(String bankAccountNumber) {
        this.bankAccountNumber = bankAccountNumber;
    }

    public String getBankAccountId() {
        return bankAccountId;
    }

    public void setBankAccountId(String bankAccountId) {
        this.bankAccountId = bankAccountId;
    }

    public boolean isSign() {
        return !StringUtils.isEmptyOrNull(bankAccountNumber);
    }

    public String getCurrency() {
        return currency;
    }

    public String getAccountId() {
        return accountId;
    }

    public String getNickName() {
        return nickName;
    }

    public String getFinanICNumber() {
        return finanICNumber;
    }

    public String getAccountState() {
        return accountState;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public void setFinanICNumber(String finanICNumber) {
        this.finanICNumber = finanICNumber;
    }

    public void setAccountState(String accountState) {
        this.accountState = accountState;
    }

    public String getFinanICType() {
        return finanICType;
    }

    public void setFinanICType(String finanICType) {
        this.finanICType = finanICType;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public boolean isFinanceAccount() {
        return TYPE_FINANCE_ACCOUNT.equals(finanICType);
    }

    public BigDecimal geteCashUpperLimit() {
        return eCashUpperLimit;
    }

    public void seteCashUpperLimit(BigDecimal eCashUpperLimit) {
        this.eCashUpperLimit = eCashUpperLimit;
    }

    public BigDecimal getSingleLimit() {
        return singleLimit;
    }

    public void setSingleLimit(BigDecimal singleLimit) {
        this.singleLimit = singleLimit;
    }

    public BigDecimal getTotalBalance() {
        return totalBalance;
    }

    public void setTotalBalance(BigDecimal totalBalance) {
        this.totalBalance = totalBalance;
    }

    public BigDecimal getSupplyBalance() {
        return supplyBalance;
    }

    public void setSupplyBalance(BigDecimal supplyBalance) {
        this.supplyBalance = supplyBalance;
    }

    public BigDecimal getMaxLoadingAmount() {
        return maxLoadingAmount;
    }

    public void setMaxLoadingAmount(BigDecimal maxLoadingAmount) {
        this.maxLoadingAmount = maxLoadingAmount;
    }

    public String getStatus() {
        switch (accountState) {
            case "A":
            case "a":
                return "正常";
            case "D":
            case "d":
                return "销卡";
            case "Q":
            case "q":
                return "预销卡";
            case "C":
            case "c":
                return "回收";
            case "H":
            case "h":
                return "临时挂失";
            case "L":
            case "l":
                return "正式挂失";
            case "J":
            case "j":
                return "补卡登记";
            case "K":
            case "k":
                return "预补卡";
            case "O":
            case "o":
                return "换卡登记";
            case "P":
            case "p":
                return "预换卡";
            case "I":
            case "i":
                return "初始";
            case "E":
            case "e":
                return "待领卡";
            case "B":
            case "b":
                return "主账户销户";
            case "S":
            case "s":
                return "已关户";
        }
        return null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FinanceModel that = (FinanceModel) o;

        return finanICNumber != null ? finanICNumber.equals(that.finanICNumber) : that.finanICNumber == null;

    }

    @Override
    public int hashCode() {
        return finanICNumber != null ? finanICNumber.hashCode() : 0;
    }
}
