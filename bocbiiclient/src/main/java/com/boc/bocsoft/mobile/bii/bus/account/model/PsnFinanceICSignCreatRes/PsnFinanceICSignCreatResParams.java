package com.boc.bocsoft.mobile.bii.bus.account.model.PsnFinanceICSignCreatRes;

import com.boc.bocsoft.mobile.bii.bus.account.model.PublicSecurityParams;

/**
 * @author wangyang
 *         16/6/17 14:06
 *         新增绑定关系-提交交易参数
 */
public class PsnFinanceICSignCreatResParams extends PublicSecurityParams {

    /** 银行账户账号 */
    private String bankAccountId;
    private String bankAccountNumber;
    /** IC卡账号 */
    private String financeICAccountId;
    private String financeICAccountNumber;

    public String getBankAccountId() {
        return bankAccountId;
    }

    public void setBankAccountId(String bankAccountId) {
        this.bankAccountId = bankAccountId;
    }

    public String getBankAccountNumber() {
        return bankAccountNumber;
    }

    public void setBankAccountNumber(String bankAccountNumber) {
        this.bankAccountNumber = bankAccountNumber;
    }

    public String getFinanceICAccountId() {
        return financeICAccountId;
    }

    public void setFinanceICAccountId(String financeICAccountId) {
        this.financeICAccountId = financeICAccountId;
    }

    public String getFinanceICAccountNumber() {
        return financeICAccountNumber;
    }

    public void setFinanceICAccountNumber(String financeICAccountNumber) {
        this.financeICAccountNumber = financeICAccountNumber;
    }
}
