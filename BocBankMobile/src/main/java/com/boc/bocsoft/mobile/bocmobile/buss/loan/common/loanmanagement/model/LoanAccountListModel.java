package com.boc.bocsoft.mobile.bocmobile.buss.loan.common.loanmanagement.model;

import com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLOANListEQuery.PsnLOANListEQueryResult;

import java.io.Serializable;
import java.util.List;

/**
 * Created by huixiaobo on 2016/6/22.
 * 提款页面、提款记录页面、提款详情页面
 */
public class LoanAccountListModel extends PsnLOANListEQueryResult implements Serializable {
    /**上送参数*/
    /**结清标示  Y：结清 N：非结清 空：全部*/
    private String eFlag;
    /**E贷标识 01：WLCF 02：PLCF 10：非签约额度 99：全部*/
    private String eLoanState;
    /**起始序号*/
    private int currentIndex;
    /**每页数*/
    private int pageSize;
    /**刷新标识*/
    private String  _refresh;
    /**已结清会话Id*/
    private String SconversationId;
    /**未结清会话Id*/
    private String conversationId;
    /**是否是刷新*/
    private boolean isRefresh;

    public boolean isRefresh() {
        return isRefresh;
    }

    public void setRefresh(boolean refresh) {
        isRefresh = refresh;
    }

    public String getSconversationId() {
        return SconversationId;
    }

    public void setSconversationId(String sconversationId) {
        SconversationId = sconversationId;
    }

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
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

//返回参数
    /**客户是否有逾期贷款 0：无逾期 1：有逾期*/
    private String overState;
    /**当前日期*/
    private String endDate;
    /**总笔数*/
    private int recordNumber;
    /**是否有更多记录*/
    private String moreFlag;

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

    /**贷款账户下查询列表*/
    private List<PsnLOANListEQueryBean> loanList;

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

    @Override
    public String toString() {
        return "LoanAccountListModel{" +
                "overState='" + overState + '\'' +
                ", endDate='" + endDate + '\'' +
                ", loanList=" + loanList +
                '}';
    }
}
