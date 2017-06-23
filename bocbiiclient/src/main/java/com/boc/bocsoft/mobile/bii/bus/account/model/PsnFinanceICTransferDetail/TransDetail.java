package com.boc.bocsoft.mobile.bii.bus.account.model.PsnFinanceICTransferDetail;

import org.threeten.bp.LocalDate;

import java.math.BigDecimal;

/**
 * @author wangyang
 * @time 16/6/23 20:30
 * <p/>
 * 账户交易明细
 */
public class TransDetail {

    /**
     * 币种
     */
    private String currency;
    /**
     * 交易类型
     */
    private String transferType;
    /**
     * 余额
     */
    private BigDecimal balance;
    /**
     * 收入/支出总额
     */
    private BigDecimal amount;
    /**
     * 交易日期
     */
    private LocalDate returnDate;
    /**
     * 钞汇
     */
    private String cashRemit;
    /**
     * 冲正标识
     */
    private boolean chargeBack;
    /**
     * 转入/转出标识  true-转出 0,false-转入 1
     */
    private boolean amountFlag;

    public boolean isAmountFlag() {
        return amountFlag;
    }

    public void setAmountFlag(boolean amountFlag) {
        this.amountFlag = amountFlag;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getTransferType() {
        return transferType;
    }

    public String getTransferTypeString() {
        switch (transferType) {
            case "201":
            case "404":
            case "711":
            case "721":
            case "731":
            case "751":
            case "760":
            case "770":
            case "791":
                return "圈存";
            case "502":
            case "506":
            case "552":
            case "556":
            case "560":
                return "换卡";
            case "514":
            case "518":
            case "568":
            case "564":
            case "572":
                return "补卡";
            case "528":
            case "530":
            case "531":
            case "BTI":
            case "BTO":
                return "转卡";
            case "781":
            case "782":
            case "784":
            case "785":
            case "803":
            case "805":
            case "807":
            case "809":
                return "圈提";
            case "790":
            case "PCS":
                return "消费";
            case "811":
                return "回收";
            case "812":
            case "813":
                return "调账";
            case "741":
                return "补登";
            case "820":
            case "821":
            case "002":
                return "退货";
            case "738":
            case "729":
                return "跨行指定账户圈存";
            case "FEE":
                return "手续费";
            case "798":
            case "799":
                return "其他";
        }
        return transferType;
    }

    public void setTransferType(String transferType) {
        this.transferType = transferType;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getCashRemit() {
        return cashRemit;
    }

    public void setCashRemit(String cashRemit) {
        this.cashRemit = cashRemit;
    }

    public boolean isChargeBack() {
        return chargeBack;
    }

    public void setChargeBack(boolean chargeBack) {
        this.chargeBack = chargeBack;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }
}