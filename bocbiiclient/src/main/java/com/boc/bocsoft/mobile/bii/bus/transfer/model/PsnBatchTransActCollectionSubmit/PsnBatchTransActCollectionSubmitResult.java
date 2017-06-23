package com.boc.bocsoft.mobile.bii.bus.transfer.model.PsnBatchTransActCollectionSubmit;

/**
 * 批量主动收款提交
 * Created by zhx on 2016/8/13
 */
public class PsnBatchTransActCollectionSubmitResult {
    /**
     * 批次号
     */
    private String batchSeq;
    /**
     * 交易状态
     */
    private String status;
    /**
     *
     */
    private String savePayeeStatus;

    public String getBatchSeq() {
        return batchSeq;
    }

    public void setBatchSeq(String batchSeq) {
        this.batchSeq = batchSeq;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSavePayeeStatus() {
        return savePayeeStatus;
    }

    public void setSavePayeeStatus(String savePayeeStatus) {
        this.savePayeeStatus = savePayeeStatus;
    }
}
