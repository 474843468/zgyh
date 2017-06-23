package com.boc.bocsoft.mobile.bii.bus.account.model.PsnFinanceICTransferDetail;

import com.boc.bocsoft.mobile.bii.bus.account.model.PageParams;

/**
 * @author wangyang
 *         16/6/17 10:12
 *         账户交易明细参数
 */
public class PsnFinanceICTransferDetailParams extends PageParams{

    /**
     * 账号Id
     */
    private String accountId;
    /**
     * 起始日期
     */
    private String startDate;
    /**
     * 结束日期
     */
    private String endDate;

    public PsnFinanceICTransferDetailParams(){}

    public PsnFinanceICTransferDetailParams(String accountId) {
        this.accountId = accountId;
        setPageSize(10);
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

}
