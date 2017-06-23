package com.boc.bocsoft.mobile.bii.bus.equickpay.model.PsnHCEQuickPassListQuery;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by gengjunying on 2016/12/6.
 * hce卡列表数据返回数据
 */
public class PsnHCEQuickPassListQueryResult {

    private Integer recordNumber;

    public List<HCEQuickPassListQueryItemEntity> getQuickPassList() {
        return quickPassList;
    }

    public void setQuickPassList(List<HCEQuickPassListQueryItemEntity> quickPassList) {
        this.quickPassList = quickPassList;
    }

    List<HCEQuickPassListQueryItemEntity> quickPassList = new ArrayList<>();

    public Integer getRecordNumber() {
        return recordNumber;
    }

    public void setRecordNumber(Integer recordNumber) {
        this.recordNumber = recordNumber;
    }


    public static class HCEQuickPassListQueryItemEntity {
        private String masterCardNo;      //主卡卡号
        private String cardType;           //从卡类型
        private String slaveCardNo;       //从卡卡号
        private BigDecimal singleQuota;   // 单笔限额
        private BigDecimal dayQuota;      // 单笔限额
        private String cardBrand;         //从卡品牌
        private String custMobile;        //客户手机号
        private String cardStatus;        //从卡状态


        public BigDecimal getDayQuota() {
            return dayQuota;
        }

        public void setDayQuota(BigDecimal dayQuota) {
            this.dayQuota = dayQuota;
        }

        public BigDecimal getSingleQuota() {
            return singleQuota;
        }

        public void setSingleQuota(BigDecimal singleQuota) {
            this.singleQuota = singleQuota;
        }

        public String getMasterCardNo() {
            return masterCardNo;
        }

        public void setMasterCardNo(String masterCardNo) {
            this.masterCardNo = masterCardNo;
        }

        public String getCardType() {
            return cardType;
        }

        public void setCardType(String cardType) {
            this.cardType = cardType;
        }

        public String getSlaveCardNo() {
            return slaveCardNo;
        }

        public void setSlaveCardNo(String slaveCardNo) {
            this.slaveCardNo = slaveCardNo;
        }


        public String getCardBrand() {
            return cardBrand;
        }

        public void setCardBrand(String cardBrand) {
            this.cardBrand = cardBrand;
        }

        public String getCustMobile() {
            return custMobile;
        }

        public void setCustMobile(String custMobile) {
            this.custMobile = custMobile;
        }

        public String getCardStatus() {
            return cardStatus;
        }

        public void setCardStatus(String cardStatus) {
            this.cardStatus = cardStatus;
        }

    }


}
