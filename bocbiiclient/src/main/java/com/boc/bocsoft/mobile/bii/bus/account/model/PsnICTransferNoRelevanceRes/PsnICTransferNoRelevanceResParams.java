package com.boc.bocsoft.mobile.bii.bus.account.model.PsnICTransferNoRelevanceRes;

import com.boc.bocsoft.mobile.bii.bus.account.model.PublicSecurityParams;

/**
 * @author wangyang
 *         16/6/17 14:43
 *         账户充值-未关联进网银-提交交易
 */
public class PsnICTransferNoRelevanceResParams extends PublicSecurityParams {

    /** 银行账户ID */
    private int bankAccountId;
    /** IC卡账户账号 */
    private String financeICAccountNumber;
    /** 收款人姓名 */
    private String payeeName;
    /** 金额 */
    private String amount;

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

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
}
