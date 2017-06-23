package com.boc.bocsoft.mobile.bii.bus.account.model.PsnICTransferNoRelevance;

import com.boc.bocsoft.mobile.bii.bus.account.model.PublicSecurityParams;

/**
 * @author wangyang
 *         16/6/17 14:21
 *         账户充值-未关联进网银-预交易
 */
public class PsnICTransferNoRelevanceParams extends PublicSecurityParams {

    /** 银行账户ID */
    private int bankAccountId;
    /** IC卡账号 */
    private String financeICAccountNumber;
    /** 收款人姓名 */
    private String payeeName;
    /** 转账金额 */
    private double amount;

    public int getBankAccountId() {
        return bankAccountId;
    }

    public void setBankAccountId(int bankAccountId) {
        this.bankAccountId = bankAccountId;
    }

    public String getFinanceICAccountNumber() {
        return financeICAccountNumber;
    }

    public void setFinanceICAccountNumber(String financeICAccountNumber) {
        this.financeICAccountNumber = financeICAccountNumber;
    }

    public String getPayeeName() {
        return payeeName;
    }

    public void setPayeeName(String payeeName) {
        this.payeeName = payeeName;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}
