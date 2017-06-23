package com.boc.bocsoft.mobile.bii.bus.fess.model.PsnFessQueryForErrormesg;

/**
 * 4.20 020PsnFessQueryForErrormesg查询交易失败原因信息
 * Created by gwluo on 2016/11/18.
 */

public class PsnFessQueryForErrormesgResult {
    private String tranRetMesg;//	失败原因信息

    public String getTranRetMesg() {
        return tranRetMesg;
    }

    public void setTranRetMesg(String tranRetMesg) {
        this.tranRetMesg = tranRetMesg;
    }
}
