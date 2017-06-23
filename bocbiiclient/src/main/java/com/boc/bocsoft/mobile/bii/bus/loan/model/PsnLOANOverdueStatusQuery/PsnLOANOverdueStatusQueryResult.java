package com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLOANOverdueStatusQuery;

/**
 * Created by huixiaobo on 2016/9/7.
 * 逾期返回参数
 */
public class PsnLOANOverdueStatusQueryResult {
    /**客户逾期状态*/
    private String overdueStatus;

    public String getOverdueStatus() {
        return overdueStatus;
    }

    public void setOverdueStatus(String overdueStatus) {
        this.overdueStatus = overdueStatus;
    }

    @Override
    public String toString() {
        return "PsnLOANOverdueStatusQueryResult{" +
                "overdueStatus='" + overdueStatus + '\'' +
                '}';
    }
}
