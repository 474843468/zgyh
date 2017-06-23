package com.boc.bocsoft.mobile.bii.bus.account.model.PsnFinanceICTransferDetail;

import java.util.List;

/**
 * @author wangyang
 *         16/6/17 10:14
 *         账户交易明细结果
 */
public class PsnFinanceICTransferDetailResult {

    /** 总记录数 */
    private int recordNumber;

    /** 交易明细列表 */
    private List<TransDetail> transDetails;

    public int getRecordNumber() {
        return recordNumber;
    }

    public void setRecordNumber(int recordNumber) {
        this.recordNumber = recordNumber;
    }

    public List<TransDetail> getTransDetails() {
        return transDetails;
    }

    public void setTransDetails(List<TransDetail> transDetails) {
        this.transDetails = transDetails;
    }

}
