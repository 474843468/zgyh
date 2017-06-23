package com.boc.bocsoft.mobile.bii.bus.fess.model.PsnFess1stTimeRiskSubmit;

/**
 * 4.11 011PsnFess1stTimeRiskSubmit客户首次使用风险确认提交
 * Created by gwluo on 2016/11/18.
 */

public class PsnFess1stTimeRiskSubmitResult {
    private boolean status;//	是否已签约	boolean	true成功 false失败

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
