package com.boc.bocsoft.mobile.bii.bus.account.model.PsnFinanceICSignCancel;

import com.boc.bocsoft.mobile.bii.bus.account.model.PublicParams;

/**
 * @author wangyang
 *         16/6/17 10:58
 *         删除绑定关系参数
 */
public class PsnFinanceICSignCancelParams extends PublicParams {

    /** 银行账户ID */
    private int bankAccountId;
    /** IC卡账号 */
    private String financeICAccountNumber;

    public int getBankAccountId() {
        return bankAccountId;
    }

    public void setBankAccountId(String bankAccountId) {
        this.bankAccountId = Integer.parseInt(bankAccountId);
    }

    public String getFinanceICAccountNumber() {
        return financeICAccountNumber;
    }

    public void setFinanceICAccountNumber(String financeICAccountNumber) {
        this.financeICAccountNumber = financeICAccountNumber;
    }
}
