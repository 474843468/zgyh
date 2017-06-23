package com.boc.bocsoft.mobile.bocmobile.buss.qrcodepay.collection.model;

/**
 * Created by fanbin on 16/9/29.
 */
public class QRPayGetTransRecordModel {
    /**
     * 收款结果查询
     */
    private String tranSeq;
    private String tranTime;
    private String tranStatus;
    private String tranAmount;
    private Object tranRemark;

    public String getTranSeq() {
        return tranSeq;
    }

    public void setTranSeq(String tranSeq) {
        this.tranSeq = tranSeq;
    }

    public String getTranTime() {
        return tranTime;
    }

    public void setTranTime(String tranTime) {
        this.tranTime = tranTime;
    }

    public String getTranStatus() {
        return tranStatus;
    }

    public void setTranStatus(String tranStatus) {
        this.tranStatus = tranStatus;
    }

    public String getTranAmount() {
        return tranAmount;
    }

    public void setTranAmount(String tranAmount) {
        this.tranAmount = tranAmount;
    }

    public Object getTranRemark() {
        return tranRemark;
    }

    public void setTranRemark(Object tranRemark) {
        this.tranRemark = tranRemark;
    }

}
