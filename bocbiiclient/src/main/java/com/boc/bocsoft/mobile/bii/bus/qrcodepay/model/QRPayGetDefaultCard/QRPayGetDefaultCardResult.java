package com.boc.bocsoft.mobile.bii.bus.qrcodepay.model.QRPayGetDefaultCard;

/**
 * 查询默认卡
 * Created by wangf on 2016/8/30.
 */
public class QRPayGetDefaultCardResult {

    private String actSeq;//默认支付卡账户流水号

    public String getActSeq() {
        return actSeq;
    }

    public void setActSeq(String actSeq) {
        this.actSeq = actSeq;
    }
}
