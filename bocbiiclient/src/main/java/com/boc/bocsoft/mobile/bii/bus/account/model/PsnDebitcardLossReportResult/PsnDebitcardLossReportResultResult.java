package com.boc.bocsoft.mobile.bii.bus.account.model.PsnDebitcardLossReportResult;

/**
 * 借记卡临时挂失提交交易响应
 *
 * Created by liuweidong on 2016/6/6.
 */
public class PsnDebitcardLossReportResultResult {

    /**
     * 账户借方冻结是否成功
     */
    private boolean accFrozenStatus;
    /**
     * 借记卡临时挂失是否成功
     */
    private boolean reportLossStatus;

    public boolean isAccFrozenStatus() {
        return accFrozenStatus;
    }

    public void setAccFrozenStatus(boolean accFrozenStatus) {
        this.accFrozenStatus = accFrozenStatus;
    }

    public boolean isReportLossStatus() {
        return reportLossStatus;
    }

    public void setReportLossStatus(boolean reportLossStatus) {
        this.reportLossStatus = reportLossStatus;
    }
}
