package com.boc.bocsoft.mobile.bii.bus.equickpay.model.PsnHCEQuickPassLukLoad;

/**
 * Created by gengjunying on 2016/12/31.
 */
public class PsnHCEQuickPassLukLoadParams {
    String deviceNo;
    String cardSeq;
    String keyNum;

    public String getSlaveCardNo() {
        return slaveCardNo;
    }

    public void setSlaveCardNo(String slaveCardNo) {
        this.slaveCardNo = slaveCardNo;
    }

    String slaveCardNo;

    public String getDeviceNo() {
        return deviceNo;
    }

    public void setDeviceNo(String deviceNo) {
        this.deviceNo = deviceNo;
    }

    public String getCardSeq() {
        return cardSeq;
    }

    public void setCardSeq(String cardSeq) {
        this.cardSeq = cardSeq;
    }

    public String getKeyNum() {
        return keyNum;
    }

    public void setKeyNum(String keyNum) {
        this.keyNum = keyNum;
    }
}
