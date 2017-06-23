package com.boc.bocsoft.mobile.bii.bus.fess.model.PsnFessQuery1stTimeRisk;

/**
 * I49 4.10 010PsnFessQuery1stTimeRisk查询客户首次使用风险确认
 * Created by gwluo on 2016/11/18.
 */

public class PsnFessQuery1stTimeRiskResult {
    private boolean status;//	是否已签约	boolean	true已开通false 未开通

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
