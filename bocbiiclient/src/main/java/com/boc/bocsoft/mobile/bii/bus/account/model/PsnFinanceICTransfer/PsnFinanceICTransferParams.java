package com.boc.bocsoft.mobile.bii.bus.account.model.PsnFinanceICTransfer;

import com.boc.bocsoft.mobile.bii.bus.account.model.PublicParams;

/**
 * @author wangyang
 *         16/6/17 14:12
 *         账户充值参数
 */
public class PsnFinanceICTransferParams extends PublicParams {

    /** 银行账户ID */
    private int bankAccountId;
    /** IC卡账户ID */
    private String financeICAccountId;
    /** 金额 */
    private double amount;
    /** 币种 */
    private String cashRemit;

    public int getBankAccountId() {
        return bankAccountId;
    }

    public void setBankAccountId(int bankAccountId) {
        this.bankAccountId = bankAccountId;
    }

    public String getFinanceICAccountId() {
        return financeICAccountId;
    }

    public void setFinanceICAccountId(String financeICAccountId) {
        this.financeICAccountId = financeICAccountId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getCashRemit() {
        return cashRemit;
    }

    public void setCashRemit(String cashRemit) {
        this.cashRemit = cashRemit;
    }
}
