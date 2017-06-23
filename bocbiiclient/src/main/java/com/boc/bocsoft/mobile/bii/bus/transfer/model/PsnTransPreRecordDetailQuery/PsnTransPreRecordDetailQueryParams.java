package com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnTransPreRecordDetailQuery;

/**
 * 预约交易详情查询
 * Created by wangf on 2016/7/21.
 */
public class PsnTransPreRecordDetailQueryParams {

    /**
     * batSeq : 2673779672
     * dateType : 0
     * transactionId : 3867926925
     */

    // 转账批次号
    private String batSeq;
    // 日期查询类型
    private String dateType;
    // 网银交易序号
    private String transactionId;

    public String getBatSeq() {
        return batSeq;
    }

    public void setBatSeq(String batSeq) {
        this.batSeq = batSeq;
    }

    public String getDateType() {
        return dateType;
    }

    public void setDateType(String dateType) {
        this.dateType = dateType;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }
}
