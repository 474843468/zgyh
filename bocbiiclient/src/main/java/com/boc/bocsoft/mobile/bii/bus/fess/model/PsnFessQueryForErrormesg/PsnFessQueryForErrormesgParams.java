package com.boc.bocsoft.mobile.bii.bus.fess.model.PsnFessQueryForErrormesg;

/**
 * 4.20 020PsnFessQueryForErrormesg查询交易失败原因信息
 * Created by gwluo on 2016/11/18.
 * 只有当交易状态为失败的交易查询详情时才允许调用此查询交易失败原因接口
 */

public class PsnFessQueryForErrormesgParams {
    private String tranRetCode;//	交易结果返回码

    public String getTranRetCode() {
        return tranRetCode;
    }

    public void setTranRetCode(String tranRetCode) {
        this.tranRetCode = tranRetCode;
    }
}
