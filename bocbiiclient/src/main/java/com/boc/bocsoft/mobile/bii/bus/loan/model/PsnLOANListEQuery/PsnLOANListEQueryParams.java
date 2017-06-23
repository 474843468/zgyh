package com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLOANListEQuery;

/**
 * Created by huixiaobo on 2016/6/16.
 * 分条件查询贷款账户列表上送参数
 */
public class PsnLOANListEQueryParams {

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
    /**会话ID*/
    private String conversationId;

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

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    @Override
    public String toString() {
        return "PsnLOANListEQueryParams{" +
                "eFlag='" + eFlag + '\'' +
                ", eLoanState='" + eLoanState + '\'' +
                ", currentIndex=" + currentIndex +
                ", pageSize=" + pageSize +
                ", _refresh=" + _refresh +
                ", conversationId='" + conversationId + '\'' +
                '}';
    }
}
