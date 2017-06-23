package com.boc.bocsoft.mobile.bii.bus.account.model.PsnFinanceICIsSign;

/**
 * @author wangyang
 *         16/6/17 14:54
 *         查询IC卡是否绑定
 */
public class PsnFinanceICIsSignParams {

    /** IC卡ID */
    private int financeICAccountId;

    public PsnFinanceICIsSignParams(int financeICAccountId) {
        this.financeICAccountId = financeICAccountId;
    }

    public int getFinanceICAccountId() {
        return financeICAccountId;
    }

    public void setFinanceICAccountId(int financeICAccountId) {
        this.financeICAccountId = financeICAccountId;
    }
}
