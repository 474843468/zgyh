package com.boc.bocsoft.mobile.bii.bus.equickpay.model.PsnHCEQuickPassApplication;

import java.util.List;

/**
 * Created by yangle on 2016/12/15.
 * 描述:
 */
public class PsnHCEQuickPassApplicationResult {

    private String masterCardNo;//	主卡卡号
    private String slaveCardNo;//	从卡卡号
    private String cardData;// 卡个性化数据 需要客户端保存
    private Integer recordNumber;//	返回密钥数
    private List<HceKey> keyList;// 密钥列表

    public Integer getRecordNumber() {
        return recordNumber;
    }

    public void setRecordNumber(Integer recordNumber) {
        this.recordNumber = recordNumber;
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

    public String getCardData() {
        return cardData;
    }

    public void setCardData(String cardData) {
        this.cardData = cardData;
    }

    public List<HceKey> getKeyList() {
        return keyList;
    }

    public void setKeyList(List<HceKey> keyList) {
        this.keyList = keyList;
    }

    public static class HceKey {

       private String keyInfo;//	密钥信息

        public HceKey(String keyInfo) {
            this.keyInfo = keyInfo;
        }

        public HceKey() {
        }

        public String getKeyInfo() {
            return keyInfo;
        }

        public void setKeyInfo(String keyInfo) {
            this.keyInfo = keyInfo;
        }
    }
}
