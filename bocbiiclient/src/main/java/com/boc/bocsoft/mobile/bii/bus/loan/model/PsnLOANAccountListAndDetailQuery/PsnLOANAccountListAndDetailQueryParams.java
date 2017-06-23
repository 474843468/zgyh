package com.boc.bocsoft.mobile.bii.bus.loan.model.PsnLOANAccountListAndDetailQuery;

/**
 * Created by XieDu on 2016/7/9.
 */
public class PsnLOANAccountListAndDetailQueryParams {

    /**
     * 额度号码
     */
    private String quotaNumber;
    /**
     * 查询类型
     * 1:单一额度  3:第三方额度
     */
    private String queryType;

    private String eFlag; //结清标示, 可选字段， Y：结清  N：非结清  空：全部

    private int currentIndex; //起始序号, 首次查询以0开始
    private int pageSize; //每页数
    private String _refresh; //刷新标志
    private String conversationId; //会话ID

    public String geteFlag() {
        return eFlag;
    }

    public void seteFlag(String eFlag) {
        this.eFlag = eFlag;
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

    public String get_refresh() {
        return _refresh;
    }

    public void set_refresh(String _refresh) {
        this._refresh = _refresh;
    }

    public String getQuotaNumber() { return quotaNumber;}

    public void setQuotaNumber(String quotaNumber) { this.quotaNumber = quotaNumber;}

    public String getQueryType() { return queryType;}

    public void setQueryType(String queryType) { this.queryType = queryType;}

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }
}
