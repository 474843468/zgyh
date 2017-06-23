package com.boc.bocsoft.mobile.bocmobile.buss.equickpay.model;

/**
 * Created by gengjunying on 2016/12/24.
 * 注销相关model
 */
public class HceCancelModel {
    private String masterCardNo;

    public String getSlaveCardNo() {
        return slaveCardNo;
    }

    public void setSlaveCardNo(String slaveCardNo) {
        this.slaveCardNo = slaveCardNo;
    }

    public String getMasterCardNo() {
        return masterCardNo;
    }

    public void setMasterCardNo(String masterCardNo) {
        this.masterCardNo = masterCardNo;
    }

    private String slaveCardNo;


    public void reset() {
        slaveCardNo = null;
        masterCardNo = null;
    }

}
