package com.boc.bocsoft.mobile.bocmobile.buss.loan.eloan.batchprepay.model;

import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLOANListEQuery.PsnLOANListEQueryResult;

import java.io.Serializable;
import java.util.List;

/**
 * Created by liuzc on 2016/9/7.
 */
public class BatchPrepayQryModel extends PsnLOANListEQueryResult implements Serializable {
    /**
     * 上送参数
     * /

    /**结清标示  Y：结清 N：非结清 空：全部*/
    private String eFlag;
    /**E贷标识 01：WLCF 02：PLCF 10：非签约额度 99：全部*/
    private String eLoanState;
    /**起始序号*/
    private int currentIndex;
    /**每页数*/
    private int pageSize;
    /**刷新标识*/
    private String _refresh;
    /**客户是否有逾期贷款 0：无逾期 1：有逾期*/
    private String overState;
    /**当前日期*/
    private String endDate;

    /**
     * 返回参数
     */

    /** 笔数 */
    private int recordNumber;
    /** 是否有更多记录, 1：有更多记录 0：无更多记录*/
    private String moreFlag;
    /**贷款账户下查询列表*/
    private List<PsnLOANListEQueryBean> loanList;
    //手续费列表，与loanList一一对应
    private List<String> chargeList;

    public List<String> getChargeList() {
        return chargeList;
    }

    public void setChargeList(List<String> chargeList) {
        this.chargeList = chargeList;
    }

    public int getRecordNumber() {
        return recordNumber;
    }

    public void setRecordNumber(int recordNumber) {
        this.recordNumber = recordNumber;
    }

    public String getMoreFlag() {
        return moreFlag;
    }

    public void setMoreFlag(String moreFlag) {
        this.moreFlag = moreFlag;
    }

    public List<PsnLOANListEQueryBean> getLoanList() {
        return loanList;
    }

    public void setLoanList(List<PsnLOANListEQueryBean> loanList) {
        this.loanList = loanList;
    }

    @Override
    public String getOverState() {
        return overState;
    }

    @Override
    public void setOverState(String overState) {
        this.overState = overState;
    }

    @Override
    public String getEndDate() {
        return endDate;
    }

    @Override
    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String geteFlag() {
        return eFlag;
    }

    public void seteFlag(String eFlag) {
        this.eFlag = eFlag;
    }

    public String geteLoanState() {
        return eLoanState;
    }

    public void seteLoanState(String eLoanState) {
        this.eLoanState = eLoanState;
    }

    public int getCurrentIndex() {
        return currentIndex;
    }

    public void setCurrentIndex(int currentIndex) {
        this.currentIndex = currentIndex;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public String is_refresh() {
        return _refresh;
    }

    public void set_refresh(String _refresh) {
        this._refresh = _refresh;
    }

    @Override
    public String toString() {
        return "LoanAccountListModel{" +
                "overState='" + overState + '\'' +
                ", endDate='" + endDate + '\'' +
                ", loanList=" + loanList +
                '}';
    }
}
