package com.boc.bocsoft.mobile.bocmobile.buss.equickpay.model;

import java.util.List;

/**
 * Created by yangle on 2016/12/29.
 * 描述: 卡本地化数据的model
 */
public class ApplicationModel {

    private String masterCardNo;//	主卡卡号
    private String slaveCardNo;//	从卡卡号
    private String cardData;// 卡个性化数据 需要客户端保存
    private Integer recordNumber;//	返回密钥数
    private List<String> keys;;// 密钥列表

    public List<String> getKeys() {
        return keys;
    }

    public void setKeys(List<String> keys) {
        this.keys = keys;
    }

    public String getCardData() {
        return cardData;
    }

    public void setCardData(String cardData) {
        this.cardData = cardData;
    }

    public String getMasterCardNo() {
        return masterCardNo;
    }

    public void setMasterCardNo(String masterCardNo) {
        this.masterCardNo = masterCardNo;
    }

    public String getSlaveCardNo() {
        return slaveCardNo;
    }

    public void setSlaveCardNo(String slaveCardNo) {
        this.slaveCardNo = slaveCardNo;
    }

    public Integer getRecordNumber() {
        return recordNumber;
    }

    public void setRecordNumber(Integer recordNumber) {
        this.recordNumber = recordNumber;
    }
}
