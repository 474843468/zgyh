package com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.batchprepay.model;

import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnELOANBatchRepaySubmit.PsnELOANBatchRepaySubmitResultBean;

import java.io.Serializable;
import java.util.List;

/**
 * 中银E贷批量提前还款提交交易
 * Created by liuzc on 2016/9/8.
 */
public class BatchPrepaySubmitConfirmModel implements Serializable{
    /**
     * 返回报文
     */
    private List<PsnELOANBatchRepaySubmitResultBean> batchRepayList; //批量列表

    public List<PsnELOANBatchRepaySubmitResultBean> getBatchRepayList() {
        return batchRepayList;
    }

    public void setBatchRepayList(List<PsnELOANBatchRepaySubmitResultBean> batchRepayList) {
        this.batchRepayList = batchRepayList;
    }
}
