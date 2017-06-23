package com.boc.bocsoft.mobile.bocmobile.buss.loan.common.loanmanagement.model;

/**
 * Created by huixiaobo on 2016/9/7.
 * 逾期返回值
 */
public class LoanOverdueModel {
    /**逾期状态*/
    private String overdueStatus;

    public String getOverdueStatus() {
        return overdueStatus;
    }

    public void setOverdueStatus(String overdueStatus) {
        this.overdueStatus = overdueStatus;
    }

    @Override
    public String toString() {
        return "LoanOverdueModel{" +
                "overdueStatus='" + overdueStatus + '\'' +
                '}';
    }
}
